<%@page import="com.novadart.novabill.shared.client.data.PriceListConstants"%>
<%@page import="com.novadart.novabill.web.mvc.Urls"%>
<%@page import="com.novadart.novabill.domain.security.Principal"%>
<%@page import="com.novadart.novabill.domain.Business"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.novadart.novabill.web.mvc.PrivateController.PAGES"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<spring:url var="logoutUrl" value="/resources/logout" />

<spring:url var="privateAssetsUrl" value="/private_assets" />
<spring:url var="frontendAssetsUrl" value="/frontend_assets" />

<spring:url var="logoUrl" value="/private/businesses/logo/thumbnail" />

<spring:url var="basePath" value="/" />

<spring:url var="dashboardUrl" value="/private/" />
<spring:url var="clientsUrl" value="/private/clients/" />
<spring:url var="invoicesUrl" value="/private/invoices/" />
<spring:url var="estimationsUrl" value="/private/estimations/" />
<spring:url var="transportDocumentsUrl" value="/private/transport-documents/" />
<spring:url var="creditNotesUrl" value="/private/credit-notes/" />
<spring:url var="commoditiesUrl" value="/private/commodities/" />
<spring:url var="priceListsUrl" value="/private/price-lists/" />
<spring:url var="paymentsUrl" value="/private/payments/" />
<spring:url var="settingsUrl" value="/private/settings/" />

<spring:url var="clientsBaseUrl" value="<%=Urls.PRIVATE_CLIENTS%>" />

 <spring:url var="gwtUrl" value="/rpc/rpc.nocache.js" />

<%
	PAGES activePage = (PAGES)request.getAttribute("activePage");
	Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	Business business = principal.getBusiness();
%>

<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<meta charset="utf-8" />
<title>Novabill | Area Privata</title>
<meta name="gwt:property" content="locale=it_IT">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />
<meta name="MobileOptimized" content="320">
<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="${privateAssetsUrl}/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
<link href="${privateAssetsUrl}/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="${privateAssetsUrl}/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css" />
<!-- END GLOBAL MANDATORY STYLES -->

<!-- BEGIN THEME STYLES -->
<link href="${privateAssetsUrl}/css/style-metronic.css" rel="stylesheet" type="text/css" />
<link href="${privateAssetsUrl}/css/style.css" rel="stylesheet" type="text/css" />
<link href="${privateAssetsUrl}/css/style-responsive.css" rel="stylesheet" type="text/css" />
<link href="${privateAssetsUrl}/css/plugins.css" rel="stylesheet" type="text/css" />
<link href="${privateAssetsUrl}/css/pages/tasks.css" rel="stylesheet" type="text/css" />
<link href="${privateAssetsUrl}/css/themes/default.css" rel="stylesheet" type="text/css" id="style_color" />
<link href="${privateAssetsUrl}/css/custom.css" rel="stylesheet" type="text/css" />
<!-- END THEME STYLES -->

<tiles:insertAttribute name="css" />

