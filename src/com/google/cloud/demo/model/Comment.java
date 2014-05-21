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
 * The comment entity interface.
 *
 * @author Michael Tang (ntang@google.com)
 */
public interface Comment extends DemoEntity {
  Long getId();

  String getContent();

  void setContent(String content);

  long getTimestamp();

  void setTimestamp(long timestamp);

  long getPhotoId();

  void setPhotoId(long photoId);

  String getPhotoOwnerId();

  void setPhotoOwnerId(String owner);

  String getCommentOwnerName();

  void setCommentOwnerName(String owner);

  String getCommentOwnerId();
}
