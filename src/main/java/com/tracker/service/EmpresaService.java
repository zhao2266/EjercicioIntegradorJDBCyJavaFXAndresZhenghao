package com.tracker.service;

import com.tracker.dao.EmpresaDao;
import com.tracker.model.Empresa;

import java.sql.SQLException;
import java.util.List;

public class EmpresaService {

    private final EmpresaDao empresaDao = new EmpresaDao();

    public List<Empresa> listar() throws SQLException {
        return empresaDao.listar();
    }

    public void guardar(Empresa empresa) throws SQLException {
        if (empresa.nombre() == null || empresa.nombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (empresa.sector() == null || empresa.sector().isBlank()) {
            throw new IllegalArgumentException("El sector es obligatorio");
        }
        if (empresa.web() != null && !empresa.web().isBlank() && !empresa.web().startsWith("http")) {
            throw new IllegalArgumentException("La web debe empezar por http");
        }
        if (empresa.id() == 0) {
            empresaDao.insertar(empresa);
        } else {
            empresaDao.actualizar(empresa);
        }
    }

    public void borrar(int id) throws SQLException {
        empresaDao.borrar(id);
    }
}
