package com.veterinaria.servicio;

import com.veterinaria.excepciones.DatosInvalidosException;
import com.veterinaria.modelo.Cliente;
import com.veterinaria.modelo.Especie;
import com.veterinaria.modelo.Mascota;
import com.veterinaria.repositorio.ClienteRepositorio;
import com.veterinaria.repositorio.MascotaRepositorio;
import com.veterinaria.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Coordina la persistencia de Cliente y Mascota dentro de una transacción.
 * Es la capa que usan los controladores de JavaFX; nunca deben usar EntityManager directamente.
 */
public class ClienteServicio {

    private final ClienteRepositorio clienteRepositorio = new ClienteRepositorio();
    private final MascotaRepositorio mascotaRepositorio = new MascotaRepositorio();

    public Cliente registrarCliente(String nombre, String apellido, Integer dni,
                                     String telefono, String correoElectronico) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            if (clienteRepositorio.buscarPorDni(dni, em).isPresent()) {
                throw new DatosInvalidosException("Ya existe un cliente registrado con ese DNI.");
            }

            Cliente cliente = new Cliente(nombre, apellido, dni, telefono, correoElectronico);
            clienteRepositorio.guardar(cliente, em);

            tx.commit();
            return cliente;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Mascota registrarMascotaParaCliente(Long clienteId, Long fichaUnica, String nombre,
                                                String raza, Especie especie, LocalDate fechaNacimiento) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Cliente cliente = clienteRepositorio.buscarPorId(clienteId, em)
                    .orElseThrow(() -> new DatosInvalidosException("No existe un cliente con id " + clienteId));

            if (mascotaRepositorio.buscarPorFichaUnica(fichaUnica, em).isPresent()) {
                throw new DatosInvalidosException("Ya existe una mascota con esa ficha única.");
            }

            Mascota mascota = new Mascota(fichaUnica, nombre, raza, especie, fechaNacimiento);
            cliente.agregarMascota(mascota);

            mascotaRepositorio.guardar(mascota, em);
            clienteRepositorio.guardar(cliente, em);

            tx.commit();
            return mascota;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void darDeBajaMascota(Long mascotaId) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Mascota mascota = mascotaRepositorio.buscarPorId(mascotaId, em)
                    .orElseThrow(() -> new DatosInvalidosException("No existe una mascota con id " + mascotaId));
            mascota.darDeBaja();
            mascotaRepositorio.guardar(mascota, em);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Optional<Cliente> buscarClientePorId(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return clienteRepositorio.buscarPorId(id, em);
        } finally {
            em.close();
        }
    }

    public List<Cliente> listarClientesActivos() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return clienteRepositorio.listarActivos(em);
        } finally {
            em.close();
        }
    }

    public List<Mascota> listarMascotasDeCliente(Long clienteId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return mascotaRepositorio.listarPorCliente(clienteId, em);
        } finally {
            em.close();
        }
    }
}
