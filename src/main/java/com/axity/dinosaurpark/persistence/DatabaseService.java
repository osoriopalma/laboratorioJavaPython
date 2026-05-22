package com.axity.dinosaurpark.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DatabaseService {

    private final DatabaseConnection databaseConnection;

    public DatabaseService() {
        this.databaseConnection = new DatabaseConnection();
    }

    public void saveEvent(int stepNumber, String eventType, String description) {

        String sql = """
                INSERT INTO simulation_events
                (step_number, event_type, description)
                VALUES (?, ?, ?)
                """;

        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, stepNumber);
            statement.setString(2, eventType);
            statement.setString(3, description);

            statement.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error guardando evento en base de datos");
            e.printStackTrace();
        }
    }

    public void saveFinalReport(int touristsRemaining,
            int totalEscapes,
            int totalAttacks,
            int totalEvacuated,
            double safetyScore) {

        String sql = """
                INSERT INTO simulation_reports
                (tourists_remaining, total_escapes, total_attacks, total_evacuated, safety_score)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, touristsRemaining);
            statement.setInt(2, totalEscapes);
            statement.setInt(3, totalAttacks);
            statement.setInt(4, totalEvacuated);
            statement.setDouble(5, safetyScore);

            statement.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error guardando reporte final");
            e.printStackTrace();
        }
    }
}