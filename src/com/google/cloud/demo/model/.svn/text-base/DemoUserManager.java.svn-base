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
package com.google.cloud.demo.model;

/**
 * User manager interface.
 *
 * @author Michael Tang (ntang@google.com)
 */
public interface DemoUserManager extends DemoEntityManager<DemoUser> {
  /**
   * Gets the user entity based on user id.
   *
   * @param userId the user id.
   *
   * @return the user entity; return null if user is not found.
   */
  DemoUser getUser(String userId);

  /**
   * Creates a new user object. The object is not serialized to datastore yet.
   *
   * @param userId the user id.
   * @return the user object.
   */
  DemoUser newUser(String userId);
}
