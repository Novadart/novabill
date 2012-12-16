<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html xmlns="http://www.w3.org/1999/xhtml">

<head>

<tiles:insertAttribute name="commonlinks" />
	
<tiles:insertAttribute name="pagelinks" />

</head>


<body>

	<div class="container">
		<tiles:insertAttribute name="header" />
		
		<tiles:insertAttribute name="body" />

	</div>
	
	<tiles:insertAttribute name="footer" />

</body>

</html>