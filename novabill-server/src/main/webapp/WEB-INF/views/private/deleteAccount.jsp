<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

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

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<a href='${exportURL}'>Export</a>

<form:form id="form" method="post" modelAttribute="deleteAccount"
	class="registrationForm">

	<table class="changePasswordTable">
		<tr>
			<td><label class="textboxLabel" for="password"><spring:message code="deleteAccount.password" /> </label></td>
			<td><form:input class="textbox" id="password" path="password" type="password" /></td>
			<td><form:errors path="password" cssClass="error" /></td>
		</tr>
		<tr>
			<td colspan="3"><input type="submit"
				class="action2-button changePasswordSubmit"
				 value='<spring:message code="deleteAccount.delete" />' /></td>
		</tr>
	</table>

</form:form>

</body>
</html>