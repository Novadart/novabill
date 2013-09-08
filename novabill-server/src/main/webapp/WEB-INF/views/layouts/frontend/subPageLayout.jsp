<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if !IE]><!--> <html lang="en"> <!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
	
	<tiles:insertAttribute name="head" />
	
</head>
<!-- END HEAD -->

<!-- BEGIN BODY -->
<body>

	<tiles:insertAttribute name="header" />

	<tiles:insertAttribute name="body" />

	<tiles:insertAttribute name="footer" />

	<tiles:insertAttribute name="javascript" />
	
</body>
<!-- END BODY -->
</html>