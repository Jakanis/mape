package com.mape.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mape.dao.LectureDao;
import com.mape.entity.Conference;
import com.mape.entity.Lecture;
import com.mape.entity.Role;
import com.mape.entity.Speaker;
import com.mape.service.impl.LectureServiceImpl;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LectureServiceImplTest {

  public static final int PART_OF_ALL_TO = 5;
  public static final int PART_OF_ALL_FROM = 0;
  public static final int RETURNED_COUNT = 5;
  private static final long ID_ONE = 1L;
  private static final int MAX_VISITORS = 50;
  private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();
  private static final int REGISTERED_VISITORS = 20;
  private static final Role SPEAKER_ROLE = new Role(2L, "Speaker");
  private static final Speaker speaker1 = (Speaker) Speaker.speakerBuilder().withRate(3.5).withRole(SPEAKER_ROLE)
      .withLogin("login2").withEmail("email2")
      .withPassword("pass2").withFirstName("firstName2").withLastName("lastName2").withId(3L).build();
  private static final Lecture lecture1 = new Lecture(1L, "topic1", speaker1, 1L, true, true);
  private static final Lecture lecture2 = new Lecture(2L, "topic2", speaker1, 1L, false, true);
  private static final Lecture lecture3 = new Lecture(3L, "topic3", speaker1, 1L, true, false);
  private static final List<Lecture> LECTURES = Arrays.asList(lecture1, lecture2, lecture3);
  private static final Conference conference1 = Conference.builder().withId(1L).withName("conferenceName1")
      .withAddress("address1").withTime(LOCAL_DATE_TIME).withMaxVisitors(
          MAX_VISITORS).withRegisteredVisitors(REGISTERED_VISITORS).withLectures(
          LECTURES).build();
  @Mock
  private LectureDao lectureDao;
  @InjectMocks
  private LectureServiceImpl lectureService;

  @Test
  public void shouldCallUpdateInDao() {
    lectureService.update(lecture1);
    verify(lectureDao, times(1)).update(lecture1);
  }

  @Test
  public void shouldCallCreateInDao() {
    lectureService.create(lecture1);
    verify(lectureDao, times(1)).create(lecture1);
  }

  @Test
  public void shouldCallDeleteByConferenceInDao() {
    lectureService.deleteByConference(conference1);
    verify(lectureDao, times(1)).deleteByConference(conference1.getId());
  }

  @Test
  public void shouldCallDeleteByConferenceIDInDao() {
    lectureService.deleteByConference(conference1.getId());
    verify(lectureDao, times(1)).deleteByConference(conference1.getId());
  }

  @Test
  public void shouldCallSaveByConferenceInDao() {
    lectureService.saveByConference(LECTURES, conference1);
    verify(lectureDao, times(1)).saveByConference(LECTURES, conference1.getId());
  }

  @Test
  public void shouldCallSaveByConferenceIdInDao() {
    lectureService.saveByConference(LECTURES, conference1.getId());
    verify(lectureDao, times(1)).saveByConference(LECTURES, conference1.getId());
  }

  @Test
  public void shouldCallApproveByIdInDao() {
    lectureService.approveById(lecture1.getId());
    verify(lectureDao, times(1)).approveById(lecture1.getId());
  }

  @Test
  public void shouldFilterApprovedLectures() {
    List<Lecture> actual = lectureService
        .filterByApproving(LECTURES, lecture1.isApprovedByModerator(), lecture1.isApprovedBySpeaker());
    List<Lecture> expected = Collections.singletonList(lecture1);
    assertEquals(expected.size(), actual.size());
    assertEquals(expected.get(0), actual.get(0));

    actual = lectureService
        .filterByApproving(LECTURES, lecture2.isApprovedByModerator(), lecture2.isApprovedBySpeaker());
    expected = Collections.singletonList(lecture2);
    assertEquals(expected.size(), actual.size());
    assertEquals(expected.get(0), actual.get(0));

    actual = lectureService
        .filterByApproving(LECTURES, lecture3.isApprovedByModerator(), lecture3.isApprovedBySpeaker());
    expected = Collections.singletonList(lecture3);
    assertEquals(expected.size(), actual.size());
    assertEquals(expected.get(0), actual.get(0));
  }

  @Test
  public void shouldReturnEmptyListAfterFindByConference() {
    Mockito.doReturn(Collections.emptyList()).when(lectureDao).findByConference(conference1.getId());
    List actual = lectureService.findByConference(conference1.getId());
    verify(lectureDao, times(1)).findByConference(conference1.getId());
    assertTrue(actual.isEmpty());
  }

  @Test
  public void shouldReturnEntitiesListAfterFindByConference() {
    List<Lecture> expected = LECTURES;
    Mockito.doReturn(expected).when(lectureDao).findByConference(conference1.getId());
    List actual = lectureService.findByConference(conference1.getId());
    verify(lectureDao, times(1)).findByConference(conference1.getId());
    assertEquals(expected.size(), actual.size());
    for (int i = 0; i < actual.size(); i++) {
      assertEquals(expected.get(i), actual.get(i));
    }
  }
}
