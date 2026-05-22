package com.axity.dinosaurpark.persistence;

import com.axity.dinosaurpark.config.ParkConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private final ParkConfig config;

    public DatabaseConnection() {
        this.config = ParkConfig.getInstance();
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                config.getString("db.url", ""),
                config.getString("db.user", ""),
                config.getString("db.password", ""));
    }
}