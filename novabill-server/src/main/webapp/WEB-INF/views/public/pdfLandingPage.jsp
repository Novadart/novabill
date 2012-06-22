<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>

<style type="text/css">
img#pdfImage {
	display: block;
	margin: 0 auto;
	width: 200px;
}

div#message {
	text-align: center;
	margin-top: 15px;
	font-family: Arial, serif;
	font-size: 25px;
	text-shadow: #CCC 2px 2px;
}
</style>

</head>
<body>
	<div>
		<img id="pdfImage"
			src="<%=request.getContextPath()%>/images/pdf-download.gif">
	</div>
	<div id="message">Preparing PDF...</div>

	<script type="text/javascript">
	var secs = 0;
	var autocloseSecs = 5;
	
	function updateLabel(){
		if(autocloseSecs-secs <= 0){
			window.close();
		}
		
		$('div#message').html('Closing in '+(autocloseSecs-secs)+' seconds...');
		secs++;
	}
	

	var contextPath = '<%=request.getContextPath()%>';
	var id = '<%=request.getAttribute("id")%>';
	var docType = '<%=request.getAttribute("document")%>';
	$.get(contextPath+'/private/pdf/token', function(data){
		var pdfUrl = contextPath+'/private/pdf/'+docType+'/'+id+'?token='+encodeURIComponent(data);
		window.location.assign(pdfUrl);
		setInterval(updateLabel, 1000);
	});
	</script>

</body>
</html>