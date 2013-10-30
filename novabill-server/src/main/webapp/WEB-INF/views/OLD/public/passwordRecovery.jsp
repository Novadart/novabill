<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<form:form id="form" method="post" modelAttribute="forgotPassword">

	<table class="passwordRecoveryTable">
		<tr>
			<td><label class="textboxLabel"><spring:message code="shared.email"></spring:message></label></td>
			<td><span class="email">${forgotPassword.email}</span></td>
			<td></td>
		</tr>
		<tr>
			<td><label class="textboxLabel" for="password"><spring:message code="shared.password"></spring:message></label></td>
			<td><form:input class="textbox" id="password" path="password" type="password" /></td>
			<td><form:errors path="password" cssClass="error" /></td>
		</tr>
		<tr>
			<td><label class="textboxLabel" for="confirmPassword"><spring:message code="shared.password.confirm"></spring:message></label></td>
			<td><form:input class="textbox" id="confirmPassword" path="confirmPassword"	type="password" /></td>
			<td><form:errors path="confirmPassword" cssClass="error" /></td>
		</tr>
		<tr>
			<td colspan="3">
				<input type="submit" class="action2-button passwordRecoverySubmit" value='<spring:message code="shared.submit"></spring:message>' />			
			</td>
		</tr>
	</table>
</form:form>


