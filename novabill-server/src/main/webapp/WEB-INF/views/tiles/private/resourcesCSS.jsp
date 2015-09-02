<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="novabillCoreUrl" value="/novabill/private" />
<spring:url var="novabillDepsUrl" value="/novabill-deps" />

<link href="${novabillDepsUrl}/AngularJS-Toaster-0.4.15/toaster.css?v=${project.version}" rel="stylesheet" type="text/css" />

<link href="${novabillCoreUrl}/css/global.css?v=${project.version}" rel="stylesheet" type="text/css" />
<link href="${novabillCoreUrl}/css/directives.css?v=${project.version}" rel="stylesheet" type="text/css" />
<link href="${novabillCoreUrl}/css/dashboard.css?v=${project.version}" rel="stylesheet" type="text/css"/>
<link href="${novabillCoreUrl}/css/clients.css?v=${project.version}" rel="stylesheet" type="text/css"/>
<link href="${novabillCoreUrl}/css/invoices.css?v=${project.version}" rel="stylesheet" type="text/css"/>
<link href="${novabillCoreUrl}/css/estimations.css?v=${project.version}" rel="stylesheet" type="text/css"/>
<link href="${novabillCoreUrl}/css/transport-documents.css?v=${project.version}" rel="stylesheet" type="text/css"/>
<link href="${novabillCoreUrl}/css/credit-notes.css?v=${project.version}" rel="stylesheet" type="text/css"/>
<link href="${novabillCoreUrl}/css/commodities.css?v=${project.version}" rel="stylesheet" type="text/css"/>
<link href="${novabillCoreUrl}/css/price-lists.css?v=${project.version}" rel="stylesheet" type="text/css"/>
<link href="${novabillCoreUrl}/css/settings.css?v=${project.version}" rel="stylesheet" type="text/css"/>
<link href="${novabillCoreUrl}/css/payments.css?v=${project.version}" rel="stylesheet" type="text/css"/>
<link href="${novabillCoreUrl}/css/stats.css?v=${project.version}" rel="stylesheet" type="text/css"/>
