package com.veterinaria.repositorio;

import com.veterinaria.modelo.Mascota;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

/**
 * Acceso a datos de Mascota. No contiene lógica de negocio, solo persistencia.
 */
public class MascotaRepositorio {

    public void guardar(Mascota mascota, EntityManager em) {
        if (mascota.getId() == null) {
            em.persist(mascota);
        } else {
            em.merge(mascota);
        }
    }

    public Optional<Mascota> buscarPorId(Long id, EntityManager em) {
        return Optional.ofNullable(em.find(Mascota.class, id));
    }

    public Optional<Mascota> buscarPorFichaUnica(Long fichaUnica, EntityManager em) {
        return em.createQuery("SELECT m FROM Mascota m WHERE m.fichaUnica = :ficha", Mascota.class)
                .setParameter("ficha", fichaUnica)
                .getResultStream()
                .findFirst();
    }

    public List<Mascota> listarPorCliente(Long clienteId, EntityManager em) {
        return em.createQuery(
                        "SELECT m FROM Mascota m WHERE m.cliente.id = :clienteId ORDER BY m.nombre", Mascota.class)
                .setParameter("clienteId", clienteId)
                .getResultList();
    }

    public List<Mascota> buscarPorNombre(String nombre, EntityManager em) {
        return em.createQuery(
                        "SELECT m FROM Mascota m WHERE LOWER(m.nombre) LIKE LOWER(:nombre)", Mascota.class)
                .setParameter("nombre", "%" + nombre + "%")
                .getResultList();
    }
}
