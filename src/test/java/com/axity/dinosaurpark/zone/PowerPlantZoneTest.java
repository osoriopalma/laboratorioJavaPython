package com.axity.dinosaurpark.zone;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PowerPlantZoneTest {

    @Test
    void shouldConsumeEnergyWhenOperational() {

        PowerPlantZone plant = new PowerPlantZone(
                100.0,
                2.0,
                0.0,
                200.0,
                500.0
        );

        plant.tick(new Random(42));

        assertEquals(98.0, plant.getEnergyLevel());
        assertTrue(plant.isOperational());
    }

    @Test
    void shouldFailWhenEnergyReachesZero() {

        PowerPlantZone plant = new PowerPlantZone(
                2.0,
                2.0,
                0.0,
                200.0,
                500.0
        );

        plant.tick(new Random(42));

        assertEquals(0.0, plant.getEnergyLevel());
        assertFalse(plant.isOperational());
    }

    @Test
    void shouldRepairAndRechargeWhenEnergyIsZero() {

        PowerPlantZone plant = new PowerPlantZone(
                2.0,
                2.0,
                0.0,
                200.0,
                500.0
        );

        plant.tick(new Random(42));
        plant.repair();

        assertTrue(plant.isOperational());
        assertEquals(50.0, plant.getEnergyLevel());
    }
}