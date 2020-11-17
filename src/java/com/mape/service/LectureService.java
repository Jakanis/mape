package com.mape.service;

import com.mape.entity.Conference;
import com.mape.entity.Lecture;
import java.util.List;

public interface LectureService extends GenericService<Lecture> {

  default List<Lecture> findByConference(Conference conference) {
    return findByConference(conference.getId());
  }

  List<Lecture> findByConference(Long conferenceId);

  default void deleteByConference(Conference conference) {
    deleteByConference(conference.getId());
  }

  void deleteByConference(Long conferenceId);

  default void saveByConference(List<Lecture> lectures, Conference conference) {
    saveByConference(lectures, conference.getId());
  }

  void saveByConference(List<Lecture> lectures, Long conferenceId);

  List<Lecture> filterByApproving(List<Lecture> lectures, boolean approvedByModerator, boolean approvedBySpeaker);

  void approveById(Long lectureId);

}
