package com.padrino.armando.exceptions;

/**
 * Excepción lanzada cuando se intenta crear un recurso duplicado
 *
 * @author Tu Nombre
 * @version 1.0
 */
public class DuplicateResourceException extends RuntimeException {

    /**
     * Constructor con mensaje personalizado
     *
     * @param message mensaje de error
     */
    public DuplicateResourceException(String message) {
        super(message);
    }

    /**
     * Constructor con detalles del recurso duplicado
     *
     * @param resourceName nombre del recurso (ej: "Producto")
     * @param fieldName nombre del campo (ej: "codigo")
     * @param fieldValue valor del campo duplicado
     */
    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s ya existe con %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
