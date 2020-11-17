package com.mape.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mape.dao.RoleDao;
import com.mape.entity.Role;
import com.mape.entity.User;
import com.mape.service.impl.RoleServiceImpl;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceImplTest {

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
  private RoleDao roleDao;
  @InjectMocks
  private RoleServiceImpl roleService;

  @Test
  public void shouldFindRoleByName() {
    Mockito.doReturn(Optional.of(USER_ROLE)).when(roleDao).findByName(USER_ROLE.getName());
    Role actual = roleService.findByName(USER_ROLE.getName()).get();
    verify(roleDao, times(1)).findByName(USER_ROLE.getName());
    assertEquals(USER_ROLE, actual);
  }

  @Test
  public void shouldReturnEmptyFindByName() {
    Mockito.doReturn(Optional.empty()).when(roleDao).findByName(USER_ROLE.getName());
    Optional<Role> actual = roleService.findByName(USER_ROLE.getName());
    verify(roleDao, times(1)).findByName(USER_ROLE.getName());
    assertFalse(actual.isPresent());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void shouldThrowUnsupportedOperationExceptionAfterCreate() {
    roleService.create(USER_ROLE);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void shouldThrowUnsupportedOperationExceptionAfterUpdate() {
    roleService.update(USER_ROLE);
  }

}
