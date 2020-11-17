package com.mape.dao.mysqlimpl;

import com.mape.dao.LectureDao;
import com.mape.dao.SpeakerDao;
import com.mape.dao.exception.DaoException;
import com.mape.dao.exception.ForeignKeyException;
import com.mape.dao.factory.DaoFactory;
import com.mape.dao.factory.DaoFactory.DataSource;
import com.mape.entity.Lecture;
import com.mape.entity.Speaker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j;

@Log4j
public class LectureDaoMySqlImpl extends GenericDaoMySqlImpl<Lecture> implements LectureDao {

  static final String TABLE_NAME = "lectures";
  private static final String UPDATE_ENTITY =
      "UPDATE " + TABLE_NAME
          + " SET `topic`=?, `speaker_id`=?, `conference_id`=?, `moderator_approved`=?, `speaker_approved`=?, `deleted`=0 WHERE `id` = ?";
  private static final String APPROVE_BY_ID =
      "UPDATE " + TABLE_NAME + " SET `moderator_approved`=1, `speaker_approved`=1 WHERE `id` = ?";
  private static final String CREATE_ENTITY =
      "INSERT INTO " + TABLE_NAME
          + " (`topic`, `speaker_id`, `conference_id`, `moderator_approved`, `speaker_approved`) VALUES (?, ?, ?, ?, ?)";
  private static final String FIND_BY_CONFERENCE_ID =
      "SELECT * FROM " + TABLE_NAME + " WHERE `conference_id`=? AND `deleted`=0";
  private static final String DELETE_BY_CONFERENCE_ID =
      "UPDATE " + TABLE_NAME + " SET `deleted`=1 WHERE `conference_id`= ?";

  private final SpeakerDao speakerDao = DaoFactory.getDAOFactory(DataSource.MYSQL).getSpeakerDao();

  public LectureDaoMySqlImpl() {
    super(TABLE_NAME);
  }

  @Override
  protected Lecture mapResultSetToEntity(ResultSet resultSet) throws SQLException {
    long id = resultSet.getLong("id");
    String topic = resultSet.getString("topic");
    long speakerId = resultSet.getLong("speaker_id");
    Speaker speaker = speakerDao.findById(speakerId)
        .orElseThrow(() -> new ForeignKeyException(
            "Error finding speaker by id during lecture mapping"));
    long conferenceID = resultSet.getLong("conference_id");
    boolean moderatorApproved = resultSet.getBoolean("moderator_approved");
    boolean speakerApproved = resultSet.getBoolean("speaker_approved");
    return new Lecture(id, topic, speaker, conferenceID, moderatorApproved, speakerApproved);
  }

  @Override
  public void create(Lecture entity) {
    try (Connection connection = Connector.getConnection();
        PreparedStatement preparedStatement = connection
            .prepareStatement(CREATE_ENTITY, Statement.RETURN_GENERATED_KEYS)) {
      preparedStatement.setString(1, entity.getTopic());
      preparedStatement.setLong(2, entity.getSpeaker().getId());
      preparedStatement.setLong(3, entity.getConferenceId());
      preparedStatement.setBoolean(4, entity.isApprovedByModerator());
      preparedStatement.setBoolean(5, entity.isApprovedBySpeaker());
      preparedStatement.executeUpdate();
      try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          entity.setId(generatedKeys.getLong(1));
        }
      }
    } catch (Exception e) {
      throw new DaoException(e);
    }
  }

  @Override
  public void update(Lecture entity) {
    try (Connection connection = Connector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ENTITY)) {
      preparedStatement.setString(1, entity.getTopic());
      preparedStatement.setLong(2, entity.getSpeaker().getId());
      preparedStatement.setLong(3, entity.getConferenceId());
      preparedStatement.setBoolean(4, entity.isApprovedByModerator());
      preparedStatement.setBoolean(5, entity.isApprovedBySpeaker());
      preparedStatement.setLong(6, entity.getId());
      preparedStatement.executeUpdate();
    } catch (Exception e) {
      throw new DaoException(e);
    }
  }

  @Override
  public List<Lecture> findByConference(Long conferenceId) {
    List<Lecture> result = new ArrayList<>();
    try (Connection connection = Connector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CONFERENCE_ID)) {
      preparedStatement.setLong(1, conferenceId);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          result.add(mapResultSetToEntity(resultSet));
        }
      }
    } catch (Exception e) {
      throw new DaoException(e);
    }
    return result;
  }


  @Override
  public void deleteByConference(Long conferenceId) {
    try (Connection connection = Connector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_CONFERENCE_ID)) {
      preparedStatement.setLong(1, conferenceId);
      preparedStatement.executeUpdate();
    } catch (Exception e) {
      throw new DaoException(e);
    }
  }


  @Override
  public void saveByConference(List<Lecture> lectures, Long conferenceId) {
    lectures.forEach(lecture -> {
      lecture.setConferenceId(conferenceId);
      save(lecture);
    });
  }

  @Override
  public void approveById(Long lectureId) {
    try (Connection connection = Connector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(APPROVE_BY_ID)) {
      preparedStatement.setLong(1, lectureId);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      throw new DaoException(e);
    }
  }
}
