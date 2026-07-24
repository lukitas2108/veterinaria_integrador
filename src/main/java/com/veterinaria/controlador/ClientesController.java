package com.veterinaria.controlador;

import com.veterinaria.excepciones.DatosInvalidosException;
import com.veterinaria.modelo.Cliente;
import com.veterinaria.modelo.Especie;
import com.veterinaria.modelo.Mascota;
import com.veterinaria.servicio.ClienteServicio;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ClientesController {

    private final ClienteServicio clienteServicio = new ClienteServicio();

    // --- Panel Clientes ---
    @FXML private TextField campoNombreCliente;
    @FXML private TextField campoApellidoCliente;
    @FXML private TextField campoDni;
    @FXML private TextField campoTelefono;
    @FXML private TextField campoCorreo;
    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, String> colApellido;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, Integer> colDni;
    @FXML private TableColumn<Cliente, String> colTelefono;

    // --- Panel Mascotas ---
    @FXML private Label labelClienteSeleccionado;
    @FXML private TextField campoFichaUnica;
    @FXML private TextField campoNombreMascota;
    @FXML private TextField campoRaza;
    @FXML private ComboBox<Especie> comboEspecie;
    @FXML private DatePicker fechaNacimientoMascota;
    @FXML private TableView<Mascota> tablaMascotas;
    @FXML private TableColumn<Mascota, Long> colFicha;
    @FXML private TableColumn<Mascota, String> colNombreMascota;
    @FXML private TableColumn<Mascota, Especie> colEspecie;
    @FXML private TableColumn<Mascota, String> colRaza;
    @FXML private TableColumn<Mascota, Integer> colEdad;

    private Cliente clienteSeleccionado;

    @FXML
    public void initialize() {
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        colFicha.setCellValueFactory(new PropertyValueFactory<>("fichaUnica"));
        colNombreMascota.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEspecie.setCellValueFactory(new PropertyValueFactory<>("especie"));
        colRaza.setCellValueFactory(new PropertyValueFactory<>("raza"));
        colEdad.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().calcularEdadEnAnios()));

        comboEspecie.setItems(FXCollections.observableArrayList(Especie.values()));

        tablaClientes.getSelectionModel().selectedItemProperty().addListener((obs, anterior, actual) -> {
            clienteSeleccionado = actual;
            if (actual != null) {
                labelClienteSeleccionado.setText("Mascotas de: " + actual.getNombreCompleto());
                cargarMascotasDeClienteSeleccionado();
            } else {
                labelClienteSeleccionado.setText("Seleccioná un cliente");
                tablaMascotas.setItems(FXCollections.observableArrayList());
            }
        });

        cargarClientes();
    }

    @FXML
    private void onGuardarCliente() {
        try {
            String nombre = campoNombreCliente.getText();
            String apellido = campoApellidoCliente.getText();
            Integer dni = parsearEntero(campoDni.getText(), "DNI");

            clienteServicio.registrarCliente(nombre, apellido, dni,
                    campoTelefono.getText(), campoCorreo.getText());

            limpiarFormularioCliente();
            cargarClientes();
            mostrarInfo("Cliente guardado correctamente.");
        } catch (DatosInvalidosException | NumberFormatException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void onGuardarMascota() {
        if (clienteSeleccionado == null) {
            mostrarError("Primero seleccioná un cliente de la tabla.");
            return;
        }
        try {
            Long fichaUnica = parsearLong(campoFichaUnica.getText(), "Ficha única");
            String nombre = campoNombreMascota.getText();
            String raza = campoRaza.getText();
            Especie especie = comboEspecie.getValue();
            LocalDate fechaNacimiento = fechaNacimientoMascota.getValue();

            if (especie == null) {
                throw new DatosInvalidosException("Seleccioná una especie.");
            }
            if (fechaNacimiento == null) {
                throw new DatosInvalidosException("Seleccioná la fecha de nacimiento.");
            }

            clienteServicio.registrarMascotaParaCliente(
                    clienteSeleccionado.getId(), fichaUnica, nombre, raza, especie, fechaNacimiento);

            limpiarFormularioMascota();
            cargarMascotasDeClienteSeleccionado();
            mostrarInfo("Mascota agregada correctamente.");
        } catch (DatosInvalidosException | NumberFormatException | DateTimeParseException e) {
            mostrarError(e.getMessage());
        }
    }

    private void cargarClientes() {
        ObservableList<Cliente> clientes = FXCollections.observableArrayList(
                clienteServicio.listarClientesActivos());
        tablaClientes.setItems(clientes);
    }

    private void cargarMascotasDeClienteSeleccionado() {
        if (clienteSeleccionado == null) return;
        ObservableList<Mascota> mascotas = FXCollections.observableArrayList(
                clienteServicio.listarMascotasDeCliente(clienteSeleccionado.getId()));
        tablaMascotas.setItems(mascotas);
    }

    private void limpiarFormularioCliente() {
        campoNombreCliente.clear();
        campoApellidoCliente.clear();
        campoDni.clear();
        campoTelefono.clear();
        campoCorreo.clear();
    }

    private void limpiarFormularioMascota() {
        campoFichaUnica.clear();
        campoNombreMascota.clear();
        campoRaza.clear();
        comboEspecie.setValue(null);
        fechaNacimientoMascota.setValue(null);
    }

    private Integer parsearEntero(String texto, String nombreCampo) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException | NullPointerException e) {
            throw new DatosInvalidosException(nombreCampo + " debe ser un número válido.");
        }
    }

    private Long parsearLong(String texto, String nombreCampo) {
        try {
            return Long.parseLong(texto.trim());
        } catch (NumberFormatException | NullPointerException e) {
            throw new DatosInvalidosException(nombreCampo + " debe ser un número válido.");
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensaje, ButtonType.OK);
        alert.setHeaderText("No se pudo completar la operación");
        alert.showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, mensaje, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
