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

import com.google.cloud.demo.model.DemoUser;

/**
 * Demo user entity for Cloud SQL.
 *
 * @author Michael Tang (ntang@google.com)
 */
public class DemoUserSql extends DemoEntitySql implements DemoUser{
  private String userId;
  private String email;
  private String nickname;

  public DemoUserSql(String userId) {
    this.userId = userId;
  }

  @Override
  public String getUserId() {
    return userId;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String getNickname() {
    return nickname;
  }

  @Override
  public void setNickname(String nickname) {
    this.nickname = nickname;
  }
}
