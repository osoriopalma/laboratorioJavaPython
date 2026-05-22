package com.axity.dinosaurpark.config;

import java.io.InputStream;
import java.util.Properties;

public class ParkConfig {
    private static ParkConfig instance;
    private final Properties properties;

    private ParkConfig() {
        properties = new Properties();
        // Cargamos el archivo park.properties, en caso de que se genere una excepsion
        // se termina la ejecucuin de la palicación
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("park.properties")) {
            if (input == null) {
                throw new RuntimeException("No se encontró park.properties");
            }
            properties.load(input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // implementamos sigleton para que todos los clases trabajen con el mismo objeto
    // de configuración
    public static ParkConfig getInstance() {
        if (instance == null) {
            instance = new ParkConfig();
        }
        return instance;
    }

    public int getInt(String key, int defaultValue) {
        return Integer.parseInt(properties.getProperty(key, String.valueOf(defaultValue)));
    }

    public double getDouble(String key, double defaultValue) {
        return Double.parseDouble(properties.getProperty(key, String.valueOf(defaultValue)));
    }

    public String getString(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public long getSeed() {
        return getInt("simulation.seed", 42);
    }

    public int getTotalSteps() {
        return getInt("simulation.totalSteps", 100);
    }
}
