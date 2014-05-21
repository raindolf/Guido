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

import com.google.appengine.api.rdbms.AppEngineDriver;
import com.google.cloud.demo.ConfigManager;
import com.google.cloud.demo.model.CommentManager;
import com.google.cloud.demo.model.DemoEntityManagerFactory;
import com.google.cloud.demo.model.DemoModelException;
import com.google.cloud.demo.model.DemoUserManager;
import com.google.cloud.demo.model.PhotoManager;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Entity manager factory implementation for Cloud SQL.
 *
 * @author Michael Tang (ntang@google.com)
 */
public class DemoEntityManagerSqlFactory implements DemoEntityManagerFactory {
  private static final Logger logger = Logger.getLogger(
      DemoEntityManagerSqlFactory.class.getCanonicalName());

  private DemoUserManager demoUserManager;
  private PhotoManager photoManager;
  private CommentManager commentManager;

  @Override
  public void init(ConfigManager configManager) {
    try {
      DriverManager.registerDriver(new AppEngineDriver());
      demoUserManager = new DemoUserManagerSql(configManager);
      photoManager = new PhotoManagerSql(configManager);
      commentManager = new CommentManagerSql(configManager);
    } catch (SQLException e) {
      logger.severe("Failed to register Cloud SQL driver: " + e.getMessage());
      throw new DemoModelException("Failed to register Cloud SQL driver", e);
    }
  }

  @Override
  public PhotoManager getPhotoManager() {
    return photoManager;
  }

  @Override
  public CommentManager getCommentManager() {
    return commentManager;
  }

  @Override
  public DemoUserManager getDemoUserManager() {
    return demoUserManager;
  }

}
