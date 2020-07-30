<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>

<spring:url value="/frontend_assets" var="frontendAssetsUrl" />
<spring:url value="/" var="basePath" />

<compress:html enabled="${mvn.tiles.minify.html}" compressJavaScript="${mvn.tiles.minify.html}" compressCss="${mvn.tiles.minify.html}">

    <!DOCTYPE html>
    <html lang="it">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <meta content="Novabill è un servizio online per la gestione della contabilità, studiato per piccole imprese e professionisti." name="description" />
        <meta content="Novadart" name="author" />

        <link rel="icon" href="${frontendAssetsUrl}/img/favicon.png">

        <title>Novabill</title>

        <!-- jQuery Cookiebar -->
        <link href="${frontendAssetsUrl}/components/jquery.cookiebar/jquery.cookiebar.css" rel="stylesheet" type="text/css"/>

        <!-- Font Awesome -->
        <link href="${frontendAssetsUrl}/bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>

        <!-- Bootstrap core CSS -->
        <link href="${frontendAssetsUrl}/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <link href="${frontendAssetsUrl}/css/ie10-viewport-bug-workaround.css" rel="stylesheet">

        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->

        <!-- Custom styles for this template -->
        <link href="${frontendAssetsUrl}/css/style.css" rel="stylesheet">

        <!-- Loading jQuery here because it might be needed in the body of the page -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
        <script>window.jQuery || document.write('<script src="${frontendAssetsUrl}/bower_components/jquery/dist/jquery.min.js"><\/script>')</script>

    </head>
<!-- END HEAD -->

<!-- BEGIN BODY -->
<body>
    <!-- BEGIN PAGE CONTAINER -->  
    
    <tiles:insertAttribute name="body" />
    
    <!-- END PAGE CONTAINER -->  
    
</body>
<!-- END BODY -->
</html>

</compress:html> 