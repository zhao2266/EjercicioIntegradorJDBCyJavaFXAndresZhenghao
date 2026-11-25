package com.tracker;

import com.tracker.util.Alertas;
import com.tracker.util.Conexion;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.sql.SQLException;

public class App extends Application {

    private static Stage stage;

    @Override
    public void start(Stage primaryStage) {
        try {
            Conexion.crearEsquema();
        } catch (SQLException | IOException e) {
            Alertas.error("No se pudo preparar la base de datos: " + e.getMessage());
            Platform.exit();
            return;
        }
        stage = primaryStage;
        stage.setTitle("Tracker de Candidaturas");
        cambiarEscena("listado.fxml");
        stage.show();
    }

    public static <T> T cambiarEscena(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.centerOnScreen();
            return loader.getController();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
