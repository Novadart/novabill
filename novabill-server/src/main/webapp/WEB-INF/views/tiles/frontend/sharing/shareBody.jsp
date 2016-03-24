<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="novabillDepsUrl" value="/novabill-deps" />
<spring:url var="novabillCoreUrl" value="/novabill/frontend" />
<spring:url var="novabillPrivateCoreUrl" value="/novabill/private" />

<h1>
    <%=request.getAttribute("businessName")%>
</h1>

<link href="${novabillCoreUrl}/css/directives-frontend.css?v=${project.version}" rel="stylesheet" type="text/css" />

<style>
    h1 {
        text-align: center;
    }

    .share-body {
        margin-top: 40px;
    }



    .tab-pane {
        margin-top: 20px;
        margin-bottom: 40px;
    }
</style>

<div class="container share-body" ng-app="novabill-frontend.share">

    <div class="row">
        <div class="col-md-12" infinite-scroll="loadMoreInvoices()">
            <tabset>
                <tab>
                    <tab-heading>
                        {{'INVOICES' | translate}}
                    </tab-heading>
                    <div class="container-fluid" style="min-height: 400px;" ng-controller="InvoicesCtrl">

                        <div class="row">

                            <alert type="info" ng-show="!hideInfo" class="col-md-6 col-md-offset-3" close="hideInfo=true">
                                <ol>
                                    <li>Seleziona l'intervallo di date di creazione dei documenti</li>
                                    <li>Premi sul tasto "Scarica" per scaricare un archivio con i PDF dei documenti compresi nell'intervallo di date</li>
                                </ol>
                                Se premi il pulsante "Ripulisci" l'intervallo di date viene re-impostato sull'anno in corso.
                            </alert>


                            <!--/span-->
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label col-md-4" style="text-align: right;">{{'PAYMENTS_STATUS_START_DATE' | translate}}</label>
                                    <div class="col-md-8">
                                        <p class="input-group input-medium">
                                            <input ng-disabled="isLoading()" type="text" class="form-control"
                                                   readonly="readonly"
                                                   datepicker-popup="dd MMMM yyyy"
                                                   ng-model="startDate"
                                                   is-open="openSD"
                                                   datepicker-options="dateOptions"
                                                   show-button-bar="false" >
                                            <span class="input-group-btn">
                                               <button ng-disabled="isLoading()" class="btn btn-default" ng-click="openStartDate($event)">
                                                   <i class="glyphicon glyphicon-calendar"></i>
                                               </button>
                                            </span>
                                        </p>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="control-label col-md-4" style="text-align: right;">{{'PAYMENTS_STATUS_END_DATE' | translate}}</label>
                                    <div class="col-md-8">
                                        <p class="input-group input-medium">
                                            <input ng-disabled="isLoading()" type="text" class="form-control"
                                                   readonly="readonly"
                                                   datepicker-popup="dd MMMM yyyy"
                                                   ng-model="endDate"
                                                   is-open="openED"
                                                   datepicker-options="dateOptions"
                                                   show-button-bar="false" >
		                                    <span class="input-group-btn">
		               <button ng-disabled="isLoading()" class="btn btn-default" ng-click="openEndDate($event)">
                           <i class="glyphicon glyphicon-calendar"></i>
                       </button>
		             </span>
                                        </p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-5 col-md-offset-1">
                                <button ng-disabled="isLoading()" class="btn red col-md-5" ng-click="clear()">
                                    {{'CLEAR' | translate}} <i class="fa fa-eraser"></i>
                                </button>

                                <button style="margin-left: 20px;" ng-show="invoices && invoices.length > 0" class="btn blue" ng-click="download()">
                                    {{'DOWNLOAD' | translate}} <i class="fa fa-download"></i>
                                </button>

                            </div>
                            <!--/span-->

                        </div>

                        <div class="row" >
                            <img class="spinner" ng-show="isLoading()" src="frontend_assets/img/ajax-loading.gif">
                            <p class="no-data-message text-center text-info" ng-show="invoices && invoices.length===0">{{'NO_DATA' | translate}}</p>
                            <n-share-doc doc="inv" ng-repeat="inv in invoices"></n-share-doc>
                        </div>


                    </div>
                </tab>

                <tab select="load()" ng-controller="CreditNotesCtrl">
                    <tab-heading>
                        {{'CREDIT_NOTES' | translate}}
                    </tab-heading>
                    <div class="container-fluid" style="min-height: 400px;" >


                        <div class="row">

                            <!--/span-->
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label col-md-4" style="text-align: right;">{{'PAYMENTS_STATUS_START_DATE' | translate}}</label>
                                    <div class="col-md-8">
                                        <p class="input-group input-medium">
                                            <input ng-disabled="isLoading()" type="text" class="form-control"
                                                   readonly="readonly"
                                                   datepicker-popup="dd MMMM yyyy"
                                                   ng-model="startDate"
                                                   is-open="openSD"
                                                   datepicker-options="dateOptions"
                                                   show-button-bar="false" >
                                            <span class="input-group-btn">
                                               <button ng-disabled="isLoading()" class="btn btn-default" ng-click="openStartDate($event)">
                                                   <i class="glyphicon glyphicon-calendar"></i>
                                               </button>
                                            </span>
                                        </p>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="control-label col-md-4" style="text-align: right;">{{'PAYMENTS_STATUS_END_DATE' | translate}}</label>
                                    <div class="col-md-8">
                                        <p class="input-group input-medium">
                                            <input ng-disabled="isLoading()" type="text" class="form-control"
                                                   readonly="readonly"
                                                   datepicker-popup="dd MMMM yyyy"
                                                   ng-model="endDate"
                                                   is-open="openED"
                                                   datepicker-options="dateOptions"
                                                   show-button-bar="false" >
		                                    <span class="input-group-btn">
		               <button ng-disabled="isLoading()" class="btn btn-default" ng-click="openEndDate($event)">
                           <i class="glyphicon glyphicon-calendar"></i>
                       </button>
		             </span>
                                        </p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-5 col-md-offset-1">
                                <button ng-disabled="isLoading()" class="btn red col-md-5" ng-click="clear()">
                                    {{'CLEAR' | translate}} <i class="fa fa-eraser"></i>
                                </button>

                                <button style="margin-left: 20px;" ng-show="creditNotes && creditNotes.length > 0" class="btn blue" ng-click="download()">
                                    {{'DOWNLOAD' | translate}} <i class="fa fa-download"></i>
                                </button>

                            </div>
                            <!--/span-->

                        </div>

                        <div class="row" >
                            <img class="spinner" ng-show="isLoading()" src="frontend_assets/img/ajax-loading.gif">
                            <p class="no-data-message text-center text-info" ng-show="creditNotes && creditNotes.length===0">{{'NO_DATA' | translate}}</p>
                            <n-share-doc doc="cn" ng-repeat="cn in creditNotes"></n-share-doc>
                        </div>

                    </div>
                </tab>




                <tab class="pull-right" ng-controller="StatsClientsCtrl" select="load()">
                    <tab-heading>
                        {{'STATS_CLIENTS' | translate}}
                    </tab-heading>
                    <div class="container-fluid">

                        <div class="row">

                            <div class="col-md-offset-6 col-md-2">
                                <select class="year-select form-control"
                                        ng-model="year"
                                        ng-options="y for y in years"></select>
                            </div>

                            <div class="col-md-4">
                                <select class="form-control"
                                        style="margin-right: 10px"
                                        ng-model="selectedClient"
                                        ng-options="cl.id as cl.name for cl in clients"></select>
                            </div>

                            <div class="col-md-12">

                                <form class="form-horizontal" role="form">
                                    <div class="form-body">
                                        <div class="form-group">
                                            <label class="col-md-2 control-label">{{'TOTAL_INVOICING_BEFORE_TAXES' | translate}} {{year}}</label>
                                            <div class="col-md-10">
                                                <p class="form-control-static">{{clientDetails.totalBeforeTaxesCurrentYear | currency}}</p>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-md-2 control-label">{{'TOTAL_INVOICING_BEFORE_TAXES_OVERALL' | translate}}</label>
                                            <div class="col-md-10">
                                                <p class="form-control-static">{{clientDetails.totalBeforeTaxes | currency}}</p>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-md-2 control-label">{{'STATS_CLIENT_ADDED_AT' | translate}}</label>
                                            <div class="col-md-10">
                                                <p class="form-control-static">{{clientDetails.timestamp | date:'dd MMMM yyyy'}}</p>
                                            </div>
                                        </div>
                                    </div>
                                </form>

                            </div>
                        </div>


                        <div class="row" style="margin-top: 50px;">

                            <div google-chart chart="totalsPerMonthsChart"></div>

                        </div>


                        <div class="row" style="margin-top: 50px;">
                            <div class="col-md-12">

                                <div class="col-md-6">
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                            <h3 class="panel-title">{{'STATS_COMMODITIES_FOR_CLIENT_SORTED_BY_INVOICING' | translate}}</h3>
                                        </div>
                                        <div class="panel-body">

                                            <div class="table-responsive">
                                                <p class="no-data-message text-center text-info" ng-show="commodities.length===0">{{'NO_DATA' | translate}}</p>
                                                <table class="table table-hover" ng-show="commodities">
                                                    <thead>
                                                    <tr>
                                                        <th>{{'DESCRIPTION' | translate}}</th>
                                                        <th>{{'QUANTITY' | translate}}</th>
                                                        <th>{{'TOTAL_INVOICING_BEFORE_TAXES' | translate}}</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <tr ng-repeat="data in commodities">
                                                        <td><a href="javascript:;" ng-click="openCommodityStats(data.id)">{{data.description}}</a></td>
                                                        <td>{{data.quantity | number}}</td>
                                                        <td>{{data.totalBeforeTaxes | currency}}</td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>

                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>

                    </div>
                </tab>





                <tab class="pull-right" ng-controller="StatsGeneralCtrl" select="load()">
                    <tab-heading>
                        {{'STATS_GENERAL' | translate}}
                    </tab-heading>
                    <div class="container-fluid" >

                        <div class="row">

                            <div class="col-md-offset-10 col-md-2">
                                <select class="pull-right year-select form-control input-small" ng-model="year" ng-options="y for y in years"></select>
                            </div>

                        </div>


                        <div class="row" >
                            <div google-chart chart="totalsPerMonthsChart"></div>
                        </div>

                        <div class="row" style="margin-top: 50px;">


                            <div class="col-md-4">
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <h3 class="panel-title">{{'TOTAL_INVOICING' | translate}} {{year}}</h3>
                                    </div>
                                    <div class="panel-body">
                                        <form class="form-horizontal" role="form">
                                            <div class="form-body">
                                                <div class="form-group">
                                                    <label class="col-md-4 control-label"><strong>{{'TOTAL_INVOICING_BEFORE_TAXES' | translate}}</strong></label>
                                                    <div class="col-md-8">
                                                        <p class="form-control-static"><strong>{{totals.totalBeforeTaxes | currency}}</strong></p>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-md-4 control-label">{{'VAT' | translate}}</label>
                                                    <div class="col-md-8">
                                                        <p class="form-control-static">{{totals.totalTaxes | currency}}</p>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-md-4 control-label">{{'TOTAL_INVOICING' | translate}}</label>
                                                    <div class="col-md-8">
                                                        <p class="form-control-static">{{totals.totalAfterTaxes | currency}}</p>
                                                    </div>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>

                            <div class="col-md-4">
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <h3 class="panel-title">{{'CLIENTS' | translate}}</h3>
                                    </div>
                                    <div class="panel-body">

                                        <form class="form-horizontal" role="form">
                                            <div class="form-body">
                                                <div class="form-group">
                                                    <label class="col-md-4 control-label">{{'TOTAL' | translate}}</label>
                                                    <div class="col-md-8">
                                                        <p class="form-control-static">{{clients.totalCount}}</p>
                                                    </div>
                                                </div>
                                            </div>
                                        </form>

                                        <div google-chart chart="clientsChart"></div>

                                    </div>
                                </div>
                            </div>

                            <div class="col-md-4">
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <h3 class="panel-title">{{'COMMODITIES' | translate}}</h3>
                                    </div>
                                    <div class="panel-body">
                                        <form class="form-horizontal" role="form">
                                            <div class="form-body">
                                                <div class="form-group">
                                                    <label class="col-md-4 control-label">{{'TOTAL' | translate}}</label>
                                                    <div class="col-md-8">
                                                        <p class="form-control-static">{{commodities.totalCount}}</p>
                                                    </div>
                                                </div>
                                            </div>
                                        </form>

                                        <div google-chart chart="commoditiesChart"></div>

                                    </div>
                                </div>
                            </div>


                        </div>


                        <div class="row" style="margin-top: 50px;">

                            <div class="col-md-6">
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <h3 class="panel-title">{{'STATS_CLIENTS_SORTED_BY_INVOICING' | translate}}</h3>
                                    </div>
                                    <div class="panel-body">

                                        <p class="no-data-message text-center text-info" ng-show="ranks.clients && ranks.clients.length===0">{{'NO_DATA' | translate}}</p>

                                        <div class="table-responsive" ng-show="ranks.clients && ranks.clients.length > 0" style="height: 500px; overflow: auto;">
                                            <table class="table table-hover">
                                                <thead>
                                                <tr>
                                                    <th>{{'NAME' | translate}}</th>
                                                    <th>{{'TOTAL_INVOICING_BEFORE_TAXES' | translate}}</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr ng-repeat="data in ranks.clients">
                                                    <td><a href="javascript:;" ng-click="openClientStats(data.id)">{{data.name}}</a></td>
                                                    <td>{{data.revenue | currency}}</td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>

                                    </div>
                                </div>
                            </div>


                            <div class="col-md-6">
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <h3 class="panel-title">{{'STATS_COMMODITIES_SORTED_BY_INVOICING' | translate}}</h3>
                                    </div>
                                    <div class="panel-body">

                                        <p class="no-data-message text-center text-info" ng-show="ranks.commodities && ranks.commodities.length===0">{{'NO_DATA' | translate}}</p>

                                        <div class="table-responsive" ng-show="ranks.commodities && ranks.commodities.length > 0" style="height: 500px; overflow: auto;">
                                            <table class="table table-hover">
                                                <thead>
                                                <tr>
                                                    <th>{{'DESCRIPTION' | translate}}</th>
                                                    <th>{{'TOTAL_INVOICING_BEFORE_TAXES' | translate}}</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr ng-repeat="data in ranks.commodities">
                                                    <td><a href="javascript:;" ng-click="openCommodityStats(data.id)">{{data.description}}</a></td>
                                                    <td>{{data.revenue | currency}}</td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>

                                    </div>
                                </div>
                            </div>

                        </div>

                    </div>
                </tab>





            </tabset>
        </div>
    </div>


</div>

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


