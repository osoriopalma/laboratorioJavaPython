package com.axity.dinosaurpark.simulation;

import com.axity.dinosaurpark.model.*;
import com.axity.dinosaurpark.zone.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.persistence.DatabaseService;

public class SimulationEngine {

    private final Random rng;
    private final ArrivalZone arrivalZone;
    private final CentralHub centralHub;
    private final BathroomZone bathroomZone;
    private final PowerPlantZone powerPlantZone;
    private final List<Guard> guards;
    private final List<Technician> technicians;
    private final ParkConfig config;
    private final double touristExitProbability;
    private final double dinosaurEscapeBaseProbability;
    private final DatabaseService databaseService;
    private int currentStep;
    private final ParkState state;

    public SimulationEngine() {
        this.config = ParkConfig.getInstance();

        long seed = config.getSeed();
        int arrivalMaxCapacity = config.getInt("arrival.maxCapacity", 30);

        int hubMaxCapacity = config.getInt("hub.maxCapacity", 20);
        double souvenirPrice = config.getDouble("hub.souvenirPrice", 15.0);
        double souvenirProbability = config.getDouble("hub.souvenirPurchaseProbability", 0.4);

        int bathroomMaxCapacity = config.getInt("bathroom.maxCapacity", 10);
        int bathroomDuration = config.getInt("bathroom.useDurationSteps", 3);
        double spaPrice = config.getDouble("bathroom.spaPrice", 20.0);
        double spaProbability = config.getDouble("bathroom.spaPurchaseProbability", 0.2);

        double initialEnergy = config.getDouble("powerplant.initialEnergy", 100.0);
        double consumptionPerStep = config.getDouble("powerplant.consumptionPerStep", 2.0);
        double failureProbability = config.getDouble("powerplant.failureProbability", 0.05);
        double maintenanceCost = config.getDouble("powerplant.maintenanceCost", 200.0);
        double repairCost = config.getDouble("powerplant.repairCost", 500.0);

        this.databaseService = new DatabaseService();
        this.touristExitProbability = config.getDouble("tourist.exitProbability", 0.15);
        this.dinosaurEscapeBaseProbability = config.getDouble("dinosaur.escapeBaseProbability", 0.05);

        this.rng = new Random(seed);
        this.arrivalZone = new ArrivalZone("Arrival Zone", arrivalMaxCapacity);
        this.centralHub = new CentralHub("Central Hub", hubMaxCapacity, souvenirPrice, souvenirProbability);
        this.bathroomZone = new BathroomZone("Bathroom Zone", bathroomMaxCapacity, bathroomDuration, spaPrice,
                spaProbability);
        this.powerPlantZone = new PowerPlantZone(initialEnergy, consumptionPerStep, failureProbability, maintenanceCost,
                repairCost);
        List<Tourist> tourists = new ArrayList<>();

        List<Dinosaur> dinosaurs = new ArrayList<>();
        dinosaurs.add(new CarnivoreDinosaur(1, "Rex", "Tyrannosaurus"));
        dinosaurs.add(new HerbivoreDinosaur(2, "Bronto", "Brachiosaurus"));
        dinosaurs.add(new HerbivoreDinosaur(3, "Trike", "Triceratops"));

        this.guards = new ArrayList<>();
        guards.add(new Guard(1, "Carlos", 150.0));
        guards.add(new Guard(2, "Miguel", 150.0));

        this.technicians = new ArrayList<>();
        technicians.add(new Technician(1, "Luis", 150.0));
        technicians.add(new Technician(2, "Pedro", 150.0));

        this.state = new ParkState(rng, arrivalZone, centralHub, bathroomZone, powerPlantZone, tourists, dinosaurs,
                guards, technicians, databaseService);
    }

    public void run(int totalSteps) {

        for (int step = 1; step <= totalSteps; step++) {
            this.currentStep = step;
            state.setCurrentStep(step);
            System.out.println();
            System.out.println("===== STEP " + step + " =====");
            generateTourists();

            moveTourists();
            dinosaurEscapes();
            dinosaurAttacks();
            removeAttackedTourists();
            guardsRecaptureDinosaurs();
            removeTourists();
            bathroomZone.tick();

            powerPlantZone.tick(rng);
            techniciansRepairPowerPlant();
            printStats();
        }
        printFinalReport();
    }

    private void generateTourists() {

        int newTourists = rng.nextInt(3) + 1;
        for (int i = 0; i < newTourists; i++) {
            Tourist tourist = new Tourist(state.getTouristCounter(), "Tourist-" + state.getTouristCounter());
            state.incrementTouristCounter();
            state.getTourists().add(tourist);
            state.getArrivalZone().enter(tourist);
            System.out.println(tourist.getName() + " entró al parque.");
        }
    }

    private void moveTourists() {

        for (Tourist tourist : state.getTourists()) {
            if (rng.nextBoolean()) {
                state.getCentralHub().visit(tourist, rng);
            } else {
                state.getBathroomZone().tryEnter(tourist, rng);
            }
        }
    }

