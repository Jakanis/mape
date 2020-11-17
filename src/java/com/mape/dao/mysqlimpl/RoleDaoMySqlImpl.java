package com.mape.dao.mysqlimpl;

import com.mape.dao.RoleDao;
import com.mape.dao.exception.DaoException;
import com.mape.entity.Role;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class RoleDaoMySqlImpl extends GenericDaoMySqlImpl<Role> implements RoleDao {

  static final String TABLE_NAME = "roles";
  private static final String FIND_BY_ROLE = "SELECT * FROM " + TABLE_NAME + " WHERE role=?";

  public RoleDaoMySqlImpl() {
    super(TABLE_NAME);
  }

  @Override
  public Optional<Role> findByName(String name) {
    Role result = null;
    try (Connection connection = Connector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ROLE)) {
      preparedStatement.setString(1, name);
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
  protected Role mapResultSetToEntity(ResultSet resultSet) throws SQLException {
    long id = resultSet.getLong("id");
    String role = resultSet.getString("role");
    return new Role(id, role);
  }

  /**
   * @param entity Editing of roles not supported because not needed in this project and also for safety
   */
  @Override
  public void create(Role entity) {
    throw new UnsupportedOperationException();
  }

  /**
   * @param entity
   * Editing of roles not supported because not needed in this project and also for safety
   */
  @Override
  public void update(Role entity) {
    throw new UnsupportedOperationException();
  }

  /**
   * @param id
   * Deleting of roles not supported because not needed in this project and also for safety
   */
  @Override
  public void deleteById(Long id) {
    throw new UnsupportedOperationException();
  }
}
