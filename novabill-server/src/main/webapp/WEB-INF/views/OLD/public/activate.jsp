<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<form action='<spring:url value="/activate"></spring:url>' method="post">
	<table class="ativateTable">
		<tr>
			<td><label class="textboxLabel"><spring:message code="shared.email"></spring:message></label></td>
			<td><span class="email">${registration.email}</span></td>
			<td></td>
		</tr>
		<tr>
			<td><label for="password" class="textboxLabel" ><spring:message code="shared.password"></spring:message></label> </td>
			<td><input id="password" class="textbox" name="j_password" type="password" /></td>
			<td>
				<%
				Boolean wrongPassword = (Boolean)request.getAttribute("wrongPassword");
				if(wrongPassword != null && wrongPassword) {
				%>
				<span class="error">
					<spring:message code="changePassword.wrong.password" />
				</span>
				<% } %>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<input type="hidden" name="j_username" value="${registration.email}" />
				<input type="submit" class="action2-button activateSubmit" value="<spring:message code="shared.submit"></spring:message>" />
			</td>
			<td></td>
		</tr>
	</table>
</form>
