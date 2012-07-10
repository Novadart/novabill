<%@page import="com.novadart.novabill.domain.security.PrincipalDetails"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.novadart.novabill.domain.Business"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>

<c:url value="/resources/j_spring_security_check" var="login_url" />

<div class="header">

	<sec:authorize access="isAnonymous()">
		<div id="loginContainer">
			<a href="#" id="loginButton"><span><spring:message
						code="header.login"></spring:message></span><em></em></a>
			<div style="clear: both"></div>
			<div id="loginBox">
				<form id="loginForm" action="${login_url}" method="post">
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
						<input type="submit" id="login"
							value="<spring:message code="header.signin"></spring:message>" />
						<label for="checkbox"><input type="checkbox" id="checkbox" />
							<spring:message code="header.rememberme"></spring:message></label>
					</fieldset>
					<div>
						<a href="#"><spring:message code="header.forgotPassword"></spring:message></a>
					</div>
				</form>
			</div>
		</div>
	</sec:authorize>
	
	
	<sec:authorize access="isAuthenticated()">
		<%PrincipalDetails pd = 
			((PrincipalDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal());%>
		
		<span class="welcomeMessage">
			<spring:message code="header.welcomeMessage"/> 
			<a href='<spring:url value="/private#business:"></spring:url>' class="businessName"><%=pd.getPrincipal().getName() %></a></span>
		
		<sec:authorize access="hasRole('ROLE_BUSINESS_FREE')">
			<a id="goPremium" class="action-button" href='<spring:url value="/premiumInfo"></spring:url>'>
				<spring:message code="header.goPremium"></spring:message>
			</a>
		</sec:authorize>
		
		<a class="logoutLink" href='<spring:url value="/j_spring_security_logout"></spring:url>'><spring:message code="header.logout"/></a>
	</sec:authorize>
	

</div>
