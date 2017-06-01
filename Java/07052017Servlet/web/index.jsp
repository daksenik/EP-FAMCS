<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 17.05.2017
  Time: 22:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <link rel="stylesheet" type="text/css" href="style.css" />
    <head>
        <title>Profile creating</title>
    </head>
    <body>
        <div id="wrapper">
            <div id="login-form">
                <form method="POST" action="/second">
                    <div class="inform-wrapper">
                        <p class="input-header">Login:</p>
                        <input class="text-input" name="login" required type="text" value="${sessionScope.login}"/>
                    </div>
                    <div class="inform-wrapper">
                        <p class="input-header">Email:</p>
                        <input class="text-input" name="email" required type="email" value="${sessionScope.email}"/>
                    </div>
                    <div class="button-wrapper">
                        <input class="button-input" type="submit" value="Next">
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>
