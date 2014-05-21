<%@ page contentType="text/html;charset=UTF-8" language="java"
  isELIgnored="false"%>

<%@ page import="com.google.cloud.demo.*"%>

<%
  AppContext appContext = AppContext.getAppContext();
  ConfigManager configManager = appContext.getConfigManager();
  String errorCodeStr = request.getParameter("code");
  int errorCode = ConfigManager.ERROR_CODE_UNKNOWN;
  if (errorCodeStr != null) {
    errorCode = Integer.parseInt(errorCodeStr);
  }
  String errorMessage = configManager.getErrorMessage(errorCode);
%>
<!DOCTYPE html>
<head>
</head>
<body>
<div class="error-message">
<%= errorMessage %>
</div>
<div>
<a href="/">Reload Main Page</a>
</div>
</body>
</html>