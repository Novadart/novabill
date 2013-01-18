<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="exportURL" value="/private/export">
	<spring:param name="${exportClientsParamName}" value="true"/>
	<spring:param name="${exportInvoicesParamName}" value="true"/>
	<spring:param name="${exportEstimationsParamName}" value="true"/>
	<spring:param name="${exportCreditnotesParamName}" value="true"/>
	<spring:param name="${exportTransportdocsParamName}" value="true"/>
	<spring:param name="${exportTokenParamName}" value="${exportToken}"/>
</spring:url>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" type="text/css" href="<spring:url value="/css/reset-min.css" />" />
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>

<style type="text/css">
.action2-button {
	cursor: pointer;
	display: inline-block;
	width: 140px;
	height: 30px;
	background: #52a8e8;
	background: -moz-linear-gradient(top, #52a8e8 0%, #2e76cf 100%);
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #52a8e8),
		color-stop(100%, #2e76cf) );
	background: -webkit-linear-gradient(top, #52a8e8 0%, #2e76cf 100%);
	background: -o-linear-gradient(top, #52a8e8 0%, #2e76cf 100%);
	background: -ms-linear-gradient(top, #52a8e8 0%, #2e76cf 100%);
	filter: progid:DXImageTransform.Microsoft.gradient(  startColorstr='#52a8e8',
		endColorstr='#2e76cf', GradientType=0 );
	background: linear-gradient(top, #52a8e8 0%, #2e76cf 100%);
	border: 1px solid #385B7A;
	-moz-border-radius: 5px;
	-webkit-border-radius: 5px;
	border-radius: 5px;
	box-shadow: inset 0px 1px 0px #93C2E6, 0px 1px 2px #777;
	-moz-box-shadow: inset 0px 1px 0px #93C2E6, 0px 1px 2px #777;
	-webkit-box-shadow: inset 0px 1px 0px #93C2E6, 0px 1px 2px #777;
	color: #12202F;
	font-size: 16px;
	text-align: center;
	line-height: 30px;
	text-decoration: none;
	font-weight: bold;
	text-shadow: 0px 1px 1px #B5CFEC;
}

.action2-button:hover {
	background: #5caafb;
	background: -moz-linear-gradient(top, #5caafb 0%, #3c85dc 100%);
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #5caafb),
		color-stop(100%, #3c85dc) );
	background: -webkit-linear-gradient(top, #5caafb 0%, #3c85dc 100%);
	background: -o-linear-gradient(top, #5caafb 0%, #3c85dc 100%);
	background: -ms-linear-gradient(top, #5caafb 0%, #3c85dc 100%);
	filter: progid:DXImageTransform.Microsoft.gradient(  startColorstr='#5caafb',
		endColorstr='#3c85dc', GradientType=0 );
	background: linear-gradient(top, #5caafb 0%, #3c85dc 100%);
}

.action2-button:active {
	background: #4596df;
	background: -moz-linear-gradient(top, #4596df 0%, #2a60b4 100%);
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #4596df),
		color-stop(100%, #2a60b4) );
	background: -webkit-linear-gradient(top, #4596df 0%, #2a60b4 100%);
	background: -o-linear-gradient(top, #4596df 0%, #2a60b4 100%);
	background: -ms-linear-gradient(top, #4596df 0%, #2a60b4 100%);
	filter: progid:DXImageTransform.Microsoft.gradient(  startColorstr='#4596df',
		endColorstr='#2a60b4', GradientType=0 );
	background: linear-gradient(top, #4596df 0%, #2a60b4 100%);
}

.exportLink {
	width: 200px;
}
</style>

</head>
<body>

<a class="action2-button exportLink" href='${exportURL}'><spring:message code="deleteAccount.exportData" /></a>

<script type="text/javascript">
<!--
$('.exportLink').click(function(e){
	setTimeout(function(){
		window.location.reload();
	}, 500);
});
//-->
</script>
</body>
</html>