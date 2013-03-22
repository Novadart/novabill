<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:url var="exportURL" value="/private/export" htmlEscape="false">
	<spring:param name="${exportClientsParamName}" value="true"/>
	<spring:param name="${exportInvoicesParamName}" value="true"/>
	<spring:param name="${exportEstimationsParamName}" value="true"/>
	<spring:param name="${exportCreditnotesParamName}" value="true"/>
	<spring:param name="${exportTransportdocsParamName}" value="true"/>
</spring:url>

<spring:url var="xsrfTokenService" value="/private/export/token" ></spring:url>
<spring:url var="loader" value="/images/loader.gif" ></spring:url>

<script type="text/javascript">
var xsrfParam = '${exportTokenParamName}';

function buildUrl(xsrfToken){
	return '${exportURL}' + '&' + xsrfParam + '=' + xsrfToken;
}

$(function(){
	$('#exportDocs').click(function(){
		$('#exportDocs').hide();
		$('#loader').show();
		
		$.get('${xsrfTokenService}', function(xsrfToken){
			$('#downloadFrame').attr('src', buildUrl(xsrfToken));
			
			$('#loader').hide();
			$('#exportDocs').show();
		});
		
	});
});
</script>



<p class="goodbyeMessage">
	<spring:message code="deleteAccount.goodbye" htmlEscape="false" />
</p>
<p class="lastChanceToExport">
	<spring:message code="deleteAccount.lastChanceToExport" />
</p>

<div id="exportDocs" class="action2-button exportLink">
	<spring:message code="deleteAccount.exportData" />
</div>
<img src="${loader}" id="loader" style="display: none;">

<p class="deleteMessage">
	<spring:message code="deleteAccount.deleteMessage" />
	<span class="lastWarning"><spring:message
			code="deleteAccount.lastWarning" /></span>
</p>
<form:form id="form" method="post" cssClass="deleteAccountForm"
	modelAttribute="deleteAccount" class="registrationForm">

	<table class="changePasswordTable">
		<tr>
			<td><label class="textboxLabel" for="password"><spring:message
						code="deleteAccount.password" /> </label></td>
			<td><form:input class="textbox" id="password" path="password"
					type="password" /></td>
			<td><form:errors path="password" cssClass="error" /></td>
		</tr>
		<tr>
			<td colspan="3"><input type="submit" class="deleteAccountSubmit"
				value='<spring:message code="deleteAccount.delete" />' /></td>
		</tr>
	</table>

</form:form>

<iframe id="downloadFrame" style="display: none;" ></iframe>
