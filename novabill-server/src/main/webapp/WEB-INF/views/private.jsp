<%@page import="java.io.StringWriter"%>
<%@page import="org.codehaus.jackson.map.ObjectMapper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>

<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta name="gwt:property" content="locale=it_IT">

    <link type="text/css" rel="stylesheet" href="css/reset-min.css">
    <link type="text/css" rel="stylesheet" href="css/private/style.css">

    <title>NovaBill</title>
    
    <script type="text/javascript">
    	var business = <%=request.getAttribute("business")%>
    </script>
    
    <script type="text/javascript" language="javascript" src="rpc/rpc.nocache.js"></script>
  </head>

  <body>

    <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>
    
    <noscript>
      <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
        Your web browser must have JavaScript enabled
        in order for this application to display correctly.
      </div>
    </noscript>
    
    

  </body>
</html>
