package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Tourist;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CentralHub implements ParkZone {

    private final String name;
    private final int maxCapacity;
    private final double souvenirPrice;
    private final double souvenirProbability;
    private final List<Tourist> tourists;

    public CentralHub(String name, int maxCapacity, double souvenirPrice, double souvenirProbability) {

        this.name = name;
        this.maxCapacity = maxCapacity;
        this.souvenirPrice = souvenirPrice;
        this.souvenirProbability = souvenirProbability;
        this.tourists = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasCapacity() {
        return tourists.size() < maxCapacity;
    }

    @Override
    public int getCurrentOccupancy() {
        return tourists.size();
    }

    @Override
    public int getMaxCapacity() {
        return maxCapacity;
    }

    @Override
    public void enter(Tourist tourist) {

        if (hasCapacity()) {
            tourists.add(tourist);
            tourist.recordVisit(name);
        }
    }

    @Override
    public void exit(Tourist tourist) {

        tourists.remove(tourist);
    }

    public void visit(Tourist tourist, Random rng) {

        enter(tourist);
        if (rng.nextDouble() < souvenirProbability) {
            tourist.spend(souvenirPrice);
            System.out.println(tourist.getName() + " compro souvenir por $" + souvenirPrice);
        }

        exit(tourist);
    }
}