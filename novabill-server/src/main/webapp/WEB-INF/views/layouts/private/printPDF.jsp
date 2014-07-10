<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>

<spring:url var="privateAssetsUrl" value="/private_assets" />
<spring:url var="frontendAssetsUrl" value="/frontend_assets" />

<compress:html enabled="${mvn.tiles.minify.html}" compressJavaScript="${mvn.tiles.minify.html}" compressCss="${mvn.tiles.minify.html}">

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
<link rel="shortcut icon" href="${frontendAssetsUrl}/img/favicon.png" />
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

</compress:html>