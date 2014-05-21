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

import com.google.cloud.demo.ConfigManager;
import com.google.cloud.demo.model.Comment;
import com.google.cloud.demo.model.CommentManager;
import com.google.cloud.demo.model.Photo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Comment manager using Cloud SQL.
 *
 * @author Michael Tang (ntang@google.com)
 */
public class CommentManagerSql extends DemoEntityManagerSql<Comment> implements CommentManager {
  private static final String SQL_UPDATE_COMMENT =
      "UPDATE Comments SET Content=?, UserId=? PhotoId=? WHERE CommentId=?";
  private static final String SQL_INSERT_COMMENT =
      "INSERT INTO Comments(Content, UserId, PhotoId) VALUES(?, ?, ?)";
  private static final String SQL_SELECT_ALL_COMMENTS =
      "SELECT CommentId, Content, u1.UserId, u1.Nickname, c.PhotoId, u2.UserId, c.PostTime "
      + "FROM Comments AS c INNER JOIN DemoUsers AS u1 ON c.UserId=u1.UserId "
      + "INNER JOIN  Photos AS p ON c.PhotoId=p.PhotoId "
      + "INNER JOIN DemoUsers AS u2 ON p.UserId=u2.UserId";
  private static final String SQL_SELECT_COMMENT =
      SQL_SELECT_ALL_COMMENTS + " WHERE u1.UserId=? AND CommentId=?";
  private static final String SQL_SELECT_ALL_COMMENTS_FOR_PHOTO =
      SQL_SELECT_ALL_COMMENTS + " WHERE c.PhotoId=? ORDER BY c.PostTime DESC";

  public CommentManagerSql(ConfigManager configManager) {
    super(configManager);
  }

  @Override
  public List<Comment> getEntities() {
    return runInTransaction(new TransactionalOperation<List<Comment>>() {
      @Override
      public List<Comment> execute(Connection conn) throws SQLException {
        return queryEntities(conn, SQL_SELECT_ALL_COMMENTS, new CommentSelectQueryCallback() {
          @Override
          public void prepareStatement(PreparedStatement stmt) {
            // NOP
          }
        });
      }
    });
  }

  @Override
  public Comment deleteEntity(final Comment entity) {
    return runInTransaction(new TransactionalOperation<Comment>() {

      @Override
      public Comment execute(Connection conn) throws SQLException {
        return deleteEntity(conn, entity, "DELETE FROM Comments WHERE CommentId=?",
            new DefaultUpdateQueryCallback() {
              @Override
              public void prepareStatement(PreparedStatement stmt) throws SQLException {
                stmt.setLong(1, entity.getId());
              }
            });
      }
    });
  }

  @Override
  public Comment upsertEntity(final Comment demoEntity) {
    return runInTransaction(new TransactionalOperation<Comment>() {
      @Override
      public Comment execute(Connection conn) throws SQLException {
        String query = SQL_INSERT_COMMENT;
        if (demoEntity.getId() != null) {
          query = SQL_UPDATE_COMMENT;
        }
        return upsertEntity(conn, demoEntity, query, new DefaultUpdateQueryCallback() {
          @Override
          public void onIdGenerated(long id) {
            if (demoEntity instanceof CommentSql && demoEntity.getId() == null) {
              CommentSql sqlEntity = (CommentSql) demoEntity;
              sqlEntity.setId(id);
            }
          }

          @Override
          public void prepareStatement(PreparedStatement stmt) throws SQLException {
            int count = 1;
            stmt.setString(count++, demoEntity.getContent());
            stmt.setString(count++, demoEntity.getCommentOwnerId());
            stmt.setLong(count++, demoEntity.getPhotoId());
            if (demoEntity.getId() != null) {
              stmt.setLong(count, demoEntity.getId());
            }
          }
        });
      }
    });
  }

  @Override
  public Comment getComment(final String userId, final Long commentId) {
    return runInTransaction(new TransactionalOperation<Comment>() {
      @Override
      public Comment execute(Connection conn) throws SQLException {
        return queryEntity(conn, SQL_SELECT_COMMENT, new CommentSelectQueryCallback() {
          @Override
          public void prepareStatement(PreparedStatement stmt) throws SQLException {
            stmt.setString(1, userId);
            stmt.setLong(2, commentId);
          }
        });
      }
    });
  }

  @Override
  public List<Comment> getComments(final Photo photo) {
    return runInTransaction(new TransactionalOperation<List<Comment>>() {

      @Override
      public List<Comment> execute(Connection conn) throws SQLException {
        return queryEntities(
            conn, SQL_SELECT_ALL_COMMENTS_FOR_PHOTO, new CommentSelectQueryCallback() {
              @Override
              public void prepareStatement(PreparedStatement stmt) throws SQLException {
                stmt.setLong(1, photo.getId());
              }
            });
      }
    });
  }

  @Override
  public Comment newComment(String userId) {
    return new CommentSql(userId);
  }

  protected Comment newComment(String userId, Long id) {
    return new CommentSql(userId, id);
  }

  private abstract class CommentSelectQueryCallback
      implements QueryCallback.SelectQueryCallback<Comment> {
    @Override
    public Comment fromResultSet(ResultSet rs) throws SQLException {
      int count = 1;
      Long commentId = rs.getLong(count++);
      String content = rs.getString(count++);
      String ownerId = rs.getString(count++);
      String ownerName = rs.getString(count++);
      Long photoId = rs.getLong(count++);
      String photoOwnerId = rs.getString(count++);
      Timestamp ts = rs.getTimestamp(count);
      Comment comment = newComment(ownerId, commentId);
      comment.setContent(content);
      comment.setCommentOwnerName(ownerName);
      comment.setPhotoId(photoId);
      comment.setPhotoOwnerId(photoOwnerId);
      comment.setTimestamp(ts.getTime());
      return comment;
    }
  }
}
