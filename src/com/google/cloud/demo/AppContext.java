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
package com.google.cloud.demo;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.cloud.demo.model.CommentManager;
import com.google.cloud.demo.model.DemoEntityManagerFactory;
import com.google.cloud.demo.model.DemoUser;
import com.google.cloud.demo.model.DemoUserManager;
import com.google.cloud.demo.model.PhotoManager;

import java.util.logging.Logger;

/**
 * A convenient singleton context object for the application.
 *
 * @author Michael Tang (ntang@google.com)
 */
public class AppContext {
  private static final Logger logger = Logger.getLogger(AppContext.class.getCanonicalName());

  private static AppContext instance = new AppContext();

  private ConfigManager configManager;

  private PhotoServiceManager photoServiceManager;

  private DemoEntityManagerFactory entityManagerFactory;

  // Prevent the class being instantiated externally.
  private AppContext() {
    configManager = new ConfigManager();

    String clsName = configManager.getDemoEntityManagerFactory();
    try {
      Class<?> cls = Class.forName(clsName);
      entityManagerFactory = (DemoEntityManagerFactory) cls.newInstance();
      entityManagerFactory.init(configManager);
    } catch (ClassNotFoundException e) {
      logger.severe("cannot find demo entity manager factory class:" + e.getMessage());
      throw new RuntimeException("cannot find demo entity manager factory class", e);
    } catch (InstantiationException e) {
      logger.severe("cannot create instance of entity manager factory");
      throw new RuntimeException("cannot create instance of entity manager factory", e);
    } catch (IllegalAccessException e) {
      logger.severe("cannot create instance of entity manager factory");
      throw new RuntimeException("cannot create instance of entity manager factory", e);
    }
    photoServiceManager = new PhotoServiceManager(configManager, getPhotoManager());
  }

  public static AppContext getAppContext() {
    return instance;
  }

  /**
   * @return the demoUserManager
   */
  public DemoUserManager getDemoUserManager() {
    return entityManagerFactory.getDemoUserManager();
  }

  public PhotoManager getPhotoManager() {
    return entityManagerFactory.getPhotoManager();
  }

  public CommentManager getCommentManager() {
    return entityManagerFactory.getCommentManager();
  }

  public ConfigManager getConfigManager() {
    return configManager;
  }

  public PhotoServiceManager getPhotoServiceManager() {
    return photoServiceManager;
  }

  public DemoUser getCurrentUser() {
    DemoUserManager demoUserManager = entityManagerFactory.getDemoUserManager();
    User user = UserServiceFactory.getUserService().getCurrentUser();
    DemoUser demoUser = demoUserManager.getUser(user.getUserId());
    if (demoUser == null) {
      demoUser = demoUserManager.newUser(user.getUserId());
      demoUser.setEmail(user.getEmail());
      demoUser.setNickname(user.getNickname());
      demoUserManager.upsertEntity(demoUser);
    }
    return demoUser;
  }
}
