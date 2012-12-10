<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
	
<div class="footer">
	<span>Copyright 2012 <strong><spring:message code="application_name" /></strong>. <spring:message code="shared.allRightsReserved" />. </span>
	<a target="_blank" href="termsAndConditions"><spring:message code="registration.termsAndConditions"></spring:message></a>
	<a target="_blank" href="privacyPolicy"><spring:message code="registration.privacyPolicy"></spring:message></a>
	<a target="_blank" href="http://www.novadart.com"><spring:message code="shared.about"></spring:message></a>
</div>

<div id="contactable"></div>
<script>
	$(function() {
		$('#contactable').contactable({
			subject: location.href,
			url: '',
			name:  '<spring:message code="feedback.name" />',
			email: '<spring:message code="feedback.email" />',
			dropdownTitle: '<spring:message code="feedback.dropdownTitle" />', 
			dropdownOptions: ['<spring:message code="feedback.dropdownOption_1" />', '<spring:message code="feedback.dropdownOption_2" />', '<spring:message code="feedback.dropdownOption_3" />'],
			message : '<spring:message code="feedback.message" />', 
			submit : '<spring:message code="feedback.submit" />',
			recievedMsg : '<spring:message code="feedback.recievedMsg" />',
			notRecievedMsg : '<spring:message code="feedback.notRecievedMsg" />',
			disclaimer: '<spring:message code="feedback.disclaimer" />',
			hideOnSubmit: true
		});
	});
</script>