<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="novabillDepsUrl" value="/novabill-deps" />
<spring:url var="novabillCoreUrl" value="/novabill/frontend" />

<script src="${novabillDepsUrl}/angular/angular.js"></script>
<script src="${novabillDepsUrl}/angular/angular-route.js"></script>
<script src="${novabillDepsUrl}/angular/angular-sanitize.js"></script>
<script src="${novabillDepsUrl}/angular/angular-animate.js"></script>
<script src="${novabillDepsUrl}/angular/angular-resource.js"></script>
<script src="${novabillDepsUrl}/angular/i18n/angular-locale_it-it.js" type="text/javascript"></script>
<script src="${novabillDepsUrl}/angular-translate.min.js" type="text/javascript"></script>
<script src="${novabillDepsUrl}/angular-ui/ui-bootstrap-tpls-0.10.0.min.js" type="text/javascript"></script>
<script src="${novabillDepsUrl}/ng-infinite-scroll.min.js"></script>

<script src="${novabillCoreUrl}/scripts/share.js" type="text/javascript"></script>
