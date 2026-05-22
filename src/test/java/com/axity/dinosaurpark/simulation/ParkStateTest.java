package com.axity.dinosaurpark.simulation;

import com.axity.dinosaurpark.model.*;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.zone.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ParkStateTest {

    @Test
    void shouldCalculateSafetyScore() {
        ParkState state = createState();

        state.incrementTotalAttacks();
        state.incrementTotalAttacks();

        assertEquals(80.0, state.calculateSafetyScore());
    }

    @Test
    void safetyScoreShouldNotBeNegative() {
        ParkState state = createState();

        for (int i = 0; i < 20; i++) {
            state.incrementTotalAttacks();
        }

        assertEquals(0.0, state.calculateSafetyScore());
    }

    @Test
    void shouldCountActiveTourists() {
        ParkState state = createState();

        state.getTourists().add(new Tourist(1, "Juan"));
        state.getTourists().add(new Tourist(2, "Pedro"));

        assertEquals(2, state.countActiveTourists());
    }

    private ParkState createState() {
        return new ParkState(
                new Random(42),
                new ArrivalZone("Arrival Zone", 30),
                new CentralHub("Central Hub", 20, 15.0, 0.4),
                new BathroomZone("Bathroom Zone", 10, 3, 20.0, 0.2),
                new PowerPlantZone(100.0, 2.0, 0.0, 200.0, 500.0),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new DatabaseService());
    }
}