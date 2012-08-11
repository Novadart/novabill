<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<form:form id="form" method="post" modelAttribute="changePassword"
	class="registrationForm">

	<table class="changePasswordTable">
		<tr>
			<td><label class="textboxLabel" for="password"><spring:message code="changePassword.oldPassword" /> </label></td>
			<td><form:input class="textbox" id="password" path="password" type="password" /></td>
			<td><form:errors path="password" cssClass="error" /></td>
		</tr>
		<tr>
			<td><label class="textboxLabel" for="newPassword"><spring:message code="changePassword.newPassword" /></label></td>
			<td><form:input class="textbox" id="newPassword" path="newPassword"
					type="password" /></td>
			<td><form:errors path="newPassword" cssClass="error" /></td>
		</tr>
		<tr>
			<td><label class="textboxLabel" for="confirmNewPassword"><spring:message code="changePassword.newPasswordConfirm" /></label></td>
			<td><form:input class="textbox" id="confirmNewPassword"
					path="confirmNewPassword" type="password" /></td>
			<td><form:errors path="confirmNewPassword" cssClass="error" /></td>
		</tr>
		<tr>
			<td colspan="3"><input type="submit"
				class="action2-button changePasswordSubmit"
				 value='<spring:message code="shared.submit" />' /></td>
		</tr>
	</table>

</form:form>


