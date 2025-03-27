package com.ss.prodsel.exception;

public class ProductNotFoundException extends RuntimeException {
  public ProductNotFoundException(final String message) {
    super(message);
  }
}
