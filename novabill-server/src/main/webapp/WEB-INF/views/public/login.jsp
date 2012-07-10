<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<form name="f"
	action='<spring:url value="/resources/j_spring_security_check"></spring:url>'
	method="POST" class="loginForm">
	<table class="loginFormTable">
		<tr>
			<td>
				<label class="loginLabel" for="j_username"><spring:message
					code="shared.email" /> </label>
			</td>
			<td>
				<input id="j_username" class="loginInput" type='text'
					name='j_username' />
			</td>
		</tr>
		<tr>
			<td>
				<label class="loginLabel" for="j_password"> <spring:message
					code="shared.password" />
				</label>
			</td>
			<td>
				<input id="j_password" type='password' class="loginInput" name='j_password' />
				<br>
				<a class="forgottenPassword" href='<spring:url value="/forgot-password" />'><spring:message code="header.forgotPassword" /> </a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<input id="proceed" class="action2-button submit" type="submit"
					value='<spring:message code="header.login" />' />	
			</td>
		</tr>
	</table>
</form>
