<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/" var="homeUrl" />

<div class="page-container">
  
        <!-- BEGIN BREADCRUMBS -->   
        <div class="row breadcrumbs">
            <div class="container">
                <div class="col-md-4 col-sm-4">
                    <h1>Oops!</h1>
                </div>
                <div class="col-md-8 col-sm-8">
                    <ul class="pull-right breadcrumb">
                        <li><a href="${homeUrl}">Home</a></li>
                        <li class="active">500</li>
                    </ul>
                </div>
            </div>
        </div>
        <!-- END BREADCRUMBS -->

        <!-- BEGIN CONTAINER -->   
        <div class="container margin-bottom-40">
          <div class="row">
            <div class="col-md-12 page-500">
                <div class="number">
                    500
                </div>
                <div class="details">
                    <h3>Oops! Qualcosa non ha funzionato a dovere.</h3>
                    <p>
                        Ci stiamo lavorando!<br>
                        Per favore riprova più tardi.<br><br>
                    </p>
                </div>
            </div>
          </div>
        </div>
        <!-- END CONTAINER -->

  </div>