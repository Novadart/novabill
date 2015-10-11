<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="novabillDepsUrl" value="/novabill-deps" />
<spring:url var="novabillCoreUrl" value="/novabill/frontend" />
<spring:url var="novabillPrivateCoreUrl" value="/novabill/private" />

<script src="${novabillDepsUrl}/angular/angular.min.js?v=${project.version}"></script>
<script src="${novabillDepsUrl}/angular/angular-route.min.js?v=${project.version}"></script>
<script src="${novabillDepsUrl}/angular/angular-sanitize.min.js?v=${project.version}"></script>
<script src="${novabillDepsUrl}/angular/angular-animate.min.js?v=${project.version}"></script>
<script src="${novabillDepsUrl}/angular/angular-resource.min.js?v=${project.version}"></script>
<script src="${novabillDepsUrl}/angular/i18n/angular-locale_it-it.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillDepsUrl}/angular-translate.min.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillDepsUrl}/angular-ui/ui-bootstrap-tpls-0.13.3.min.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillDepsUrl}/ng-infinite-scroll.min.js?v=${project.version}"></script>
<script src="${novabillDepsUrl}/ng-google-chart-0.0.11.js?v=${project.version}"></script>
<script src="${novabillDepsUrl}/bignumber-1.3.0/bignumber.min.js?v=${project.version}"></script>

<script src="${novabillCoreUrl}/scripts/translations-frontend.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/constants-frontend.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/ajax-frontend.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/directives-frontend.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/share.js?v=${project.version}" type="text/javascript"></script>