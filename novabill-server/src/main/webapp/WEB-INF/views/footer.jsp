<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<spring:url value="/" var="root"></spring:url>
	
<div class="footer">
	<span>Copyright 2012 <strong><spring:message code="application_name" /></strong>. <spring:message code="shared.allRightsReserved" />. </span>
	<a class="iframe tec" href="<spring:url value="/tos" />"><spring:message code="registration.termsAndConditions"></spring:message></a>
	<%-- <a class="iframe" href="<spring:url value="/privacyPolicy" />"><spring:message code="registration.privacyPolicy"></spring:message></a> --%>
	<a href="https://www.iubenda.com/privacy-policy/257554" class="iubenda-black iubenda-embed" title="Privacy Policy">Privacy Policy</a>
	<script type="text/javascript">
		(function(w, d) {
			var loader = function() {
				var s = d.createElement("script"), tag = d
						.getElementsByTagName("script")[0];
				s.src = "https://cdn.iubenda.com/iubenda.js";
				tag.parentNode.insertBefore(s, tag);
			};
			if (w.addEventListener) {
				w.addEventListener("load", loader, false);
			} else if (w.attachEvent) {
				w.attachEvent("onload", loader);
			} else {
				w.onload = loader;
			}
		})(window, document);
	</script>
	<a target="_blank" href="http://www.novadart.com"><spring:message code="shared.about"></spring:message></a>
	<a href="//plus.google.com/102114996050047445767?prsrc=3" rel="publisher" style="text-decoration:none;float:right;">
	<img src="//ssl.gstatic.com/images/icons/gplus-32.png" alt="Google+" style="border:0;width:32px;height:32px;"/></a>
</div>

<tiles:insertAttribute name="credit" />

<script type="text/javascript">
$(function() {
	$(".footer a.iframe").fancybox({
		width: '70%',
		height: '90%'
	});
});
</script>

<sec:authorize access="isAuthenticated()">
<div id="contactable"></div>
<script>
	$(function() {
		$('#contactable').contactable({
			subject: location.href,
			url: '<spring:url value="/private/feedback" />',
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
</sec:authorize>