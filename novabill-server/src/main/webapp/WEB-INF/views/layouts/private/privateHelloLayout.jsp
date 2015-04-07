<%@page import="com.novadart.novabill.domain.Business"%>
<%@page import="com.novadart.novabill.domain.security.Principal"%>
<%@page import="com.novadart.novabill.domain.security.RoleType"%>
<%@page import="com.novadart.novabill.shared.client.data.PriceListConstants"%>
<%@page import="com.novadart.novabill.web.mvc.PrivateController.PAGES"%>
<%@page import="com.novadart.novabill.web.mvc.Urls"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>

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
<spring:url var="clientUiErrorUrl" value="/private/ajax/clientuierror" />

<spring:url var="premiumUrl" value="/private/premium" />

<spring:url var="clientsBaseUrl" value="<%=Urls.PRIVATE_CLIENTS%>" />

<%
	PAGES activePage = (PAGES)request.getAttribute("activePage");
	Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	Business business = principal.getBusiness();
%>

<compress:html enabled="${mvn.tiles.minify.html}" compressJavaScript="${mvn.tiles.minify.html}" compressCss="${mvn.tiles.minify.html}"> 

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
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>

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

<!-- Load jQuery -->
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script>window.jQuery || document.write('<script src="${privateAssetsUrl}/plugins/jquery-1.10.2.min.js"><\/script>');</script>

<script type="text/javascript">
window.onerror = function(message, source, line, column) {
	var escape = function(x) { return x.replace('\\', '\\\\').replace('\"', '\\"'); };
    var XHR = window.XMLHttpRequest || function() {
        try { return new ActiveXObject("Msxml3.XMLHTTP"); } catch (e0) {}
        try { return new ActiveXObject("Msxml2.XMLHTTP.6.0"); } catch (e1) {}
        try { return new ActiveXObject("Msxml2.XMLHTTP.3.0"); } catch (e2) {}
        try { return new ActiveXObject("Msxml2.XMLHTTP"); } catch (e3) {}
        try { return new ActiveXObject("Microsoft.XMLHTTP"); } catch (e4) {}
    };
    var xhr = new XHR();
    xhr.open('POST', '${clientUiErrorUrl}', true);
    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.setRequestHeader('${_csrf.headerName}', '${_csrf.token}');
    xhr.send('{ ' + 
        '"message": "' + escape(message || '') + '",' + 
        '"source": "' + escape(source || '') + '",' + 
        '"url": "' + escape(window.location.href) + '",' + 
        '"line": "' + (line || 0) + '",' + 
        '"column": "' + (column || 0) + '"' + 
    '}');
};

</script>
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
			</a>
			<!-- END LOGO -->
			<!-- BEGIN RESPONSIVE MENU TOGGLER -->
			<a href="javascript:;" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse"> <img
				src="${privateAssetsUrl}/img/menu-toggler.png" alt="" />
			</a>
			<!-- END RESPONSIVE MENU TOGGLER -->
		</div>
		<!-- END TOP NAVIGATION BAR -->
	</div>
	<!-- END HEADER -->
	<div class="clearfix"></div>
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		
		<!-- BEGIN PAGE -->

            
		<div class="page-content" style="margin-left: 0;" ng-app="novabill.hello" ng-controller="HelloCtrl">
		    
		</div>
		
		
		<!-- END PAGE -->
	</div>
	<!-- END CONTAINER -->
	<!-- BEGIN FOOTER -->
	<div class="footer" style="margin-left: 0;">
		<div class="footer-inner">2014 &copy; Novadart.</div>
		<div class="footer-tools">
			<span class="go-top"> <i class="fa fa-angle-up"></i>
			</span>
		</div>
	</div>
	<!-- END FOOTER -->
	<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
	
	<script>
    var NovabillConf = {
            businessId : '<%=business != null ? business.getId() : -1%>',
         	businessName : '<%=business != null ? business.getName() : ""%>',
            premium : <%=principal.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_PREMIUM)%>,
            defaultPriceListName : '<%=PriceListConstants.DEFAULT%>',
            basePath : '${basePath}',
            version : '<tiles:insertAttribute name="novabill.version" />'
    };
    </script>
    
    <!-- BEGIN CORE PLUGINS -->
	
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
	
	<script src="${privateAssetsUrl}/scripts/app.js" type="text/javascript"></script>
	
	<tiles:insertAttribute name="javascript" />
	
    <script>
    // Include the UserVoice JavaScript SDK (only needed once on a page)
    UserVoice=window.UserVoice||[];(function(){var uv=document.createElement('script');uv.type='text/javascript';uv.async=true;uv.src='//widget.uservoice.com/qijZFrEigj9IF6UL4zLtNw.js';var s=document.getElementsByTagName('script')[0];s.parentNode.insertBefore(uv,s)})();
    
    //
    // UserVoice Javascript SDK developer documentation:
    // https://www.uservoice.com/o/javascript-sdk
    //
    
    // Set colors
    UserVoice.push(['set', {
      accent_color: '#448dd6',
      trigger_color: 'white',
      trigger_background_color: 'rgba(46, 49, 51, 0.6)'
    }]);
    
    // Identify the user and pass traits
    // To enable, replace sample data with actual user traits and uncomment the line
    UserVoice.push(['identify', {
      email:      '<%=principal.getUsername()%>', // User’s email address
      //name:       'John Doe', // User’s real name
      //created_at: 1364406966, // Unix timestamp for the date the user signed up
      //id:         123, // Optional: Unique id of the user (if set, this should not change)
      //type:       'Owner', // Optional: segment your users by type
      //account: {
      //  id:           123, // Optional: associate multiple users with a single account
      //  name:         'Acme, Co.', // Account name
      //  created_at:   1364406966, // Unix timestamp for the date the account was created
      //  monthly_rate: 9.99, // Decimal; monthly rate of the account
      //  ltv:          1495.00, // Decimal; lifetime value of the account
      //  plan:         'Enhanced' // Plan name for the account
      //}
    }]);
    
    // Add default trigger to the bottom-right corner of the window:
    UserVoice.push(['addTrigger', { mode: 'contact', trigger_position: 'bottom-left' }]);
    
    // Or, use your own custom trigger:
    //UserVoice.push(['addTrigger', '#id', { mode: 'contact' }]);
    
    // Autoprompt for Satisfaction and SmartVote (only displayed under certain conditions)
    UserVoice.push(['autoprompt', {}]);
    </script>
	
</body>
<!-- END BODY -->
</html>

</compress:html>