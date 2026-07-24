package com.veterinaria.modelo;

import com.veterinaria.excepciones.DatosInvalidosException;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

@Entity
@Table(name = "mascotas")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long fichaUnica;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String raza;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Especie especie;

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    @Column(nullable = false)
    private boolean activo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    protected Mascota() {
        // constructor vacío requerido por JPA
    }

    public Mascota(Long fichaUnica, String nombre, String raza, Especie especie, LocalDate fechaNacimiento) {
        if (fichaUnica == null) {
            throw new DatosInvalidosException("La ficha única de la mascota es obligatoria.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new DatosInvalidosException("El nombre de la mascota no puede estar vacío.");
        }
        if (especie == null) {
            throw new DatosInvalidosException("La especie de la mascota es obligatoria.");
        }
        if (fechaNacimiento == null || fechaNacimiento.isAfter(LocalDate.now())) {
            throw new DatosInvalidosException("La fecha de nacimiento no es válida.");
        }
        this.fichaUnica = fichaUnica;
        this.nombre = nombre;
        this.raza = raza;
        this.especie = especie;
        this.fechaNacimiento = fechaNacimiento;
        this.activo = true;
    }

    /**
     * Regla de negocio: una mascota no puede existir sin un cliente dueño.
     * Se llama internamente desde Cliente.agregarMascota() para mantener la relación consistente.
     */
    void asignarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new DatosInvalidosException("Toda mascota debe pertenecer a un cliente.");
        }
        this.cliente = cliente;
    }

    public int calcularEdadEnAnios() {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    public void darDeBaja() {
        this.activo = false;
    }

    public void reactivar() {
        this.activo = true;
    }

    // === Getters ===

    public Long getId() {
        return id;
    }

    public Long getFichaUnica() {
        return fichaUnica;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRaza() {
        return raza;
    }

    public Especie getEspecie() {
        return especie;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public boolean isActivo() {
        return activo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    // === Setters (solo para campos editables sin regla de negocio asociada) ===

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new DatosInvalidosException("El nombre de la mascota no puede estar vacío.");
        }
        this.nombre = nombre;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mascota mascota)) return false;
        return id != null && id.equals(mascota.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fichaUnica);
    }

    @Override
    public String toString() {
        return "Mascota{id=" + id + ", nombre='" + nombre + "', especie=" + especie + '}';
    }
}
