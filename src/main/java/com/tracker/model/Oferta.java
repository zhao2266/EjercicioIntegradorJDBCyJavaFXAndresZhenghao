package com.tracker.model;

import java.time.LocalDate;
import java.util.List;

public record Oferta(int id, Empresa empresa, String titulo, String descripcion, String estado,
                     LocalDate fechaPostulacion, String enlace, double salario, String notas) {

    public static final List<String> ESTADOS = List.of("pendiente", "entrevista", "rechazado", "aceptado");
}