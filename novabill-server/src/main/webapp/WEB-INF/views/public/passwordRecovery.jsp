<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<form:form id="form" method="post" modelAttribute="forgotPassword">

		<table>
			<tr>
				<td>Email:</td>
				<td>${forgotPassword.email}</td>
			</tr>
			<tr>
				<td>Password:</td>
				<td><form:input id="password" path="password" type="password" />
				</td>
				<td><form:errors path="password" cssClass="error" /></td>
			</tr>
			<tr>
				<td>Retype password:</td>
				<td><form:input id="confirmPassword" path="confirmPassword"
						type="password" /></td>
				<td><form:errors path="confirmPassword" cssClass="error" /></td>
			</tr>
		</table>
		<input type="submit" value="Change password" />
	</form:form>


</body>
</html>