<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<title><spring:message code="application_name"></spring:message></title>
<link rel="shortcut icon" type="image/png" href="images/favicon.png"></link>
<link rel="stylesheet" type="text/css" href="css/reset-min.css" />
<link rel="stylesheet" type="text/css" href="css/common.css" />
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>

<sec:authorize access="isAuthenticated()">
<link rel="stylesheet" href="js/contactable/contactable.css" type="text/css"></link>
<script type="text/javascript" src="js/contactable/jquery.contactable.min.js"></script>
</sec:authorize>

<tiles:insertAttribute name="pagelinks" />

</head>


<body>

	<div class="container">
		<tiles:insertTemplate template="/WEB-INF/views/public/headerSimple.jsp"></tiles:insertTemplate>
		
		<tiles:insertAttribute name="body" />

	</div>
	
	<tiles:insertAttribute name="footer" />

</body>

</html>