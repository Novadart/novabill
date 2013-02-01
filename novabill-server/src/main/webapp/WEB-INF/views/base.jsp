<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>

<html xmlns="http://www.w3.org/1999/xhtml">

<head>

<tiles:insertAttribute name="commonlinks" />

<tiles:insertAttribute name="pagelinks" />

</head>

<spring:message var="link1" htmlEscape="false" text="<a target='_blank' href='http://windows.microsoft.com/en-US/internet-explorer/downloads/ie-9/worldwide-languages'>" />
<spring:message var="link2" htmlEscape="false" text="</a>" /> 

<body>
	<div class="browser-alert" style="display: none;">
		<table>
			<tr>
				<td><span style="max-width: 50%;" class="browserMessage">
					<spring:message code="header.browsermessage" arguments="${link1},${link2}" htmlEscape="false"/> 
					</span></td>
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
			if($.browser.msie && (parseInt($.browser.version) <= 7 ) ){
				$('div.browser-alert').show();
			}	
		})
	</script>
	
	<div class="container">
		<tiles:insertAttribute name="header" />
		
		<tiles:insertAttribute name="body" />
		
	</div>
	
	<tiles:insertAttribute name="footer" />

</body>

</html>
