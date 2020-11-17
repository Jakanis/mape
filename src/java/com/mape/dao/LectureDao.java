package com.mape.dao;

import com.mape.entity.Lecture;
import java.util.List;

//CRUD
public interface LectureDao extends GenericDao<Lecture> {

  List<Lecture> findByConference(Long conferenceId);

  void deleteByConference(Long conferenceId);

  void saveByConference(List<Lecture> lectures, Long conferenceId);

  void approveById(Long lectureId);

}
