<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>

<h1>
    <%=request.getAttribute("businessName")%>
</h1>

<div class="container share-body" ng-app="novabill-frontend.share" ng-controller="ShareCtrl">

    <div class="row">
		<!--/span-->
		<div class="col-md-6">
		   <div class="form-group">
		        <label class="control-label col-md-4">{{'PAYMENTS_STATUS_START_DATE' | translate}}</label>
		        <div class="col-md-8">
		           <p class="input-group input-medium">
		             <input ng-disabled="loading" type="text" class="form-control"
		                readonly="readonly" 
		                datepicker-popup="dd MMMM yyyy" 
		                ng-model="startDate" 
		                is-open="openSD" 
		                datepicker-options="dateOptions"
		                show-button-bar="false" />
		             <span class="input-group-btn">
		               <button ng-disabled="loading" class="btn btn-default" ng-click="openStartDate($event)">
		                    <i class="glyphicon glyphicon-calendar"></i>
		               </button>
		             </span>
		           </p>
		        </div>
		    </div>
		
		    <div class="form-group">
		        <label class="control-label col-md-4">{{'PAYMENTS_STATUS_END_DATE' | translate}}</label>
		        <div class="col-md-8">
		           <p class="input-group input-medium">
		             <input ng-disabled="loading" type="text" class="form-control"
		                readonly="readonly" 
		                datepicker-popup="dd MMMM yyyy" 
		                ng-model="endDate" 
		                is-open="openED" 
		                datepicker-options="dateOptions"
		                show-button-bar="false" />
		             <span class="input-group-btn">
		               <button ng-disabled="loading" class="btn btn-default" ng-click="openEndDate($event)">
		                    <i class="glyphicon glyphicon-calendar"></i>
		               </button>
		             </span>
		           </p>
		        </div>
		    </div>
		</div>
		<div class="col-md-5 col-md-offset-1">
		   <a  ng-disabled="loading" href="javascript:void(0);" class="btn red col-md-5" ng-click="clear()">
		       {{'CLEAR' | translate}} <i class="fa fa-eraser"></i>
		   </a>
		   <a  ng-show="invoices && invoices.length > 0" href="javascript:void(0);" class="btn default col-md-5 col-md-offset-1" ng-click="print()">
		       {{'PAYMENTS_STATUS_PRINT' | translate}} <i class="fa fa-print"></i>
		   </a>
		</div>
		<!--/span-->
    </div>
		
		<div class="row results">
		    <div class="col-md-12" infinite-scroll="loadMoreInvoices()">
		        <img class="spinner" ng-show="loading" src="frontend_assets/img/ajax-loading.gif">
		        <p class="no-data-message text-center text-info" ng-show="invoices && invoices.length===0">{{'NO_DATA' | translate}}</p>
		        <n-share-invoice invoice="inv" ng-repeat="inv in invoices"></n-share-invoice>
		    </div>
		</div>


</div>


