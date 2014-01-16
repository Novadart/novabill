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
                    <h1>Registrazione</h1>
                </div>
                <div class="col-md-8 col-sm-8">
                    <ul class="pull-right breadcrumb">
                        <li><a href="${homeUrl}">Home</a></li>
                        <li><a href="">Pages</a></li>
                        <li class="active">Registrazione</li>
                    </ul>
                </div>
            </div>
        </div>
        <!-- END BREADCRUMBS -->

        <!-- BEGIN CONTAINER -->   
        <div class="container " style="margin-bottom: 150px;"> <!-- margin-bottom-40 -->
          <div class="row">
            <div class="col-md-8 col-md-offset-2 col-sm-6 col-sm-offset-3">
               <div class="alert alert-info">
                    <h2>La registrazione è temporaneamente disabilitata.</h2>
                    Riattiveremo la registrazione di nuovi utenti non appena avremo completato la migrazione ai nuovi sistemi.<br/>
                    <b>Grazie</b>, a presto!
               </div>
            </div>
          </div>
        </div>
        
</div>