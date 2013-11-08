<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="privateAssetsUrl" value="/private_assets"></spring:url>

<!-- BEGIN PAGE LEVEL PLUGIN STYLES --> 
<link href="${privateAssetsUrl}/plugins/gritter/css/jquery.gritter.css" rel="stylesheet" type="text/css"/>
<link href="${privateAssetsUrl}/plugins/bootstrap-daterangepicker/daterangepicker.css" rel="stylesheet" type="text/css" />
<link href="${privateAssetsUrl}/plugins/fullcalendar/fullcalendar/fullcalendar.css" rel="stylesheet" type="text/css"/>
<link href="${privateAssetsUrl}/plugins/jqvmap/jqvmap/jqvmap.css" rel="stylesheet" type="text/css" media="screen"/>
<link href="${privateAssetsUrl}/plugins/jquery-easy-pie-chart/jquery.easy-pie-chart.css" rel="stylesheet" type="text/css" media="screen"/>
<!-- END PAGE LEVEL PLUGIN STYLES -->
<!-- BEGIN PAGE LEVEL STYLES --> 
<link href="${privateAssetsUrl}/css/pages/tasks.css" rel="stylesheet" type="text/css" media="screen"/>
<!-- END PAGE LEVEL STYLES -->