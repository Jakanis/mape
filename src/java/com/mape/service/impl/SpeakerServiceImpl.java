package com.mape.service.impl;

import com.mape.dao.SpeakerDao;
import com.mape.entity.Speaker;
import com.mape.service.SpeakerService;
import lombok.extern.log4j.Log4j;

@Log4j
public class SpeakerServiceImpl extends GenericServiceImpl<Speaker> implements SpeakerService {

  private SpeakerDao speakerDao;

  public SpeakerServiceImpl(SpeakerDao speakerDao) {
    this.speakerDao = speakerDao;
    this.genericDao = speakerDao;
  }

  @Override
  public void rateSpeakerById(long speakerId, long userId, int rate) {
    speakerDao.rateSpeakerById(speakerId, userId, rate);
  }

  @Override
  public int checkRate(long speakerId, long userId) {
    return speakerDao.checkRate(speakerId, userId);
  }

  @Override
  public double getAverageRate(long speakerId) {
    return speakerDao.getAverageRate(speakerId);
  }

  /**
   * @param entity Editing of speakers not supported in this implementation. Please use {@link UserServiceImpl}
   */
  @Override
  public void create(Speaker entity) {
    throw new UnsupportedOperationException();
  }
  /**
   * @param entity
   * Editing of speakers not supported in this implementation. Please use {@link UserServiceImpl}
   */
  @Override
  public void update(Speaker entity) {
    throw new UnsupportedOperationException();
  }
}
