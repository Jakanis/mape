package com.mape.dao.exception;

public class InconsistentDatabaseException extends DaoException {

  public InconsistentDatabaseException() {
  }

  public InconsistentDatabaseException(String s) {
    super(s);
  }

  public InconsistentDatabaseException(String s, Throwable throwable) {
    super(s, throwable);
  }

  public InconsistentDatabaseException(Throwable throwable) {
    super(throwable);
  }
}
