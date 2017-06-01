<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 01.06.2017
  Time: 1:23
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="style.css" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css"
          integrity="sha384-rwoIResjU2yc3z8GV/NPeZWAv56rSmLldC3R/AZzGRnGxQQKnKkoFVhFQhNUwEyJ" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.1.1.slim.min.js"
            integrity="sha384-A7FZj7v+d/sdmMqp/nOQwliLvUsJfDHW+k9Omg/a/EheAdgtzNs3hpfag6Ed950n"
            crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js"
            integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb"
            crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js"
            integrity="sha384-vBWWzlZJ8ea9aCX4pEW3rVHjgjt7zpkNpZk+02D9phzyeVkE+jo0ieGizqPLForn"
            crossorigin="anonymous"></script>
    <title>Products table</title>
    <title>Profile</title>
</head>
<body>
    <div id="back">
        <form method="POST" action="/home">
            <input type="submit" name="ACTION" value="Return to the home page" id="home-button">
        </form>
    </div>
    <div id="user-info">
        <div id="non-editable-info">
            <p class="field-value">${sessionScope.login}</p><p class="field-header">Login name:</p>
            <p class="field-value">${sessionScope.is_admin}</p><p class="field-header">Admin:</p>
        </div>
        <div id="editable-info">
            <form method="POST" action="/edit_profile" style="width: 100%; margin: 0; padding: 0;">
                <input type="password" name="password" class="field-value"><p class="field-header">New password:</p>
                <c:if test = "${sessionScope.is_admin}">
                    <input type="text" name="editable_user" placeholder="Login" class="field-value"><p class="field-header">Profile to edit:</p>
                </c:if>
                <input type="submit" name="action" value="Submit" class="submit-button">
            </form>
        </div>
    </div>
</body>
</html>
