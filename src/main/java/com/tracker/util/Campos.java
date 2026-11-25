package com.tracker.util;

public class Campos {

    public static double decimal(String texto, String nombreCampo) {
        String limpio = texto == null ? "" : texto.trim().replace(',', '.');
        if (limpio.isEmpty()) {
            return 0;
        }
        try {
            return Double.parseDouble(limpio);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo " + nombreCampo + " debe ser un número");
        }
    }
}
