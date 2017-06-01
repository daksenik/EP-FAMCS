<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 31.05.2017
  Time: 23:01
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="style.css"/>
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
    <title>Items table</title>
</head>
<body>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="index.jsp">Back</a>
        </div>
    </div>
</nav>

<jsp:useBean id="items" scope="request" type="java.util.ArrayList"/>
<table class="table table-striped">
    <thead>
    <tr>
        <th>SID</th>
        <th>ID</th>
        <th>Barcode</th>
        <th>Receiving date</th>
        <th>Selling date</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${items}" var="item">
        <tr>
            <td>${item.sidProduct}</td>
            <td>${item.idProduct}</td>
            <td>${item.barcode}</td>
            <td>${item.receivingDate}</td>
            <td>${item.sellingDate}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<form method="POST" action="/edit_item">
    <div id="submit-buttons">
        <input type="submit" name="ACTION" value="Edit">
        <input type="submit" name="ACTION" value="Remove">
    </div>
    <div id="input-fields">
        <input type="number" name="sid" required placeholder="SID">
        <input type="text" name="selling_date" placeholder="DATE">
    </div>
</form>
</body>
</html>
