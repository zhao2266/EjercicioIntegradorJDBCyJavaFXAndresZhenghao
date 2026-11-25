package com.tracker.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Conexion {

    private static final Properties CONFIG = cargarConfiguracion();

    private static Properties cargarConfiguracion() {
        Properties propiedades = new Properties();
        try (InputStream entrada = Conexion.class.getResourceAsStream("/config.properties")) {
            propiedades.load(entrada);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer config.properties", e);
        }
        return propiedades;
    }

    public static Connection obtener() throws SQLException {
        return DriverManager.getConnection(
                CONFIG.getProperty("db.url"),
                CONFIG.getProperty("db.user"),
                CONFIG.getProperty("db.password"));
    }

    public static void crearEsquema() throws SQLException, IOException {
        String script;
        try (InputStream entrada = Conexion.class.getResourceAsStream("/schema.sql")) {
            script = new String(entrada.readAllBytes(), StandardCharsets.UTF_8);
        }
        try (Connection conexion = obtener(); Statement sentencia = conexion.createStatement()) {
            for (String instruccion : script.split(";")) {
                if (!instruccion.isBlank()) {
                    sentencia.execute(instruccion);
                }
            }
        }
    }
}
