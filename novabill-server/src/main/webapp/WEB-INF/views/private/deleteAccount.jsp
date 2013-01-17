<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:url var="exportURL" value="/private/export">
	<spring:param name="${exportClientsParamName}" value="true"/>
	<spring:param name="${exportInvoicesParamName}" value="true"/>
	<spring:param name="${exportEstimationsParamName}" value="true"/>
	<spring:param name="${exportCreditnotesParamName}" value="true"/>
	<spring:param name="${exportTransportdocsParamName}" value="true"/>
	<spring:param name="${exportTokenParamName}" value="${exportToken}"/>
</spring:url>

<p class="goodbyeMessage"><spring:message code="deleteAccount.goodbye" htmlEscape="false" /></p>
<p class="lastChanceToExport"><spring:message code="deleteAccount.lastChanceToExport" /></p>
<a class="action2-button exportLink" href='${exportURL}'><spring:message code="deleteAccount.exportData" /></a>

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

