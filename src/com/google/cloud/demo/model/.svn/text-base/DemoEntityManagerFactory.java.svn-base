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
package com.google.cloud.demo.model;

import com.google.cloud.demo.ConfigManager;

/**
 * The entity manager factory interface.
 *
 * @author Michael Tang (ntang@google.com)
 */
public interface DemoEntityManagerFactory {
  /**
   * Initiates the factory object.
   *
   * @param configManager the configuration manager.
   */
  void init(ConfigManager configManager);

  /**
   * Gets the photo manager.
   *
   * @return the photo manager object.
   */
  PhotoManager getPhotoManager();

  /**
   * Gets the comment manager.
   *
   * @return the comment manager object.
   */
  CommentManager getCommentManager();

  /**
   * Gets the demo user manager.
   *
   * @return the demo user manager object.
   */
  DemoUserManager getDemoUserManager();
}
