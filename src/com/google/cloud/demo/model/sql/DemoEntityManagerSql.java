/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.cloud.demo.model.sql;

import com.google.cloud.demo.ConfigManager;
import com.google.cloud.demo.model.DemoEntity;
import com.google.cloud.demo.model.DemoEntityManager;
import com.google.cloud.demo.model.DemoModelException;
import com.google.cloud.demo.model.Utils;
import com.google.cloud.demo.model.sql.QueryCallback.SelectQueryCallback;
import com.google.cloud.demo.model.sql.QueryCallback.UpdateQueryCallback;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Entity manager base class for Cloud SQL.
 *
 * @author Michael Tang (ntang@google.com)
 *
 * @param <T> The query entity type.
 */
public abstract class DemoEntityManagerSql<T extends DemoEntity> implements DemoEntityManager<T> {
  private static final Logger logger =
      Logger.getLogger(DemoEntityManagerSql.class.getCanonicalName());
  private ConfigManager configManager;

  protected DemoEntityManagerSql(ConfigManager configManager) {
    this.configManager = configManager;
  }

  /**
   * Executes an SQL UPDATE/DELETE/INSERT query.
   *
   * @param conn a JDBC connection object.
   * @param query the SQL query string.
   * @param callback the callback.
   *
   * @return number of entities processed.
   *
   * @throws SQLException if operation fails.
   */
  protected int processEntity(Connection conn, String query, UpdateQueryCallback callback)
      throws SQLException {
    PreparedStatement stmt = conn.prepareStatement(query);
    try {
      if (callback != null) {
        callback.prepareStatement(stmt);
      }
      int result = stmt.executeUpdate();
      ResultSet rs = stmt.getGeneratedKeys();
      if (rs.next()) {
        callback.onIdGenerated(rs.getLong(1));
      }
      return result;
    } finally {
      stmt.close();
    }
  }

  protected <TT> TT runInTransaction(TransactionalOperation<TT> op) {
    Connection conn = null;
    try {
      conn = getDatabaseConnection();
      boolean committed = false;
      try {
        TT result = op.execute(conn);
        conn.commit();
        committed = true;
        return result;
      } finally {
        if (!committed) {
          conn.rollback();
        }
        conn.close();
      }
    } catch (SQLException e) {
      handleSqlException(e);
    }
    return null;
  }

  private Connection getDatabaseConnection() throws SQLException {
    Connection conn = DriverManager.getConnection(configManager.getDatabaseUrl());
    conn.setAutoCommit(false);
    return conn;
  }

  protected List<T> queryEntities(Connection conn, String query, SelectQueryCallback<T> callback)
      throws SQLException {
    Utils.assertTrue(callback != null, "query callback is null");
    List<T> entities = new ArrayList<T>();
    PreparedStatement stmt = conn.prepareStatement(query);
    try {
      callback.prepareStatement(stmt);
      ResultSet rs = stmt.executeQuery();
      try {
        while (rs.next()) {
          T entity = callback.fromResultSet(rs);
          if (entity instanceof DemoEntitySql) {
            DemoEntitySql sqlEntity = (DemoEntitySql) entity;
            sqlEntity.setPersistent(true);
          }
          entities.add(entity);
        }
      } finally {
        rs.close();
      }
    } finally {
      stmt.close();
    }
    return entities;
  }

  private void handleSqlException(SQLException e) throws DemoModelException {
    logger.severe("Failed to access database:" + e.getMessage());
    throw new DemoModelException("Failed to access database", e);
  }

  protected T queryEntity(Connection conn, String query, SelectQueryCallback<T> callback)
      throws SQLException {
    List<T> entities = queryEntities(conn, query, callback);
    if (entities.size() == 1) {
      return entities.get(0);
    } else if (entities.size() > 1) {
      throw new DemoModelException("Should only return a single entity");
    }
    return null;

  }

  protected T upsertEntity(Connection conn,
      final T demoEntity, String query, UpdateQueryCallback callback) throws SQLException {
    int result = processEntity(conn, query, callback);
    if (result == 1) {
      return demoEntity;
    }
    return null;
  }

  protected T deleteEntity(Connection conn, T entity, String query, UpdateQueryCallback callback)
      throws SQLException {
    if (checkEntityForDelete(entity)) {
      int result = processEntity(conn, query, callback);

      if (result == 1) {
        return entity;
      }
    }
    return null;
  }

  /**
   * Callback before entity is deleted.
   *
   * @param entity the entity to be deleted.
   *
   * @return true if the entity should be deleted; otherwise, false.
   */
  protected boolean checkEntityForDelete(T entity) {
    return entity != null;
  }

  /**
   * A default update callback.
   */
  protected abstract static class DefaultUpdateQueryCallback
      implements QueryCallback.UpdateQueryCallback {
    @Override
    public void onIdGenerated(long id) {
      // NOP
    }
  }

  /**
   * Transaction operation interface.
   * 
   * @param <TT> the return type of the transactional operation.
   */
  protected interface TransactionalOperation<TT> {
    TT execute(Connection conn) throws SQLException;
  }
}
