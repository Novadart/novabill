<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Issue Paypal Subscription Request</title>
</head>
<body>

<form action="${paypalAction}" method="post" id="paypalSubscriptionRequestForm">
	<input type="hidden" name="cmd" value="_s-xclick">
	<input type="hidden" name="hosted_button_id" value="${hostedButtonID}">
	<input type="hidden" name="rm" value="2">
	<input type="hidden" name="return" value="${returnUrl}">
	<input type="hidden" name="custom" value="${email}">
</form>
<script type="text/javascript">
	document.forms["paypalSubscriptionRequestForm"].submit()
</script>

</body>
</html>