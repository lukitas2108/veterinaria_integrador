package com.veterinaria.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {

    @FXML
    private Label labelEstado;

    @FXML
    public void initialize() {
        labelEstado.setText("Sistema de Gestión Veterinaria - listo.");
    }

    @FXML
    private void onProbarConexion() {
        try {
            com.veterinaria.util.JpaUtil.getEntityManagerFactory();
            labelEstado.setText("Conexión a la base de datos OK.");
        } catch (Exception e) {
            labelEstado.setText("Error de conexión: " + e.getMessage());
        }
    }
}
