<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>

<spring:url value="/frontend_assets" var="frontendAssetsUrl" />
<spring:url value="/" var="basePath" />

<%
    String pageName = (String)request.getAttribute("pageName");
%>

<compress:html enabled="${mvn.tiles.minify.html}" compressJavaScript="${mvn.tiles.minify.html}" compressCss="${mvn.tiles.minify.html}"> 

<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if !IE]><!--> <html lang="en"> <!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
    <meta charset="utf-8" />
    <title>Novabill | <%=pageName%></title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
    <meta content="Novabill è un servizio online per la gestione della contabilità, studiato per piccole imprese e professionisti." name="description" />
    <meta content="Novadart" name="author" />

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

    <script src="${frontendAssetsUrl}/plugins/jquery-1.10.2.min.js" type="text/javascript"></script>
   <link href="${frontendAssetsUrl}/plugins/jquery.cookiebar/jquery.cookiebar.css" rel="stylesheet" type="text/css"/>
    <script src="${frontendAssetsUrl}/plugins/jquery.cookiebar/jquery.cookiebar.js" type="text/javascript"></script>
   
   <!-- BEGIN PAGE LEVEL PLUGIN STYLES --> 
   <link href="${frontendAssetsUrl}/plugins/fancybox/source/jquery.fancybox.css" rel="stylesheet" /> 
   <link href="${frontendAssetsUrl}/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css"/>
   <link href="${frontendAssetsUrl}/plugins/iealert/css/style.css" rel="stylesheet" type="text/css"/>
   <!-- END PAGE LEVEL PLUGIN STYLES -->
   
   <tiles:insertAttribute ignore="true" name="head" />
   
   <tiles:insertAttribute  ignore="true" name="css" />
   
   <tiles:insertAttribute  ignore="true" name="cssExtra" />

   <link rel="shortcut icon" href="${frontendAssetsUrl}/img/favicon.png" />
   
   <style type="text/css">
   .page-container {
        min-height: 400px;
   }
   </style>
   
</head>
<!-- END HEAD -->

<!-- BEGIN BODY -->
<body>

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
    <script src="${frontendAssetsUrl}/plugins/jquery.cookie-1.4.0.js" type="text/javascript"></script>
    <script src="${frontendAssetsUrl}/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
    <script src="${frontendAssetsUrl}/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>      
    <script type="text/javascript" src="${frontendAssetsUrl}/plugins/hover-dropdown.js"></script>
    <script type="text/javascript" src="${frontendAssetsUrl}/plugins/back-to-top.js"></script>    
    <script src="${frontendAssetsUrl}/plugins/uniform/jquery.uniform.min.js" type="text/javascript" ></script>
    <script src="${frontendAssetsUrl}/plugins/iealert/iealert.min.js" type="text/javascript"></script>
    <script src="${frontendAssetsUrl}/plugins/jquery.pwstrength.bootstrap/src/pwstrength.js" type="text/javascript"></script>
    <!-- END CORE PLUGINS -->

    <script>
	var NovabillFrontendConf = {
	        basePath : '${basePath}',
	        version : '<tiles:insertAttribute name="novabill.version" />'
	};
	
	$(function(){
        var message = 'Questo sito o gli strumenti terzi da questo utilizzati si avvalgono di cookie necessari al funzionamento ed utili alle finalità illustrate nella cookie policy. ' +
                'Se vuoi saperne di più o negare il consenso a tutti o ad alcuni cookie, consulta la cookie policy. <br>Chiudendo questo banner, scorrendo questa pagina, ' +
                'cliccando su un link o proseguendo la navigazione in altra maniera, acconsenti all’uso dei cookie.';
        var acceptOnScroll = typeof window.COOKIEBAR_DISABLE_ONSCROLL === 'undefined';
        $.cookieBar({
            autoEnable: false,
            message: message,
            acceptButton: true,
            acceptText: 'Chiudi',
            policyButton: true,
            policyText: 'Cookie Policy',
            policyURL: '${cookiesPolicy}',
            fixed:true,
            zindex:'9999999',
            acceptOnContinue: true,
            acceptOnScroll: acceptOnScroll
        });
	});
	</script>
	
	<!-- BEGIN PAGE LEVEL JAVASCRIPTS(REQUIRED ONLY FOR CURRENT PAGE) -->
	<script type="text/javascript" src="${frontendAssetsUrl}/plugins/fancybox/source/jquery.fancybox.pack.js"></script>
	
	<script src="${frontendAssetsUrl}/scripts/app.js"></script>  
	<script type="text/javascript">
	    jQuery(document).ready(function() {    
	       App.init();
	       App.initUniform();
	       
	       if(!window.skipIEAlert && !$.cookie('ie_alert_shown_public')){
		       $("body").iealert({
		    	   support:"ie8",
		           title:"Il tuo browser è vecchio e insicuro e non è supportato da Novabill",
		           text:"Non è sicuro utilizzare questo browser per lavorare su dati sensibili.<br>Per favore premi sul pulsante 'Aggiorna' qui sotto e installa una versione più recente di Internet Explorer o uno dei browser alternativi suggeriti.<br><br>Grazie",
		           upgradeTitle:"Aggiorna",
		           upgradeLink:"http://browsehappy.com/",
		           overlayClose:false,
		           closeBtn: true
		       });
	
	           $.cookie('ie_alert_shown_public', 'true', { path: '/' });
	       }
	    });
	</script>
	<!-- END PAGE LEVEL JAVASCRIPTS -->

    <tiles:insertAttribute  ignore="true" name="javascript" />
    
    <tiles:insertAttribute  ignore="true" name="javascriptExtra" />
    
    <tiles:insertAttribute name="analytics" />
    
</body>
<!-- END BODY -->
</html>

</compress:html>