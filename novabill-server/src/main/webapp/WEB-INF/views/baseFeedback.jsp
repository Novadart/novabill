<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>

<html xmlns="http://www.w3.org/1999/xhtml">

<head>

<link rel="stylesheet" type="text/css" href="<spring:url value="/css/header.css" />" />
<link rel="stylesheet" type="text/css" href="<spring:url value="/css/page/feedback.css" />" />

<tiles:insertAttribute name="commonlinks" />

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
		<tiles:insertTemplate template="/WEB-INF/views/public/headerSimple.jsp"></tiles:insertTemplate>
		
		<div class="feedbackMessage">
			<tiles:insertAttribute name="feedbackMessage" />
		</div>
		
		<a class="errorGotohome" href="<spring:url value="/" />"><spring:message code="error.gotohome"></spring:message></a>

	</div>
	
	<tiles:insertAttribute name="footer" />

</body>

</html>
