package com.tracker.controller;

import com.tracker.App;
import com.tracker.model.Entrevista;
import com.tracker.model.Oferta;
import com.tracker.service.EntrevistaService;
import com.tracker.util.Alertas;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import java.sql.SQLException;
import java.time.LocalDate;

public class DetalleController {

    @FXML private Label lblTitulo;
    @FXML private Label lblEstado;
    @FXML private Label lblFecha;
    @FXML private Label lblSalario;
    @FXML private Label lblEnlace;
    @FXML private Label lblDescripcion;
    @FXML private Label lblNotas;
    @FXML private Label lblEmpresa;
    @FXML private Label lblSector;
    @FXML private Label lblWeb;
    @FXML private TableView<Entrevista> tablaEntrevistas;
    @FXML private TableColumn<Entrevista, LocalDate> colFecha;
    @FXML private TableColumn<Entrevista, String> colTipo;
    @FXML private TableColumn<Entrevista, String> colResultado;
    @FXML private DatePicker dpFecha;
    @FXML private ChoiceBox<String> cmbTipo;
    @FXML private ChoiceBox<String> cmbResultado;
    @FXML private TextArea txtNotas;
    @FXML private Button btnGuardarEntrevista;

    private final EntrevistaService entrevistaService = new EntrevistaService();
    private final ObservableList<Entrevista> entrevistas = FXCollections.observableArrayList();
    private Oferta oferta;

    @FXML
    private void initialize() {
        colFecha.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().fecha()));
        colTipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().tipo()));
        colResultado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().resultado()));
        tablaEntrevistas.setItems(entrevistas);

        cmbTipo.setItems(FXCollections.observableArrayList(Entrevista.TIPOS));
        cmbResultado.setItems(FXCollections.observableArrayList(Entrevista.RESULTADOS));
        tablaEntrevistas.getSelectionModel().selectedItemProperty()
                .addListener((observable, anterior, actual) -> mostrarEntrevista(actual));

        btnGuardarEntrevista.disableProperty().bind(
                dpFecha.valueProperty().isNull()
                        .or(cmbTipo.valueProperty().isNull())
                        .or(cmbResultado.valueProperty().isNull()));
    }

    public void cargar(Oferta oferta) {
        this.oferta = oferta;
        lblTitulo.setText(oferta.titulo());
        lblEstado.setText(oferta.estado());
        lblFecha.setText(oferta.fechaPostulacion().toString());
        lblSalario.setText(oferta.salario() > 0 ? String.valueOf(oferta.salario()) : "No indicado");
        lblEnlace.setText(oferta.enlace());
        lblDescripcion.setText(oferta.descripcion());
        lblNotas.setText(oferta.notas());
        lblEmpresa.setText(oferta.empresa().nombre());
        lblSector.setText(oferta.empresa().sector());
        lblWeb.setText(oferta.empresa().web());
        recargarEntrevistas();
    }

    @FXML
    private void guardarEntrevista() {
        Entrevista seleccionada = tablaEntrevistas.getSelectionModel().getSelectedItem();
        int id = seleccionada == null ? 0 : seleccionada.id();
        try {
            entrevistaService.guardar(new Entrevista(
                    id, oferta.id(), dpFecha.getValue(), cmbTipo.getValue(), txtNotas.getText(), cmbResultado.getValue()));
            recargarEntrevistas();
            nuevaEntrevista();
        } catch (IllegalArgumentException | SQLException e) {
            Alertas.error(e.getMessage());
        }
    }

    @FXML
    private void nuevaEntrevista() {
        tablaEntrevistas.getSelectionModel().clearSelection();
        mostrarEntrevista(null);
    }

    @FXML
    private void borrarEntrevista() {
        Entrevista seleccionada = tablaEntrevistas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            Alertas.error("Selecciona una entrevista de la tabla");
            return;
        }
        if (!Alertas.confirmar("¿Eliminar la entrevista del " + seleccionada.fecha() + "?")) {
            return;
        }
        try {
            entrevistaService.borrar(seleccionada.id());
            recargarEntrevistas();
            nuevaEntrevista();
        } catch (SQLException e) {
            Alertas.error(e.getMessage());
        }
    }

    @FXML
    private void editar() {
        FormularioOfertaController controlador = App.cambiarEscena("formulario_oferta.fxml");
        controlador.cargar(oferta);
    }

    @FXML
    private void volver() {
        App.cambiarEscena("listado.fxml");
    }

    private void recargarEntrevistas() {
        try {
            entrevistas.setAll(entrevistaService.listarPorOferta(oferta.id()));
        } catch (SQLException e) {
            Alertas.error(e.getMessage());
        }
    }

    private void mostrarEntrevista(Entrevista entrevista) {
        if (entrevista == null) {
            dpFecha.setValue(null);
            cmbTipo.setValue(null);
            cmbResultado.setValue(null);
            txtNotas.clear();
        } else {
            dpFecha.setValue(entrevista.fecha());
            cmbTipo.setValue(entrevista.tipo());
            cmbResultado.setValue(entrevista.resultado());
            txtNotas.setText(entrevista.notas());
        }
    }
}
