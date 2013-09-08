<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<h1 class="title"><spring:message code="registration.title" /></h1>
<p class="description"><spring:message code="registration.description" /></p>

<form:form id="form" method="post" modelAttribute="registration" class="registrationForm">

	<table class="registrationFormTable">
		<tr>
			<td><label for="email" class="registrationLabel"><spring:message code="shared.email" /></label> </td>
			<td>
				<form:input id="email" path="email" class="registrationInput" />
				<br>
			</td>
			<td>
				<form:errors path="email" cssClass="error" />
			</td>
		</tr>
		<tr>
			<td><label for="email" class="registrationLabel"><spring:message code="shared.password" /></label></td>
			<td>
				<form:input id="password" path="password" type="password" class="registrationInput"/>
			</td>
			<td>
				<form:errors path="password" cssClass="error" />
			</td>
		</tr>
		<tr>
			<td><label for="email" class="registrationLabel"><spring:message code="shared.password.confirm" /></label></td>
			<td><form:input id="confirmPassword" path="confirmPassword"
					type="password" class="registrationInput" />
			</td>
			<td>
				<form:errors path="confirmPassword" cssClass="error" />
			</td>
		</tr>
		<tr>
			<td colspan="2" >
				<span class="acceptAgreementWrapper">
					<form:checkbox path="agreementAccepted" id="acceptAgreement" class="accept-agreement" />
					<label class="acceptAgreementLabel" for="acceptAgreement">
						<spring:message code="registration.agreement"/>
						<a class="iframe tec" href="<spring:url value="/tos" />"><spring:message code="registration.termsAndConditions" /></a>
						<a href="https://www.iubenda.com/privacy-policy/257554" class="iubenda-white iubenda-embed" title="Privacy Policy">Privacy Policy</a>
					</label>
				</span>
			</td>
			<td>
				<form:errors path="agreementAccepted" cssClass="error" />
			</td>
		</tr>
		<tr>
			<td colspan="2"><input type="submit" class="action2-button submit" value='<spring:message code="registration.register" />' /></td>
		</tr>
	</table>
	
</form:form>

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

<script type="text/javascript">
$(function() {
	$("a.iframe").fancybox({
		width: '70%',
		height: '90%'
	});
});
</script>
