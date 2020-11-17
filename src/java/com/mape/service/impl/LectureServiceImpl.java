package com.mape.service.impl;

import com.mape.dao.LectureDao;
import com.mape.entity.Lecture;
import com.mape.service.LectureService;
import java.util.List;
import java.util.stream.Collectors;

public class LectureServiceImpl extends GenericServiceImpl<Lecture> implements
    LectureService {

  LectureDao lectureDao;

  public LectureServiceImpl(LectureDao lectureDao) {
    this.genericDao = lectureDao;
    this.lectureDao = lectureDao;
  }

  @Override
  public List<Lecture> findByConference(Long conferenceId) {
    return lectureDao.findByConference(conferenceId);
  }

  @Override
  public void update(Lecture entity) {
    lectureDao.update(entity);
  }

  @Override
  public void create(Lecture entity) {
    lectureDao.create(entity);
  }

  @Override
  public void deleteByConference(Long conferenceId) {
    lectureDao.deleteByConference(conferenceId);
  }

  @Override
  public void saveByConference(List<Lecture> lectures, Long conferenceId) {
    lectureDao.saveByConference(lectures, conferenceId);
  }

  @Override
  public List<Lecture> filterByApproving(List<Lecture> lectures, boolean approvedByModerator,
      boolean approvedBySpeaker) {
    return lectures.stream().filter(lecture -> lecture.isApprovedByModerator() == approvedByModerator
        && lecture.isApprovedBySpeaker() == approvedBySpeaker).collect(Collectors.toList());
  }

  @Override
  public void approveById(Long lectureId) {
    lectureDao.approveById(lectureId);
  }
}
