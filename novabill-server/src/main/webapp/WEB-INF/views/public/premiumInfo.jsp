<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:url value="/private/upgrade/send-paypal-supscription-request" var="commence_upgrade_url" />

<h3>List premium account benefits here.</h3>
<h4>Make a not that subscription payment is done via paypal.</h4>
<form action="${commence_upgrade_url}">
	<input type="submit" value="Upgrade">
</form>
