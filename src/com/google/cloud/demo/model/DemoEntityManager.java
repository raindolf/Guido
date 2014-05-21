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
 * Base class for entity managers. Each entity manager contains
 * query and update operations for one type of entity.
 *
 * @author Michael Tang (ntang@google.com)
 *
 * @param <T> type extends {@code DemoEntity}
 */
public interface DemoEntityManager<T extends DemoEntity> {
  public static final int INDEX_NOT_SPECIFIED = -1;

  /**
   * Retrieves all the entities.
   *
   * @return an {@code Iterable} collection of all entities.
   */
  Iterable<T> getEntities();

  /**
   * Deletes an entity.
   *
   * @param entity the entity to be deleted.
   *
   * @return the deleted entity; return null if the entity does not exist.
   */
  T deleteEntity(T entity);

  /**
   * Updates or insert an entity.
   *
   * @param demoEntity the entity object.
   *
   * @return the entity object after upserted.
   */
  T upsertEntity(T demoEntity);
}
