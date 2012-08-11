<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<form:form id="form" method="post" modelAttribute="forgotPassword">
	<table class="forgotPasswordTable">
		<tr>
			<td><span class="textbox"><spring:message
						code="shared.email" /> </span></td>
			<td><form:input class="textbox" id="email" path="email" /></td>
			<td><form:errors path="email" cssClass="error" /></td>
		</tr>
		<tr>
			<td colspan="3"><input
				class="forgotPasswordSubmit action2-button" type="submit"
				value='<spring:message code="shared.submit" ></spring:message>' />
			</td>
		</tr>
	</table>

</form:form>

