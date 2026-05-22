package com.axity.dinosaurpark.simulation;

import com.axity.dinosaurpark.model.*;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.zone.*;

import java.util.List;
import java.util.Random;

public class ParkState {

    private final Random rng;

    private final ArrivalZone arrivalZone;
    private final CentralHub centralHub;
    private final BathroomZone bathroomZone;
    private final PowerPlantZone powerPlantZone;

    private final List<Tourist> tourists;
    private final List<Dinosaur> dinosaurs;
    private final List<Guard> guards;
    private final List<Technician> technicians;

    private final DatabaseService databaseService;

    private int touristCounter;
    private int currentStep;

    private int totalAttacks;
    private int totalEvacuated;
    private int totalEscapes;

    public ParkState(Random rng,
            ArrivalZone arrivalZone,
            CentralHub centralHub,
            BathroomZone bathroomZone,
            PowerPlantZone powerPlantZone,
            List<Tourist> tourists,
            List<Dinosaur> dinosaurs,
            List<Guard> guards,
            List<Technician> technicians,
            DatabaseService databaseService) {

        this.rng = rng;
        this.arrivalZone = arrivalZone;
        this.centralHub = centralHub;
        this.bathroomZone = bathroomZone;
        this.powerPlantZone = powerPlantZone;
        this.tourists = tourists;
        this.dinosaurs = dinosaurs;
        this.guards = guards;
        this.technicians = technicians;
        this.databaseService = databaseService;

        this.touristCounter = 1;
        this.currentStep = 0;
        this.totalAttacks = 0;
        this.totalEvacuated = 0;
        this.totalEscapes = 0;
    }

    public Random getRng() {
        return rng;
    }

    public ArrivalZone getArrivalZone() {
        return arrivalZone;
    }

    public CentralHub getCentralHub() {
        return centralHub;
    }

    public BathroomZone getBathroomZone() {
        return bathroomZone;
    }

    public PowerPlantZone getPowerPlantZone() {
        return powerPlantZone;
    }

    public List<Tourist> getTourists() {
        return tourists;
    }

    public List<Dinosaur> getDinosaurs() {
        return dinosaurs;
    }

    public List<Guard> getGuards() {
        return guards;
    }

    public List<Technician> getTechnicians() {
        return technicians;
    }

    public DatabaseService getDatabaseService() {
        return databaseService;
    }

    public int getTouristCounter() {
        return touristCounter;
    }

    public void incrementTouristCounter() {
        touristCounter++;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }

    public int getTotalAttacks() {
        return totalAttacks;
    }

    public void incrementTotalAttacks() {
        totalAttacks++;
    }

    public int getTotalEvacuated() {
        return totalEvacuated;
    }

    public void incrementTotalEvacuated() {
        totalEvacuated++;
    }

    public int getTotalEscapes() {
        return totalEscapes;
    }

    public void incrementTotalEscapes() {
        totalEscapes++;
    }

    public int countActiveTourists() {
        return tourists.size();
    }

    public double calculateSafetyScore() {
        double safetyScore = 100.0 - (totalAttacks * 10);

        if (safetyScore < 0) {
            return 0;
        }

        return safetyScore;
    }
}