<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a> </h3>
<hr>
<h2>${meal == null ? 'Add meal' : 'Edit meal'}</h2>
<form method="POST" action="${pageContext.request.contextPath}/meals"/>
    <input type="hidden" name="id" value="${meal.id}" />
  DateTime: <input type="datetime-local" name="dateTime" value="${meal == null ? currentDateTime.format(dateTimeFormatter) : meal.dateTime}" required/><br/><br/>
  Description: <input type="text" name="description" value="${meal.description}" required/><br/><br/>
  Calories: <input type="number" name="calories" value="${meal.calories}" required/><br/><br/>
  <input type="submit" value="Save"/> <button onclick="window.history.back()" type="button">Cancel</button>
</form>
</body>
</html>
