<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Resolution Test</title>

<style type="text/css">
	iframe {
		border: 1px solid #CCC;
	}
</style>
</head>
<body>

<% 
	String height = request.getParameter("height");
	String width = request.getParameter("width");
	
	width = width==null ? "1024" : width;
	height = height==null ? "768" : height;
%>

<iframe src='<spring:url value="/" ></spring:url>' style="height: <%=height+"px" %>; width: <%=width+"px"%>">

</iframe>

</body>
</html>