<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>

<meta name="description" content="<spring:message code="application_description" />" />
<meta name="keywords" content="<spring:message code="application_keywords" />" />
<meta http-equiv="X-UA-Compatible" content="IE=8" />

<link rel="shortcut icon" type="image/png" href="<spring:url value="/images/favicon.png" />"></link>
<title><spring:message code="application_name" /></title>
<link rel="stylesheet" type="text/css" href="<spring:url value="/css/reset-min.css" />" />
<link rel="stylesheet" type="text/css" href="<spring:url value="/css/common.css" />" />
<link rel="stylesheet" href="<spring:url value="/js/fancybox/jquery.fancybox-1.3.4.css" />" type="text/css" media="screen" />

<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>

<script type="text/javascript"	src="<spring:url value="/js/jquery.cookie.js" />"></script>	
<script type="text/javascript"	src="<spring:url value="/js/fancybox/jquery.fancybox-1.3.4.pack.js" />"></script>
<script type="text/javascript"	src="<spring:url value="/js/fancybox/jquery.easing-1.3.pack.js" />"></script>
<script type="text/javascript"	src="<spring:url value="/js/fancybox/jquery.mousewheel-3.0.4.pack.js" />"></script>


<sec:authorize access="isAuthenticated()">
<link rel="stylesheet" href="<spring:url value="/js/contactable/contactable.css" />" type="text/css"></link>
<script type="text/javascript" src="<spring:url value="/js/contactable/jquery.contactable.js" />"></script>
</sec:authorize>