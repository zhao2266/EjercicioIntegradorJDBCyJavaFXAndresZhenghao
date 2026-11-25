package com.tracker.controller;

import com.tracker.App;
import com.tracker.model.Empresa;
import com.tracker.model.FiltroOferta;
import com.tracker.model.Oferta;
import com.tracker.service.EmpresaService;
import com.tracker.service.OfertaService;
import com.tracker.util.Alertas;
import com.tracker.util.Campos;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class ListadoController {

    @FXML private TextField txtTitulo;
    @FXML private ChoiceBox<Empresa> cmbEmpresa;
    @FXML private ChoiceBox<String> cmbEstado;
    @FXML private TextField txtSalarioMinimo;
    @FXML private DatePicker dpDesde;
    @FXML private ChoiceBox<String> cmbNuevoEstado;
    @FXML private TableView<Oferta> tablaOfertas;
    @FXML private TableColumn<Oferta, String> colTitulo;
    @FXML private TableColumn<Oferta, String> colEmpresa;
    @FXML private TableColumn<Oferta, String> colEstado;
    @FXML private TableColumn<Oferta, LocalDate> colFecha;
    @FXML private TableColumn<Oferta, Number> colSalario;

    private final OfertaService ofertaService = new OfertaService();
    private final EmpresaService empresaService = new EmpresaService();
    private final ObservableList<Oferta> ofertas = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colTitulo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().titulo()));
        colEmpresa.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().empresa().nombre()));
        colEstado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().estado()));
        colFecha.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().fechaPostulacion()));
        colSalario.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().salario()));
        tablaOfertas.setItems(ofertas);

        cmbEstado.setItems(FXCollections.observableArrayList(Oferta.ESTADOS));
        cmbNuevoEstado.setItems(FXCollections.observableArrayList(Oferta.ESTADOS));

        cargarEmpresas();
        filtrar();
    }

    @FXML
    private void filtrar() {
        try {
            FiltroOferta filtro = new FiltroOferta(
                    cmbEmpresa.getValue(),
                    cmbEstado.getValue(),
                    Campos.decimal(txtSalarioMinimo.getText(), "salario mínimo"),
                    dpDesde.getValue(),
                    txtTitulo.getText());
            ofertas.setAll(ofertaService.listar(filtro));
        } catch (IllegalArgumentException | SQLException e) {
            Alertas.error(e.getMessage());
        }
    }

    @FXML
    private void limpiar() {
        txtTitulo.clear();
        cmbEmpresa.setValue(null);
        cmbEstado.setValue(null);
        txtSalarioMinimo.clear();
        dpDesde.setValue(null);
        filtrar();
    }

    @FXML
    private void nueva() {
        FormularioOfertaController controlador = App.cambiarEscena("formulario_oferta.fxml");
        controlador.cargar(null);
    }

    @FXML
    private void editar() {
        Oferta oferta = seleccionada();
        if (oferta == null) {
            return;
        }
        FormularioOfertaController controlador = App.cambiarEscena("formulario_oferta.fxml");
        controlador.cargar(oferta);
    }

    @FXML
    private void verDetalle() {
        Oferta oferta = seleccionada();
        if (oferta == null) {
            return;
        }
        DetalleController controlador = App.cambiarEscena("detalle.fxml");
        controlador.cargar(oferta);
    }

    @FXML
    private void borrar() {
        Oferta oferta = seleccionada();
        if (oferta == null || !Alertas.confirmar("¿Eliminar la candidatura \"" + oferta.titulo() + "\"?")) {
            return;
        }
        try {
            ofertaService.borrar(oferta.id());
            filtrar();
        } catch (SQLException e) {
            Alertas.error(e.getMessage());
        }
    }

    @FXML
    private void cambiarEstado() {
        Oferta oferta = seleccionada();
        if (oferta == null) {
            return;
        }
        String estado = cmbNuevoEstado.getValue();
        if (estado == null) {
            Alertas.error("Elige el nuevo estado");
            return;
        }
        try {
            ofertaService.cambiarEstado(oferta.id(), estado);
            filtrar();
        } catch (SQLException e) {
            Alertas.error(e.getMessage());
        }
    }

    @FXML
    private void importar() {
        FileChooser selector = new FileChooser();
        selector.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        File archivo = selector.showOpenDialog(tablaOfertas.getScene().getWindow());
        if (archivo == null) {
            return;
        }
        try {
            int total = ofertaService.importarCsv(archivo.toPath());
            cargarEmpresas();
            filtrar();
            Alertas.info("Candidaturas importadas: " + total);
        } catch (IOException | SQLException | RuntimeException e) {
            Alertas.error("No se pudo importar el archivo: " + e.getMessage());
        }
    }

    @FXML
    private void irEmpresas() {
        App.cambiarEscena("empresas.fxml");
    }

    private void cargarEmpresas() {
        try {
            cmbEmpresa.setItems(FXCollections.observableArrayList(empresaService.listar()));
        } catch (SQLException e) {
            Alertas.error(e.getMessage());
        }
    }

    private Oferta seleccionada() {
        Oferta oferta = tablaOfertas.getSelectionModel().getSelectedItem();
        if (oferta == null) {
            Alertas.error("Selecciona una candidatura de la tabla");
        }
        return oferta;
    }
}
