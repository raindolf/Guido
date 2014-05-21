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

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.cloud.demo.model.DemoUser;
import com.google.cloud.demo.model.Photo;
import com.google.cloud.demo.model.PhotoManager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet to handle photo upload to Google Cloud Storage.
 *
 * @author Michael Tang (ntang@google.com)
 */
public class UploadHandlerServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
    AppContext appContext = AppContext.getAppContext();
    DemoUser user = appContext.getCurrentUser();
    if (user == null) {
      res.sendError(401, "You have to login to upload image.");
      return;
    }
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
    List<BlobKey> keys = blobs.get("photo");
    String id = null;
    boolean succeeded = false;
    if (keys != null && keys.size() > 0) {
      PhotoManager photoManager = appContext.getPhotoManager();
      Photo photo = photoManager.newPhoto(user.getUserId());
      String title = req.getParameter("title");
      if (title != null) {
        photo.setTitle(title);
      }

      String isPrivate = req.getParameter(ServletUtils.REQUEST_PARAM_NAME_PRIVATE);
      photo.setShared(isPrivate == null);

      photo.setOwnerNickname(ServletUtils.getProtectedUserNickname(user.getNickname()));

      BlobKey blobKey = keys.get(0);
      photo.setBlobKey(blobKey);

      photo.setUploadTime(System.currentTimeMillis());

      photo = photoManager.upsertEntity(photo);
      id = photo.getId().toString();
      succeeded = true;
    }
    if (succeeded) {
      res.sendRedirect(appContext.getPhotoServiceManager().getRedirectUrl(
          req.getParameter(ServletUtils.REQUEST_PARAM_NAME_TARGET_URL), user.getUserId(), id));
    } else {
      res.sendError(400, "Request cannot be handled.");
    }
  }
}
