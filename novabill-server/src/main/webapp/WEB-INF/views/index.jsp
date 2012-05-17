<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<c:url value="/resources/j_spring_security_check" var="login_url" />

<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
		<title>Product Splash</title>
		<link rel="stylesheet" type="text/css" href="css/index.css" />
		<link rel="stylesheet" type="text/css" href="css/login.css" />
		<link rel="stylesheet" href="js/nivoslider/nivo-slider.css" type="text/css" media="screen" />
		<link rel="stylesheet" href="js/nivoslider/themes/default/default.css" type="text/css" media="screen" />
		<link rel="stylesheet" href="js/fancybox/jquery.fancybox-1.3.4.css" type="text/css" media="screen" />
	</head> 


	<body>
	
		<div class="container">
			<div class="header">
				<h1>Landing Page</h1>
				<div class="get-it-buttons">
					<a href="#" class="free-trial-button">Free Trial</a>
					<a href="#" class="buy-it-button">Buy It Now</a>
					<!-- Login Starts Here -->
		            <div id="loginContainer">
		                <a href="#" id="loginButton"><span>Login</span><em></em></a>
		                <div style="clear:both"></div>
		                <div id="loginBox">                
		                    <form id="loginForm" action="${login_url}" method="post">
		                        <fieldset id="body">
		                            <fieldset>
		                                <label for="email">Email Address</label>
		                                <input type="text" name="j_username" id="email" />
		                            </fieldset>
		                            <fieldset>
		                                <label for="password">Password</label>
		                                <input type="password" name="j_password" id="password" />
		                            </fieldset>
		                            <input type="submit" id="login" value="Sign in" />
		                            <label for="checkbox"><input type="checkbox" id="checkbox" />Remember me</label>
		                        </fieldset>
		                        <span><a href="#">Forgot your password?</a></span>
		                    </form>
		                </div>
		            </div>
		            <!-- Login Ends Here -->
				</div>
			</div>
			<div class="intro">
				<h2>Product Name</h2>
				<p>Promote your products or apps in style with this free template from MediaLoot</p>
				
				
				<div class="slider-wrapper theme-default mask-container-large">
				    <div id="slider" class="nivoSlider">
				        <img src="images/image-large-1.jpg" alt="" />
				        <img src="images/image-large-2.jpg" alt="" />
				        <img src="images/image-large-3.jpg" alt="" />
				        <img src="images/image-large-4.jpg" alt="" />
				    </div>
				    <div class="mask-gloss-large"></div>
				</div>
			</div>
			<div class="divider-2"></div>
			<div class="top-features">
				<h3>Top Features</h3>
				<div class="inset-top"></div>
				<div class="inset">
					<div class="row"><!--Start of Row-->
						
						<div class="col"><!--Start of Feature-->
							<div class="feature-image">
								<a rel="fancybox" href="images/image-large-1.jpg">
									<div class="mask-container-thumbnail">
										<img src="images/image-thumbnail-1.jpg" alt="" />
										<div class="mask-gloss-thumbnail"></div>
									</div>
								</a>
							</div>
							<div class="description">
								<h4>Lorem Ipsum</h4>
								<p>Integer posuere erat a ante venenatis dapibus posuere velit aliquet. Etiam porta sem malesuada magna mollis euismod.</p>
								
								<p>Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor auctor.</p>
								
								<p>Praesent commodo cursus magna, vel scelerisque nisl consectetur et. Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor.</p>
							</div>
						</div><!--End of Feature-->
						
						<div class="col"><!--Start of Feature-->
							<div class="feature-image">
								<a rel="fancybox" href="images/image-large-2.jpg">
									<div class="mask-container-thumbnail">
										<img src="images/image-thumbnail-2.jpg" alt="" />
										<div class="mask-gloss-thumbnail"></div>
									</div>
								</a>
							</div>
							<div class="description">
								<h4>Lorem Ipsum</h4>
								<p>Integer posuere erat a ante venenatis dapibus posuere velit aliquet. Etiam porta sem malesuada magna mollis euismod.</p>
								
								<p>Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor auctor.</p>
								
								<p>Praesent commodo cursus magna, vel scelerisque nisl consectetur et. Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor.</p>
							</div>
						</div><!--End of Feature-->
						
						<br class="clear" />
					</div><!--End of Row-->
					
					<div class="row"><!--Start of Row-->
						
						<div class="col"><!--Start of Feature-->
							<div class="feature-image">
								<a rel="fancybox" href="images/image-large-3.jpg">
									<div class="mask-container-thumbnail">
										<img src="images/image-thumbnail-3.jpg" alt="" />
										<div class="mask-gloss-thumbnail"></div>
									</div>
								</a>
							</div>
							<div class="description">
								<h4>Lorem Ipsum</h4>
								<p>Integer posuere erat a ante venenatis dapibus posuere velit aliquet. Etiam porta sem malesuada magna mollis euismod.</p>
								
								<p>Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor auctor.</p>
								
								<p>Praesent commodo cursus magna, vel scelerisque nisl consectetur et. Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor.</p>
							</div>
						</div><!--End of Feature-->
						
						<div class="col"><!--Start of Feature-->
							<div class="feature-image">
								<a rel="fancybox" href="images/image-large-4.jpg">
									<div class="mask-container-thumbnail">
										<img src="images/image-thumbnail-4.jpg" alt="" />
										<div class="mask-gloss-thumbnail"></div>
									</div>
								</a>
							</div>
							<div class="description">
								<h4>Lorem Ipsum</h4>
								<p>Integer posuere erat a ante venenatis dapibus posuere velit aliquet. Etiam porta sem malesuada magna mollis euismod.</p>
								
								<p>Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor auctor.</p>
								
								<p>Praesent commodo cursus magna, vel scelerisque nisl consectetur et. Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor.</p>
							</div>
						</div><!--End of Feature-->
						
						<br class="clear" />
					</div><!--End of Row-->
					
										
				</div>
				<div class="inset-btm"></div>
			</div>
			
			<div class="testimonials">
				<h3>Testimonials</h3>
				
				<div class="quote"><!--Start of Quote-->
					<img src="images/quotation-mark.png" alt="quotation mark" class="quotation-mark" />
					<p>Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nulla vitae elit libero, a pharetra augue.</p>
					
					<p>Nulla vitae elit libero, a pharetra augue. Vestibulum id ligula porta felis euismod semper. Curabitur blandit tempus porttitor. Etiam porta sem malesuada magna mollis euismod. </p>
					
					<p><strong>Full Name</strong></p>
				</div><!--End of Quote-->
				
				<div class="quote"><!--Start of Quote-->
					<img src="images/quotation-mark.png" alt="quotation mark" class="quotation-mark" />
					<p>Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nulla vitae elit libero, a pharetra augue.</p>
					
					<p>Nulla vitae elit libero, a pharetra augue. Vestibulum id ligula porta felis euismod semper. Curabitur blandit tempus porttitor. Etiam porta sem malesuada magna mollis euismod. </p>
					
					<p><strong>Full Name</strong></p>
				</div><!--End of Quote-->
				
				<br class="clear" />
				
			</div>
			
			<div class="footer">
				<p>Copyright 2011 <strong>Product Name</strong>. All Rights Reserved.
				<div class="get-it-buttons">
					<a href="#" class="free-trial-button">Free Trial</a>
					<a href="#" class="buy-it-button">Buy It Now</a>
				</div>
			</div>
			
		</div>
		
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
		<script type="text/javascript" src="js/nivoslider/jquery.nivo.slider.pack.js"></script>
		<script type="text/javascript" src="js/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
		<script type="text/javascript" src="js/fancybox/jquery.easing-1.3.pack.js"></script>
		<script type="text/javascript" src="js/fancybox/jquery.mousewheel-3.0.4.pack.js"></script>
		<script type="text/javascript" src="js/login.js"></script>
		
		<script type="text/javascript">
		$(window).load(function() {
		    $('#slider').nivoSlider();
		});
		</script>
		
		<script type="text/javascript">
				$(window).load(function() {
				    $("*[rel=fancybox]").fancybox();
				});
		</script>
		
	
	</body>

</html>




<%-- <html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Novabill home</title>

</head>
<body>
<h2>Welcome to NovaBill</h2>

<form action="${login_url}" method="post">
	<fieldset style="float: left;">
		<legend>Sign in</legend>
		<table>
			<tr>
				<td>Email</td>
				<td><input type="text" name="j_username" />
				</td>
			</tr>
			<tr>
				<td>Password</td>
				<td><input  type="password" name="j_password" />
				</td>
			</tr>
		</table>
		<input type="submit" value="Login"/>
	</fieldset>
</form>

</body>
</html> --%>