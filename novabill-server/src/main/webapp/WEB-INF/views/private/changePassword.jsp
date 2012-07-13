<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Change password</title>
</head>
<body>

	<form:form id="form" method="post" modelAttribute="changePassword"
		class="registrationForm">

		<table>
			<tr>
				<td><label for="password">Old password</label></td>
				<td><form:input id="password" path="password" type="password"/> <br> <form:errors path="password" cssClass="error" /></td>
			</tr>
			<tr>
				<td><label for="newPassword">New password</label></td>
				<td><form:input id="newPassword" path="newPassword" type="password"/> <br> <form:errors path="newPassword" cssClass="error" /></td>
			</tr>
			<tr>
				<td><label for="confirmNewPassword">Confirm new password</label></td>
				<td><form:input id="confirmNewPassword" path="confirmNewPassword" type="password"/> <br> <form:errors path="confirmNewPassword" cssClass="error" /></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit"
					class="action2-button submit"
					value='Change password' /></td>
			</tr>
		</table>

	</form:form>


</body>
</html>