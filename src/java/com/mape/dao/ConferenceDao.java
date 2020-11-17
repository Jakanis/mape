package com.mape.dao;

import com.mape.entity.Conference;
import com.mape.entity.User;
import java.util.List;

//CRUD
public interface ConferenceDao extends GenericDao<Conference> {

  void registerUser(Conference conference, User user);

  void unregisterUser(Conference conference, User user);

  boolean checkRegistration(Conference conference, User user);

  List<Conference> findByVisitor(User visitor);

}
