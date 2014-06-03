<%@page import="com.novadart.novabill.web.mvc.FrontendController.PAGES"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>

<spring:url var="indexPageUrl" value="/" />
<spring:url var="logoutUrl" value="/resources/logout" />
<spring:url var="aboutPageUrl" value="/about" />
<spring:url var="securityPageUrl" value="/security" />
<spring:url var="pricesPageUrl" value="/prices" />
<spring:url var="privatePageUrl" value="/private/" />
<spring:url var="registerPageUrl" value="/register" />
<spring:url var="frontendAssetsUrl" value="/frontend_assets" />

<%
	PAGES activePage = (PAGES)request.getAttribute("activePage");
%>

    <!-- BEGIN HEADER -->
    <div class="header navbar navbar-default navbar-static-top">
        <!-- BEGIN TOP BAR -->
        <div class="front-topbar">
            <div class="container">
                <div class="row">
                    <div class="col-md-9 col-sm-9">
                        <ul class="list-unstyle inline">
                            <!-- <li><i class="fa fa-phone topbar-info-icon top-2"></i>Call us: <span>(1) 456 6717</span></li>
                            <li class="sep"><span>|</span></li>
                            <li><i class="fa fa-envelope-o topbar-info-icon top-2"></i>Email: <span>test@test.it</span></li> -->
                        </ul>
                    </div>
                    <div class="col-md-3 col-sm-3 login-reg-links">
                        <sec:authorize access="isAnonymous()">
                            <ul class="list-unstyled inline">
	                            <li><a href="${privatePageUrl}">Entra</a></li>
	                            <li class="sep"><span>|</span></li>
	                            <li><a href="${registerPageUrl}" class="btn btn-sm green">Registrati</a></li>
	                        </ul>
                        </sec:authorize>
                        <sec:authorize access="isAuthenticated()">
						    <ul class="list-unstyled inline">
                                <li><a href="${privatePageUrl}" class="btn btn-sm green"><i class="fa fa-file"></i> Accedi ai tuoi Documenti</a></li>
                                <li class="sep"><span>|</span></li>
                                <li>
                                	<form action="${logoutUrl}" method="post">
										<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
										<input class="btn btn-sm default" type="submit" value="Esci">
									</form>
                                </li>
                            </ul>
						</sec:authorize>
                    </div>
                </div>
            </div>        
        </div>
        <!-- END TOP BAR -->
        <div class="container">
            <div class="navbar-header">
                <!-- BEGIN RESPONSIVE MENU TOGGLER -->
                <button class="navbar-toggle btn navbar-btn" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <!-- END RESPONSIVE MENU TOGGLER -->
                <!-- BEGIN LOGO (you can use logo image instead of text)-->
                <a class="brand logo-v1" href="${indexPageUrl}">
                    <img src="${frontendAssetsUrl}/img/logo_thin.png" id="logoimg" alt="">
                    <img src="${frontendAssetsUrl}/img/beta-small.png" style="position: relative; top: 10px; right: 2px;" title="beta" alt="">
                </a>
                <!-- END LOGO -->
            </div>
        
            <!-- BEGIN TOP NAVIGATION MENU -->
            <div class="navbar-collapse collapse">
                <ul class="nav navbar-nav">
                    <li class="<%=PAGES.HOME.equals(activePage) ? "active" : "" %>"><a href="${indexPageUrl}">Home</a></li>
                    <li class="<%=PAGES.PRICES.equals(activePage) ? "active" : "" %>"><a href="${pricesPageUrl}">Quanto costa?</a></li>
                    <li class="<%=PAGES.SECURITY.equals(activePage) ? "active" : "" %>"><a href="${securityPageUrl}">Sicurezza</a></li>
                    <li class="<%=PAGES.ABOUT.equals(activePage) ? "active" : "" %>"><a href="${aboutPageUrl}">Chi Siamo</a></li>
                </ul>                         
            </div>
            <!-- BEGIN TOP NAVIGATION MENU -->
        </div>
    </div>
    <!-- END HEADER -->
