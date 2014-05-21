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
 * Entity manager class to support comment datastore operations.
 *
 * @author Michael Tang (ntang@google.com)
 */
public interface CommentManager extends DemoEntityManager<Comment> {
  /**
   * Gets a comment based on the user id and comment id.
   *
   * @param userId the user id of the owner.
   * @param id the photo id.
   *
   * @return the comment entity; return null if entity does not exist.
   */
  Comment getComment(String userId, Long id);

  /**
   * Gets an {@code Iterable} collection of comments for a photo.
   *
   * @param photo a photo entity.
   * @return an {@code Iterable} collection of comments on the photo.
   */
  Iterable<Comment> getComments(Photo photo);

  /**
   * Creates a new comment object based on the user id. The object is not yet
   * serialized to datastore.
   *
   * @param userId the user id of the owner.
   *
   * @return a comment entity.
   */
  Comment newComment(String userId);
}
