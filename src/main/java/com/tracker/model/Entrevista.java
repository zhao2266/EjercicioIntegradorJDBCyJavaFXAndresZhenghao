package com.tracker.model;

import java.time.LocalDate;
import java.util.List;

public record Entrevista(int id, int idOferta, LocalDate fecha, String tipo, String notas, String resultado) {

    public static final List<String> TIPOS = List.of("telefonica", "tecnica", "presencial", "online");
    public static final List<String> RESULTADOS = List.of("pendiente", "positivo", "negativo");
}