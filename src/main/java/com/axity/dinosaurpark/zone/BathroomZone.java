package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Tourist;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BathroomZone implements ParkZone {

    private final String name;
    private final int maxCapacity;
    private final int useDurationSteps;
    private final double spaPrice;
    private final double spaProbability;
    private final Map<Tourist, Integer> occupants;

    public BathroomZone(String name, int maxCapacity, int useDurationSteps, double spaPrice, double spaProbability) {

        this.name = name;
        this.maxCapacity = maxCapacity;
        this.useDurationSteps = useDurationSteps;
        this.spaPrice = spaPrice;
        this.spaProbability = spaProbability;
        this.occupants = new HashMap<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasCapacity() {
        return occupants.size() < maxCapacity;
    }

    @Override
    public int getCurrentOccupancy() {
        return occupants.size();
    }

    @Override
    public int getMaxCapacity() {
        return maxCapacity;
    }

    @Override
    public void enter(Tourist tourist) {

        if (hasCapacity()) {
            occupants.put(tourist, useDurationSteps);
            tourist.recordVisit(name);
        }
    }

    @Override
    public void exit(Tourist tourist) {
        occupants.remove(tourist);
    }

    public void tryEnter(Tourist tourist, Random rng) {

        if (!occupants.containsKey(tourist) && hasCapacity()) {

            enter(tourist);
            if (rng.nextDouble() < spaProbability) {
                tourist.spend(spaPrice);
                System.out.println(tourist.getName() + " compró servicio SPA por $" + spaPrice);
            }
        }
    }

    public void tick() {

        Map<Tourist, Integer> updated = new HashMap<>();

        for (Map.Entry<Tourist, Integer> entry : occupants.entrySet()) {
            int remaining = entry.getValue() - 1;
            if (remaining > 0) {
                updated.put(entry.getKey(), remaining);
            }
        }

        occupants.clear();
        occupants.putAll(updated);
    }
}