package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import java.util.ArrayList;
import java.util.List;

public class ArrivalZone implements ParkZone {

    private final String name;
    private final int maxCapacity;
    private final List<Tourist> tourists;

    public ArrivalZone(String name, int maxCapacity) {

        this.name = name;
        this.maxCapacity = maxCapacity;
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

        if (hasCapacity() && !tourists.contains(tourist)) {
            tourists.add(tourist);
            tourist.setStatus(TouristStatus.IN_PARK);
            tourist.recordVisit(name);
        }
    }

    @Override
    public void exit(Tourist tourist) {

        tourists.remove(tourist);
    }

    public List<Tourist> getTourists() {
        return tourists;
    }
}