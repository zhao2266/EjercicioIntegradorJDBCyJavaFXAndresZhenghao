package com.tracker.dao;

import com.tracker.model.Empresa;
import com.tracker.util.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpresaDao {

    public List<Empresa> listar() throws SQLException {
        List<Empresa> empresas = new ArrayList<>();
        try (Connection conexion = Conexion.obtener();
             Statement sentencia = conexion.createStatement();
             ResultSet resultado = sentencia.executeQuery("SELECT * FROM empresa ORDER BY nombre")) {
            while (resultado.next()) {
                empresas.add(mapear(resultado));
            }
        }
        return empresas;
    }

    public Empresa buscarPorNombre(Connection conexion, String nombre) throws SQLException {
        try (PreparedStatement sentencia = conexion.prepareStatement("SELECT * FROM empresa WHERE nombre = ?")) {
            sentencia.setString(1, nombre);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next() ? mapear(resultado) : null;
            }
        }
    }

    public Empresa insertar(Empresa empresa) throws SQLException {
        try (Connection conexion = Conexion.obtener()) {
            return insertar(conexion, empresa);
        }
    }

    public Empresa insertar(Connection conexion, Empresa empresa) throws SQLException {
        String sql = "INSERT INTO empresa (nombre, sector, web, fechaRegistro) VALUES (?, ?, ?, ?)";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setString(1, empresa.nombre());
            sentencia.setString(2, empresa.sector());
            sentencia.setString(3, empresa.web());
            sentencia.setString(4, empresa.fechaRegistro().toString());
            sentencia.executeUpdate();
            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                claves.next();
                return empresa.conId(claves.getInt(1));
            }
        }
    }

    public void actualizar(Empresa empresa) throws SQLException {
        String sql = "UPDATE empresa SET nombre = ?, sector = ?, web = ? WHERE id = ?";
        try (Connection conexion = Conexion.obtener();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, empresa.nombre());
            sentencia.setString(2, empresa.sector());
            sentencia.setString(3, empresa.web());
            sentencia.setInt(4, empresa.id());
            sentencia.executeUpdate();
        }
    }

    public void borrar(int id) throws SQLException {
        try (Connection conexion = Conexion.obtener();
             PreparedStatement sentencia = conexion.prepareStatement("DELETE FROM empresa WHERE id = ?")) {
            sentencia.setInt(1, id);
            sentencia.executeUpdate();
        }
    }

    private Empresa mapear(ResultSet resultado) throws SQLException {
        return new Empresa(
                resultado.getInt("id"),
                resultado.getString("nombre"),
                resultado.getString("sector"),
                resultado.getString("web"),
                LocalDate.parse(resultado.getString("fechaRegistro")));
    }
}
