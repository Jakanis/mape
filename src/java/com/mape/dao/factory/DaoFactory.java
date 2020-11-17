package com.mape.dao.factory;

import com.mape.dao.ConferenceDao;
import com.mape.dao.LectureDao;
import com.mape.dao.RoleDao;
import com.mape.dao.SpeakerDao;
import com.mape.dao.UserDao;

public abstract class DaoFactory {

  public static DaoFactory getDAOFactory(DataSource dataSource) {
    switch (dataSource) {
      case MYSQL:
        return new MySqlDaoFactory();
      default:
        throw new IllegalArgumentException("Incorrect data source");
    }
  }

  public abstract UserDao getUserDao();

  public abstract SpeakerDao getSpeakerDao();

  public abstract LectureDao getLectureDao();

  public abstract ConferenceDao getConferenceDao();

  public abstract RoleDao getRoleDao();

  public enum DataSource {
    MYSQL
  }
}
