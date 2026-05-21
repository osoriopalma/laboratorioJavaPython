package com.axity.dinosaurpark;

import com.axity.dinosaurpark.zone.PowerPlantZone;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        Random rng = new Random(42);

        PowerPlantZone plant = new PowerPlantZone(
                100.0,
                10.0,
                0.3,
                200.0,
                500.0);

        for (int i = 1; i <= 10; i++) {

            System.out.println("STEP " + i);

            plant.tick(rng);

            System.out.println("Energía: " + plant.getEnergyLevel());
            System.out.println("Operativa: " + plant.isOperational());
            System.out.println("----------------");
        }
    }
}