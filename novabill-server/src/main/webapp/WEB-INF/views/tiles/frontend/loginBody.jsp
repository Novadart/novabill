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
                    <h1>Login</h1>
                </div>
                <div class="col-md-8 col-sm-8">
                    <ul class="pull-right breadcrumb">
                        <li><a href="${homeUrl}">Home</a></li>
                        <li><a href="">Pages</a></li>
                        <li class="active">Login</li>
                    </ul>
                </div>
            </div>
        </div>
        <!-- END BREADCRUMBS -->

        <!-- BEGIN CONTAINER -->   
        <div class="container " style="margin-bottom: 150px;"> <!-- margin-bottom-40 -->
          <div class="row">
            <div class="col-md-4 col-md-offset-4 col-sm-6 col-sm-offset-3 login-signup-page">
                <form action="${loginUrl}" method="post" >           
                    
                    <h2>Login to your account</h2>

                    <div class="input-group margin-bottom-20">
                        <span class="input-group-addon"><i class="fa fa-envelope"></i></span>
                        <input type="text" class="form-control" name="j_username" placeholder="E-mail">
                    </div>                    
                    <div class="input-group margin-bottom-20">
                        <span class="input-group-addon"><i class="fa fa-lock"></i></span>
                        <input type="password" class="form-control" name="j_password" placeholder="Password">

                        <a href="#" class="login-signup-forgot-link">Forgot?</a>
                    </div>                    

                    <div class="row">
                        <div class="col-md-6 col-sm-6">
                            <div class="checkbox-list"><label class="checkbox"><input type="checkbox" name="_spring_security_remember_me"> Remember me</label></div>                        
                        </div>
                        <div class="col-md-6 col-sm-6">
                            <button type="submit" class="btn theme-btn pull-right">Login</button>                        
                        </div>
                    </div>

                    <hr>

                    <!-- <div class="login-socio">
                        <p class="text-muted">or login using:</p>
                        <ul class="social-icons">
                            <li><a class="facebook" data-original-title="facebook" href="#"></a></li>
                            <li><a class="twitter" data-original-title="Twitter" href="#"></a></li>
                            <li><a class="googleplus" data-original-title="Goole Plus" href="#"></a></li>
                            <li><a class="linkedin" data-original-title="Linkedin" href="#"></a></li>
                        </ul>
                    </div> -->
                </form>
            </div>
          </div>
        </div>
        
</div>