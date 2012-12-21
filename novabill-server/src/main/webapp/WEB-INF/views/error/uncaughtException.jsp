<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/" var="root"></spring:url>

<img class="errorImg" src='${root}images/error/nosignal.gif'>
<div class="errorMessage"><spring:message code="error.uncaught"></spring:message> </div>
<a class="errorGotohome" href="<spring:url value="/" />"><spring:message code="error.gotohome"></spring:message></a>