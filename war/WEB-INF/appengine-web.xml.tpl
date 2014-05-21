<?xml version="1.0" encoding="utf-8"?>
<appengine-web-app xmlns="http://appengine.google.com/ns/1.0">
  <application>@applicationId@</application>
  <version>@applicationVersion@</version>

  <!-- Allows App Engine to send multiple requests to one instance in parallel: -->
  <threadsafe>true</threadsafe>

  <!-- Configure serving/caching of GWT files -->
  <static-files>
    <include path="**" />

    <!-- The following line requires App Engine 1.3.2 SDK -->
    <include path="**.nocache.*" expiration="0s" />

    <include path="**.cache.*" expiration="365d" />
    <exclude path="**.gwt.rpc" />
  </static-files>

  <!-- Configure java.util.logging -->
  <system-properties>
    <property name="java.util.logging.config.file" value="WEB-INF/logging.properties" />
    <!-- The entity manager factory class name
     By default, if this property is not set, the entity manager factory is
     "com.google.cloud.demo.model.nosql.DemoEntityManagerNoSqlFactory". That means the NoSQL
     datastore is used. If you want to use the Cloud SQL for datastore, you could set this value
     to "com.google.cloud.demo.model.sql.DemoEntityManagerSqlFactory":
     <property name="entityManagerFactory"
        value="com.google.cloud.demo.model.sql.DemoEntityManagerSqlFactory"/>
     -->
     <property name="entityManagerFactory"
        value="@entityManagerFactory@"/>

     <!-- The Cloud SQL connection URL -->
     <property name="cloudSQLConnectionUrl"
       value="@cloudSQLConnectionUrl@"/>

     <!-- The Cloud Storage bucket name -->
     <property name="cloudStorageBucketName" value="@cloudStorageBucketName@"/>
  </system-properties>

  <!-- HTTP Sessions are disabled by default. To enable HTTP sessions specify: <sessions-enabled>true</sessions-enabled>
    It's possible to reduce request latency by configuring your application to asynchronously
    write HTTP session data to the datastore: <async-session-persistence enabled="true"
    /> With this feature enabled, there is a very small chance your app will see stale
    session data. For details, see http://code.google.com/appengine/docs/java/config/appconfig.html#Enabling_Sessions -->

</appengine-web-app>
