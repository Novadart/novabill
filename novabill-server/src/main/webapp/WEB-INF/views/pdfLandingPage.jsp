<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
</head>
<body>
	<script type="text/javascript">
		var contextPath = '<%=request.getContextPath()%>';
		var id = '<%=request.getAttribute("id")%>';
		var docType = '<%=request.getAttribute("document")%>';
		$.get(contextPath+'/private/pdf/token', function(data){
			var pdfUrl = contextPath+'/private/pdf/'+docType+'/'+id+'?token='+encodeURIComponent(data);
			window.location.assign(pdfUrl);
			setTimeout("window.close()",5000);
		});
	</script>
</body>
</html>