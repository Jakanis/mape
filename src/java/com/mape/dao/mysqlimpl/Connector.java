package com.mape.dao.mysqlimpl;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import lombok.extern.log4j.Log4j;
import org.apache.commons.dbcp2.BasicDataSource;

@Log4j
public class Connector {

  private static Properties properties = new Properties();
  private static BasicDataSource ds = new BasicDataSource();

  static {
    try {
      properties.load(Connector.class.getClassLoader().getResourceAsStream("config.properties"));
    } catch (IOException e) {
      log.error("Couldn't find config.properties", e);
    }

    ds.setDriverClassName(properties.getProperty("db.driver"));
    ds.setUrl(properties.getProperty("db.address"));
    ds.setUsername(properties.getProperty("db.login"));
    ds.setPassword(properties.getProperty("db.password"));
    ds.setMinIdle(5);
    ds.setMaxIdle(10);
    ds.setMaxOpenPreparedStatements(100);
    ds.setValidationQueryTimeout(5);
    ds.setDefaultQueryTimeout(5);
    ds.setMaxWaitMillis(TimeUnit.SECONDS.toMillis(5));
    ds.setConnectionProperties("connectTimeout=5;socketTimeout=5");
  }

  private Connector() {
  }

  public static Connection getConnection() {
    Connection connection = null;
    try {
      Class.forName("com.mysql.jdbc.Driver");
      connection = DriverManager.getConnection(properties.getProperty("db.address"),
                                               properties.getProperty("db.login"),
                                               properties.getProperty("db.password"));
//      connection = ds.getConnection();
    } catch (SQLException | ClassNotFoundException e) {
      log.error("Couldn't get connection", e);
    }
    return connection;
  }
}