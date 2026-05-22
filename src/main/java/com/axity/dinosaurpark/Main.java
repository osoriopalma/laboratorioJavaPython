package com.axity.dinosaurpark;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.persistence.LiquibaseRunner;
import com.axity.dinosaurpark.simulation.SimulationEngine;

public class Main {

    public static void main(String[] args) {

        LiquibaseRunner liquibaseRunner = new LiquibaseRunner();
        liquibaseRunner.runMigrations();

        ParkConfig config = ParkConfig.getInstance();

        SimulationEngine engine = new SimulationEngine();

        engine.run(config.getTotalSteps());
    }
}