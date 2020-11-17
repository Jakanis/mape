package com.mape.dao;

import com.mape.entity.Speaker;

//CRUD
public interface SpeakerDao extends GenericDao<Speaker> {

  void rateSpeakerById(long speakerId, long userId, int rate);

  int checkRate(long speakerId, long userId);

  double getAverageRate(long speakerId);
}
