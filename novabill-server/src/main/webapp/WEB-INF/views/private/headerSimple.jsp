<%@page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.novadart.novabill.domain.security.PrincipalDetails"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<div class="header">
	<a class="logo" href='<spring:url value="/" />'><spring:message
			code="application_name"></spring:message> </a>


		<span class="authLinks"> <a class="private"
			href='<spring:url value="/private"></spring:url>'><spring:message
					code="header.private" /></a> <sec:authorize
				access="hasRole('ROLE_BUSINESS_FREE')">
				<a id="goPremium" class="action-button"
					href='<spring:url value="/premiumInfo"></spring:url>'> <spring:message
						code="header.goPremium"></spring:message>
				</a>
			</sec:authorize> <a class="logoutLink"
			href='<spring:url value="/resources/j_spring_security_logout"></spring:url>'><spring:message
					code="header.logout" /></a>
		</span>
</div>

