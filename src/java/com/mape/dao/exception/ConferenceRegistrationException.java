package com.mape.dao.exception;

public class ConferenceRegistrationException extends RuntimeException {

  public ConferenceRegistrationException() {
  }

  public ConferenceRegistrationException(String s) {
    super(s);
  }

  public ConferenceRegistrationException(String s, Throwable throwable) {
    super(s, throwable);
  }

  public ConferenceRegistrationException(Throwable throwable) {
    super(throwable);
  }
}
