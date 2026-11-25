package com.tracker.dao;

import com.tracker.model.Empresa;
import com.tracker.model.FiltroOferta;
import com.tracker.model.Oferta;
import com.tracker.util.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OfertaDao {

    private static final String CONSULTA_BASE = """
            SELECT o.id, o.titulo, o.descripcion, o.estado, o.fechaPostulacion, o.enlace, o.salario, o.notas,
                   e.id AS idEmpresa, e.nombre, e.sector, e.web, e.fechaRegistro
            FROM oferta o
            JOIN empresa e ON o.id_empresa = e.id
            WHERE 1 = 1
            """;

    public List<Oferta> listar(FiltroOferta filtro) throws SQLException {
        StringBuilder sql = new StringBuilder(CONSULTA_BASE);
        List<Object> parametros = new ArrayList<>();

        if (filtro.empresa() != null) {
            sql.append(" AND o.id_empresa = ?");
            parametros.add(filtro.empresa().id());
        }
        if (filtro.estado() != null) {
            sql.append(" AND o.estado = ?");
            parametros.add(filtro.estado());
        }
        if (filtro.salarioMinimo() > 0) {
            sql.append(" AND o.salario >= ?");
            parametros.add(filtro.salarioMinimo());
        }
        if (filtro.fechaDesde() != null) {
            sql.append(" AND o.fechaPostulacion >= ?");
            parametros.add(filtro.fechaDesde().toString());
        }
        if (filtro.titulo() != null && !filtro.titulo().isBlank()) {
            sql.append(" AND o.titulo LIKE ?");
            parametros.add("%" + filtro.titulo().trim() + "%");
        }
        sql.append(" ORDER BY o.fechaPostulacion DESC");

        List<Oferta> ofertas = new ArrayList<>();
        try (Connection conexion = Conexion.obtener();
             PreparedStatement sentencia = conexion.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                sentencia.setObject(i + 1, parametros.get(i));
            }
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    ofertas.add(mapear(resultado));
                }
            }
        }
        return ofertas;
    }

    public void insertar(Oferta oferta) throws SQLException {
        try (Connection conexion = Conexion.obtener()) {
            insertar(conexion, oferta);
        }
    }

    public void insertar(Connection conexion, Oferta oferta) throws SQLException {
        String sql = """
                INSERT INTO oferta (id_empresa, titulo, descripcion, estado, fechaPostulacion, enlace, salario, notas)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            rellenar(sentencia, oferta);
            sentencia.executeUpdate();
        }
    }

    public void actualizar(Oferta oferta) throws SQLException {
        String sql = """
                UPDATE oferta SET id_empresa = ?, titulo = ?, descripcion = ?, estado = ?,
                       fechaPostulacion = ?, enlace = ?, salario = ?, notas = ?
                WHERE id = ?
                """;
        try (Connection conexion = Conexion.obtener();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            rellenar(sentencia, oferta);
            sentencia.setInt(9, oferta.id());
            sentencia.executeUpdate();
        }
    }

    public void cambiarEstado(int id, String estado) throws SQLException {
        try (Connection conexion = Conexion.obtener();
             PreparedStatement sentencia = conexion.prepareStatement("UPDATE oferta SET estado = ? WHERE id = ?")) {
            sentencia.setString(1, estado);
            sentencia.setInt(2, id);
            sentencia.executeUpdate();
        }
    }

    public void borrar(int id) throws SQLException {
        try (Connection conexion = Conexion.obtener();
             PreparedStatement sentencia = conexion.prepareStatement("DELETE FROM oferta WHERE id = ?")) {
            sentencia.setInt(1, id);
            sentencia.executeUpdate();
        }
    }

    private void rellenar(PreparedStatement sentencia, Oferta oferta) throws SQLException {
        sentencia.setInt(1, oferta.empresa().id());
        sentencia.setString(2, oferta.titulo());
        sentencia.setString(3, oferta.descripcion());
        sentencia.setString(4, oferta.estado());
        sentencia.setString(5, oferta.fechaPostulacion().toString());
        sentencia.setString(6, oferta.enlace());
        sentencia.setDouble(7, oferta.salario());
        sentencia.setString(8, oferta.notas());
    }

    private Oferta mapear(ResultSet resultado) throws SQLException {
        Empresa empresa = new Empresa(
                resultado.getInt("idEmpresa"),
                resultado.getString("nombre"),
                resultado.getString("sector"),
                resultado.getString("web"),
                LocalDate.parse(resultado.getString("fechaRegistro")));
        return new Oferta(
                resultado.getInt("id"),
                empresa,
                resultado.getString("titulo"),
                resultado.getString("descripcion"),
                resultado.getString("estado"),
                LocalDate.parse(resultado.getString("fechaPostulacion")),
                resultado.getString("enlace"),
                resultado.getDouble("salario"),
                resultado.getString("notas"));
    }
}
