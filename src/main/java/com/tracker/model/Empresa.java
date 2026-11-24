package com.tracker.model;

import java.time.LocalDate;

public record Empresa(int id, String nombre, String sector, String web, LocalDate fechaRegistro) {

    public Empresa conId(int nuevoId) {
        return new Empresa(nuevoId, nombre, sector, web, fechaRegistro);
    }

    @Override
    public String toString() {
        return nombre;
    }
}