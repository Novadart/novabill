<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="novabillDepsUrl" value="/novabill-deps" />
<spring:url var="novabillCoreUrl" value="/novabill" />

<script src="${novabillDepsUrl}/angular/angular.min.js"></script>
<script src="${novabillDepsUrl}/angular/angular-route.min.js"></script>
<script src="${novabillDepsUrl}/angular/angular-sanitize.min.js"></script>
<script src="${novabillDepsUrl}/angular/angular-animate.min.js"></script>
<script src="${novabillDepsUrl}/angular/i18n/angular-locale_it-it.js" type="text/javascript"></script>
<script src="${novabillDepsUrl}/angular-translate.min.js" type="text/javascript"></script>
<script src="${novabillDepsUrl}/angular-ui/ui-bootstrap-tpls-0.10.0.min.js" type="text/javascript"></script>
<script src="${novabillDepsUrl}/bignumber-1.3.0/bignumber.min.js"></script>
<script src="${novabillDepsUrl}/ng-infinite-scroll.min.js"></script>

<script src="${novabillCoreUrl}/scripts/translations.js" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/constants.js" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/calc.js" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/utils.js" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/directives-dialogs.js" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/directives.js" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/dashboard-controllers.js" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/dashboard.js" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/clients-controllers.js" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/clients.js" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/invoices-controllers.js" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/invoices.js" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/estimations-controllers.js" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/estimations.js" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/transport-documents-controllers.js" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/transport-documents.js" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/credit-notes-controllers.js" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/credit-notes.js" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/commodities-controllers.js" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/commodities.js" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/price-lists-controllers.js" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/price-lists.js" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/settings.js" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/settings-controllers.js" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/payments.js" type="text/javascript"></script>
<script src="${novabillCoreUrl}/scripts/payments-controllers.js" type="text/javascript"></script>
