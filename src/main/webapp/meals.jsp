<%--
  Created by IntelliJ IDEA.
  User: NooBik
  Date: 03.02.2023
  Time: 16:05
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <title>Meal list</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<a href="<%=request.getContextPath()%>/meals?action=insert">Add Meal</a>
<br><br>
<table border="1px" bordercolor="black" cellpadding="10px" cellspacing="0">
    <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="meal" items="${mealsTo}">
            <tr style="color: <c:out value="${meal.excess ? 'red' : 'green'}" />">
                <td>${meal.dateTime.format(dateTimeFormatter)}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="<%=request.getContextPath()%>/meals?action=edit&mealId=<c:out value="${meal.id}"/>">Update</a></td>
                <td><a href="<%=request.getContextPath()%>/meals?action=delete&mealId=<c:out value="${meal.id}"/>">Delete</a></td>
            </tr>
        </c:forEach>
    </tbody>
</table>
</body>
</html>
