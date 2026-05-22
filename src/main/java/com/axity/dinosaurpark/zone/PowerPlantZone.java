package com.axity.dinosaurpark.zone;

import java.util.Random;

public class PowerPlantZone {

    private double energyLevel;
    private final double consumptionPerStep;
    private final double failureProbability;
    private final double maintenanceCost;
    private final double repairCost;
    private boolean operational;

    public PowerPlantZone(double initialEnergy, double consumptionPerStep, double failureProbability,
            double maintenanceCost, double repairCost) {

        this.energyLevel = initialEnergy;
        this.consumptionPerStep = consumptionPerStep;
        this.failureProbability = failureProbability;
        this.maintenanceCost = maintenanceCost;
        this.repairCost = repairCost;
        this.operational = true;
    }

    public void tick(Random rng) {

        if (!operational) {
            return;
        }

        energyLevel -= consumptionPerStep;

        if (energyLevel < 0) {
            energyLevel = 0;
        }

        if (energyLevel == 0) {
            operational = false;
            System.out.println("SIN ENERGIA EN LA PLANTA ELECTRICA");
        }

        if (rng.nextDouble() < failureProbability) {
            operational = false;
            System.out.println("¡¡FALLA EN LA PLANTA ELÉCTRICA!!");
        }
    }

    public void repair() {

        operational = true;
        if (energyLevel <= 0) {
            energyLevel = 50.0;
        }
        System.out.println("Planta eléctrica reparada.");
    }

    public void recharge(double amount) {
        energyLevel += amount;
    }

    public double getEnergyLevel() {
        return energyLevel;
    }

    public boolean isOperational() {
        return operational;
    }

    public double getMaintenanceCost() {
        return maintenanceCost;
    }

    public double getRepairCost() {
        return repairCost;
    }
}