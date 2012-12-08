<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<title><spring:message code="application_name"></spring:message></title>
<link rel="shortcut icon" type="image/png" href="images/favicon.png"></link>
<link rel="stylesheet" type="text/css" href="../css/reset-min.css" />
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
	
<tiles:insertAttribute name="pagelinks" />

</head>


<body>
	<div class="browser-alert" style="display: none;">
		<table>
			<tr>
				<td><span class="browserMessage"><spring:message code="header.browsermessage" /> </span></td>
				<td><a target="_blank" href="http://www.google.com/chrome"><img src='<spring:url value="images/chrome.png" />' /></a></td>
				<td><a target="_blank" href="http://www.google.com/chrome">Google Chrome</a></td>
				<td><a target="_blank" href="http://www.mozilla.org/firefox/"><img src='<spring:url value="images/firefox.png" />' /></a></td>
				<td><a target="_blank" href="http://www.mozilla.org/firefox/">Mozilla Firefox</a></td>
				<td><a target="_blank" href="http://www.opera.com/browser/download/"><img src='<spring:url value="images/opera.png" />' /></a></td>
				<td><a target="_blank" href="http://www.opera.com/browser/download/">Opera</a></td>
			</tr>
		</table>
	</div>
	<script>
		$(function(){
			if($.browser.msie){
				$('div.browser-alert').show();
			}	
		})
	</script>
	<div class="container">
		<tiles:insertAttribute name="header" />
		
		<tiles:insertAttribute name="body" />

		<div class="footer">
			<p>	Copyright 2011 <strong><spring:message code="application_name"></spring:message></strong>. All Rights Reserved. </p>
		</div>
	</div>

</body>

</html>