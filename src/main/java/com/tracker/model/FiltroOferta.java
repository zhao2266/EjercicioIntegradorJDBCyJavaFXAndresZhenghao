package com.tracker.model;

import java.time.LocalDate;

public record FiltroOferta(Empresa empresa, String estado, double salarioMinimo, LocalDate fechaDesde, String titulo) {
}