package com.tracker.service;

import com.tracker.dao.EntrevistaDao;
import com.tracker.model.Entrevista;

import java.sql.SQLException;
import java.util.List;

public class EntrevistaService {

    private final EntrevistaDao entrevistaDao = new EntrevistaDao();

    public List<Entrevista> listarPorOferta(int idOferta) throws SQLException {
        return entrevistaDao.listarPorOferta(idOferta);
    }

    public void guardar(Entrevista entrevista) throws SQLException {
        if (entrevista.fecha() == null) {
            throw new IllegalArgumentException("La fecha es obligatoria");
        }
        if (entrevista.tipo() == null || entrevista.resultado() == null) {
            throw new IllegalArgumentException("El tipo y el resultado son obligatorios");
        }
        if (entrevista.id() == 0) {
            entrevistaDao.insertar(entrevista);
        } else {
            entrevistaDao.actualizar(entrevista);
        }
    }

    public void borrar(int id) throws SQLException {
        entrevistaDao.borrar(id);
    }
}
