/* Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.cloud.demo.model.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Callback interface for SQL query execution flow.
 *
 * @author Michael Tang (ntang@google.com)
 */
public interface QueryCallback {
  void prepareStatement(PreparedStatement stmt) throws SQLException;

  /**
   * Callback for SELECT query execution.
   *
   * @param <T> The query entity type.
   */
  public interface SelectQueryCallback<T> extends QueryCallback {
    T fromResultSet(ResultSet rs) throws SQLException;
  }

  /**
   * Callback for UPDATE/INSERT/DELETE SQL query.
   */
  public interface UpdateQueryCallback extends QueryCallback {
    void onIdGenerated(long id);
  }
}