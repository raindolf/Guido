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
package com.google.cloud.demo.model.nosql;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

/**
 * Base entity class for NoSQL.
 *
 * @author Michael Tang (ntang@google.com)
 */
public abstract class DemoEntityNoSql {
  protected final Entity entity;

  protected DemoEntityNoSql(Entity entity) {
    this.entity = entity;
  }

  protected DemoEntityNoSql(Key parentKey, String kind) {
    this.entity = new Entity(kind, parentKey);
  }

  protected DemoEntityNoSql(String kind, String keyName) {
    this.entity = new Entity(kind, keyName);
  }

  public Entity getEntity() {
    return entity;
  }
}
