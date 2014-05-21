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
package com.google.cloud.demo;

import com.google.cloud.demo.model.Photo;
import com.google.cloud.demo.model.PhotoManager;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet to handle photo meta data update and photo delete.
 *
 * @author Michael Tang (ntang@google.com)
 */
public class PhotoEditServlet extends HttpServlet {
  private static final String REQUEST_PARAM_NAME_DELETE = "delete";
  private static final String REQUEST_PARAM_NAME_SAVE = "save";
  private static final String REQUEST_PARAM_NAME_TITLE = "title";
  private static final long serialVersionUID = 1L;

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
    AppContext appContext = AppContext.getAppContext();
    PhotoManager photoManager = appContext.getPhotoManager();
    String user = req.getParameter(ServletUtils.REQUEST_PARAM_NAME_PHOTO_OWNER_ID);
    String id = req.getParameter(ServletUtils.REQUEST_PARAM_NAME_PHOTO_ID);
    Long photoId = ServletUtils.validatePhotoId(id);
    String save = req.getParameter(REQUEST_PARAM_NAME_SAVE);
    String delete = req.getParameter(REQUEST_PARAM_NAME_DELETE);
    boolean succeeded = false;
    if (photoId != null && user != null) {
      Photo photo = photoManager.getPhoto(user, photoId);
      if (photo != null) {
        if (save != null) {
          String title = req.getParameter(REQUEST_PARAM_NAME_TITLE);
          String isPrivate = req.getParameter(ServletUtils.REQUEST_PARAM_NAME_PRIVATE);
          photo.setTitle(title);
          photo.setShared(isPrivate == null);
          photoManager.upsertEntity(photo);
        } else if (delete != null) {
          photoManager.deactivePhoto(photo.getOwnerId(), photo.getId());
        }
        succeeded = true;
      }
    }
    if (succeeded) {
      if (delete != null) {
        res.sendRedirect(appContext.getPhotoServiceManager().getRedirectUrl(
            req.getParameter(ServletUtils.REQUEST_PARAM_NAME_TARGET_URL), null, null));
      } else {
        res.sendRedirect(appContext.getPhotoServiceManager().getRedirectUrl(
            req.getParameter(ServletUtils.REQUEST_PARAM_NAME_TARGET_URL), user, id));
      }
    } else {
      res.sendError(400, "Request cannot be handled.");
    }
  }
}
