--
-- Copyright (c) 2012 Google Inc.
--
-- Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
-- in compliance with the License. You may obtain a copy of the License at
--
-- http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software distributed under the License
-- is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
-- or implied. See the License for the specific language governing permissions and limitations under
-- the License.

DROP TABLE IF EXISTS Comments;
DROP TABLE IF EXISTS Photos;
DROP TABLE IF EXISTS DemoUsers;

-- A demo user table.
CREATE TABLE DemoUsers(
  UserId VARCHAR(255) NOT NULL, -- Unique User Id string.
  Email VARCHAR(255) NOT NULL, -- User email string.
  Nickname VARCHAR (64) NOT NULL, -- User nickname.
  PRIMARY KEY(UserId)
);

-- A photo table.
CREATE TABLE Photos(
  PhotoId BIGINT NOT NULL AUTO_INCREMENT, -- auto-generated unique photo id
  Title TEXT, -- Photo title as string.
  BlobKey TEXT NOT NULL, -- The blobstore key as a string.
  UserId VARCHAR(255), -- The id of the user who uploaded the photo.
  IsShared BOOLEAN DEFAULT FALSE, -- If the photo is shared with others.
  IsActive BOOLEAN DEFAULT TRUE, -- If the photo is still active.
  PostTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp when
  -- photo is created.
  FOREIGN KEY(UserId) REFERENCES DemoUsers(UserId),
  PRIMARY KEY(PhotoId)
);

-- A comment table.
CREATE TABLE Comments(
  CommentId BIGINT NOT NULL AUTO_INCREMENT, -- auto-generated unique photo id
  Content TEXT NOT NULL, -- comment text.
  UserId VARCHAR(255) NOT NULL, -- The id of the user who writes the comment.
  PhotoId BIGINT NOT NULL, -- The photo id on which comment is made
  PostTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- The timestamp when the
  -- comment is made.
  INDEX(UserId),
  FOREIGN KEY(UserId) REFERENCES DemoUsers(UserId),
  INDEX(PhotoId),
  -- PhotId is not a foreign key because we keep the comment when
  -- photo is deleted.
  PRIMARY KEY(CommentId)
);
