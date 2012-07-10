<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<h1 class="title"><spring:message code="registration.title" /></h1>
<p class="description"><spring:message code="registration.description" /></p>

<form:form id="form" method="post" modelAttribute="registration" class="registrationForm">

	<table class="registrationFormTable">
		<tr>
			<td><label for="email" class="registrationLabel"><spring:message code="shared.email" /></label> </td>
			<td>
				<form:input id="email" path="email" class="registrationInput" />
				<br>
				<form:errors path="email" cssClass="error" />
			</td>
		</tr>
		<tr>
			<td><label for="email" class="registrationLabel"><spring:message code="shared.password" /></label></td>
			<td>
				<form:input id="password" path="password" type="password" class="registrationInput"/>
				<br>
				<form:errors path="password" cssClass="error" />
			</td>
		</tr>
		<tr>
			<td><label for="email" class="registrationLabel"><spring:message code="shared.password.confirm" /></label></td>
			<td><form:input id="confirmPassword" path="confirmPassword"
					type="password" class="registrationInput" />
					<br>
				<form:errors path="confirmPassword" cssClass="error" />
			</td>
		</tr>
		<tr>
			<td colspan="2"><input type="submit" class="action2-button submit" value='<spring:message code="registration.register" />' /></td>
		</tr>
	</table>
	
</form:form>
