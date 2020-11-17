package com.mape.service.impl;

import com.mape.dao.UserDao;
import com.mape.entity.User;
import com.mape.service.UserService;
import com.mape.util.PasswordEncryption;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.extern.log4j.Log4j;

@Log4j
public class UserServiceImpl extends GenericServiceImpl<User> implements UserService {

  public static final String VALID_DATA = "valid";
  public static final String ERROR_FIELD_EMPTY = "fieldEmpty";
  public static final String ERROR_USER_LOGIN_NOT_EXISTS = "userLoginNotExists";
  public static final String ERROR_INCORRECT_PASSWORD = "incorrectPassword";
  public static final String ERROR_NOT_EQUAL_PASSWORDS = "notEqualPasswords";
  public static final String ERROR_NOT_VALID_EMAIL = "notEmail";
  public static final String ERROR_WEAK_PASSWORD = "weakPassword";
  public static final String ERROR_USER_LOGIN_EXISTS = "userLoginExists";
  public static final String ERROR_USER_EMAIL_EXISTS = "userEmailExists";
  private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
      Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
  private static final Pattern STRONG_PASSWORD_REGEX = Pattern
      .compile("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}", Pattern.CASE_INSENSITIVE);
  protected UserDao userDao;

  public UserServiceImpl(UserDao userDao) {
    this.genericDao = userDao;
    this.userDao = userDao;
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return userDao.findByEmail(email);
  }

  @Override
  public Optional<User> findByLogin(String login) {
    return userDao.findByLogin(login);
  }

  @Override
  public void update(User entity) {
    userDao.update(entity);
  }

  @Override
  public void create(User entity) {
    entity.setPassword(PasswordEncryption.encryptPassword(entity.getPassword()));
    userDao.create(entity);
  }

  private boolean isNullOrEmpty(String str) {
    return str == null || str.trim().isEmpty();
  }

  public String validateRegistrationData(String login, String password, String password2,
      String email) {
    if (isNullOrEmpty(login) || isNullOrEmpty(password) || isNullOrEmpty(password2) || isNullOrEmpty(email)) {
      return ERROR_FIELD_EMPTY;
    } else if (!password.equals(password2)) {
      return ERROR_NOT_EQUAL_PASSWORDS;
    } else if (!isEmail(email)) {
      return ERROR_NOT_VALID_EMAIL;
    } else if (!isStrongPassword(password)) {
      return ERROR_WEAK_PASSWORD;
    } else if (findByLogin(login).isPresent()) {
      return ERROR_USER_LOGIN_EXISTS;
    } else if (findByEmail(email).isPresent()) {
      return ERROR_USER_EMAIL_EXISTS;
    }
    return VALID_DATA;
  }

  private boolean isEmail(String login) {
    return VALID_EMAIL_ADDRESS_REGEX.matcher(login).matches();
  }

  private boolean isStrongPassword(String password) {
    return STRONG_PASSWORD_REGEX.matcher(password).matches();
  }

  @Override
  public String validateLoginData(String login, String password) {
    if (isNullOrEmpty(login) || isNullOrEmpty(password)) {
      return ERROR_FIELD_EMPTY;
    } else if (!findByLogin(login).isPresent()) {
      return ERROR_USER_LOGIN_NOT_EXISTS;
    } else if (!isPasswordCorrect(login, password)) {
      return ERROR_INCORRECT_PASSWORD;
    }
    return VALID_DATA;
  }

  public boolean isPasswordCorrect(String login, String password) {
    Optional<User> optionalUser = findByLogin(login);
    return optionalUser.map(user -> user.getPassword().equals(PasswordEncryption.encryptPassword(password)))
        .orElse(false);
  }
}
