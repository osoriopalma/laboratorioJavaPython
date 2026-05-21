package com.axity.dinosaurpark.model;

public abstract class Dinosaur {

    private final int id;
    private final String name;
    private final String species;
    private DinosaurStatus status;
    private final double feedingCostPerDay;

    public Dinosaur(int id, String name, String species, double feedingCostPerDay) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.feedingCostPerDay = feedingCostPerDay;
        this.status = DinosaurStatus.IN_ENCLOSURE;
    }

    public abstract String getDiet();
    public abstract double getDangerLevel();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public DinosaurStatus getStatus() {
        return status;
    }

    public double getFeedingCostPerDay() {
        return feedingCostPerDay;
    }

    public void escape() {
        status = DinosaurStatus.ESCAPED;
    }

    public void recapture() {
        status = DinosaurStatus.RECAPTURED;
    }

    public void returnToEnclosure() {
        status = DinosaurStatus.IN_ENCLOSURE;
    }
}