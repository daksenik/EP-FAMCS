<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 17.05.2017
  Time: 22:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page errorPage="errorpage.jsp"%>
<%@ taglib prefix="gc" uri="/WEB-INF/tld/colortag.tld" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <link rel="stylesheet" type="text/css" href="style.css" />
    <head>
        <title>Confirm profile information</title>
    </head>
    <body>
        <%@ include file="incheader.jsp" %>
        <div id="wrapper">
            <div class="known-info">
                <p class="field-header"><gc:grad color="red">Login:</gc:grad></p><p class="field-content">${sessionScope.login}</p>
                <p class="field-header"><gc:grad color="green">Email:</gc:grad></p><p class="field-content">${sessionScope.email}</p>
                <p class="field-header"><gc:grad color="blue">Name:</gc:grad></p><p class="field-content">${sessionScope.name}</p>
                <p class="field-header"><gc:grad color="red">Surname</gc:grad></p><p class="field-content">${sessionScope.surname}</p>
                <p class="field-header"><gc:grad color="green">Age:</gc:grad></p><p class="field-content">${sessionScope.age}</p>
            </div>
            <div class="button-wrapper">
                <input class="button-input" type="reset" onclick="location.href='/second.jsp'" value="Back"/>
            </div>
        </div>
    </body>
</html>
