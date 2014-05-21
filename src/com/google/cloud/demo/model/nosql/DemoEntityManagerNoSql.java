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
package com.google.cloud.demo.model.nosql;

import static com.google.appengine.api.datastore.DatastoreServiceFactory.getDatastoreService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;
import com.google.cloud.demo.model.DemoEntity;
import com.google.cloud.demo.model.DemoEntityManager;
import com.google.cloud.demo.model.Utils;

import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Base class for entity managers for NoSQL implementation.
 *
 * @author Michael Tang (ntang@google.com)
 *
 * @param <T> type extends {@code DemoEntity}
 */
public abstract class DemoEntityManagerNoSql<T extends DemoEntity> implements DemoEntityManager<T> {
  private static final Logger logger =
      Logger.getLogger(DemoEntityManagerNoSql.class.getCanonicalName());
  private final Class<T> entityClass;

  protected DemoEntityManagerNoSql(Class<T> entityClass) {
    this.entityClass = entityClass;
  }

  @Override
  public Iterable<T> getEntities() {
    Query query = new Query(getKind());
    FetchOptions options = FetchOptions.Builder.withDefaults();
    return queryEntities(query, options);
  }

  @Override
  public T deleteEntity(T demoEntity) {
    Utils.assertTrue(demoEntity != null, "entity cannot be null");
    DemoEntityNoSql entityNoSql = downCastEntity(demoEntity);
    DatastoreService ds = getDatastoreService();
    Transaction txn = ds.beginTransaction();
    try {
      if (checkEntityForDelete(ds, entityNoSql)) {
        ds.delete(entityNoSql.getEntity().getKey());
        txn.commit();
        logger.info("entity deleted.");
        return demoEntity;
      }
    } catch (Exception e) {
      logger.severe("Failed to delete entity from datastore:" + e.getMessage());
    } finally {
      if (txn.isActive()) {
        txn.rollback();
      }
    }
    return null;
  }

  @Override
  public T upsertEntity(T demoEntity) {
    Utils.assertTrue(demoEntity != null, "demoEntity cannot be null");
    DatastoreService ds = getDatastoreService();
    DemoEntityNoSql entityNoSql = downCastEntity(demoEntity);
    Entity entity = entityNoSql.getEntity();
    ds.put(entity);
    return demoEntity;
  }

  /**
   * Gets the entity class.
   *
   * @return entity class.
   */
  protected Class<T> getEntityClass() {
    return this.entityClass;
  }

  /**
   * Gets the entity kind as a string.
   *
   * @return the entity kind string.
   */
  protected String getKind() {
    return entityClass.getSimpleName();
  }

  /**
   * Looks up a demo entity by key.
   *
   * @param key the entity key.
   * @return the demo entity; null if the key could not be found.
   */
  protected T getEntity(Key key) {
    DatastoreService ds = getDatastoreService();
    Entity entity = getDatastoreEntity(ds, key);
    if (entity != null) {
      return fromEntity(entity);
    }
    return null;
  }

  /**
   * Looks up an entity by key.
   *
   * @param ds the datastore service objct.
   * @param key the entity key.
   * @return the entity; null if the key could not be found.
   */
  protected Entity getDatastoreEntity(DatastoreService ds, Key key) {
    try {
      return ds.get(key);
    } catch (EntityNotFoundException e) {
      logger.fine("No entity found:" + key.toString());
    }
    return null;
  }
  /**
   * Queries the datastore for an {@code Iterable} collection of entities.
   *
   * @param query datastore query object.
   * @param options query options.
   *
   * @return an {@code Iterable} collection of datastore entities.
   */
  protected Iterable<T> queryEntities(Query query, FetchOptions options) {
    PreparedQuery preparedQuery = getDatastoreService().prepare(query);
    final Iterable<Entity> iterable = preparedQuery.asIterable();
    Iterable<T> iterableWrapper = new Iterable<T>() {
      @Override
      public Iterator<T> iterator() {
        final Iterator<Entity> iterator = iterable.iterator();
        return new Iterator<T>() {
          @Override
          public void remove() {
            iterator.remove();
          }

          @Override
          public T next() {
            return fromEntity(iterator.next());
          }

          @Override
          public boolean hasNext() {
            return iterator.hasNext();
          }
        };
      }
    };
    return iterableWrapper;
  }

  /**
   * Down casts the entity to a NoSQL base entity. The method makes sure the entity is created by
   * NoSQL datastore module.
   *
   * @param demoEntity the model entity.
   *
   * @return the downcast NoSQL base entity.
   */
  private DemoEntityNoSql downCastEntity(T demoEntity) {
    Utils.assertTrue(
        demoEntity instanceof DemoEntityNoSql, "entity has to be a valid NoSQL entity");
    DemoEntityNoSql entityNoSql = (DemoEntityNoSql) demoEntity;
    return entityNoSql;
  }

  /**
   * Callback before entity is deleted. Checks if the entity exists.
   *
   * @param ds the datastore service object.
   * @param demoEntity the entity to be deleted.
   *
   * @return true if the entity should be deleted; otherwise, false.
   */
  protected boolean checkEntityForDelete(DatastoreService ds, DemoEntityNoSql demoEntity) {
    if (demoEntity != null) {
      Entity entity = demoEntity.getEntity();
      if (entity != null) {
        return getDatastoreEntity(ds, entity.getKey()) != null;
      }
    }
    return false;
  }

  /**
   * Creates a model entity based on parent key.
   *
   * @param parentKey the parent key.
   *
   * @return an model entity.
   */
  protected abstract T fromParentKey(Key parentKey);

  /**
   * Creates a model the entity based on datastore entity.
   *
   * @param entity datastore entity.
   *
   * @return an model entity.
   */
  protected abstract T fromEntity(Entity entity);
}
