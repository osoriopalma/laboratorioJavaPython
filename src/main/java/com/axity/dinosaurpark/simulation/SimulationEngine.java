package com.axity.dinosaurpark.simulation;

import com.axity.dinosaurpark.model.*;
import com.axity.dinosaurpark.zone.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.axity.dinosaurpark.config.ParkConfig;

public class SimulationEngine {

    private final Random rng;
    private final ArrivalZone arrivalZone;
    private final CentralHub centralHub;
    private final BathroomZone bathroomZone;
    private final PowerPlantZone powerPlantZone;
    private final List<Tourist> tourists;
    private final List<Dinosaur> dinosaurs;
    private final List<Guard> guards;
    private int touristCounter;
    private int totalAttacks;
    private int totalEvacuated;
    private int totalEscapes;
    private final ParkConfig config;
    private final double touristExitProbability;
    private final double dinosaurEscapeBaseProbability;

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

        this.touristExitProbability = config.getDouble("tourist.exitProbability", 0.15);
        this.dinosaurEscapeBaseProbability = config.getDouble("dinosaur.escapeBaseProbability", 0.05);

        this.rng = new Random(seed);
        this.arrivalZone = new ArrivalZone("Arrival Zone", arrivalMaxCapacity);
        this.centralHub = new CentralHub("Central Hub", hubMaxCapacity, souvenirPrice, souvenirProbability);
        this.bathroomZone = new BathroomZone("Bathroom Zone", bathroomMaxCapacity, bathroomDuration, spaPrice,
                spaProbability);
        this.powerPlantZone = new PowerPlantZone(initialEnergy, consumptionPerStep, failureProbability, maintenanceCost,
                repairCost);
        this.tourists = new ArrayList<>();

        this.dinosaurs = new ArrayList<>();
        dinosaurs.add(new CarnivoreDinosaur(1, "Rex", "Tyrannosaurus"));
        dinosaurs.add(new HerbivoreDinosaur(2, "Bronto", "Brachiosaurus"));
        dinosaurs.add(new HerbivoreDinosaur(3, "Trike", "Triceratops"));

        this.guards = new ArrayList<>();
        guards.add(new Guard(1, "Carlos", 150.0));
        guards.add(new Guard(2, "Miguel", 150.0));

        this.touristCounter = 1;
        this.totalAttacks = 0;
        this.totalEvacuated = 0;
        this.totalEscapes = 0;
    }

    public void run(int totalSteps) {

        for (int step = 1; step <= totalSteps; step++) {
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
            printStats();
        }
        printFinalReport();
    }

    private void generateTourists() {

        int newTourists = rng.nextInt(3) + 1;

        for (int i = 0; i < newTourists; i++) {
            Tourist tourist = new Tourist(
                    touristCounter,
                    "Tourist-" + touristCounter);

            touristCounter++;
            tourists.add(tourist);
            arrivalZone.enter(tourist);
            System.out.println(tourist.getName() + " entró al parque.");
        }
    }

    private void moveTourists() {

        for (Tourist tourist : tourists) {

            if (rng.nextBoolean()) {

                centralHub.visit(tourist, rng);

            } else {

                bathroomZone.tryEnter(tourist, rng);
            }
        }
    }

    private void removeTourists() {

        List<Tourist> exiting = new ArrayList<>();

        for (Tourist tourist : tourists) {

            if (rng.nextDouble() < touristExitProbability) {

                exiting.add(tourist);

                System.out.println(tourist.getName() + " salio del parque.");
            }
        }

        for (Tourist tourist : exiting) {

            tourists.remove(tourist);
            arrivalZone.exit(tourist);
            centralHub.exit(tourist);
            bathroomZone.exit(tourist);
        }
    }

    private void dinosaurEscapes() {

        for (Dinosaur dinosaur : dinosaurs) {
            if (dinosaur.getStatus().name().equals("IN_ENCLOSURE")) {
                double chance = dinosaur.getDangerLevel() * dinosaurEscapeBaseProbability;
                if (rng.nextDouble() < chance) {
                    dinosaur.escape();
                    totalEscapes++;
                    System.out.println("PELIGRO: " + dinosaur.getName() + " ESCAPO!");
                }
            }
        }
    }

    private void guardsRecaptureDinosaurs() {
        for (Guard guard : guards) {
            guard.recaptureEscapedDinosaurs(dinosaurs);
        }
    }

    private void dinosaurAttacks() {
        for (Dinosaur dinosaur : dinosaurs) {
            if (dinosaur.getStatus().name().equals("ESCAPED")) {

                if (!tourists.isEmpty()) {
                    Tourist victim = tourists.get(rng.nextInt(tourists.size()));
                    victim.setStatus(com.axity.dinosaurpark.model.TouristStatus.ATTACKED);
                    totalAttacks++;
                    System.out.println("ATAQUE: " + dinosaur.getName() + " ataco a " + victim.getName());
                }
            }
        }
    }

    private void removeAttackedTourists() {

        List<Tourist> attacked = new ArrayList<>();
        for (Tourist tourist : tourists) {
            if (tourist.getStatus().name().equals("ATTACKED")) {
                attacked.add(tourist);
                totalEvacuated++;
                System.out.println(tourist.getName() + " fue evacuado del parque.");
            }
        }

        for (Tourist tourist : attacked) {
            tourists.remove(tourist);
            arrivalZone.exit(tourist);
            centralHub.exit(tourist);
            bathroomZone.exit(tourist);
        }
    }

    private void printStats() {

        System.out.println();
        System.out.println("ArrivalZone: " + arrivalZone.getCurrentOccupancy());
        System.out.println("CentralHub: " + centralHub.getCurrentOccupancy());
        System.out.println("BathroomZone: " + bathroomZone.getCurrentOccupancy());
        System.out.println("Energia Planta: " + powerPlantZone.getEnergyLevel());
        System.out.println("Planta Operativa: " + powerPlantZone.isOperational());
        System.out.println("Total turistas: " + tourists.size());
        printDinosaurStats();
    }

    private void printFinalReport() {

        System.out.println();
        System.out.println("===== REPORTE FINAL =====");
        System.out.println("Turistas restantes: " + tourists.size());
        System.out.println("Escapes totales: " + totalEscapes);
        System.out.println("Ataques totales: " + totalAttacks);
        System.out.println("Evacuados totales: " + totalEvacuated);
        double safetyScore = 100.0 - (totalAttacks * 10);
        if (safetyScore < 0) {
            safetyScore = 0;
        }

        System.out.println("Score seguridad: " + safetyScore);
    }

    private void printDinosaurStats() {

        System.out.println("Dinosaurios:");

        for (Dinosaur dinosaur : dinosaurs) {
            System.out.println("- " + dinosaur.getName() + " | " + dinosaur.getDiet() + " | " + dinosaur.getStatus());
        }
    }
}