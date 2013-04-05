<%@page import="com.novadart.novabill.domain.security.Principal"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.novadart.novabill.domain.Business"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>


<div class="header">

	<sec:authorize access="isAnonymous()">
		<div id="loginContainer">
			<a href="#" id="loginButton"><span><spring:message
						code="header.login"></spring:message></span><em></em></a>
			<div style="clear: both"></div>
			<div id="loginBox">
				<form id="loginForm" action='<spring:url value="/resources/j_spring_security_check"/>' method="post">
					<fieldset id="body">
						<fieldset>
							<label for="email"><spring:message code="shared.email"></spring:message></label>
							<input type="text" name="j_username" id="email" />
						</fieldset>
						<fieldset>
							<label for="password"><spring:message
									code="shared.password"></spring:message></label> <input type="password"
								name="j_password" id="password" />
						</fieldset>
						<label id="rememberme" for="checkbox"><input type="checkbox" id="checkbox" name="_spring_security_remember_me"/>
							<spring:message code="header.rememberme"></spring:message></label>
						<input type="submit" id="login" class="action2-button"
							value="<spring:message code="header.signin"></spring:message>" />
					</fieldset>
					<div>
						<a href="<spring:url value="/forgot-password" />"><spring:message code="header.forgotPassword"></spring:message></a>
					</div>
				</form>
			</div>
		</div>
		<script>
			var remMe = $('#checkbox');
			remMe.attr('checked', $.cookie("com.novadart.novabill.rememberMeChecked")!=null);
			remMe.change(function(e){
				$.cookie("com.novadart.novabill.rememberMeChecked", this.checked ? true : null, { expires : 365 });
			});
		</script>
	</sec:authorize>
	
	
	<sec:authorize access="isAuthenticated()">
		<%
			Principal pd = ((Principal)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			String principalName = Business.findBusiness(pd.getBusiness().getId()).getName();
		%>
		
		<span class="welcomeMessage">
			<%
			if(principalName != null && !principalName.isEmpty()){
			%>
				<spring:message code="header.welcomeMessage"/>
				<a href='<spring:url value="/private#business:"></spring:url>' class="businessName"><%=principalName%></a>
			<%
			} else {
			%>
				<span class="thanksForSignup"><spring:message code="header.thanksForSignup"/></span>
				<a href='<spring:url value="/private"></spring:url>' class="action-button noBusinessName"><spring:message code="header.noBusinessName"/></a>
			<%	
			}
			%>
			
		</span>
		
		<span class="authLinks">
			<a class="private" href='<spring:url value="/private"></spring:url>'><spring:message code="shared.private"/></a>
		
			<%-- <sec:authorize access="hasRole('ROLE_BUSINESS_FREE')">
				<a id="goPremium" class="action-button" href='<spring:url value="/premiumInfo"></spring:url>'>
					<spring:message code="header.goPremium"></spring:message>
				</a>
			</sec:authorize> --%>
			
			<a class="logoutLink" href='<spring:url value="/resources/j_spring_security_logout"></spring:url>'><spring:message code="header.logout"/></a>
		</span>
	</sec:authorize>
	

</div>

<script type="text/javascript" src="js/login.js"></script>
