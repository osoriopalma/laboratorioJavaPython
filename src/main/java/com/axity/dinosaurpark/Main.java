package com.axity.dinosaurpark;

import com.axity.dinosaurpark.model.CarnivoreDinosaur;
import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.HerbivoreDinosaur;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;

public class Main {

    public static void main(String[] args) {

        Tourist tourist = new Tourist(1, "Juan");

        tourist.setStatus(TouristStatus.IN_PARK);
        tourist.spend(25.0);
        tourist.recordVisit("Arrival Zone");

        System.out.println("ID: " + tourist.getId());
        System.out.println("Nombre: " + tourist.getName());
        System.out.println("Estado: " + tourist.getStatus());
        System.out.println("Gastado: " + tourist.getMoneySpent());
        System.out.println("Zonas visitadas: " + tourist.getVisitedZones());

        Dinosaur rex = new CarnivoreDinosaur(1, "Rex", "Tyrannosaurus");
        Dinosaur bronto = new HerbivoreDinosaur(2, "Bronto", "Brachiosaurus");

        rex.escape();

        System.out.println("Dinosaurio: " + rex.getName());
        System.out.println("Especie: " + rex.getSpecies());
        System.out.println("Dieta: " + rex.getDiet());
        System.out.println("Peligro: " + rex.getDangerLevel());
        System.out.println("Estado: " + rex.getStatus());

        System.out.println("-------------------");

        System.out.println("Dinosaurio: " + bronto.getName());
        System.out.println("Especie: " + bronto.getSpecies());
        System.out.println("Dieta: " + bronto.getDiet());
        System.out.println("Peligro: " + bronto.getDangerLevel());
        System.out.println("Estado: " + bronto.getStatus());
    }
}