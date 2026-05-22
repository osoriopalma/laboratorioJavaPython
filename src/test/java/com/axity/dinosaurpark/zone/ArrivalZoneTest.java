package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrivalZoneTest {

    @Test
    void shouldAllowTouristToEnterWhenHasCapacity() {
        ArrivalZone zone = new ArrivalZone("Arrival Zone", 2);
        Tourist tourist = new Tourist(1, "Juan");

        zone.enter(tourist);

        assertEquals(1, zone.getCurrentOccupancy());
        assertEquals(TouristStatus.IN_PARK, tourist.getStatus());
    }

    @Test
    void shouldNotExceedCapacity() {
        ArrivalZone zone = new ArrivalZone("Arrival Zone", 1);

        zone.enter(new Tourist(1, "Juan"));
        zone.enter(new Tourist(2, "Pedro"));

        assertEquals(1, zone.getCurrentOccupancy());
    }

    @Test
    void shouldRemoveTouristOnExit() {
        ArrivalZone zone = new ArrivalZone("Arrival Zone", 2);
        Tourist tourist = new Tourist(1, "Juan");

        zone.enter(tourist);
        zone.exit(tourist);

        assertEquals(0, zone.getCurrentOccupancy());
    }
}