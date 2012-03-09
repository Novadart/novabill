<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<c:url value="/resources/j_spring_security_check" var="login_url" />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Novabill home</title>

</head>
<body>
<h2>Welcome to NovaBill</h2>

<form action="${login_url}" method="post">
	<fieldset style="float: left;">
		<legend>Sign in</legend>
		<table>
			<tr>
				<td>Email</td>
				<td><input type="text" name="j_username" />
				</td>
			</tr>
			<tr>
				<td>Password</td>
				<td><input  type="password" name="j_password" />
				</td>
			</tr>
		</table>
		<input type="submit" value="Login"/>
	</fieldset>
</form>

</body>
</html>