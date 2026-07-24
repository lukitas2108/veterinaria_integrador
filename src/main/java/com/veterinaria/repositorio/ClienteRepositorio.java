package com.veterinaria.repositorio;

import com.veterinaria.modelo.Cliente;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

/**
 * Acceso a datos de Cliente. No contiene lógica de negocio, solo persistencia.
 */
public class ClienteRepositorio {

    public void guardar(Cliente cliente, EntityManager em) {
        if (cliente.getId() == null) {
            em.persist(cliente);
        } else {
            em.merge(cliente);
        }
    }

    public Optional<Cliente> buscarPorId(Long id, EntityManager em) {
        return Optional.ofNullable(em.find(Cliente.class, id));
    }

    public Optional<Cliente> buscarPorDni(Integer dni, EntityManager em) {
        return em.createQuery("SELECT c FROM Cliente c WHERE c.dni = :dni", Cliente.class)
                .setParameter("dni", dni)
                .getResultStream()
                .findFirst();
    }

    public List<Cliente> listarActivos(EntityManager em) {
        return em.createQuery("SELECT c FROM Cliente c WHERE c.activo = true ORDER BY c.apellido", Cliente.class)
                .getResultList();
    }

    public List<Cliente> listarTodos(EntityManager em) {
        return em.createQuery("SELECT c FROM Cliente c ORDER BY c.apellido", Cliente.class)
                .getResultList();
    }
}
