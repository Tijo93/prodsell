package com.ss.prodsel.exception;

public class InsufficientQuantityException extends RuntimeException {
  public InsufficientQuantityException(final String message) {
    super(message);
  }
}
