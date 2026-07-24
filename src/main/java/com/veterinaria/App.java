package com.veterinaria;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Punto de entrada de la aplicación JavaFX.
 * Corre con: mvn javafx:run
 */
public class App extends Application {

    @Override
    public void start(Stage stagePrincipal) throws IOException {
        Parent raiz = FXMLLoader.load(
                getClass().getResource("/com/veterinaria/vista/clientes-view.fxml"));

        Scene escena = new Scene(raiz, 950, 650);

        stagePrincipal.setTitle("Sistema de Gestión Veterinaria");
        stagePrincipal.setScene(escena);
        stagePrincipal.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
