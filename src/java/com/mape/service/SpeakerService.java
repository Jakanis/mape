package com.mape.service;

import com.mape.entity.Speaker;

public interface SpeakerService extends GenericService<Speaker> {

  void rateSpeakerById(long speakerId, long userId, int rate);

  int checkRate(long speakerId, long userId);

  double getAverageRate(long speakerId);

}
