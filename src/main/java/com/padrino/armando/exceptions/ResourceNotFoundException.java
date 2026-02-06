package com.padrino.armando.exceptions;

/**
 * Excepción lanzada cuando no se encuentra un recurso solicitado
 *
 * @author Tu Nombre
 * @version 1.0
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor con mensaje personalizado
     *
     * @param message mensaje de error
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor con detalles del recurso no encontrado
     *
     * @param resourceName nombre del recurso (ej: "Producto")
     * @param fieldName nombre del campo (ej: "id")
     * @param fieldValue valor del campo (ej: 123)
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s no encontrado con %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
