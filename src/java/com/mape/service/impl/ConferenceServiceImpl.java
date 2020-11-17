package com.mape.service.impl;

import com.mape.dao.ConferenceDao;
import com.mape.entity.Conference;
import com.mape.entity.User;
import com.mape.service.ConferenceService;
import java.util.List;

public class ConferenceServiceImpl extends GenericServiceImpl<Conference> implements
    ConferenceService {

  private ConferenceDao conferenceDao;

  public ConferenceServiceImpl(ConferenceDao conferenceDao) {
    this.genericDao = conferenceDao;
    this.conferenceDao = conferenceDao;
  }

  @Override
  public void registerUser(Conference conference, User user) {
    conferenceDao.registerUser(conference, user);
  }

  @Override
  public void unregisterUser(Conference conference, User user) {
    conferenceDao.unregisterUser(conference, user);
  }

  @Override
  public boolean checkRegistration(Conference conference, User user) {
    return conferenceDao.checkRegistration(conference, user);
  }

  @Override
  public void update(Conference entity) {
    conferenceDao.update(entity);
  }

  @Override
  public void create(Conference entity) {
    conferenceDao.create(entity);
  }

  @Override
  public List<Conference> findByVisitor(User visitor) {
    return conferenceDao.findByVisitor(visitor);
  }
}
