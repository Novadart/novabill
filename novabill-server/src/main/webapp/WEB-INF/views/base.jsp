<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:url value="/resources/j_spring_security_check" var="login_url" />

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<title>Product Splash</title>
<link rel="stylesheet" type="text/css" href="css/reset-min.css" />
<link rel="stylesheet" type="text/css" href="css/common.css" />

<tiles:insertAttribute name="pagelinks" />

</head>


<body>

	<div class="container">
		<tiles:insertAttribute name="header" />
		
		<tiles:insertAttribute name="body" />

		<div class="footer">
			<p>	Copyright 2011 <strong><spring:message code="application_name"></spring:message></strong>. All Rights Reserved. </p>
		</div>
	</div>

</body>

</html>