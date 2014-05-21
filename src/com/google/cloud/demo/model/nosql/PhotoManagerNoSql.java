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
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Transaction;
import com.google.cloud.demo.model.Photo;
import com.google.cloud.demo.model.PhotoManager;
import com.google.cloud.demo.model.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * The photo entity manager class for NoSQL.
 *
 * @author Michael Tang (ntang@google.com)
 */
public class PhotoManagerNoSql extends DemoEntityManagerNoSql<Photo> implements PhotoManager {
  private static final Logger logger = Logger.getLogger(PhotoManagerNoSql.class.getCanonicalName());
  private DemoUserManagerNoSql userManager;

  public PhotoManagerNoSql(DemoUserManagerNoSql userManager) {
    super(Photo.class);
    this.userManager = userManager;
  }

  @Override
  public Photo getPhoto(String userId, long id) {
    Key key = createPhotoKey(userId, id);
    return getEntity(key);
  }

  public Key createPhotoKey(String userId, Long id) {
    Utils.assertTrue(id != null, "id cannot be null");
    if (userId != null) {
      Key parentKey = userManager.createDemoUserKey(userId);
      return KeyFactory.createKey(parentKey, getKind(), id);
    } else {
      return KeyFactory.createKey(getKind(), id);
    }
  }

  @Override
  public Iterable<Photo> getActivePhotos() {
    Query query = new Query(getKind());
    Query.Filter filter = new Query.FilterPredicate(PhotoNoSql.FIELD_NAME_ACTIVE,
        FilterOperator.EQUAL, true);
    query.addSort(PhotoNoSql.FIELD_NAME_UPLOAD_TIME, SortDirection.DESCENDING);
    query.setFilter(filter);
    FetchOptions options = FetchOptions.Builder.withDefaults();
    return queryEntities(query, options);    
  }

  @Override
  public Iterable<Photo> getOwnedPhotos(String userId) {
    Query query = new Query(getKind());
    query.setAncestor(userManager.createDemoUserKey(userId));
    Query.Filter filter = new Query.FilterPredicate(PhotoNoSql.FIELD_NAME_ACTIVE,
        FilterOperator.EQUAL, true);
    query.setFilter(filter);
    FetchOptions options = FetchOptions.Builder.withDefaults();
    return queryEntities(query, options);
  }

  @Override
  public Iterable<Photo> getSharedPhotos(String userId) {
    Query query = new Query(getKind());
    Query.Filter ownerFilter =
        new Query.FilterPredicate(PhotoNoSql.FIELD_NAME_OWNER_ID, FilterOperator.NOT_EQUAL, userId);
    List<Query.Filter> filterList =
        Arrays.asList(ownerFilter,
            new Query.FilterPredicate(PhotoNoSql.FIELD_NAME_SHARED, FilterOperator.EQUAL, true),
            new Query.FilterPredicate(PhotoNoSql.FIELD_NAME_ACTIVE, FilterOperator.EQUAL, true)
            );
    Filter filter = new Query.CompositeFilter(CompositeFilterOperator.AND, filterList);
    query.setFilter(filter);
    FetchOptions options = FetchOptions.Builder.withDefaults();
    return queryEntities(query, options);
  }

  @Override
  public Iterable<Photo> getDeactivedPhotos() {
    Query query = new Query(getKind());
    Query.Filter filter = new Query.FilterPredicate(PhotoNoSql.FIELD_NAME_ACTIVE,
        FilterOperator.EQUAL, false);
    query.setFilter(filter);
    FetchOptions options = FetchOptions.Builder.withDefaults();
    return queryEntities(query, options);
  }

  @Override
  public PhotoNoSql fromParentKey(Key parentKey) {
    return new PhotoNoSql(parentKey, getKind());
  }

  @Override
  public PhotoNoSql newPhoto(String userId) {
    return new PhotoNoSql(userManager.createDemoUserKey(userId), getKind());
  }

  @Override
  protected PhotoNoSql fromEntity(Entity entity) {
    return new PhotoNoSql(entity);
  }

  @Override
  public Photo deactivePhoto(String userId, long id) {
    Utils.assertTrue(userId != null, "user id cannot be null");
    DatastoreService ds = getDatastoreService();
    Transaction txn = ds.beginTransaction();
    try {
      Entity entity = getDatastoreEntity(ds, createPhotoKey(userId, id));
      if (entity != null) {
        PhotoNoSql photo = new PhotoNoSql(entity);
        if (photo.isActive()) {
          photo.setActive(false);
          ds.put(entity);
        }
        txn.commit();

        return photo;
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
}
