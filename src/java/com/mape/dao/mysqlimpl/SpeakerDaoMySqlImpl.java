package com.mape.dao.mysqlimpl;

import com.mape.dao.SpeakerDao;
import com.mape.dao.UserDao;
import com.mape.dao.exception.DaoException;
import com.mape.dao.exception.ForeignKeyException;
import com.mape.dao.exception.UserRoleException;
import com.mape.dao.factory.DaoFactory;
import com.mape.dao.factory.DaoFactory.DataSource;
import com.mape.entity.Role;
import com.mape.entity.Speaker;
import com.mape.entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import lombok.extern.log4j.Log4j;

@Log4j
public class SpeakerDaoMySqlImpl extends GenericDaoMySqlImpl<Speaker> implements SpeakerDao {

  public static final String RATING_SPEAKER_ERROR = "Error during rating speaker";
  private static final String TABLE_NAME = "users";
  private static final String RATE_TABLE_NAME = "rates";
  private static final String CHECK_RATE =
      "SELECT `rate` FROM " + RATE_TABLE_NAME + " WHERE `speaker_id`=? and `user_id`=?";
  private static final String INSERT_RATE =
      "INSERT INTO " + RATE_TABLE_NAME + " (`rate`,`speaker_id`,`user_id`) VALUES (?,?,?)";
  private static final String UPDATE_RATE =
      "UPDATE " + RATE_TABLE_NAME + " SET `rate`=? WHERE `speaker_id`=? AND `user_id`=?";
  private static final String ROLE_NAME = "Speaker";
  private static final Role ROLE = DaoFactory.getDAOFactory(DataSource.MYSQL).getRoleDao().findByName(ROLE_NAME)
      .orElseThrow(UserRoleException::new);
  private static final String FIND_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE id=? AND `role_id`=" + ROLE.getId();
  private static final String FIND_ALL = "SELECT * FROM " + TABLE_NAME + " WHERE `role_id`=" + ROLE.getId();
  private static final String GET_AVERAGE_RATE = "SELECT AVG(rate) FROM rates WHERE `speaker_id`=?";
  private static final UserDao USER_DAO = DaoFactory.getDAOFactory(DataSource.MYSQL).getUserDao();
  private static final String FIND_PART_OF_ALL =
      "SELECT * FROM " + TABLE_NAME + " WHERE `role_id`=" + ROLE.getId() + " LIMIT ?,?";
  private static final String COUNT_RECORDS = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE role_id=" + ROLE.getId();


  public SpeakerDaoMySqlImpl() {
    super(TABLE_NAME, FIND_BY_ID, FIND_ALL, FIND_PART_OF_ALL, COUNT_RECORDS, null);
  }

  @Override
  public int checkRate(long speakerId, long userId) {
    Integer result = null;
    try (Connection connection = Connector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(CHECK_RATE)) {
      preparedStatement.setLong(1, speakerId);
      preparedStatement.setLong(2, userId);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          result = resultSet.getInt(1);
        }
      }
    } catch (SQLException e) {
      throw new DaoException(e);
    }
    return Optional.ofNullable(result).orElse(0);
  }

  @Override
  public void rateSpeakerById(long speakerId, long userId, int rate) {
    User speaker = findById(speakerId).orElseThrow(() -> new ForeignKeyException(RATING_SPEAKER_ERROR));
    if (!speaker.getRole().getName().equals(ROLE_NAME)) {
      throw new ForeignKeyException(RATING_SPEAKER_ERROR);
    }
    String statement;
    if (checkRate(speakerId, userId) != 0) {
      statement = UPDATE_RATE;
    } else {
      statement = INSERT_RATE;

    }
    try (Connection connection = Connector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
      preparedStatement.setInt(1, rate);
      preparedStatement.setLong(2, speakerId);
      preparedStatement.setLong(3, userId);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      throw new DaoException(e);
    }

  }

  @Override
  protected Speaker mapResultSetToEntity(ResultSet resultSet) throws SQLException {
    long id = resultSet.getLong("id");
    String login = resultSet.getString("login");
    String email = resultSet.getString("email");
    String firstName = resultSet.getString("first_name");
    String lastName = resultSet.getString("last_name");
    String password = resultSet.getString("password");
    long roleId = resultSet.getLong("role_id");
    double averageRate = getAverageRate(id);
    Role role = new RoleDaoMySqlImpl().findById(roleId).orElseThrow(
        () -> new ForeignKeyException("Error finding role by role_id during user mapping"));
    if (role.getName().equals(ROLE_NAME)) {
      return (Speaker) Speaker.speakerBuilder().withRate(averageRate).withRole(role).withFirstName(firstName)
          .withLastName(lastName).withEmail(email)
          .withLogin(login).withPassword(password).withId(id).build();
    } else {
      throw new UserRoleException();
    }
  }

  @Override
  public double getAverageRate(long speakerId) {
    double averageRate = 0;
    try (Connection connection = Connector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(GET_AVERAGE_RATE)) {
      preparedStatement.setLong(1, speakerId);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          averageRate = resultSet.getFloat(1);
        }
      }
    } catch (SQLException e) {
      log.error("Error finding speaker's rate " + TABLE_NAME, e);
    }
    return averageRate;
  }

  @Override
  public void create(Speaker entity) {
    USER_DAO.create(entity);
  }

  @Override
  public void update(Speaker entity) {
    USER_DAO.update(entity);
  }
}
