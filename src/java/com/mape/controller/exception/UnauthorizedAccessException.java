package com.mape.controller.exception;

public class UnauthorizedAccessException extends RuntimeException {

  public UnauthorizedAccessException(String s, Throwable throwable) {
    super(s, throwable);
  }

  public UnauthorizedAccessException(Throwable throwable) {
    super(throwable);
  }

  public UnauthorizedAccessException() {
  }

  public UnauthorizedAccessException(String s) {
    super(s);
  }
}
