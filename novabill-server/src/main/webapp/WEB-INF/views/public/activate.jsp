<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:url var="activateUrl" value="/activate"/>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Activate</title>
</head>
<body>

	<c:if test="${not empty wrongPassword and wrongPassword}">
		<p style="color: red;">
			<spring:message code="incorrect.password" />
		</p>
	</c:if>
	<form action="${activateUrl}" method="post">
		<table>
			<tr>
				<td>Email:</td>
				<td>${registration.email}</td>
			</tr>
			<tr>
				<td>Password:</td>
				<td><input name="j_password" type="password" /></td>
			</tr>
		</table>
		<input type="hidden" name="j_username" value="${registration.email}" />
		<input type="submit" value="Activate" />
	</form>
</body>
</html>