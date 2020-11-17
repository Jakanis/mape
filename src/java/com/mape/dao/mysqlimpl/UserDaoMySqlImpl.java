package com.mape.dao.mysqlimpl;

import com.mape.dao.UserDao;
import com.mape.dao.exception.DaoException;
import com.mape.dao.exception.ForeignKeyException;
import com.mape.entity.Role;
import com.mape.entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class UserDaoMySqlImpl extends GenericDaoMySqlImpl<User> implements UserDao {

  private static final String TABLE_NAME = "users";
  private static final String UPDATE_ENTITY =
      "UPDATE " + TABLE_NAME
          + " SET `login`=?, `password`=?, `first_name`=?, `last_name`=?, `email`=?, `role_id`=? WHERE `id` = ?";
  private static final String CREATE_ENTITY = "INSERT INTO " + TABLE_NAME
      + " (`login`, `password`, `first_name`, `last_name`, `email`, `role_id`) VALUES (?, ?, ?, ?, ?, ?)";

  private static final String FIND_BY_EMAIL = "SELECT * FROM " + TABLE_NAME + " WHERE email=?";
  private static final String FIND_BY_LOGIN = "SELECT * FROM " + TABLE_NAME + " WHERE login=?";

  public UserDaoMySqlImpl() {
    super(TABLE_NAME);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return findOneBy(FIND_BY_EMAIL, email);
  }

  @Override
  public Optional<User> findByLogin(String login) {
    return findOneBy(FIND_BY_LOGIN, login);
  }


  private Optional<User> findOneBy(String statement, String value) {
    User result = null;
    try (Connection connection = Connector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
      preparedStatement.setString(1, value);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          result = mapResultSetToEntity(resultSet);
        }
      }
    } catch (SQLException e) {
      throw new DaoException(e);
    }
    return Optional.ofNullable(result);
  }

  @Override
  protected User mapResultSetToEntity(ResultSet resultSet) throws SQLException {
    long id = resultSet.getLong("id");
    String login = resultSet.getString("login");
    String email = resultSet.getString("email");
    String firstName = resultSet.getString("first_name");
    String lastName = resultSet.getString("last_name");
    String password = resultSet.getString("password");
    long roleId = resultSet.getLong("role_id");
    Role role = new RoleDaoMySqlImpl().findById(roleId).orElseThrow(
        () -> new ForeignKeyException("Error finding role by role_id during user mapping"));
    return User.builder().withRole(role).withFirstName(firstName).withLastName(lastName).withEmail(email)
        .withLogin(login).withPassword(password).withId(id).build();
  }

  @Override
  public void update(User entity) {
    try (Connection connection = Connector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ENTITY)) {
      preparedStatement.setString(1, entity.getLogin());
      preparedStatement.setString(2, entity.getPassword());
      preparedStatement.setString(3, entity.getFirstName());
      preparedStatement.setString(4, entity.getLastName());
      preparedStatement.setString(5, entity.getEmail());
      preparedStatement.setLong(6, entity.getRole().getId());
      preparedStatement.setLong(7, entity.getId());
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      throw new DaoException(e);
    }
  }

  @Override
  public void create(User entity) {
    try (Connection connection = Connector.getConnection();
        PreparedStatement preparedStatement = connection
            .prepareStatement(CREATE_ENTITY, Statement.RETURN_GENERATED_KEYS)) {
      preparedStatement.setString(1, entity.getLogin());
      preparedStatement.setString(2, entity.getPassword());
      preparedStatement.setString(3, entity.getFirstName());
      preparedStatement.setString(4, entity.getLastName());
      preparedStatement.setString(5, entity.getEmail());
      preparedStatement.setLong(6, entity.getRole().getId());
      preparedStatement.executeUpdate();
      try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          entity.setId(generatedKeys.getLong(1));
        }
      }
    } catch (SQLException e) {
      throw new DaoException(e);
    }
  }

}
