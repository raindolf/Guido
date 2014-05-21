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
import com.google.cloud.demo.model.Photo;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Photo download servlet.
 *
 * @author Michael Tang (ntang@google.com).
 */
public class DownloadServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
    String user = req.getParameter("user");
    String id = req.getParameter("id");
    Long photoId = ServletUtils.validatePhotoId(id);
    if (photoId != null && user != null) {
      Photo photo = AppContext.getAppContext().getPhotoManager().getPhoto(user, photoId);
      BlobKey blobKey = photo.getBlobKey();
      BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
      blobstoreService.serve(blobKey, res);
    } else {
      res.sendError(400, "One or more parameters are not set");
    }
  }
}
