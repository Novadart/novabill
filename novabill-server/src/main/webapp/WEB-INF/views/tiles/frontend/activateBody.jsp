<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="frontendAssetsUrl" value="/frontend_assets" />
<spring:url var="activateUrl" value="/activate" />
<spring:url var="homeUrl" value="/" />

<%
boolean wrongPwd = request.getAttribute("wrongPassword") != null;
%>

<div class="page-container">
  
        <!-- BEGIN BREADCRUMBS -->   
        <div class="row breadcrumbs margin-bottom-40">
            <div class="container">
                <div class="col-md-4 col-sm-4">
                    <h1>Attivazione Account</h1>
                </div>
                <div class="col-md-8 col-sm-8">
                    <ul class="pull-right breadcrumb">
                        <li><a href="${homeUrl}">Home</a></li>
                        <li class="active">Attivazione Account</li>
                    </ul>
                </div>
            </div>
        </div>
        <!-- END BREADCRUMBS -->

        <!-- BEGIN CONTAINER -->   
        <div class="container " style="margin-bottom: 150px;"> <!-- margin-bottom-40 -->
          <div class="row">
            <div class="col-md-4 col-md-offset-4 col-sm-6 col-sm-offset-3 login-signup-page">
                <form action="${activateUrl}" method="post" >           
                    
                    <h2>Accedi per attivare l'account</h2>

                    <div class="input-group margin-bottom-20">
                        <span class="input-group-addon"><i class="fa fa-envelope"></i></span>
                        <input type="text" class="form-control" name="j_username" placeholder="E-mail">
                    </div>                    
                    <div class="input-group">
                        <span class="input-group-addon"><i class="fa fa-lock"></i></span>
                        <input type="password" class="form-control" name="j_password" placeholder="Password">
                    </div>
                    <span class="text-danger" style="display: <%=wrongPwd ? "inline" : "none"%>">Password non corretta</span>

                    <div class="row margin-top-20">
                        <div class="col-md-12 text-center">
                            <button type="submit" class="btn theme-btn">Invia</button>                        
                        </div>
                    </div>
                </form>
            </div>
          </div>
        </div>
        
</div>