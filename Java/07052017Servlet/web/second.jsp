<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 17.05.2017
  Time: 22:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <link rel="stylesheet" type="text/css" href="style.css" />
    <head>
        <title>Fill personal info</title>
    </head>
    <body>
        <div id="wrapper">
            <div class="known-info">
                <p class="field-header">Login:</p><p class="field-content">${sessionScope.login}</p>
                <p class="field-header">Email:</p><p class="field-content">${sessionScope.email}</p>
            </div>
            <div id="login-form">
                <form id="personal-info-form" method="POST" action="/third">
                    <div class="inform-wrapper">
                        <p class="input-header">Name:</p>
                        <input class="text-input" name="name" type="text" value="${sessionScope.name}"/>
                    </div>
                    <div class="inform-wrapper">
                        <p class="input-header">Surname:</p>
                        <input class="text-input" name="surname" type="text" value="${sessionScope.surname}"/>
                    </div>
                    <div class="inform-wrapper">
                        <p class="input-header">Age:</p>
                        <input class="numb-input" name="age" type="number" value="${sessionScope.age}"/>
                    </div>
                    <div class="button-wrapper">
                        <input class="button-input" type="reset" onclick="location.href='/index.jsp'" value="Back"/>
                        <input class="button-input" type="submit" value="Next">
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>
