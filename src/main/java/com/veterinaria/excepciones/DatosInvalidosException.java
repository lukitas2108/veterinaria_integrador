package com.veterinaria.excepciones;

/**
 * Se lanza cuando un dato de entrada no cumple una regla de validación del dominio.
 */
public class DatosInvalidosException extends RuntimeException {

    public DatosInvalidosException(String mensaje) {
        super(mensaje);
    }
}
