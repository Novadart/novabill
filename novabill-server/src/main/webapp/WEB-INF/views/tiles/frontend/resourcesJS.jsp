<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="novabillDepsUrl" value="/novabill-deps" />
<spring:url var="novabillCoreUrl" value="/novabill/frontend" />

<script src="${novabillDepsUrl}/angular/angular.js?v=${project.version}"></script>
<script src="${novabillDepsUrl}/angular/angular-route.js?v=${project.version}"></script>
<script src="${novabillDepsUrl}/angular/angular-sanitize.js?v=${project.version}"></script>
<script src="${novabillDepsUrl}/angular/angular-animate.js?v=${project.version}"></script>
<script src="${novabillDepsUrl}/angular/angular-resource.js?v=${project.version}"></script>
<script src="${novabillDepsUrl}/angular/i18n/angular-locale_it-it.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillDepsUrl}/angular-translate.min.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillDepsUrl}/angular-ui/ui-bootstrap-tpls-0.11.0.min.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillDepsUrl}/ng-infinite-scroll.min.js?v=${project.version}"></script>

<script src="${novabillCoreUrl}/scripts/translations-frontend.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/constants-frontend.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/ajax-frontend.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/directives-frontend.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/share.js?v=${project.version}" type="text/javascript"></script>
