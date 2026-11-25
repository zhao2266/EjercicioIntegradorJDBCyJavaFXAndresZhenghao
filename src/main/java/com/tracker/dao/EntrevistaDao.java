package com.tracker.dao;

import com.tracker.model.Entrevista;
import com.tracker.util.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EntrevistaDao {

    public List<Entrevista> listarPorOferta(int idOferta) throws SQLException {
        List<Entrevista> entrevistas = new ArrayList<>();
        String sql = "SELECT * FROM entrevista WHERE id_oferta = ? ORDER BY fecha DESC";
        try (Connection conexion = Conexion.obtener();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idOferta);
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    entrevistas.add(new Entrevista(
                            resultado.getInt("id"),
                            resultado.getInt("id_oferta"),
                            LocalDate.parse(resultado.getString("fecha")),
                            resultado.getString("tipo"),
                            resultado.getString("notas"),
                            resultado.getString("resultado")));
                }
            }
        }
        return entrevistas;
    }

    public void insertar(Entrevista entrevista) throws SQLException {
        String sql = "INSERT INTO entrevista (id_oferta, fecha, tipo, notas, resultado) VALUES (?, ?, ?, ?, ?)";
        try (Connection conexion = Conexion.obtener();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, entrevista.idOferta());
            sentencia.setString(2, entrevista.fecha().toString());
            sentencia.setString(3, entrevista.tipo());
            sentencia.setString(4, entrevista.notas());
            sentencia.setString(5, entrevista.resultado());
            sentencia.executeUpdate();
        }
    }

    public void actualizar(Entrevista entrevista) throws SQLException {
        String sql = "UPDATE entrevista SET fecha = ?, tipo = ?, notas = ?, resultado = ? WHERE id = ?";
        try (Connection conexion = Conexion.obtener();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, entrevista.fecha().toString());
            sentencia.setString(2, entrevista.tipo());
            sentencia.setString(3, entrevista.notas());
            sentencia.setString(4, entrevista.resultado());
            sentencia.setInt(5, entrevista.id());
            sentencia.executeUpdate();
        }
    }

    public void borrar(int id) throws SQLException {
        try (Connection conexion = Conexion.obtener();
             PreparedStatement sentencia = conexion.prepareStatement("DELETE FROM entrevista WHERE id = ?")) {
            sentencia.setInt(1, id);
            sentencia.executeUpdate();
        }
    }
}
