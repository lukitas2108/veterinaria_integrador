package com.veterinaria.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Punto único de acceso al EntityManagerFactory.
 * El nombre "veterinariaPU" tiene que coincidir con el definido en persistence.xml.
 */
public class JpaUtil {

    private static final String UNIDAD_PERSISTENCIA = "veterinariaPU";
    private static EntityManagerFactory entityManagerFactory;

    private JpaUtil() {
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory(UNIDAD_PERSISTENCIA);
        }
        return entityManagerFactory;
    }

    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    public static void cerrar() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
