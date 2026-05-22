package com.axity.dinosaurpark.model;

import com.axity.dinosaurpark.zone.PowerPlantZone;

public class Technician extends Worker {

    public Technician(int id, String name, double dailySalary) {
        super(id, name, dailySalary);
    }

    @Override
    public String getRole() {
        return "TECHNICIAN";
    }

    public void repairIfNeeded(PowerPlantZone plant) {
        if (!plant.isOperational()) {
            plant.repair();
        }
    }
}