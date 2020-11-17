package com.mape.dao.exception;

public class UserRoleException extends DaoException {

  public UserRoleException() {
  }

  public UserRoleException(String s) {
    super(s);
  }

  public UserRoleException(String s, Throwable throwable) {
    super(s, throwable);
  }

  public UserRoleException(Throwable throwable) {
    super(throwable);
  }
}