    private void removeTourists() {

        List<Tourist> touristsLeaving = new ArrayList<>();
        for (Tourist tourist : state.getTourists()) {
            if (rng.nextDouble() < 0.20) {
                touristsLeaving.add(tourist);
            }
        }
        for (Tourist tourist : touristsLeaving) {
            state.getTourists().remove(tourist);
            state.getArrivalZone().exit(tourist);
            state.getBathroomZone().exit(tourist);
            System.out.println(tourist.getName() + " salio del parque.");
        }
    }

    private void dinosaurEscapes() {

        for (Dinosaur dinosaur : state.getDinosaurs()) {
            if (dinosaur.getStatus().name().equals("IN_ENCLOSURE")) {
                double chance = dinosaur.getDangerLevel() * dinosaurEscapeBaseProbability;
                if (rng.nextDouble() < chance) {
                    dinosaur.escape();
                    state.incrementTotalEscapes();
                    System.out.println("PELIGRO: " + dinosaur.getName() + " ESCAPO!");
                    databaseService.saveEvent(currentStep, "DINOSAUR_ESCAPE",
                            dinosaur.getName() + " escapo del recinto");
                }
            }
        }
    }

    private void guardsRecaptureDinosaurs() {

        for (Guard guard : state.getGuards()) {
            guard.recaptureEscapedDinosaurs(state.getDinosaurs());
        }
    }

    private void dinosaurAttacks() {
        for (Dinosaur dinosaur : state.getDinosaurs()) {
            if (dinosaur.getStatus().name().equals("ESCAPED")) {

                if (!state.getTourists().isEmpty()) {
                    Tourist victim = state.getTourists().get(rng.nextInt(state.getTourists().size()));
                    victim.setStatus(com.axity.dinosaurpark.model.TouristStatus.ATTACKED);
                    state.incrementTotalAttacks();
                    System.out.println("ATAQUE: " + dinosaur.getName() + " ataco a " + victim.getName());
                    databaseService.saveEvent(currentStep, "DINOSAUR_ATTACK",
                            dinosaur.getName() + " ataco a " + victim.getName());
                }
            }
        }
    }

    private void techniciansRepairPowerPlant() {

        if (!state.getPowerPlantZone().isOperational()) {
            Technician technician = state.getTechnicians().get(0);
            technician.repairIfNeeded(state.getPowerPlantZone());
            state.getDatabaseService().saveEvent(state.getCurrentStep(), "POWER_PLANT_REPAIRED",
                    technician.getName() + " reparo la planta electrica");
        }
    }

    private void removeAttackedTourists() {

        List<Tourist> attacked = new ArrayList<>();
        for (Tourist tourist : state.getTourists()) {
            if (tourist.getStatus().name().equals("ATTACKED")) {
                attacked.add(tourist);
                state.incrementTotalEvacuated();
                System.out.println(tourist.getName() + " fue evacuado del parque.");
                databaseService.saveEvent(currentStep, "TOURIST_EVACUATED",
                        tourist.getName() + " fue evacuado por ataque");
            }
        }

        for (Tourist tourist : attacked) {
            state.getTourists().remove(tourist);
            arrivalZone.exit(tourist);
            centralHub.exit(tourist);
            bathroomZone.exit(tourist);
        }
    }

    private void printStats() {

        System.out.println();
        System.out.println("ArrivalZone: " + state.getArrivalZone().getCurrentOccupancy());
        System.out.println("CentralHub: " + state.getCentralHub().getCurrentOccupancy());
        System.out.println("BathroomZone: " + state.getBathroomZone().getCurrentOccupancy());
        System.out.println("Energia Planta: " + state.getPowerPlantZone().getEnergyLevel());
        System.out.println("Planta Operativa: " + state.getPowerPlantZone().isOperational());
        System.out.println("Total turistas: " + state.getTourists().size());

        printDinosaurStats();
    }

    private void printFinalReport() {

        double safetyScore = state.calculateSafetyScore();

        System.out.println("\n===== REPORTE FINAL =====");
        System.out.println("Turistas restantes: " + state.countActiveTourists());
        System.out.println("Escapes totales: " + state.getTotalEscapes());
        System.out.println("Ataques totales: " + state.getTotalAttacks());
        System.out.println("Evacuados totales: " + state.getTotalEvacuated());
        System.out.println("Score seguridad: " + safetyScore);

        databaseService.saveFinalReport(state.countActiveTourists(), state.getTotalEscapes(), state.getTotalAttacks(),
                state.getTotalEvacuated(), safetyScore);
    }

    private void printDinosaurStats() {

        System.out.println("Dinosaurios:");

        for (Dinosaur dinosaur : state.getDinosaurs()) {

            System.out.println(
                    "- "
                            + dinosaur.getName()
                            + " | "
                            + dinosaur.getDiet()
                            + " | "
                            + dinosaur.getStatus());
        }
    }
}