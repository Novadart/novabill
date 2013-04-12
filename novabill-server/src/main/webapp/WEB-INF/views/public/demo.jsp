<%@page import="java.io.StringWriter"%>
<%@page import="org.codehaus.jackson.map.ObjectMapper"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<!doctype html>

<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta name="gwt:property" content="locale=it_IT">
    <meta http-equiv="X-UA-Compatible" content="IE=8" />
    
	<link rel="shortcut icon" type="image/png" href="<spring:url value="/images/favicon.png" />"></link>
    <link type="text/css" rel="stylesheet" href="<spring:url value="/css/reset-min.css" />">
    <link type="text/css" rel="stylesheet" href="<spring:url value="/js/contactable/contactable-private.css" />">

    <title><spring:message code="application_name"></spring:message></title>
    
    <script type="text/javascript">
    	var business = <%=request.getAttribute("business")%>;
    	var daysToExpiration = '-1';
    	var notesBitMask = '-1';
    	var debugEnabled = 'false';
    </script>
    
    <style type="text/css">
    #loadingGWT {
    font-family: Arial, sans-serif, serif;
    padding: 2em;
    font-size: 1.2em;
    }
    
    #loadingGWT span {
	margin-left: 2em;    
    }
    
    #loadingGWT img {
    position: relative;
	top: 0.5em;
    }
    </style>
    
    <script type="text/javascript" language="javascript" src="<spring:url value="/demo/demo.nocache.js" />"></script>

  </head>

  <body>
	
	<div id="loadingGWT">
		<img src="images/loader.gif"><span><spring:message code="private.loading" /></span>
	</div>
	
	
    <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>
    
    <noscript>
      <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
        Your web browser must have JavaScript enabled
        in order for this application to display correctly.
      </div>
    </noscript>
    
    

  </body>
</html>
