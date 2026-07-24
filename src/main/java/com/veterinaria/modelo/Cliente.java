package com.veterinaria.modelo;

import com.veterinaria.excepciones.DatosInvalidosException;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private Integer dni;

    private String telefono;

    private String correoElectronico;

    @Column(nullable = false)
    private boolean activo;

    @OneToMany(mappedBy = "cliente", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Mascota> mascotas = new ArrayList<>();

    protected Cliente() {
        // constructor vacío requerido por JPA
    }

    public Cliente(String nombre, String apellido, Integer dni, String telefono, String correoElectronico) {
        if (nombre == null || nombre.isBlank()) {
            throw new DatosInvalidosException("El nombre del cliente no puede estar vacío.");
        }
        if (apellido == null || apellido.isBlank()) {
            throw new DatosInvalidosException("El apellido del cliente no puede estar vacío.");
        }
        if (dni == null || dni <= 0) {
            throw new DatosInvalidosException("El DNI del cliente no es válido.");
        }
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.activo = true;
    }

    /**
     * Asocia una mascota a este cliente, manteniendo la relación bidireccional consistente.
     */
    public void agregarMascota(Mascota mascota) {
        if (mascota == null) {
            throw new DatosInvalidosException("No se puede agregar una mascota nula.");
        }
        if (!this.mascotas.contains(mascota)) {
            this.mascotas.add(mascota);
            mascota.asignarCliente(this);
        }
    }

    public void darDeBaja() {
        this.activo = false;
    }

    public void reactivar() {
        this.activo = true;
    }

    public String getNombreCompleto() {
        return apellido + ", " + nombre;
    }

    // === Getters ===

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Integer getDni() {
        return dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public boolean isActivo() {
        return activo;
    }

    public List<Mascota> getMascotas() {
        return List.copyOf(mascotas);
    }

    // === Setters (solo para campos editables sin regla de negocio asociada) ===

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new DatosInvalidosException("El nombre del cliente no puede estar vacío.");
        }
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        if (apellido == null || apellido.isBlank()) {
            throw new DatosInvalidosException("El apellido del cliente no puede estar vacío.");
        }
        this.apellido = apellido;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente cliente)) return false;
        return id != null && id.equals(cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dni);
    }

    @Override
    public String toString() {
        return "Cliente{id=" + id + ", nombre='" + getNombreCompleto() + "', dni=" + dni + '}';
    }
}
