<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="novabillDepsUrl" value="/novabill-deps" />
<spring:url var="novabillCoreUrl" value="/novabill/private" />

<script src="${novabillDepsUrl}/angular/angular.min.js?v=${project.version}"></script>
<script src="${novabillDepsUrl}/angular/angular-route.min.js?v=${project.version}"></script>
<script src="${novabillDepsUrl}/angular/angular-cookies.min.js?v=${project.version}"></script>
<script src="${novabillDepsUrl}/angular/angular-sanitize.min.js?v=${project.version}"></script>
<script src="${novabillDepsUrl}/angular/angular-animate.min.js?v=${project.version}"></script>
<script src="${novabillDepsUrl}/angular/angular-resource.min.js?v=${project.version}"></script>
<script src="${novabillDepsUrl}/angular/i18n/angular-locale_it-it.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillDepsUrl}/angular-translate.min.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillDepsUrl}/angular-ui/ui-bootstrap-tpls-0.13.3.min.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillDepsUrl}/angular-file-upload.min.js?v=${project.version}"></script>
<script src="${novabillDepsUrl}/bignumber-1.3.0/bignumber.min.js?v=${project.version}"></script>
<script src="${novabillDepsUrl}/ng-infinite-scroll.min.js?v=${project.version}"></script>
<script src="${novabillDepsUrl}/stacktracejs-0.6.0/stacktrace.js?v=${project.version}"></script>
<script src="${novabillDepsUrl}/AngularJS-Toaster-0.4.7/toaster.js?v=${project.version}"></script>
<script src="${novabillDepsUrl}/ng-google-chart-0.0.11.js?v=${project.version}"></script>

<script src="${novabillCoreUrl}/scripts/logging.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/ajax.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/translations.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/constants.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/calc.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/utils.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/gwt-bridge.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/directives-validation.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/directives-forms.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/directives-dialogs.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/directives.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/dashboard-controllers.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/dashboard.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/clients-controllers.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/clients.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/invoices-controllers.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/invoices.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/estimations-controllers.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/estimations.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/transport-documents-controllers.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/transport-documents.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/credit-notes-controllers.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/credit-notes.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/commodities-controllers.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/commodities.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/price-lists-controllers.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/price-lists.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/settings.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/settings-controllers.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/share.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/share-controllers.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/stats-general.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/stats-clients.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/stats-commodities.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/stats-controllers.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/payments.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/payments-controllers.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/hello.js?v=${project.version}" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/notifications.js?v=${project.version}" type="text/javascript"></script>
