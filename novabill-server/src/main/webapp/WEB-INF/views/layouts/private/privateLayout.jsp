<%@page import="com.novadart.novabill.web.mvc.Urls"%>
<%@page import="com.novadart.novabill.domain.security.Principal"%>
<%@page import="com.novadart.novabill.domain.Business"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.novadart.novabill.web.mvc.PrivateController.PAGES"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>

<spring:url var="logoutUrl" value="/resources/logout" />

<spring:url var="privateAssetsUrl" value="/private_assets" />
<spring:url var="frontendAssetsUrl" value="/frontend_assets" />

<spring:url var="logoUrl" value="/private/businesses/logo/thumbnail" />

<spring:url var="dashboardUrl" value="/private/" />
<spring:url var="clientsUrl" value="/private/clients/" />
<spring:url var="invoicesUrl" value="/private/invoices/" />
<spring:url var="estimationsUrl" value="/private/estimations/" />
<spring:url var="transportDocumentsUrl" value="/private/transport-documents/" />
<spring:url var="creditNotesUrl" value="/private/credit-notes/" />
<spring:url var="itemsUrl" value="/private/items/" />
<spring:url var="paymentsUrl" value="/private/payments/" />
<spring:url var="settingsUrl" value="/private/settings/" />

<spring:url var="clientsBaseUrl" value="<%=Urls.PRIVATE_CLIENTS%>" />

<%
	PAGES activePage = (PAGES)request.getAttribute("activePage");
	Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	Business business = principal.getBusiness();
%>

