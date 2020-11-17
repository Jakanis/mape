package com.mape.dao.mysqlimpl;

import com.mape.dao.GenericDao;
import com.mape.dao.exception.DaoException;
import com.mape.entity.Entity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j;

@Log4j
public abstract class GenericDaoMySqlImpl<T extends Entity> implements GenericDao<T> {

  private final String findById;
  private final String findAll;
  private final String findPartOfAll;
  private final String countRecords;
  private final String deleteById;

  protected GenericDaoMySqlImpl(String tableName) {
    this(tableName, null, null, null, null, null);
  }

  public GenericDaoMySqlImpl(String tableName, String findById, String findAll,
      String findPartOfAll, String countRecords, String deleteById) {
    this.findById = Optional.ofNullable(findById)
        .orElse("SELECT * FROM " + tableName + " WHERE id=?");
    this.findAll = Optional.ofNullable(findAll).orElse("SELECT * FROM " + tableName + " WHERE `deleted`=0");
    this.findPartOfAll = Optional.ofNullable(findPartOfAll)
        .orElse("SELECT * FROM " + tableName + " WHERE `deleted`=0 LIMIT ?,?");
    this.countRecords = Optional.ofNullable(countRecords)
        .orElse("SELECT COUNT(*) FROM " + tableName + " WHERE `deleted`=0");
    this.deleteById = Optional.ofNullable(deleteById)
        .orElse("UPDATE " + tableName + " SET `deleted`=1 WHERE `id` = ?");
  }

  protected abstract T mapResultSetToEntity(ResultSet resultSet) throws SQLException;

  @Override
  public Optional<T> findById(Long id) {
    T entity = null;
    try (Connection connection = Connector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(findById)) {
      preparedStatement.setLong(1, id);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          entity = mapResultSetToEntity(resultSet);
        }
      }
    } catch (SQLException e) {
      throw new DaoException(e);
    }
    return Optional.ofNullable(entity);
  }

  @Override
  public List<T> findAll() {
    List<T> result = new ArrayList<>();
    try (Connection connection = Connector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(findAll);
        ResultSet resultSet = preparedStatement.executeQuery()) {
      while (resultSet.next()) {
        result.add(mapResultSetToEntity(resultSet));
      }
    } catch (Exception e) {
      throw new DaoException(e);
    }
    return result;
  }

  @Override
  public int count() {
    try (Connection connection = Connector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(countRecords);
        ResultSet resultSet = preparedStatement.executeQuery()) {
      if (resultSet.next()) {
        return resultSet.getInt(1);
      }
    } catch (Exception e) {
      throw new DaoException(e);
    }
    return -1;
  }

  @Override
  public List<T> findPartOfAll(int from, int count) {
    List<T> result = new ArrayList<>();
    try (Connection connection = Connector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(findPartOfAll)) {
      preparedStatement.setInt(1, from);
      preparedStatement.setInt(2, count);
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
  public void deleteById(Long id) {
    try (Connection connection = Connector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(deleteById)) {
      preparedStatement.setLong(1, id);
      preparedStatement.executeUpdate();
    } catch (Exception e) {
      throw new DaoException(e);
    }
  }
}

