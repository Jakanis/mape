package com.mape.dao.mysqlimpl;

import com.mape.dao.ConferenceDao;
import com.mape.dao.exception.ConferenceRegistrationException;
import com.mape.dao.exception.DaoException;
import com.mape.dao.exception.InconsistentDatabaseException;
import com.mape.dao.factory.DaoFactory;
import com.mape.dao.factory.DaoFactory.DataSource;
import com.mape.entity.Conference;
import com.mape.entity.Lecture;
import com.mape.entity.User;
import com.mape.service.LectureService;
import com.mape.service.impl.LectureServiceImpl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j;

@Log4j
public class ConferenceDaoMySqlImpl extends GenericDaoMySqlImpl<Conference> implements
    ConferenceDao {

  private static final String TABLE_NAME = "conferences";
  private static final String REGISTER_TABLE_NAME = "registrations";
  private static final String UPDATE_ENTITY =
      "UPDATE " + TABLE_NAME
          + " SET `name`=?, `address`=?, `time`=?, `visitors_capacity`=? WHERE `id` = ?";
  private static final String CREATE_ENTITY =
      "INSERT INTO " + TABLE_NAME
          + " (`name`, `address`, `time`, `visitors_capacity`) VALUES (?, ?, ?, ?)";
  private static final String FIND_BY_VISITOR =
      "SELECT * FROM " + TABLE_NAME + " JOIN " + REGISTER_TABLE_NAME
          + " ON conferences.id = registrations.conference_id WHERE `user_id`=? AND `deleted`=0 ORDER BY `time`";

  private static final String CHECK_REGISTRATION =
      "SELECT * FROM " + REGISTER_TABLE_NAME + " WHERE `conference_id`=? AND `user_id`=?";
  private static final String ADD_REGISTRATION =
      "INSERT INTO " + REGISTER_TABLE_NAME + " (`conference_id`, `user_id`) VALUES (?, ?)";
  private static final String REMOVE_REGISTRATION =
      "DELETE FROM " + REGISTER_TABLE_NAME + " WHERE `conference_id`=? AND `user_id`=?";
  private final LectureService lectureService = new LectureServiceImpl(
      DaoFactory.getDAOFactory(DataSource.MYSQL).getLectureDao());

  public ConferenceDaoMySqlImpl() {
    super(TABLE_NAME);
  }

  @Override
  protected Conference mapResultSetToEntity(ResultSet resultSet) throws SQLException {
    long conferenceId = resultSet.getLong("id");
    String name = resultSet.getString("name");
    String address = resultSet.getString("address");
    LocalDateTime time = resultSet.getTimestamp("time").toLocalDateTime();
    int maxVisitors = resultSet.getInt("visitors_capacity");
    int registeredVisitors = resultSet.getInt("visitors_registered");
    List<Lecture> lectureList = lectureService.findByConference(conferenceId);
    return Conference.builder().withId(conferenceId).withName(name).withAddress(address)
        .withTime(time).withMaxVisitors(maxVisitors).withRegisteredVisitors(registeredVisitors)
        .withLectures(lectureList).build();
  }

  @Override
  public void create(Conference conference) {
    try (Connection connection = Connector.getConnection();
        PreparedStatement preparedStatement = connection
            .prepareStatement(CREATE_ENTITY, Statement.RETURN_GENERATED_KEYS)) {
      preparedStatement.setString(1, conference.getName());
      preparedStatement.setString(2, conference.getAddress());
      preparedStatement.setTimestamp(3, Timestamp.valueOf(conference.getTime()));
      preparedStatement.setInt(4, conference.getMaxVisitors());
      preparedStatement.executeUpdate();
      try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          conference.setId(generatedKeys.getLong(1));
        }
      }
      lectureService.deleteByConference(conference);
      lectureService.saveByConference(conference.getLectures(), conference.getId());
    } catch (SQLException e) {
      throw new DaoException(e);
    }
  }

  @Override
  public void update(Conference conference) {
    try (Connection connection = Connector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ENTITY)) {
      preparedStatement.setString(1, conference.getName());
      preparedStatement.setString(2, conference.getAddress());
      preparedStatement.setTimestamp(3, Timestamp.valueOf(conference.getTime()));
      preparedStatement.setInt(4, conference.getMaxVisitors());
      preparedStatement.setLong(5, conference.getId());
      preparedStatement.executeUpdate();
      lectureService.deleteByConference(conference);
      lectureService.saveByConference(conference.getLectures(), conference.getId());
    } catch (SQLException e) {
      throw new DaoException(e);
    }
  }

  private void registrationChange(Conference conference, User user, String registrationQuery) {
    try (Connection connection = Connector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(registrationQuery)) {
      preparedStatement.setLong(1, conference.getId());
      preparedStatement.setLong(2, user.getId());
      if (preparedStatement.executeUpdate() != 1) {
        throw new InconsistentDatabaseException();
      }
    } catch (SQLException e) {
      throw new DaoException(e);
    }
  }

  @Override
  public void registerUser(Conference conference, User user) {
    if (!checkRegistration(conference, user)) {
      registrationChange(conference, user, ADD_REGISTRATION);
    } else {
      throw new ConferenceRegistrationException("Trying to register already registered user");
    }
  }

  @Override
  public void unregisterUser(Conference conference, User user) {
    if (checkRegistration(conference, user)) {
      registrationChange(conference, user, REMOVE_REGISTRATION);
    } else {
      throw new ConferenceRegistrationException("Trying to unregister not registered user");
    }
  }

  @Override
  public boolean checkRegistration(Conference conference, User user) {
    try (Connection connection = Connector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(CHECK_REGISTRATION)) {
      preparedStatement.setLong(1, conference.getId());
      preparedStatement.setLong(2, user.getId());
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          return true;
        }
      }
    } catch (SQLException e) {
      throw new DaoException(e);
    }
    return false;
  }

  @Override
  public List<Conference> findByVisitor(User visitor) {
    List<Conference> result = new ArrayList<>();
    try (Connection connection = Connector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_VISITOR)) {
      preparedStatement.setLong(1, visitor.getId());
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          result.add(mapResultSetToEntity(resultSet));
        }
      }
    } catch (SQLException e) {
      throw new DaoException(e);
    }
    return result;
  }
}
