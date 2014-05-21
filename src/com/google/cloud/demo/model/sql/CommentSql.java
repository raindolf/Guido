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

import com.google.cloud.demo.model.Comment;

/**
 * Comment entity in Cloud SQL.
 *
 * @author Michael Tang (ntang@google.com)
 */
public class CommentSql extends DemoEntitySql implements Comment {
  private Long id;
  private String content;
  private long timestamp;
  private long photoId;
  private String photoOwnerId;
  private String commentOwnerName;
  private String userId;

  public CommentSql(Long id) {
    this.id = id;
  }

  public CommentSql(String userId) {
    this.userId = userId;
  }

  public CommentSql(String userId, Long id) {
    this.userId = userId;
    this.id = id;
  }

  @Override
  public Long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Override
  public String getContent() {
    return content;
  }

  @Override
  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public long getTimestamp() {
    return timestamp;
  }

  @Override
  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public long getPhotoId() {
    return photoId;
  }

  @Override
  public void setPhotoId(long photoId) {
    this.photoId = photoId;
  }

  @Override
  public String getPhotoOwnerId() {
    return photoOwnerId;
  }

  @Override
  public void setPhotoOwnerId(String owner) {
    photoOwnerId = owner;
  }

  @Override
  public String getCommentOwnerName() {
    return commentOwnerName;
  }

  @Override
  public void setCommentOwnerName(String owner) {
    commentOwnerName = owner;
  }

  @Override
  public String getCommentOwnerId() {
    return userId;
  }

  public void setCommentOwnerId(String userId) {
    this.userId = userId;
  }
}
