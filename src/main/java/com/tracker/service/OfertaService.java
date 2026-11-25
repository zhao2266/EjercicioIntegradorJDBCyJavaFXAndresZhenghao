package com.tracker.service;

import com.tracker.dao.EmpresaDao;
import com.tracker.dao.OfertaDao;
import com.tracker.model.Empresa;
import com.tracker.model.FiltroOferta;
import com.tracker.model.Oferta;
import com.tracker.util.Conexion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OfertaService {

    private static final DateTimeFormatter FORMATO_CSV = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final OfertaDao ofertaDao = new OfertaDao();
    private final EmpresaDao empresaDao = new EmpresaDao();

    public List<Oferta> listar(FiltroOferta filtro) throws SQLException {
        return ofertaDao.listar(filtro);
    }

    public void guardar(Oferta oferta) throws SQLException {
        if (oferta.titulo() == null || oferta.titulo().isBlank()) {
            throw new IllegalArgumentException("El título es obligatorio");
        }
        if (oferta.empresa() == null) {
            throw new IllegalArgumentException("Selecciona una empresa");
        }
        if (oferta.estado() == null || oferta.fechaPostulacion() == null) {
            throw new IllegalArgumentException("El estado y la fecha son obligatorios");
        }
        if (oferta.salario() < 0) {
            throw new IllegalArgumentException("El salario no puede ser negativo");
        }
        if (oferta.id() == 0) {
            ofertaDao.insertar(oferta);
        } else {
            ofertaDao.actualizar(oferta);
        }
    }

    public void cambiarEstado(int id, String estado) throws SQLException {
        ofertaDao.cambiarEstado(id, estado);
    }

    public void borrar(int id) throws SQLException {
        ofertaDao.borrar(id);
    }

    public int importarCsv(Path ruta) throws IOException, SQLException {
        List<String> lineas = Files.readAllLines(ruta);
        int importadas = 0;
        try (Connection conexion = Conexion.obtener()) {
            conexion.setAutoCommit(false);
            try {
                for (String linea : lineas.subList(1, lineas.size())) {
                    if (linea.isBlank()) {
                        continue;
                    }
                    String[] campos = linea.split(";", -1);
                    if (campos.length < 7) {
                        throw new IllegalArgumentException("Línea con formato incorrecto: " + linea);
                    }
                    String nombreEmpresa = campos[2].trim();
                    Empresa empresa = empresaDao.buscarPorNombre(conexion, nombreEmpresa);
                    if (empresa == null) {
                        empresa = empresaDao.insertar(conexion,
                                new Empresa(0, nombreEmpresa, "Sin especificar", "", LocalDate.now()));
                    }
                    Oferta oferta = new Oferta(0, empresa, campos[1].trim(), "", campos[3].trim(),
                            LocalDate.parse(campos[4].trim(), FORMATO_CSV), campos[5].trim(), 0, campos[6].trim());
                    ofertaDao.insertar(conexion, oferta);
                    importadas++;
                }
                conexion.commit();
            } catch (SQLException | RuntimeException e) {
                conexion.rollback();
                throw e;
            }
        }
        return importadas;
    }
}
