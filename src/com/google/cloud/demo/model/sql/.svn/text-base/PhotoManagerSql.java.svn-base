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
package com.google.cloud.demo.model.sql;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.cloud.demo.ConfigManager;
import com.google.cloud.demo.model.Photo;
import com.google.cloud.demo.model.PhotoManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Photo manager for Cloud SQL.
 *
 * @author Michael Tang (ntang@google.com)
 */
public class PhotoManagerSql extends DemoEntityManagerSql<Photo> implements PhotoManager {
  private static final String SQL_DELETE_PHOTO = "DELETE FROM Photos WHERE PhotoId=?";
  private static final String SQL_ORDER_BY_POSTTIME = " ORDER BY PostTime DESC";
  private static final String SQL_SELECT_ALL_PHOTOS_PREFIX =
      "SELECT PhotoId, Title, BlobKey, PostTime, UserId,"
      + " Nickname, IsShared, IsActive FROM Photos INNER JOIN DemoUsers USING(UserId)";
  private static final String SQL_SELECT_ALL_PHOTOS = SQL_SELECT_ALL_PHOTOS_PREFIX
      + SQL_ORDER_BY_POSTTIME;
  private static final String SQL_SELECT_ALL_ACTIVE_PHOTOS = SQL_SELECT_ALL_PHOTOS_PREFIX
      + " WHERE IsActive" + SQL_ORDER_BY_POSTTIME;
  private static final String SQL_SELECT_ALL_DEACTIVATED_PHOTOS =
      SQL_SELECT_ALL_PHOTOS_PREFIX + " WHERE NOT IsActive";
  private static final String SQL_SELECT_ALL_OWNED_PHOTOS =
      SQL_SELECT_ALL_PHOTOS_PREFIX + " WHERE IsActive AND UserId=?" + SQL_ORDER_BY_POSTTIME;
  private static final String SQL_SELECT_ALL_SHARED_PHOTOS =
      SQL_SELECT_ALL_PHOTOS_PREFIX + " WHERE IsActive AND UserId != ? AND IsShared"
          + SQL_ORDER_BY_POSTTIME;
  private static final String SQL_SELECT_PHOTO =
      SQL_SELECT_ALL_PHOTOS_PREFIX + " WHERE PhotoId=? ANd UserId=?";
  private static final String SQL_UPDATE_SQL =
      "UPDATE Photos SET Title=?, BlobKey=?, UserId=?, IsShared=?, IsActive=? WHERE PhotoId=?";
  private static final String SQL_INSERT_PHOTO =
      "INSERT INTO Photos(Title, BlobKey, UserId, IsShared, IsActive) VALUES(?, ?, ?, ?, ?)";

  public PhotoManagerSql(ConfigManager configManager) {
    super(configManager);
  }

  @Override
  public Iterable<Photo> getEntities() {
    return getEntitiesInTransaction(SQL_SELECT_ALL_PHOTOS);
  }

  private Iterable<Photo> getEntitiesInTransaction(final String query) {
    return runInTransaction(new TransactionalOperation<List<Photo>>() {
      @Override
      public List<Photo> execute(Connection conn) throws SQLException {
        return queryEntities(conn, query, new PhotoSelectQueryCallback() {
          @Override
          public void prepareStatement(PreparedStatement stmt) {
            // NOP
          }
        });
      }});
  }

  @Override
  public Iterable<Photo> getDeactivedPhotos() {
    return getEntitiesInTransaction(SQL_SELECT_ALL_DEACTIVATED_PHOTOS);
  }

  @Override
  public Iterable<Photo> getActivePhotos() {
    return getEntitiesInTransaction(SQL_SELECT_ALL_ACTIVE_PHOTOS);    
  }

  @Override
  public Photo deleteEntity(final Photo entity) {
    return runInTransaction(new TransactionalOperation<Photo>() {
      @Override
      public Photo execute(Connection conn) throws SQLException {
        return deleteEntity(conn, entity, SQL_DELETE_PHOTO, new DefaultUpdateQueryCallback() {
          @Override
          public void prepareStatement(PreparedStatement stmt) throws SQLException {
            stmt.setLong(1, entity.getId());
          }
        });
      }
    });
  }

  @Override
  public Photo upsertEntity(final Photo demoEntity) {
    return runInTransaction(new TransactionalOperation<Photo>() {
      @Override
      public Photo execute(Connection conn) throws SQLException {
        return upsertPhoto(conn, demoEntity);
      }
    });
  }