<link rel="shortcut icon" href="${frontendAssetsUrl}/img/favicon.png" />
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="page-header-fixed page-sidebar-fixed">
    
    <tiles:insertAttribute name="analytics" />

	<!-- BEGIN HEADER -->
	<div class="header navbar navbar-inverse navbar-fixed-top">
		<!-- BEGIN TOP NAVIGATION BAR -->
		<div class="header-inner">
			<!-- BEGIN LOGO -->
			<a class="navbar-brand" href="${dashboardUrl}"> 
		      <img src="${frontendAssetsUrl}/img/logo_thin_white.png" alt="logo" class="img-responsive" />
				<img src="${privateAssetsUrl}/img/beta-small-w.png" alt="" style="position: relative; bottom: 10px; right: 49px; float: right;" />
			</a>
			<!-- END LOGO -->
			<!-- BEGIN RESPONSIVE MENU TOGGLER -->
			<a href="javascript:;" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse"> <img
				src="${privateAssetsUrl}/img/menu-toggler.png" alt="" />
			</a>
			<!-- END RESPONSIVE MENU TOGGLER -->
			<!-- BEGIN TOP NAVIGATION MENU -->
			<ul class="nav navbar-nav pull-right">
				<!-- BEGIN USER LOGIN DROPDOWN -->
				<li class="dropdown user"><a href="#" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown"
					data-close-others="true"> <span class="btn btn-sm dark"><%=business.getName()%></span> <i
						class="fa fa-angle-down"></i>
				</a>
					<ul class="dropdown-menu">
						<li><a href="${settingsUrl}"><i class="fa fa-user"></i> Impostazioni</a></li>
						<li class="divider"></li>
						<li><a href="javascript:;" id="trigger_fullscreen"><i class="fa fa-move"></i> Schermo Intero</a></li>
						<li><a href="${logoutUrl}"><i class="fa fa-key"></i> Esci</a></li>
					</ul></li>
				<!-- END USER LOGIN DROPDOWN -->
			</ul>
			<!-- END TOP NAVIGATION MENU -->
		</div>
		<!-- END TOP NAVIGATION BAR -->
	</div>
	<!-- END HEADER -->
	<div class="clearfix"></div>
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN SIDEBAR -->
		<div class="page-sidebar navbar-collapse collapse">
			<!-- BEGIN SIDEBAR MENU -->
			<ul class="page-sidebar-menu">
				<li>
					<!-- BEGIN SIDEBAR TOGGLER BUTTON -->
					<div class="sidebar-toggler hidden-phone"></div> <!-- BEGIN SIDEBAR TOGGLER BUTTON -->
				</li>

				<li>
					<div class="spacer" style="margin: 20px 0;"></div>
				</li>

				<li class="start <%=PAGES.DASHBOARD.equals(activePage) ? "active" : "" %>"><a href="${dashboardUrl}"> <i
						class="fa fa-dashboard"></i> <span class="title">Dashboard</span> <% if(PAGES.DASHBOARD.equals(activePage)) { %> <span
						class="selected"></span> <%} %>
				</a></li>

				<li>
					<div class="spacer" style="margin: 20px 0;"></div>
				</li>

				<li class="<%=PAGES.CLIENTS.equals(activePage) ? "active" : "" %>"><a href="${clientsUrl}"> <i
						class="fa fa-user"></i> <span class="title">Clienti</span> <% if(PAGES.CLIENTS.equals(activePage)) { %> <span
						class="selected"></span> <%} %>
				</a></li>

				<li>
					<div class="spacer" style="margin: 20px 0;"></div>
				</li>

				<li class="<%=PAGES.INVOICES.equals(activePage) ? "active" : "" %>"><a href="${invoicesUrl}"> <i
						class="fa fa-file"></i> <span class="title">Fatture</span> <% if(PAGES.INVOICES.equals(activePage)) { %> <span
						class="selected"></span> <%} %>
				</a></li>

				<li class="<%=PAGES.TRANSPORT_DOCUMENTS.equals(activePage) ? "active" : "" %>"><a
					href="${transportDocumentsUrl}"> <i class="fa fa-file"></i> <span class="title">Documenti di Trasporto</span> <% if(PAGES.TRANSPORT_DOCUMENTS.equals(activePage)) { %>
						<span class="selected"></span> <%} %>
				</a></li>

				<li class="<%=PAGES.ESTIMATIONS.equals(activePage) ? "active" : "" %>"><a href="${estimationsUrl}"> <i
						class="fa fa-file"></i> <span class="title">Offerte</span> <% if(PAGES.ESTIMATIONS.equals(activePage)) { %> <span
						class="selected"></span> <%} %>
				</a></li>

				<li class="<%=PAGES.CREDIT_NOTES.equals(activePage) ? "active" : "" %>"><a href="${creditNotesUrl}"> <i
						class="fa fa-file"></i> <span class="title">Note di Credito</span> <% if(PAGES.CREDIT_NOTES.equals(activePage)) { %> <span
						class="selected"></span> <%} %>
				</a></li>

				<li>
					<div class="spacer" style="margin: 20px 0;"></div>
				</li>

				<li class="<%=PAGES.COMMODITIES.equals(activePage) ? "active" : ""%>"><a href="${commoditiesUrl}"> <i
						class="fa fa-th"></i> <span class="title">Articoli</span> <%
                        if(PAGES.COMMODITIES.equals(activePage)) {
                    %> <span class="selected"></span> <%} %>
				</a></li>

				<li class="<%=PAGES.PRICE_LISTS.equals(activePage) ? "active" : ""%>"><a href="${priceListsUrl}"> <i
						class="fa fa-dollar"></i> <span class="title">Listini</span> <%
                        if(PAGES.PRICE_LISTS.equals(activePage)) {
                    %> <span class="selected"></span> <%} %>
				</a></li>

				<li>
					<div class="spacer" style="margin: 20px 0;"></div>
				</li>

				<li class="<%=PAGES.PAYMENTS.equals(activePage) ? "active last" : "last" %>"><a href="${paymentsUrl}"> <i
						class="fa fa-briefcase"></i> <span class="title">Tipologie di Pagamento</span> <% if(PAGES.PAYMENTS.equals(activePage)) { %> <span
						class="selected"></span> <%} %>
				</a></li>

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
		<div class="footer-inner">2013 &copy; Novadart.</div>
		<div class="footer-tools">
			<span class="go-top"> <i class="fa fa-angle-up"></i>
			</span>
		</div>
	</div>
	<!-- END FOOTER -->
	<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
	
	   <script>
    var NovabillConf = {
            businessId : '<%=business.getId()%>',
            defaultPriceListName : '<%=PriceListConstants.DEFAULT%>',
            basePath : '${basePath}',
            version : '<tiles:insertAttribute name="novabill.version" />'
    };
    </script>
    
    <!-- BEGIN CORE PLUGINS -->
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
	<script>window.jQuery || document.write('<script src="${privateAssetsUrl}/plugins/jquery-1.10.2.min.js"><\/script>');</script>
	
	
	<!--[if lt IE 9]>
	<script src="${privateAssetsUrl}/plugins/respond.min.js"></script>
	<script src="${privateAssetsUrl}/plugins/excanvas.min.js"></script> 
	<![endif]-->
	<script src="${privateAssetsUrl}/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
	<!-- IMPORTANT! Load jquery-ui-1.10.3.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
	<script src="${privateAssetsUrl}/plugins/jquery-ui/jquery-ui-1.10.3.custom.min.js" type="text/javascript"></script>
	<script src="${privateAssetsUrl}/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="${privateAssetsUrl}/plugins/bootstrap-hover-dropdown/twitter-bootstrap-hover-dropdown.min.js"
	    type="text/javascript"></script>
	<script src="${privateAssetsUrl}/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
	<script src="${privateAssetsUrl}/plugins/jquery.blockui.min.js" type="text/javascript"></script>
	<script src="${privateAssetsUrl}/plugins/jquery.cookie.min.js" type="text/javascript"></script>
	<script src="${privateAssetsUrl}/plugins/uniform/jquery.uniform.min.js" type="text/javascript"></script>
	<!-- END CORE PLUGINS -->
	
	<script src="${gwtUrl}" type="text/javascript"></script>
	<script src="${privateAssetsUrl}/scripts/app.js" type="text/javascript"></script>
	
	<tiles:insertAttribute name="javascript" />
	
	
	<script type="text/javascript">
	$(App.init);
	</script>
	
	<tiles:insertAttribute name="javascriptExtra" ignore="true" />
	
</body>
<!-- END BODY -->
</html>