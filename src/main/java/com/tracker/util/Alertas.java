package com.tracker.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Alertas {

    public static void error(String mensaje) {
        new Alert(Alert.AlertType.ERROR, mensaje).showAndWait();
    }

    public static void info(String mensaje) {
        new Alert(Alert.AlertType.INFORMATION, mensaje).showAndWait();
    }

    public static boolean confirmar(String mensaje) {
        return new Alert(Alert.AlertType.CONFIRMATION, mensaje)
                .showAndWait()
                .filter(boton -> boton == ButtonType.OK)
                .isPresent();
    }
}
