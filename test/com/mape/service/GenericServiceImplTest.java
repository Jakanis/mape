package com.mape.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mape.dao.GenericDao;
import com.mape.entity.Role;
import com.mape.entity.User;
import com.mape.service.impl.GenericServiceImpl;
import com.mape.service.impl.UserServiceImpl;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GenericServiceImplTest {

  public static final int PART_OF_ALL_TO = 5;
  public static final int PART_OF_ALL_FROM = 0;
  public static final int RETURNED_COUNT = 5;
  private static final long ID_ONE = 1L;
  private static final Role USER_ROLE = new Role(ID_ONE, "User");
  private static final User user1 = User.builder().withRole(USER_ROLE).withLogin("login1").withEmail("email1")
      .withPassword("pass1").withFirstName("firstName1").withLastName("lastName1").withId(1L).build();
  private static final User user2 = User.builder().withRole(USER_ROLE).withLogin("login2").withEmail("email2")
      .withPassword("pass2").withFirstName("firstName2").withLastName("lastName2").withId(2L).build();
  @Mock
  private GenericDao genericDao;
  @InjectMocks
  private GenericServiceImpl genericService = new UserServiceImpl(null);

  @Test
  public void shouldReturnEmptyAfterFindById() {
    Mockito.doReturn(Optional.empty()).when(genericDao).findById(ID_ONE);
    Optional actual = genericService.findById(ID_ONE);
    verify(genericDao, times(1)).findById(ID_ONE);
    assertFalse(actual.isPresent());
  }

  @Test
  public void shouldReturnEntityAfterFindById() {
    Mockito.doReturn(Optional.of(user1)).when(genericDao).findById(ID_ONE);
    Optional actual = genericService.findById(ID_ONE);
    verify(genericDao, times(1)).findById(ID_ONE);
    assertEquals(user1, actual.get());
  }

  @Test
  public void shouldReturnEmptyListAfterFindPartOfAll() {
    Mockito.doReturn(Collections.emptyList()).when(genericDao).findPartOfAll(PART_OF_ALL_FROM, PART_OF_ALL_TO);
    List actual = genericService.findPartOfAll(PART_OF_ALL_FROM, PART_OF_ALL_TO);
    verify(genericDao, times(1)).findPartOfAll(PART_OF_ALL_FROM, PART_OF_ALL_TO);
    assertTrue(actual.isEmpty());
  }

  @Test
  public void shouldReturnEntitiesListAfterFindPartOfAll() {
    List<User> expected = Arrays.asList(user1, user2);
    Mockito.doReturn(expected).when(genericDao).findPartOfAll(PART_OF_ALL_FROM, PART_OF_ALL_TO);
    List actual = genericService.findPartOfAll(PART_OF_ALL_FROM, PART_OF_ALL_TO);
    verify(genericDao, times(1)).findPartOfAll(PART_OF_ALL_FROM, PART_OF_ALL_TO);
    assertEquals(expected, actual);
  }

  @Test
  public void shouldReturnEmptyListAfterFindAll() {
    Mockito.doReturn(Collections.emptyList()).when(genericDao).findAll();
    List actual = genericService.findAll();
    verify(genericDao, times(1)).findAll();
    assertTrue(actual.isEmpty());
  }

  @Test
  public void shouldReturnEntitiesListAfterFindAll() {
    List<User> expected = Arrays.asList(user1, user2);
    Mockito.doReturn(expected).when(genericDao).findAll();
    List actual = genericService.findAll();
    verify(genericDao, times(1)).findAll();
    assertEquals(expected.size(), actual.size());
    for (int i = 0; i < actual.size(); i++) {
      assertEquals(expected.get(i), actual.get(i));
    }
  }

  @Test
  public void shouldReturnValueAfterCountInDao() {
    Mockito.doReturn(RETURNED_COUNT).when(genericDao).count();
    int actual = genericService.count();
    verify(genericDao, times(1)).count();
    assertEquals(RETURNED_COUNT, actual);
  }

  @Test
  public void shouldCallDeleteByIdInDao() {
    genericService.deleteById(ID_ONE);
    verify(genericDao, times(1)).deleteById(ID_ONE);
  }

}
