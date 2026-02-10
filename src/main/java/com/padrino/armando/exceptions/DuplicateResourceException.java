package com.padrino.armando.exceptions;

public class DuplicateResourceException extends RuntimeException {

  public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
    super(String.format("%s ya existe con %s: '%s'", resourceName, fieldName, fieldValue));
  }
}