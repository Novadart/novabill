<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<form:form id="form" method="post" modelAttribute="registration">

	<table>
		<tr>
			<td>Email:</td>
			<td><form:input id="email" path="email" /></td>
			<td><form:errors path="email" cssClass="error" /></td>
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
	<input type="submit" value="Register" />
</form:form>
