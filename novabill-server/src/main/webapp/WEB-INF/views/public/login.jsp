<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

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
