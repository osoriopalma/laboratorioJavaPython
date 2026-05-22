package com.axity.dinosaurpark.persistence;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;

public class LiquibaseRunner {

    private final DatabaseConnection databaseConnection;

    public LiquibaseRunner() {
        this.databaseConnection = new DatabaseConnection();
    }

    public void runMigrations() {

        try (Connection connection = databaseConnection.getConnection()) {

            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(
                            new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(
                    "db/changelog/db.changelog-master.xml",
                    new ClassLoaderResourceAccessor(),
                    database);

            liquibase.update(
                    new Contexts(),
                    new LabelExpression());

            System.out.println("Migraciones Liquibase ejecutadas correctamente");

        } catch (Exception e) {
            System.out.println("Error ejecutando migraciones Liquibase");
            e.printStackTrace();
        }
    }
}