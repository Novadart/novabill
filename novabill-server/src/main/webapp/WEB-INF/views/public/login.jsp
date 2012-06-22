<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<form name="f"
		action="<%=request.getContextPath()%>/resources/j_spring_security_check"
		method="POST">
		<div>
			<label for="j_username"></label> <input id="j_username" type='text'
				name='j_username' style="width: 150px" />
		</div>
		<br />
		<div>
			<label for="j_password"> </label> <input id="j_password"
				type='password' name='j_password' style="width: 150px" />
		</div>
		<br />
		<div class="submit">
			<input id="proceed" type="submit" value="Submit" />
		</div>
	</form>



</body>
</html>