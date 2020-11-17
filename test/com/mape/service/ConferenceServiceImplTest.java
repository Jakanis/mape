package com.mape.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mape.dao.ConferenceDao;
import com.mape.entity.Conference;
import com.mape.entity.Lecture;
import com.mape.entity.Role;
import com.mape.entity.Speaker;
import com.mape.entity.User;
import com.mape.service.impl.ConferenceServiceImpl;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConferenceServiceImplTest {

  public static final int REGISTERED_VISITORS = 20;
  private static final int MAX_VISITORS = 50;
  private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();
  private static final Role USER_ROLE = new Role(1L, "User");
  private static final User user1 = User.builder().withRole(USER_ROLE).withLogin("login1").withEmail("email1")
      .withPassword("pass1").withFirstName("firstName1").withLastName("lastName1").withId(1L).build();
  private static final User user2 = User.builder().withRole(USER_ROLE).withLogin("login2").withEmail("email2")
      .withPassword("pass2").withFirstName("firstName2").withLastName("lastName2").withId(2L).build();
  private static final Role SPEAKER_ROLE = new Role(2L, "Speaker");
  private static final Speaker speaker1 = (Speaker) Speaker.speakerBuilder().withRate(3.5).withRole(SPEAKER_ROLE)
      .withLogin("login2").withEmail("email2")
      .withPassword("pass2").withFirstName("firstName2").withLastName("lastName2").withId(3L).build();
  private static final Lecture lecture = new Lecture(1L, "topic1", speaker1, 1L, true, true);
  private static final Conference conference1 = Conference.builder().withId(1L).withName("conferenceName1")
      .withAddress("address1").withTime(LOCAL_DATE_TIME).withMaxVisitors(
          MAX_VISITORS).withRegisteredVisitors(REGISTERED_VISITORS).withLectures(Arrays.asList(lecture)).build();
  @Mock
  private ConferenceDao conferenceDao;
  @InjectMocks
  private ConferenceServiceImpl conferenceService;

  @Test
  public void shouldCallRegisterUserInDao() {
    conferenceService.registerUser(conference1, user1);
    verify(conferenceDao, times(1)).registerUser(conference1, user1);
  }

  @Test
  public void shouldSuccesfullyCheckExistingRegistration() {
    Mockito.doReturn(true).when(conferenceDao).checkRegistration(conference1, user1);
    boolean registrationCheck = conferenceService.checkRegistration(conference1, user1);
    verify(conferenceDao, times(1)).checkRegistration(conference1, user1);
    assertTrue(registrationCheck);
  }

  @Test
  public void shouldSuccesfullyCheckUnexistingRegistration() {
    Mockito.doReturn(false).when(conferenceDao).checkRegistration(conference1, user1);
    boolean registrationCheck = conferenceService.checkRegistration(conference1, user1);
    verify(conferenceDao, times(1)).checkRegistration(conference1, user1);
    assertFalse(registrationCheck);
  }

  @Test
  public void shouldFindConferencesByVisitor() {
    List<Conference> expected = Arrays.asList(conference1);
    Mockito.doReturn(expected).when(conferenceDao).findByVisitor(user1);
    List<Conference> actual = conferenceService.findByVisitor(user1);
    assertEquals(expected.size(), actual.size());
    for (int i = 0; i < actual.size(); i++) {
      assertEquals(expected.get(i), actual.get(i));
    }
  }

  @Test
  public void shouldReturnEmptyListAfterFindingConferencesByVisitor() {
    List<Conference> expected = new ArrayList<>();
    Mockito.doReturn(expected).when(conferenceDao).findByVisitor(user1);
    List<Conference> actual = conferenceService.findByVisitor(user1);
    assertEquals(expected.size(), actual.size());
    for (int i = 0; i < actual.size(); i++) {
      assertEquals(expected.get(i), actual.get(i));
    }
  }

  @Test
  public void shouldCallUnegisterUserInDao() {
    conferenceService.unregisterUser(conference1, user1);
    verify(conferenceDao, times(1)).unregisterUser(conference1, user1);
  }

  @Test
  public void shouldCallUpdateInDao() {
    conferenceService.update(conference1);
    verify(conferenceDao, times(1)).update(conference1);
  }

  @Test
  public void shouldCallCreateInDao() {
    conferenceService.create(conference1);
    verify(conferenceDao, times(1)).create(conference1);
  }
}