  private Photo upsertPhoto(Connection conn, final Photo demoEntity)
      throws SQLException {
    String query = SQL_INSERT_PHOTO;
    if (demoEntity.getId() != null) {
      query = SQL_UPDATE_SQL;
    }
    return upsertEntity(conn, demoEntity, query, new DefaultUpdateQueryCallback() {

      @Override
      public void prepareStatement(PreparedStatement stmt) throws SQLException {
        int count = 1;
        stmt.setString(count++, demoEntity.getTitle());
        stmt.setString(count++, demoEntity.getBlobKey().getKeyString());
        stmt.setString(count++, demoEntity.getOwnerId());
        stmt.setBoolean(count++, demoEntity.isShared());
        stmt.setBoolean(count++, demoEntity.isActive());
        if (demoEntity.getId() != null) {
          stmt.setLong(count, demoEntity.getId());
        }
      }

      @Override
      public void onIdGenerated(long id) {
        if (demoEntity instanceof PhotoSql && demoEntity.getId() == null) {
          PhotoSql photoSql = (PhotoSql) demoEntity;
          photoSql.setId(id);
        }
      }
    });
  }

  @Override
  public Photo getPhoto(final String userId, final long id) {
    return runInTransaction(new TransactionalOperation<Photo>() {
      @Override
      public Photo execute(Connection conn) throws SQLException {
        return getPhoto(conn, userId, id);
      }
    });
  }

  private Photo getPhoto(Connection conn, final String userId, final long id)
      throws SQLException {
    return queryEntity(conn, SQL_SELECT_PHOTO, new PhotoSelectQueryCallback() {
      @Override
      public void prepareStatement(PreparedStatement stmt) throws SQLException {
        stmt.setLong(1, id);
        stmt.setString(2, userId);
      }
    });
  }

  @Override
  public Iterable<Photo> getOwnedPhotos(final String userId) {
    return runInTransaction(new TransactionalOperation<List<Photo>>() {
      @Override
      public List<Photo> execute(Connection conn) throws SQLException {
        return queryEntities(conn, SQL_SELECT_ALL_OWNED_PHOTOS, new PhotoSelectQueryCallback() {
          @Override
          public void prepareStatement(PreparedStatement stmt) throws SQLException {
            stmt.setString(1, userId);
          }
        });
      }
    });
  }

  @Override
  public Iterable<Photo> getSharedPhotos(final String userId) {
    return runInTransaction(new TransactionalOperation<List<Photo>>() {
      @Override
      public List<Photo> execute(Connection conn) throws SQLException {
        return queryEntities(conn, SQL_SELECT_ALL_SHARED_PHOTOS, new PhotoSelectQueryCallback() {
          @Override
          public void prepareStatement(PreparedStatement stmt) throws SQLException {
            stmt.setString(1, userId);
          }
        });
      }
    });
  }

  @Override
  public Photo newPhoto(String userId) {
    return new PhotoSql(userId);
  }

  @Override
  public Photo deactivePhoto(final String userId, final long id) {
    return runInTransaction(new TransactionalOperation<Photo>() {
      @Override
      public Photo execute(Connection conn) throws SQLException {
        Photo photo = getPhoto(conn, userId, id);
        if (photo != null && photo.isActive()) {
          photo.setActive(false);
          upsertPhoto(conn, photo);
        }
        return photo;
      }});
  }

  /**
   * Helper callback classes that does result processing.
   */
  private abstract class PhotoSelectQueryCallback
      implements QueryCallback.SelectQueryCallback<Photo> {
    @Override
    public Photo fromResultSet(ResultSet rs) throws SQLException {
      int count = 1;
      Long photoId = rs.getLong(count++);
      String title = rs.getString(count++);
      String blobKey = rs.getString(count++);
      Timestamp ts = rs.getTimestamp(count++);
      String userId = rs.getString(count++);
      String nickname = rs.getString(count++);
      Boolean isShared = rs.getBoolean(count++);
      Boolean isActive = rs.getBoolean(count++);
      Photo photo = new PhotoSql(photoId, userId);
      photo.setTitle(title);
      photo.setBlobKey(new BlobKey(blobKey));
      photo.setUploadTime(ts.getTime());
      photo.setOwnerNickname(nickname);
      photo.setShared(isShared != null && isShared);
      photo.setActive(isActive == null || isActive);
      return photo;
    }
  }
}
