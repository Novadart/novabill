<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<div class="wrapper">

	<% 	if(request.getParameterMap().containsKey("login_error")){ %>
	
		<div class="loginError"><spring:message code="login.error" /></div>
	
	<% } %>


	<form name="f"
		action='<spring:url value="/resources/login_check"></spring:url>'
		method="POST" class="loginForm">
		<table class="loginFormTable">
			<tr>
				<td>
					<label class="loginLabel" for="j_username"><spring:message
						code="shared.email" /> </label>
				</td>
				<td>
					<input id="j_username" class="loginInput" type='text'
						name='j_username' />
				</td>
			</tr>
			<tr>
				<td>
					<label class="loginLabel" for="j_password"> <spring:message
						code="shared.password" />
					</label>
				</td>
				<td>
					<input id="j_password" type='password' class="loginInput" name='j_password' />
					<br>
					<a class="forgottenPassword" href='<spring:url value="/forgot-password" />'><spring:message code="header.forgotPassword" /> </a>
				</td>
			</tr>
			<tr>
				<td> </td>
				<td>
					<label id="rememberMe" for="rememberMeCheckBox">
						<input type="checkbox" id="rememberMeCheckBox" name="_spring_security_remember_me"/>
								<spring:message code="header.rememberme"></spring:message></label>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<input id="proceed" class="action2-button submit" type="submit"
						value='<spring:message code="header.login" />' />	
				</td>
			</tr>
		</table>
		<script>
			var remMe = $('#rememberMeCheckBox');
			remMe.attr('checked', $.cookie("com.novadart.novabill.rememberMeChecked")!=null);
			remMe.change(function(e){
				$.cookie("com.novadart.novabill.rememberMeChecked", this.checked ? true : null, { expires : 365 });
			});
		</script>
		
	</form>
	
	
	<div class="registerBox">
		<span class=""><spring:message code="login.notregistered" /> </span>
		<a id="registerButton" class="action-button" href="<%=request.getContextPath()%>/register">
		<spring:message code="header.signupForFree"></spring:message>
	</a>
	</div>

</div>

