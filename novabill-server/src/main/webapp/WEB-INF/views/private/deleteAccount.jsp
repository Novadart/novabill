<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<p class="goodbyeMessage"><spring:message code="deleteAccount.goodbye" htmlEscape="false" /></p>
<p class="lastChanceToExport"><spring:message code="deleteAccount.lastChanceToExport" /></p>
<iframe src='<spring:url value="/private/delete/deleteAccountExportButton"></spring:url>'></iframe>




<p class="deleteMessage"><spring:message code="deleteAccount.deleteMessage" /> <span class="lastWarning"><spring:message code="deleteAccount.lastWarning" /></span></p>
<form:form id="form" method="post" cssClass="deleteAccountForm" modelAttribute="deleteAccount"
	class="registrationForm">

	<table class="changePasswordTable">
		<tr>
			<td><label class="textboxLabel" for="password"><spring:message code="deleteAccount.password" /> </label></td>
			<td><form:input class="textbox" id="password" path="password" type="password" /></td>
			<td><form:errors path="password" cssClass="error" /></td>
		</tr>
		<tr>
			<td colspan="3">
				<input type="submit" class="deleteAccountSubmit" value='<spring:message code="deleteAccount.delete" />' />
			</td>
		</tr>
	</table>

</form:form>

