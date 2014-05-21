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

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.cloud.demo.model.Comment;
import com.google.cloud.demo.model.CommentManager;
import com.google.cloud.demo.model.Photo;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Entity manager class to support comment datastore operations.
 *
 * @author Michael Tang (ntang@google.com)
 */
public class CommentManagerNoSql extends DemoEntityManagerNoSql<Comment>
    implements CommentManager {
  private final DemoUserManagerNoSql userManager;

  public CommentManagerNoSql(DemoUserManagerNoSql userManager) {
    super(Comment.class);
    this.userManager = userManager;
  }

  @Override
  public Comment getComment(String userId, Long id) {
    return getEntity(createCommentKey(userId, id));
  }

  @Override
  public Iterable<Comment> getComments(Photo photo) {
    Query query = new Query(getKind());
    Query.Filter photoIdFilter =
        new Query.FilterPredicate(CommentNoSql.FIELD_NAME_PHOTO_ID,
            FilterOperator.EQUAL, photo.getId());
    List<Filter> filters = Arrays.asList(photoIdFilter, new Query.FilterPredicate(
        CommentNoSql.FIELD_NAME_PHOTO_OWNER_ID, FilterOperator.EQUAL, photo.getOwnerId()));
    Filter filter = new Query.CompositeFilter(CompositeFilterOperator.AND, filters);
    query.setFilter(filter);
    query.addSort(CommentNoSql.FIELD_NAME_TIMESTAMP, SortDirection.DESCENDING);
    FetchOptions options = FetchOptions.Builder.withDefaults();
    return queryEntities(query, options);
  }

  /**
   * Creates a comment entity key.
   *
   * @param userId the user id. If null, no parent key is set.
   * @param id the comment id.
   * @return a datastore key object.
   */
  public Key createCommentKey(@Nullable String userId, long id) {
    if (userId != null) {
      Key parentKey = userManager.createDemoUserKey(userId);
      return KeyFactory.createKey(parentKey, getKind(), id);
    } else {
      return KeyFactory.createKey(getKind(), id);
    }
  }

  @Override
  public CommentNoSql fromParentKey(Key parentKey) {
    return new CommentNoSql(parentKey, getKind());
  }

  @Override
  public CommentNoSql newComment(String userId) {
    return new CommentNoSql(userManager.createDemoUserKey(userId), getKind());
  }

  @Override
  protected CommentNoSql fromEntity(Entity entity) {
    return new CommentNoSql(entity);
  }
}
