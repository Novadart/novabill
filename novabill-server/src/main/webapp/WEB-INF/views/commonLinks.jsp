<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>

<spring:url value="/" var="root"></spring:url>

<link rel="shortcut icon" type="image/png" href="${root}images/favicon.png"></link>
<title><spring:message code="application_name"></spring:message></title>
<link rel="stylesheet" type="text/css" href="${root}css/reset-min.css" />
<link rel="stylesheet" type="text/css" href="${root}css/common.css" />
<link rel="stylesheet" href="${root}js/fancybox/jquery.fancybox-1.3.4.css" type="text/css" media="screen" />

<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>

<script type="text/javascript"	src="${root}js/jquery.cookie.js"></script>	
<script type="text/javascript"	src="${root}js/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<script type="text/javascript"	src="${root}js/fancybox/jquery.easing-1.3.pack.js"></script>
<script type="text/javascript"	src="${root}js/fancybox/jquery.mousewheel-3.0.4.pack.js"></script>


<sec:authorize access="isAuthenticated()">
<link rel="stylesheet" href="${root}js/contactable/contactable.css" type="text/css"></link>
<script type="text/javascript" src="${root}js/contactable/jquery.contactable.js"></script>
</sec:authorize>