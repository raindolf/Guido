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

import com.google.appengine.api.blobstore.BlobKey;
import com.google.cloud.demo.model.Photo;

/**
 * Photo entity for Cloud SQL.
 *
 * @author Michael Tang (ntang@google.com)
 */
public class PhotoSql extends DemoEntitySql implements Photo {
  private Long id;
  private BlobKey blobKey;
  private boolean shared;
  private String title;
  private String ownerEmail;
  private String ownerId;
  private long uploadTime;
  private boolean active = true;

  public PhotoSql(Long id) {
    this.id = id;
  }

  public PhotoSql(String userId) {
    this.ownerId = userId;
  }

  public PhotoSql(Long id, String userId) {
    this.id = id;
    this.ownerId = userId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public BlobKey getBlobKey() {
    return blobKey;
  }

  @Override
  public void setBlobKey(BlobKey blobKey) {
    this.blobKey = blobKey;
  }

  @Override
  public boolean isShared() {
    return shared;
  }

  @Override
  public void setShared(boolean shared) {
    this.shared = shared;
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public String getOwnerNickname() {
    return ownerEmail;
  }

  @Override
  public void setOwnerNickname(String ownerEmail) {
    this.ownerEmail = ownerEmail;
  }

  @Override
  public String getOwnerId() {
    return ownerId;
  }

  @Override
  public long getUploadTime() {
    return uploadTime;
  }

  @Override
  public void setUploadTime(long uploadTime) {
    this.uploadTime = uploadTime;
  }

  @Override
  public boolean isActive() {
    return active;
  }

  @Override
  public void setActive(boolean active) {
    this.active = active;
  }
}
