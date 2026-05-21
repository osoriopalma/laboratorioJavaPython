package com.axity.dinosaurpark;

import com.axity.dinosaurpark.model.CarnivoreDinosaur;
import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.Guard;
import com.axity.dinosaurpark.model.HerbivoreDinosaur;
import com.axity.dinosaurpark.model.SatisfactionSurvey;
import com.axity.dinosaurpark.model.Technician;
import com.axity.dinosaurpark.model.Ticket;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Dinosaur rex = new CarnivoreDinosaur(1, "Rex", "Tyrannosaurus");
        Dinosaur bronto = new HerbivoreDinosaur(2, "Bronto", "Brachiosaurus");

        rex.escape();

        List<Dinosaur> dinosaurs = new ArrayList<>();
        dinosaurs.add(rex);
        dinosaurs.add(bronto);

        Guard guard = new Guard(1, "Carlos", 150.0);
        Technician technician = new Technician(2, "Luis", 150.0);

        System.out.println("Estado de Rex antes del guardia: " + rex.getStatus());

        guard.recaptureEscapedDinosaurs(dinosaurs);

        System.out.println("Estado de Rex después del guardia: " + rex.getStatus());

        Ticket ticket = new Ticket(1, 101, 25.0, "GENERAL");

        SatisfactionSurvey survey = new SatisfactionSurvey(101, "Premium Enclosure", 5);

        System.out.println("Rol guardia: " + guard.getRole());
        System.out.println("Rol técnico: " + technician.getRole());
        System.out.println("Ticket turista: " + ticket.getTouristId());
        System.out.println("Encuesta puntuación: " + survey.getScore());
    }
}