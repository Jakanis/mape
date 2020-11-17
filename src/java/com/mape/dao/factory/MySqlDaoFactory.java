package com.mape.dao.factory;

import com.mape.dao.ConferenceDao;
import com.mape.dao.LectureDao;
import com.mape.dao.RoleDao;
import com.mape.dao.SpeakerDao;
import com.mape.dao.UserDao;
import com.mape.dao.mysqlimpl.ConferenceDaoMySqlImpl;
import com.mape.dao.mysqlimpl.LectureDaoMySqlImpl;
import com.mape.dao.mysqlimpl.RoleDaoMySqlImpl;
import com.mape.dao.mysqlimpl.SpeakerDaoMySqlImpl;
import com.mape.dao.mysqlimpl.UserDaoMySqlImpl;

public class MySqlDaoFactory extends DaoFactory {

  private static volatile UserDao userDao;
  private static volatile SpeakerDao speakerDao;
  private static volatile RoleDao roleDao;
  private static volatile LectureDao lectureDao;
  private static volatile ConferenceDao conferenceDao;

  @Override
  public UserDao getUserDao() {
    UserDao tempUserDao = userDao;
    if (tempUserDao == null) {
      synchronized (this) {
        tempUserDao = userDao;
        if (tempUserDao == null) {
          tempUserDao = new UserDaoMySqlImpl();
          userDao = tempUserDao;
        }
      }
    }
    return tempUserDao;
  }

  @Override
  public SpeakerDao getSpeakerDao() {
    SpeakerDao tempSpeakerDao = speakerDao;
    if (tempSpeakerDao == null) {
      synchronized (this) {
        tempSpeakerDao = speakerDao;
        if (tempSpeakerDao == null) {
          tempSpeakerDao = new SpeakerDaoMySqlImpl();
          speakerDao = tempSpeakerDao;
        }
      }
    }
    return tempSpeakerDao;
  }

  @Override
  public RoleDao getRoleDao() {
    RoleDao tempRoleDao = roleDao;
    if (tempRoleDao == null) {
      synchronized (this) {
        tempRoleDao = roleDao;
        if (tempRoleDao == null) {
          tempRoleDao = new RoleDaoMySqlImpl();
          roleDao = tempRoleDao;
        }
      }
    }
    return tempRoleDao;
  }

  @Override
  public LectureDao getLectureDao() {
    LectureDao tempLectureDao = lectureDao;
    if (tempLectureDao == null) {
      synchronized (this) {
        tempLectureDao = lectureDao;
        if (tempLectureDao == null) {
          tempLectureDao = new LectureDaoMySqlImpl();
          lectureDao = tempLectureDao;
        }
      }
    }
    return tempLectureDao;
  }

  @Override
  public ConferenceDao getConferenceDao() {
    ConferenceDao tempConferenceDao = conferenceDao;
    if (tempConferenceDao == null) {
      synchronized (this) {
        tempConferenceDao = conferenceDao;
        if (tempConferenceDao == null) {
          tempConferenceDao = new ConferenceDaoMySqlImpl();
          conferenceDao = tempConferenceDao;
        }
      }
    }
    return tempConferenceDao;
  }
}
