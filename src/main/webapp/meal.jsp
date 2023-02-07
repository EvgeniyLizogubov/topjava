<%--
  Created by IntelliJ IDEA.
  User: NooBik
  Date: 07.02.2023
  Time: 15:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a> </h3>
<hr>
<h2>Add or edit meal</h2>
<form method="POST" action="<%=request.getContextPath()%>/meals?mealId=<c:out value="${meal.id}"/>">
  DateTime: <input type="datetime-local" name="dob" value="<c:out value="${meal.dateTime}"/>"/><br/><br/>
  Description: <input type="text" name="description" value="<c:out value="${meal.description}"/>"/><br/><br/>
  Calories: <input type="text" name="calories" value="<c:out value="${meal.calories}"/>"/><br/><br/>
  <input type="submit" value="Save"/> <button onclick="window.history.back()" type="button">Cancel</button>
</form>
</body>
</html>
