package com.mape.service;

import com.mape.entity.User;
import java.util.Optional;

public interface UserService extends GenericService<User> {

  Optional<User> findByEmail(String email);

  Optional<User> findByLogin(String login);

  boolean isPasswordCorrect(String login, String password);

  String validateLoginData(String login, String password);

  String validateRegistrationData(String login, String password, String password2, String email);
}
