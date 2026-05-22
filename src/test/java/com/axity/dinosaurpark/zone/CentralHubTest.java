package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Tourist;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CentralHubTest {

    @Test
    void shouldEnterAndExitTourist() {
        CentralHub hub = new CentralHub("Central Hub", 2, 15.0, 0.0);
        Tourist tourist = new Tourist(1, "Juan");

        hub.enter(tourist);

        assertEquals(1, hub.getCurrentOccupancy());

        hub.exit(tourist);

        assertEquals(0, hub.getCurrentOccupancy());
    }

    @Test
    void visitShouldLeaveOccupancyAtZero() {
        CentralHub hub = new CentralHub("Central Hub", 2, 15.0, 0.0);
        Tourist tourist = new Tourist(1, "Juan");

        hub.visit(tourist, new Random(42));

        assertEquals(0, hub.getCurrentOccupancy());
    }

    @Test
    void shouldRespectMaxCapacity() {
        CentralHub hub = new CentralHub("Central Hub", 1, 15.0, 0.0);

        hub.enter(new Tourist(1, "Juan"));
        hub.enter(new Tourist(2, "Pedro"));

        assertEquals(1, hub.getCurrentOccupancy());
    }
}