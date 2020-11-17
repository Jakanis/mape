package com.mape.dao;

import com.mape.entity.User;
import java.util.Optional;

public interface UserDao extends GenericDao<User> {

  Optional<User> findByEmail(String email);

  Optional<User> findByLogin(String login);

}
