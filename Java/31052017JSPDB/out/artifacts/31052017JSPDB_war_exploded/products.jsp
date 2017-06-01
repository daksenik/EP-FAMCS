<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 31.05.2017
  Time: 23:21
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
</head>
<body>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="index.jsp">Back</a>
        </div>
    </div>
</nav>
<jsp:useBean id="descs" scope="request" type="java.util.ArrayList"/>
<table class="table table-striped">
    <thead>
    <tr>
        <th>ID</th>
        <th>Category</th>
        <th>Product name</th>
        <th>Price</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${descs}" var="desc">
        <tr>
            <td>${desc.idProduct}</td>
            <td>${desc.category}</td>
            <td>${desc.productName}</td>
            <td>${desc.price}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<form method="POST" action="/edit_products">
    <div id="submit-buttons">
        <input type="submit" name="ACTION" value="Set price">
    </div>
    <div id="input-fields">
        <input type="number" name="id" placeholder="ID">
        <input type="number" name="delta" required placeholder="Price delta percentage">
    </div>
</form>
</body>
</html>
