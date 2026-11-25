package com.tracker.controller;

import com.tracker.App;
import com.tracker.model.Empresa;
import com.tracker.service.EmpresaService;
import com.tracker.util.Alertas;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDate;

public class EmpresasController {

    @FXML private TableView<Empresa> tablaEmpresas;
    @FXML private TableColumn<Empresa, String> colNombre;
    @FXML private TableColumn<Empresa, String> colSector;
    @FXML private TableColumn<Empresa, String> colWeb;
    @FXML private TableColumn<Empresa, LocalDate> colRegistro;
    @FXML private TextField txtNombre;
    @FXML private TextField txtSector;
    @FXML private TextField txtWeb;
    @FXML private Button btnGuardar;

    private final EmpresaService empresaService = new EmpresaService();
    private final ObservableList<Empresa> empresas = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().nombre()));
        colSector.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().sector()));
        colWeb.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().web()));
        colRegistro.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().fechaRegistro()));
        tablaEmpresas.setItems(empresas);

        tablaEmpresas.getSelectionModel().selectedItemProperty()
                .addListener((observable, anterior, actual) -> mostrarEmpresa(actual));
        btnGuardar.disableProperty().bind(
                txtNombre.textProperty().isEmpty().or(txtSector.textProperty().isEmpty()));

        cargar();
    }

    @FXML
    private void guardar() {
        Empresa seleccionada = tablaEmpresas.getSelectionModel().getSelectedItem();
        int id = seleccionada == null ? 0 : seleccionada.id();
        LocalDate registro = seleccionada == null ? LocalDate.now() : seleccionada.fechaRegistro();
        try {
            empresaService.guardar(new Empresa(
                    id, txtNombre.getText().trim(), txtSector.getText().trim(), txtWeb.getText().trim(), registro));
            cargar();
            nueva();
        } catch (IllegalArgumentException | SQLException e) {
            Alertas.error(e.getMessage());
        }
    }

    @FXML
    private void nueva() {
        tablaEmpresas.getSelectionModel().clearSelection();
        mostrarEmpresa(null);
    }

    @FXML
    private void eliminar() {
        Empresa seleccionada = tablaEmpresas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            Alertas.error("Selecciona una empresa de la tabla");
            return;
        }
        if (!Alertas.confirmar("Se eliminará \"" + seleccionada.nombre() + "\" junto con sus candidaturas y entrevistas")) {
            return;
        }
        try {
            empresaService.borrar(seleccionada.id());
            cargar();
            nueva();
        } catch (SQLException e) {
            Alertas.error(e.getMessage());
        }
    }

    @FXML
    private void volver() {
        App.cambiarEscena("listado.fxml");
    }

    private void cargar() {
        try {
            empresas.setAll(empresaService.listar());
        } catch (SQLException e) {
            Alertas.error(e.getMessage());
        }
    }

    private void mostrarEmpresa(Empresa empresa) {
        if (empresa == null) {
            txtNombre.clear();
            txtSector.clear();
            txtWeb.clear();
        } else {
            txtNombre.setText(empresa.nombre());
            txtSector.setText(empresa.sector());
            txtWeb.setText(empresa.web());
        }
    }
}
