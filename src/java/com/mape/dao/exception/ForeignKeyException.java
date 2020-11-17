package com.mape.dao.exception;

public class ForeignKeyException extends DaoException {

  public ForeignKeyException() {
  }

  public ForeignKeyException(String s) {
    super(s);
  }

  public ForeignKeyException(String s, Throwable throwable) {
    super(s, throwable);
  }

  public ForeignKeyException(Throwable throwable) {
    super(throwable);
  }
}
