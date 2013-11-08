<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<spring:url value="/private_assets" var="privateAssetsUrl" />
<spring:url value="/frontend_assets" var="frontendAssetsUrl" />
<spring:url value="/" var="homeUrl" />
    
    
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if !IE]><!--> <html lang="en"> <!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
	<meta charset="utf-8" />
	<title>Novabill | 404</title>
	<meta content="width=device-width, initial-scale=1.0" name="viewport" />
	<meta content="" name="description" />
	<meta content="" name="author" />
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
	<!-- BEGIN PAGE LEVEL STYLES -->
	<link href="${privateAssetsUrl}/css/pages/error.css" rel="stylesheet" type="text/css"/>
	<!-- END PAGE LEVEL STYLES -->
	<link rel="shortcut icon" href="${frontendAssetsUrl}/img/favicon.png" />
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="page-404-3">
	<tiles:insertAttribute name="analytics" />

	<div class="page-inner">
		<img src="${privateAssetsUrl}/img/pages/earth.jpg" alt="">
	</div>
	<div class="container error-404">
		<h1>404</h1>
		<h2>Houston, we have a problem.</h2>
		<p>
			Actually, the page you are looking for does not exist. 
		</p>
		<p>
			<a href="${homeUrl}">Return home</a>
			<br>
		</p>
	</div>
	<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
	<!-- BEGIN CORE PLUGINS -->   <script src="${privateAssetsUrl}/plugins/jquery-1.10.1.min.js" type="text/javascript"></script>
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
	<script src="${privateAssetsUrl}/scripts/app.js"></script>  
	<script>
		jQuery(document).ready(function() {    
		   App.init();
		});
	</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>