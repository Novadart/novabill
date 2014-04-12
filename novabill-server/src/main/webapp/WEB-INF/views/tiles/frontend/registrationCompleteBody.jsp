<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="frontendAssetsUrl" value="/frontend_assets" />
<spring:url var="loginUrl" value="/resources/login_check" />
<spring:url var="homeUrl" value="/" />

<div class="page-container">
  
        <!-- BEGIN BREADCRUMBS -->   
        <div class="row breadcrumbs margin-bottom-40">
            <div class="container">
                <div class="col-md-4 col-sm-4">
                    <h1>Registrazione Completata</h1>
                </div>
                <div class="col-md-8 col-sm-8">
                    <ul class="pull-right breadcrumb">
                        <li><a href="${homeUrl}">Home</a></li>
                        <li class="active">Registrazione Completata</li>
                    </ul>
                </div>
            </div>
        </div>
        <!-- END BREADCRUMBS -->

        <!-- BEGIN CONTAINER -->   
        <div class="container " style="margin-bottom: 150px; margin-top: 150px;"> <!-- margin-bottom-40 -->
          <div class="row">
            <div class="col-md-6 col-md-offset-3 col-sm-6 col-sm-offset-3 alert alert-info">
                Controlla la tua casella email per completare la registrazione e attivare l'account. Grazie.
            </div>
          </div>
        </div>
        
</div>