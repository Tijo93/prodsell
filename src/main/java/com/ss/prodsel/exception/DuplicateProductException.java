package com.ss.prodsel.exception;

public class DuplicateProductException extends RuntimeException {
  public DuplicateProductException(final String message) {
    super(message);
  }
}
