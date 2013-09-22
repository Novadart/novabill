<%@page import="com.novadart.novabill.web.mvc.FrontendController.PAGES"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>

<spring:url var="indexPageUrl" value="/" />
<spring:url var="aboutPageUrl" value="/about" />
<spring:url var="contactPageUrl" value="/contact" />
<spring:url var="pricesPageUrl" value="/prices" />
<spring:url var="loginPageUrl" value="/login" />
<spring:url value="/frontend_assets" var="frontendAssetsUrl" />

<%
	PAGES activePage = (PAGES)request.getAttribute("activePage");
%>

<!-- BEGIN HEADER -->
<div class="front-header">
    <div class="container">
        <div class="navbar">
            <div class="navbar-inner">
                <!-- BEGIN LOGO (you can use logo image instead of text)-->
                <a class="brand logo-v1" href="${indexPageUrl}">
                    <img src="${frontendAssetsUrl}/img/logo_thin.png" id="logoimg" alt="">
                </a>
                <!-- END LOGO -->

                <!-- BEGIN RESPONSIVE MENU TOGGLER -->
                <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </a>
                <!-- END RESPONSIVE MENU TOGGLER -->

                <!-- BEGIN TOP NAVIGATION MENU -->
                <div class="nav-collapse collapse">
                    <ul class="nav">
                        <li class="<%=PAGES.HOME.equals(activePage) ? "active" : "" %>"><a href="${indexPageUrl}">Home</a></li>
                        <li class="<%=PAGES.ABOUT.equals(activePage) ? "active" : "" %>"><a href="${aboutPageUrl}">About Us</a></li>
                        <li class="<%=PAGES.PRICES.equals(activePage) ? "active" : "" %>"><a href="${pricesPageUrl}">Prices</a></li>
<!--                         <li class="dropdown">
                            <a class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-delay="0" data-close-others="false" href="#">
                                Blog
                                <i class="icon-angle-down"></i>
                            </a>
                            <ul class="dropdown-menu">
                                <li><a href="blog.html">Blog Page</a></li>
                                <li><a href="blog_item.html">Blog Item</a></li>
                            </ul>
                        </li> -->
                        <li class="<%=PAGES.CONTACT.equals(activePage) ? "active" : "" %>"><a href="${contactPageUrl}">Contact</a></li>
                        <li><a href="${loginPageUrl}">Login / Register</a></li>
                    </ul>
                </div>
                <!-- BEGIN TOP NAVIGATION MENU -->
            </div>
        </div>
    </div>       
</div> 
<!-- END HEADER -->
