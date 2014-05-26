<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="privateAssetsUrl" value="/private_assets" />

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Stampa PDF</title>
    <link rel="stylesheet" type="text/css" href="${privateAssetsUrl}/plugins/yui-css-reset/cssreset-min.css">

<style type="text/css">
html, body {
    width: 100%;
    height: 100%;
    overflow: hidden;
}

iframe#printArea {
    width: 100%;
    height: 100%;
}
</style>
</head>
<body>

    <iframe id="printArea" src="${pdfUrl}"></iframe>

	<script	src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
	<script>window.jQuery || document.write('<script src="${privateAssetsUrl}/plugins/jquery-1.10.2.min.js"><\/script>');</script>

	<script type="text/javascript">
    $(function() {
    	var PDFIframe = $('iframe#printArea');
    	
    	PDFIframe.load(function(){
    		setTimeout(function(){
    			PDFIframe[0].focus();
    			try {
    			    PDFIframe[0].contentWindow.print();
    			} catch (e) {
				}
    		}, 1000);
    	});
   	});
    
    </script>

</body>
</html>