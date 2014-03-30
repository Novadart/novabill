<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/frontend_assets" var="frontendAssetsUrl" />
<spring:url value="/" var="basePath" />

<%
    String pageName = (String)request.getAttribute("pageName");
%>

<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if !IE]><!--> <html lang="en"> <!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
    <meta charset="utf-8" />
    <title>NOVABILL | <%=pageName%></title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
    <meta content="" name="description" />
    <meta content="" name="author" />

   <!-- BEGIN GLOBAL MANDATORY STYLES -->          
   <link href="${frontendAssetsUrl}/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
   <link href="${frontendAssetsUrl}/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
   <!-- END GLOBAL MANDATORY STYLES -->
   
   <!-- BEGIN THEME STYLES --> 
   <link href="${frontendAssetsUrl}/css/style-metronic.css" rel="stylesheet" type="text/css"/>
   <link href="${frontendAssetsUrl}/css/style.css" rel="stylesheet" type="text/css"/>
   <link href="${frontendAssetsUrl}/css/themes/blue.css" rel="stylesheet" type="text/css" id="style_color"/>
   <link href="${frontendAssetsUrl}/css/style-responsive.css" rel="stylesheet" type="text/css"/>
   <link href="${frontendAssetsUrl}/css/custom.css" rel="stylesheet" type="text/css"/>
   <!-- END THEME STYLES -->
   
   <tiles:insertAttribute ignore="true" name="head" />
   
   <tiles:insertAttribute  ignore="true" name="css" />
   
   <tiles:insertAttribute  ignore="true" name="cssExtra" />

   <link rel="shortcut icon" href="${frontendAssetsUrl}/img/favicon.png" />
</head>
<!-- END HEAD -->

<!-- BEGIN BODY -->
<body>
    <tiles:insertAttribute name="analytics" />

    <tiles:insertAttribute name="header" />

    <!-- BEGIN PAGE CONTAINER -->  
    
    <tiles:insertAttribute name="body" />
    
    <!-- END PAGE CONTAINER -->  

    <tiles:insertAttribute name="footer" />

    <!-- Load javascripts at bottom, this will reduce page load time -->
    <!-- BEGIN CORE PLUGINS(REQUIRED FOR ALL PAGES) -->
    <!--[if lt IE 9]>
    <script src="${frontendAssetsUrl}/plugins/respond.min.js"></script>  
    <![endif]-->  
    <script src="${frontendAssetsUrl}/plugins/jquery-1.10.2.min.js" type="text/javascript"></script>
    <script src="${frontendAssetsUrl}/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
    <script src="${frontendAssetsUrl}/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>      
    <script type="text/javascript" src="${frontendAssetsUrl}/plugins/hover-dropdown.js"></script>
    <script type="text/javascript" src="${frontendAssetsUrl}/plugins/back-to-top.js"></script>    
    <script src="${frontendAssetsUrl}/plugins/uniform/jquery.uniform.min.js" type="text/javascript" ></script>
    <!-- END CORE PLUGINS -->

    <script>
	var NovabillFrontendConf = {
	        basePath : '${basePath}',
	        version : '<tiles:insertAttribute name="novabill.version" />'
	};
	</script>

    <tiles:insertAttribute  ignore="true" name="javascript" />
    
    <tiles:insertAttribute  ignore="true" name="javascriptExtra" />
    
    <script type="text/javascript">
    if(window.ga){
        ga('send', 'pageview');
    }
    </script>
    
    
    
</body>
<!-- END BODY -->
</html>