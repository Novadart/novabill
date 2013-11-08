<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/frontend_assets" var="frontendAssetsUrl" />

<%
	String pageName = (String)request.getAttribute("pageName");
%>

<meta charset="utf-8" />
<title>Novabill | <%=pageName%></title>
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />
<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="${frontendAssetsUrl}/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="${frontendAssetsUrl}/plugins/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet" type="text/css"/>
<link href="${frontendAssetsUrl}/css/reset.css" rel="stylesheet" type="text/css"/>
<link href="${frontendAssetsUrl}/css/style-metro.css" rel="stylesheet" type="text/css"/>
<link href="${frontendAssetsUrl}/css/style.css" rel="stylesheet" type="text/css"/>
<link href="${frontendAssetsUrl}/css/style-responsive.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" href="${frontendAssetsUrl}/plugins/fancybox/source/jquery.fancybox.css">               
<link href="${frontendAssetsUrl}/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
<!-- END GLOBAL MANDATORY STYLES -->
<!-- BEGIN PAGE LEVEL STYLES -->
<link href="${frontendAssetsUrl}/css/pages/prices.css" rel="stylesheet" type="text/css"/>
<!-- END PAGE LEVEL STYLES -->    
<link href="${frontendAssetsUrl}/css/themes/blue.css" rel="stylesheet" type="text/css" id="style_color"/>    
<link rel="shortcut icon" href="${frontendAssetsUrl}/img/favicon.png" />