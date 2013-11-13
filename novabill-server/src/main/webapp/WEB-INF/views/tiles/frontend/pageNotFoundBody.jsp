<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/" var="homeUrl" />

    <div class="page-container">
  
        <!-- BEGIN BREADCRUMBS -->   
        <div class="row breadcrumbs">
            <div class="container">
                <div class="col-md-4 col-sm-4">
                    <h1>Page Not Found</h1>
                </div>
                <div class="col-md-8 col-sm-8">
                    <ul class="pull-right breadcrumb">
                        <li><a href="index.html">Home</a></li>
                        <li><a href="">Pages</a></li>
                        <li class="active">404</li>
                    </ul>
                </div>
            </div>
        </div>
        <!-- END BREADCRUMBS -->

        <!-- BEGIN CONTAINER -->   
        <div class="container margin-bottom-40">
          <div class="row">
            <div class="col-md-12 page-404">
               <div class="number">
                  404
               </div>
               <div class="details">
                  <h3>Oops!  You're lost.</h3>
                  <p>
                     We can not find the page you're looking for.<br>
                     <a href="${homeUrl}">Return home</a> 
                  </p>
               </div>
            </div>
          </div>
        </div>
        <!-- END CONTAINER -->

  </div>