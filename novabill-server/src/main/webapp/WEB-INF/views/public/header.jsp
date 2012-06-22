<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<div class="header">
	<div class="get-it-buttons">
		<a href="#" class="action-button">Free Trial</a>
		<!-- Login Starts Here -->
		<div id="loginContainer">
			<a href="#" id="loginButton"><span>Login</span><em></em></a>
			<div style="clear: both"></div>
			<div id="loginBox">
				<form id="loginForm" action="${login_url}" method="post">
					<fieldset id="body">
						<fieldset>
							<label for="email">Email Address</label> <input type="text"
								name="j_username" id="email" />
						</fieldset>
						<fieldset>
							<label for="password">Password</label> <input type="password"
								name="j_password" id="password" />
						</fieldset>
						<input type="submit" id="login" value="Sign in" /> <label
							for="checkbox"><input type="checkbox" id="checkbox" />Remember
							me</label>
					</fieldset>
					<div>
						<a href="#">Forgot your password?</a>
					</div>
				</form>
			</div>
		</div>
		<!-- Login Ends Here -->
	</div>
</div>
