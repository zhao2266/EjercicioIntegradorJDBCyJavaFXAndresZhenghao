package com.tracker.controller;

import com.tracker.App;
import com.tracker.model.Empresa;
import com.tracker.model.Oferta;
import com.tracker.service.EmpresaService;
import com.tracker.service.OfertaService;
import com.tracker.util.Alertas;
import com.tracker.util.Campos;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDate;

public class FormularioOfertaController {

    @FXML private Label lblTitulo;
    @FXML private TextField txtTitulo;
    @FXML private ChoiceBox<Empresa> cmbEmpresa;
    @FXML private ChoiceBox<String> cmbEstado;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtSalario;
    @FXML private TextField txtEnlace;
    @FXML private TextArea txtDescripcion;
    @FXML private TextArea txtNotas;
    @FXML private Button btnGuardar;

    private final OfertaService ofertaService = new OfertaService();
    private final EmpresaService empresaService = new EmpresaService();
    private int idOferta;

    @FXML
    private void initialize() {
        cmbEstado.setItems(FXCollections.observableArrayList(Oferta.ESTADOS));
        cmbEstado.setValue("pendiente");
        dpFecha.setValue(LocalDate.now());
        try {
            cmbEmpresa.setItems(FXCollections.observableArrayList(empresaService.listar()));
        } catch (SQLException e) {
            Alertas.error(e.getMessage());
        }
        btnGuardar.disableProperty().bind(
                txtTitulo.textProperty().isEmpty()
                        .or(cmbEmpresa.valueProperty().isNull())
                        .or(cmbEstado.valueProperty().isNull())
                        .or(dpFecha.valueProperty().isNull()));
    }

    public void cargar(Oferta oferta) {
        if (oferta == null) {
            return;
        }
        idOferta = oferta.id();
        lblTitulo.setText("Editar candidatura");
        txtTitulo.setText(oferta.titulo());
        cmbEmpresa.setValue(oferta.empresa());
        cmbEstado.setValue(oferta.estado());
        dpFecha.setValue(oferta.fechaPostulacion());
        txtSalario.setText(oferta.salario() > 0 ? String.valueOf(oferta.salario()) : "");
        txtEnlace.setText(oferta.enlace());
        txtDescripcion.setText(oferta.descripcion());
        txtNotas.setText(oferta.notas());
    }

    @FXML
    private void guardar() {
        try {
            Oferta oferta = new Oferta(
                    idOferta,
                    cmbEmpresa.getValue(),
                    txtTitulo.getText().trim(),
                    txtDescripcion.getText(),
                    cmbEstado.getValue(),
                    dpFecha.getValue(),
                    txtEnlace.getText(),
                    Campos.decimal(txtSalario.getText(), "salario"),
                    txtNotas.getText());
            ofertaService.guardar(oferta);
            App.cambiarEscena("listado.fxml");
        } catch (IllegalArgumentException | SQLException e) {
            Alertas.error(e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        App.cambiarEscena("listado.fxml");
    }
}
