# Guido 41st Birthday Photo Feed app

Biography : Guido Sohne was the Platform Strategy Manager of Microsoft East Africa based in Nairobi, Kenya but with responsibility for 52 countries in West, East & Central Africa. He died suddenly on 2nd May 2008 aged 34.Guido Sohne was widely recognized as one of Africa’s top software developers, having been called the ‘very young elder statesman’ of free software in Africa in connection with his longstanding contributions and efforts to increase the usage of advanced software technology for the development of Africa’s people and its economies.

Overview : The Photofeed Java app demonstrates the use of several Google Cloud platform products in one application to provide a media sharing and management solution. It runs on App Engine and for datastore uses your choice of either the App Engine non SQL Datastore or Google Cloud SQL to store photo metadata and comments about the photos. The actual photo binaries (blobs) are stored in Google Cloud Storage (GCS) regardless of which datastore you choose. (The datastore choice is a build-time choice made via build properties prior to compiling the app; the default is the non SQL App Engine Datastore.) The user starts off by logging in with a valid Google account. After login, the user sees a shared photo stream containing photos and comments uploaded by the users of the app. The photos are displayed in the chronological order posted, and any comments for each photo are also shown in the chronological order posted.


[View the live app here](http://gsohnephotos.appspot.com/

Built by:

[Raindolf Owusu](http://www.twitter.com/raindolf)


Reference:

[Google PhotoFeed](https://developers.google.com/cloud/samples/photofeed/)

