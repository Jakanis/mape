package com.mape.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mape.dao.UserDao;
import com.mape.entity.Role;
import com.mape.entity.User;
import com.mape.service.impl.UserServiceImpl;
import com.mape.util.PasswordEncryption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

  public static final String EMPTY_STRING = "";
  private static final Role USER_ROLE = new Role(1L, "User");
  private static final String STRONG_PASS_1 = "strongPass1";
  private static final String STRONG_PASS_2 = "strongPass2";
  private static final String VALID_EMAIL_ONE = "email1@g.co";
  private static final String VALID_EMAIL_TWO = "email2@g.co";
  private static final String LOGIN_ONE = "login1";
  private static final String LOGIN_TWO = "login2";
  private static final User user1 = User.builder().withRole(USER_ROLE).withLogin(LOGIN_ONE).withEmail(VALID_EMAIL_ONE)
      .withPassword(STRONG_PASS_1).withFirstName("firstName1").withLastName("lastName1").withId(1L).build();
  private static final User user2 = User.builder().withRole(USER_ROLE).withLogin(LOGIN_TWO).withEmail(VALID_EMAIL_TWO)
      .withPassword(STRONG_PASS_2).withFirstName("firstName2").withLastName("lastName2").withId(2L).build();
  private static final String NOT_VALID_EMAIL = "notEmail";
  private static final String WEAK_PASSWORD = "weakpass";


  @Mock
  private UserDao userDao;
  @InjectMocks
  private UserServiceImpl userSevice;

  @Test
  public void findAll() {
    List<User> expected = Arrays.asList(user1, user2);
    when(userDao.findAll()).thenReturn(expected);
    List<User> actual = userSevice.findAll();
    verify(userDao, times(1)).findAll();
    assertEquals(expected.size(), actual.size());
  }

  @Test
  public void findByEmailReturnsUser() {
    Mockito.doReturn(Optional.of(user1)).when(userDao).findByEmail("email1@g.co");
    Optional<User> actual = userSevice.findByEmail("email1@g.co");
    verify(userDao, times(1)).findByEmail("email1@g.co");
    assertTrue(actual.isPresent());
    assertEquals(user1, actual.get());
  }

  @Test
  public void findByEmailReturnsEmpty() {
    Mockito.doReturn(Optional.empty()).when(userDao).findByEmail("f");
    Optional<User> actual = userSevice.findByEmail("f");
    verify(userDao, times(1)).findByEmail("f");
    assertFalse(actual.isPresent());
  }

  @Test
  public void findByLoginReturnsEmpty() {
    Mockito.doReturn(Optional.empty()).when(userDao).findByLogin("login");
    Optional<User> actual = userSevice.findByLogin("login");
    verify(userDao, times(1)).findByLogin("login");
    assertFalse(actual.isPresent());
  }

  @Test
  public void findByLoginReturnsUser() {
    Mockito.doReturn(Optional.of(user1)).when(userDao).findByLogin("login");
    Optional<User> actual = userSevice.findByLogin("login");
    verify(userDao, times(1)).findByLogin("login");
    assertTrue(actual.isPresent());
    assertEquals(user1, actual.get());
  }

  @Test
  public void shoudCallUpdateInUserDaoWithSameParameters() {
    userSevice.update(user1);
    verify(userDao, times(1)).update(user1);
  }

  @Test
  public void shoudCallCreateInUserDaoWithEncryptedPassword() {
    String sourcePassword = "password";
    String encryptedPassword = PasswordEncryption.encryptPassword(sourcePassword);
    User creationUser = User.builder().withPassword(sourcePassword).build();
    userSevice.create(creationUser);
    verify(userDao, times(1)).create(creationUser);
    assertEquals(creationUser.getPassword(), encryptedPassword);
  }

  @Test
  public void shoudValidateRegistrationData() {
    String actual = userSevice
        .validateRegistrationData(LOGIN_ONE, STRONG_PASS_1, STRONG_PASS_1, VALID_EMAIL_ONE);
    assertEquals(UserServiceImpl.VALID_DATA, actual);
  }

  @Test
  public void shoudntValidateRegistrationDataNotEmail() {
    String actual = userSevice
        .validateRegistrationData(LOGIN_ONE, STRONG_PASS_1, STRONG_PASS_1, NOT_VALID_EMAIL);
    assertEquals(UserServiceImpl.ERROR_NOT_VALID_EMAIL, actual);
  }

  @Test
  public void shoudntValidateRegistrationDataNotEqualPasswords() {
    String actual = userSevice
        .validateRegistrationData(LOGIN_ONE, STRONG_PASS_1, STRONG_PASS_2, VALID_EMAIL_ONE);
    assertEquals(UserServiceImpl.ERROR_NOT_EQUAL_PASSWORDS, actual);
  }

  @Test
  public void shoudntValidateRegistrationDataWeakPassword() {
    String actual = userSevice
        .validateRegistrationData(LOGIN_ONE, WEAK_PASSWORD, WEAK_PASSWORD, VALID_EMAIL_ONE);
    assertEquals(UserServiceImpl.ERROR_WEAK_PASSWORD, actual);
  }

  @Test
  public void shoudntValidateRegistrationDataEmptyField() {
    String actual = userSevice.validateRegistrationData(EMPTY_STRING, STRONG_PASS_1, STRONG_PASS_1, VALID_EMAIL_ONE);
    assertEquals(UserServiceImpl.ERROR_FIELD_EMPTY, actual);
    actual = userSevice.validateRegistrationData(LOGIN_ONE, EMPTY_STRING, EMPTY_STRING, VALID_EMAIL_ONE);
    assertEquals(UserServiceImpl.ERROR_FIELD_EMPTY, actual);
    actual = userSevice.validateRegistrationData(LOGIN_ONE, STRONG_PASS_1, STRONG_PASS_1, EMPTY_STRING);
    assertEquals(UserServiceImpl.ERROR_FIELD_EMPTY, actual);
  }

  @Test
  public void shoudntValidateRegistrationDataLoginExists() {
    Mockito.doReturn(Optional.of(user1)).when(userDao).findByLogin(LOGIN_ONE);
    String actual = userSevice.validateRegistrationData(LOGIN_ONE, STRONG_PASS_1, STRONG_PASS_1, VALID_EMAIL_ONE);
    assertEquals(UserServiceImpl.ERROR_USER_LOGIN_EXISTS, actual);
  }

  @Test
  public void shoudntValidateRegistrationDataEmailExists() {
    Mockito.doReturn(Optional.of(user1)).when(userDao).findByEmail(VALID_EMAIL_ONE);
    String actual = userSevice.validateRegistrationData(LOGIN_ONE, STRONG_PASS_1, STRONG_PASS_1, VALID_EMAIL_ONE);
    assertEquals(UserServiceImpl.ERROR_USER_EMAIL_EXISTS, actual);
  }

  @Test
  public void shoudntValidateLoginDataEmptyField() {
    String actual = userSevice.validateLoginData(EMPTY_STRING, STRONG_PASS_1);
    assertEquals(UserServiceImpl.ERROR_FIELD_EMPTY, actual);
    actual = userSevice.validateLoginData(LOGIN_ONE, EMPTY_STRING);
    assertEquals(UserServiceImpl.ERROR_FIELD_EMPTY, actual);
  }

  @Test
  public void shoudntValidateLoginDataLoginNotExist() {
    Mockito.doReturn(Optional.empty()).when(userDao).findByLogin(LOGIN_ONE);
    String actual = userSevice.validateLoginData(LOGIN_ONE, STRONG_PASS_1);
    assertEquals(UserServiceImpl.ERROR_USER_LOGIN_NOT_EXISTS, actual);
  }

  @Test
  public void shoudntValidateLoginDataIncorrectPassword() {
    Mockito.doReturn(Optional.of(user1)).when(userDao).findByLogin(LOGIN_ONE);
    String actual = userSevice.validateLoginData(LOGIN_ONE, STRONG_PASS_1);
    assertEquals(UserServiceImpl.ERROR_INCORRECT_PASSWORD, actual);
  }

  @Test
  public void shoudValidateLoginData() {
    String encryptedPassword = PasswordEncryption.encryptPassword(STRONG_PASS_1);
    User creationUser = User.builder().withPassword(encryptedPassword).build();
    Mockito.doReturn(Optional.of(creationUser)).when(userDao).findByLogin(LOGIN_ONE);
    String actual = userSevice.validateLoginData(LOGIN_ONE, STRONG_PASS_1);
    assertEquals(UserServiceImpl.VALID_DATA, actual);
  }

}
