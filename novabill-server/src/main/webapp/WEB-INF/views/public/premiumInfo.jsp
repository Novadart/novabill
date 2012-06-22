<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<c:url value="/private/upgrade/send-paypal-supscription-request" var="commence_upgrade_url" />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h3>List premium account benefits here.</h3>
	<h4>Make a not that subscription payment is done via paypal.</h4>
	<form action="${commence_upgrade_url}">
		<input type="submit" value="Upgrade">
	</form>
</body>
</html>