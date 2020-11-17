package com.mape.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mape.dao.SpeakerDao;
import com.mape.entity.Role;
import com.mape.entity.Speaker;
import com.mape.entity.User;
import com.mape.service.impl.SpeakerServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SpeakerServiceImplTest {

  public static final int PART_OF_ALL_TO = 5;
  public static final int PART_OF_ALL_FROM = 0;
  public static final int RETURNED_COUNT = 5;
  public static final double RATE_DELTA = 0.1;
  public static final double SPEAKER_AVERAGE_RATE = 3.5;
  public static final int SPEAKER_RATE = 3;
  private static final long ID_ONE = 1L;
  private static final Role USER_ROLE = new Role(ID_ONE, "User");
  private static final User user1 = User.builder().withRole(USER_ROLE).withLogin("login1").withEmail("email1")
      .withPassword("pass1").withFirstName("firstName1").withLastName("lastName1").withId(1L).build();
  private static final User user2 = User.builder().withRole(USER_ROLE).withLogin("login2").withEmail("email2")
      .withPassword("pass2").withFirstName("firstName2").withLastName("lastName2").withId(2L).build();
  private static final Role SPEAKER_ROLE = new Role(2L, "Speaker");
  private static final Speaker speaker1 = (Speaker) Speaker.speakerBuilder().withRate(SPEAKER_AVERAGE_RATE)
      .withRole(SPEAKER_ROLE).withLogin("login2").withEmail("email2")
      .withPassword("pass2").withFirstName("firstName2").withLastName("lastName2").withId(3L).build();
  @Mock
  private SpeakerDao speakerDao;
  @InjectMocks
  private SpeakerServiceImpl speakerService;

  @Test
  public void shouldFindRate() {
    Mockito.doReturn(SPEAKER_RATE).when(speakerDao).checkRate(speaker1.getId(), user1.getId());
    int actual = speakerService.checkRate(speaker1.getId(), user1.getId());
    verify(speakerDao, times(1)).checkRate(speaker1.getId(), user1.getId());
    assertEquals(SPEAKER_RATE, actual);
  }

  @Test
  public void shouldFindAverageRate() {
    Mockito.doReturn(SPEAKER_AVERAGE_RATE).when(speakerDao).getAverageRate(speaker1.getId());
    double actual = speakerService.getAverageRate(speaker1.getId());
    verify(speakerDao, times(1)).getAverageRate(speaker1.getId());
    assertEquals(SPEAKER_AVERAGE_RATE, actual, RATE_DELTA);
  }

  @Test
  public void shouldCallRateSpeakerByIdInDao() {
    speakerService.rateSpeakerById(speaker1.getId(), user1.getId(), SPEAKER_RATE);
    verify(speakerDao, times(1)).rateSpeakerById(speaker1.getId(), user1.getId(), SPEAKER_RATE);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void shouldThrowUnsupportedOperationExceptionAfterCreate() {
    speakerService.create(speaker1);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void shouldThrowUnsupportedOperationExceptionAfterUpdate() {
    speakerService.update(speaker1);
  }

}