<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!--> <html lang="en" class="no-js"> <!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
	<meta charset="utf-8" />
	<title>Novabill | Private Area</title>
	<meta content="width=device-width, initial-scale=1.0" name="viewport" />
	<meta content="" name="description" />
	<meta content="" name="author" />
	<meta name="gwt:property" content="locale=it_IT">
	<!-- BEGIN GLOBAL MANDATORY STYLES -->        
	<link href="${privateAssetsUrl}/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<link href="${privateAssetsUrl}/plugins/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet" type="text/css"/>
	<link href="${privateAssetsUrl}/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
	<link href="${privateAssetsUrl}/css/style-metro.css" rel="stylesheet" type="text/css"/>
	<link href="${privateAssetsUrl}/css/style.css" rel="stylesheet" type="text/css"/>
	<link href="${privateAssetsUrl}/css/style-responsive.css" rel="stylesheet" type="text/css"/>
	<link href="${privateAssetsUrl}/css/themes/default.css" rel="stylesheet" type="text/css" id="style_color"/>
	<link href="${privateAssetsUrl}/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css"/>
	<!-- END GLOBAL MANDATORY STYLES -->
	
	<link href="${privateAssetsUrl}/pages/css/global.css" rel="stylesheet" type="text/css"/>
	
	<tiles:insertAttribute ignore="true" name="head" />
	
	<link rel="shortcut icon" href="${frontendAssetsUrl}/img/favicon.png" />
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="page-header-fixed page-sidebar-fixed">
	<!-- BEGIN HEADER -->   
	<div class="header navbar navbar-inverse navbar-fixed-top">
		<!-- BEGIN TOP NAVIGATION BAR -->
		<div class="navbar-inner">
			<div class="container-fluid">
				<!-- BEGIN LOGO -->
				<a class="brand" href="${dashboardUrl}">
				<img src="${frontendAssetsUrl}/img/logo_thin.png" alt="logo" />
				</a>
				<!-- END LOGO -->
				<!-- BEGIN RESPONSIVE MENU TOGGLER -->
				<a href="javascript:;" class="btn-navbar collapsed" data-toggle="collapse" data-target=".nav-collapse">
				<img src="${privateAssetsUrl}/img/menu-toggler.png" alt="" />
				</a>          
				<!-- END RESPONSIVE MENU TOGGLER -->            
				<!-- BEGIN TOP NAVIGATION MENU -->              
				<ul class="nav pull-right">
					<!-- BEGIN USER LOGIN DROPDOWN -->
					<li class="dropdown user">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
						<img alt="" src="${logoUrl}" width="29px" height="29px" />
						<span class="username"><%=business.getName()%></span>
						<i class="icon-angle-down"></i>
						</a>
						<ul class="dropdown-menu">
							<li><a href="${settingsUrl}"><i class="icon-user"></i> My Profile</a></li>
							<li class="divider"></li>
							<li><a href="javascript:;" id="trigger_fullscreen"><i class="icon-move"></i> Full Screen</a></li>
							<li><a href="${logoutUrl}"><i class="icon-key"></i> Log Out</a></li>
						</ul>
					</li>
					<!-- END USER LOGIN DROPDOWN -->
					<!-- END USER LOGIN DROPDOWN -->
				</ul>
				<!-- END TOP NAVIGATION MENU --> 
			</div>
		</div>
		<!-- END TOP NAVIGATION BAR -->
	</div>
	<!-- END HEADER -->
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN SIDEBAR -->
		<div class="page-sidebar nav-collapse collapse">
			<!-- BEGIN SIDEBAR MENU -->        
			<ul class="page-sidebar-menu">
				<li>
					<!-- BEGIN SIDEBAR TOGGLER BUTTON -->
					<div class="sidebar-toggler hidden-phone"></div>
					<!-- BEGIN SIDEBAR TOGGLER BUTTON -->
				</li>
				<li>
					<div class="spacer" style="margin: 20px 0;"></div>
				</li>
				
				<li class="start <%=PAGES.DASHBOARD.equals(activePage) ? "active" : "" %>">
					<a href="${dashboardUrl}">
					<i class="icon-home"></i> 
					<span class="title">Dashboard</span>
					<% if(PAGES.DASHBOARD.equals(activePage)) { %>
					<span class="selected"></span>
					<%} %>
					</a>
				</li>
				
				<li class="<%=PAGES.CLIENTS.equals(activePage) ? "active" : "" %>">
					<a href="${clientsUrl}">
					<i class="icon-user"></i> 
					<span class="title">Clients</span>
					<% if(PAGES.CLIENTS.equals(activePage)) { %>
					<span class="selected"></span>
					<%} %>
					</a>
				</li>
				
				<li class="<%=(PAGES.INVOICES.equals(activePage) 
						|| PAGES.TRANSPORT_DOCUMENTS.equals(activePage)
						|| PAGES.CREDIT_NOTES.equals(activePage)
						|| PAGES.ESTIMATIONS.equals(activePage)) ? "active" : "" %>">
					<a href="javascript:;">
					<i class="icon-file-text"></i> 
					<span class="title">Documents</span>
					<%if(PAGES.INVOICES.equals(activePage) 
							|| PAGES.TRANSPORT_DOCUMENTS.equals(activePage)
							|| PAGES.CREDIT_NOTES.equals(activePage)
							|| PAGES.ESTIMATIONS.equals(activePage)) {%>
					<span class="selected"></span>
					<span class="arrow open"></span>
					<% } else { %>
					<span class="arrow"></span>
					<%} %>
					</a>
					<ul class="sub-menu">
						<li class="<%=PAGES.INVOICES.equals(activePage) ? "active" : "" %>">
							<a href="${invoicesUrl}">
							Invoices</a>
						</li>
						<li class="<%=PAGES.ESTIMATIONS.equals(activePage) ? "active" : "" %>">
							<a href="${estimationsUrl}">
							Estimations</a>
						</li>
						<li class="<%=PAGES.TRANSPORT_DOCUMENTS.equals(activePage) ? "active" : "" %>">
							<a href="${transportDocumentsUrl}">
							Transport Documents</a>
						</li>
						<li class="<%=PAGES.CREDIT_NOTES.equals(activePage) ? "active" : "" %>">
							<a href="${creditNotesUrl}">
							Credit Notes</a>
						</li>
					</ul>
				</li>
				
				<li class="<%=PAGES.ITEMS.equals(activePage) ? "active" : "" %>">
					<a href="${itemsUrl}">
					<i class="icon-th"></i> 
					<span class="title">Items</span>
					<% if(PAGES.ITEMS.equals(activePage)) { %>
					<span class="selected"></span>
					<%} %>
					</a>
				</li>
				
				<li class="<%=PAGES.PAYMENTS.equals(activePage) ? "active" : "" %>">
					<a href="${paymentsUrl}">
					<i class="icon-briefcase"></i> 
					<span class="title">Payments</span>
					<% if(PAGES.PAYMENTS.equals(activePage)) { %>
					<span class="selected"></span>
					<%} %>
					</a>
				</li>
				
				<li>
					<div class="spacer" style="margin: 40px 0;"></div>
				</li>
				
				<%-- <li class="last <%=PAGES.SETTINGS.equals(activePage) ? "active" : "" %>">
					<a href="${settingsUrl}">
					<i class="icon-cogs"></i> 
					<span class="title">Settings</span>
					<% if(PAGES.SETTINGS.equals(activePage)) { %>
					<span class="selected"></span>
					<%} %>
					</a>
				</li> --%>
			</ul>
			<!-- END SIDEBAR MENU -->
		</div>
		<!-- END SIDEBAR -->
		<!-- BEGIN PAGE -->

		<tiles:insertAttribute name="body" />

		<!-- END PAGE -->
	</div>
	<!-- END CONTAINER -->
	<!-- BEGIN FOOTER -->
	<div class="footer">
		<div class="footer-inner">
			2013 &copy; Novadart.
		</div>
		<div class="footer-tools">
			<span class="go-top">
			<i class="icon-angle-up"></i>
			</span>
		</div>
	</div>
	<!-- END FOOTER -->
	<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
	<!-- BEGIN CORE PLUGINS -->   
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
	<script>window.jQuery || document.write('<script src="${privateAssetsUrl}/plugins/jquery-1.10.1.min.js"><\/script>');</script>
	
	<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.0-rc.3/angular.min.js"></script>
	<script>window.angular || document.write('<script src="${privateAssetsUrl}/plugins/angular-1.2.0-rc.3.min.js"><\/script>');</script>
	<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.0-rc.3/angular-route.min.js"></script>
	<script src="${privateAssetsUrl}/plugins/angular-i18n/angular-locale_it-it.js" type="text/javascript"></script>
	<script src="${privateAssetsUrl}/plugins/angular-translate.min.js" type="text/javascript"></script>
	<script src="${privateAssetsUrl}/pages/scripts/translations.js" type="text/javascript"></script>
	
	<script src="${privateAssetsUrl}/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
	<!-- IMPORTANT! Load jquery-ui-1.10.1.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
	<script src="${privateAssetsUrl}/plugins/jquery-ui/jquery-ui-1.10.1.custom.min.js" type="text/javascript"></script>      
	<script src="${privateAssetsUrl}/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="${privateAssetsUrl}/plugins/bootstrap-hover-dropdown/twitter-bootstrap-hover-dropdown.min.js" type="text/javascript" ></script>
	<!--[if lt IE 9]>
	<script src="${privateAssetsUrl}/plugins/excanvas.min.js"></script>
	<script src="${privateAssetsUrl}/plugins/respond.min.js"></script>  
	<![endif]-->   
	<script src="${privateAssetsUrl}/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
	<script src="${privateAssetsUrl}/plugins/jquery.blockui.min.js" type="text/javascript"></script>  
	<script src="${privateAssetsUrl}/plugins/jquery.cookie.min.js" type="text/javascript"></script>
	<script src="${privateAssetsUrl}/plugins/uniform/jquery.uniform.min.js" type="text/javascript" ></script>
	<!-- END CORE PLUGINS -->
	
	<script>
	var NovabillConf = {
		    businessId : '<%=business.getId()%>',
		    
		    dashboardUrl : '${dashboardUrl}',
		    clientsBaseUrl : '${clientsBaseUrl}',
		    invoicesBaseUrl : '${invoicesUrl}',
		    estimationsBaseUrl : '${estimationsUrl}',
		    creditNotesBaseUrl : '${creditNotesUrl}',
		    transportDocumentsBaseUrl : '${transportDocumentsUrl}',
		    partialsBaseUrl : '${privateAssetsUrl}/pages/partials/'
	};
	</script>
	
	<tiles:insertAttribute ignore="true" name="javascript" />
	
</body>
<!-- END BODY -->
</html>