<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 25.05.2017
  Time: 16:58
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css" integrity="sha384-rwoIResjU2yc3z8GV/NPeZWAv56rSmLldC3R/AZzGRnGxQQKnKkoFVhFQhNUwEyJ" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.1.1.slim.min.js" integrity="sha384-A7FZj7v+d/sdmMqp/nOQwliLvUsJfDHW+k9Omg/a/EheAdgtzNs3hpfag6Ed950n" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js" integrity="sha384-vBWWzlZJ8ea9aCX4pEW3rVHjgjt7zpkNpZk+02D9phzyeVkE+jo0ieGizqPLForn" crossorigin="anonymous"></script>
    <title>Students</title>
</head>
<body>
<table class="table table-striped">
    <thead>
    <tr>
        <th>Id</th>
        <th>Name</th>
        <th>Year</th>
        <th>Group</th>
        <th>Average</th>
        <th>isCountry</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${result}" var="item">
        <tr>
            <td>${item.id}</td>
            <td>${item.name}</td>
            <td>${item.year}</td>
            <td>${item.group}</td>
            <td>${item.avgmark}</td>
            <td>
            <c:if test="${item.isCountry=='Y'}">
                <input type="checkbox" value="" checked>
            </c:if>
            <c:if test="${item.isCountry=='N'}">
                <input type="checkbox" value="">
            </c:if></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<form method="POST" action="/back">
    <input type="submit" name="Back" value="Back" class="btn">
</form>
</body>
</html>
