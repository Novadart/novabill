<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<spring:url var="privateAssetsUrl" value="/private_assets" />

<div class="page-content">

    <div class="container-fluid" ng-app="novabill.paypal" ng-controller="PayPalCtrl">
        <div class="row">
            <br><br>
            <div class="col-md-6 col-md-offset-3 text-center alert alert-info">
                Siamo in attesa che PayPal completi la transazione.<br>
                Per favore attendi qualche istante...<br><br>
                <img alt="loader" src="${privateAssetsUrl}/img/ajax-loading.gif">               
            </div>
        </div>           
    </div>
</div>
