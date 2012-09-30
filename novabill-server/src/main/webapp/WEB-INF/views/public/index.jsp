<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>

<div class="intro">
	<h2>
		<spring:message code="application_name"></spring:message>
	</h2>
	<p><spring:message code="index.promotion" /> </p>

	<sec:authorize access="isAnonymous()">
		<a id="registerForFree" class="action-button" href="<%=request.getContextPath()%>/register">
			<spring:message code="header.signupForFree"></spring:message>
		</a>
	</sec:authorize>
	<div class="slider-wrapper theme-default mask-container-large">
		<div id="slider" class="nivoSlider">
			<img src="images/slider/slider-3.jpg" alt="" /> 
			<img src="images/slider/slider-2.jpg" alt="" /> 
			<img src="images/slider/slider-1.jpg" alt="" /> 
			<img src="images/slider/slider-4.jpg" alt="" />
		</div>
		<div class="mask-gloss-large"></div>
	</div>
</div>
<div class="divider-2"></div>
<div class="top-features">
	<h3><spring:message code="home.topfeatures" /></h3>
	<div class="inset-top"></div>
	<div class="inset">
		<div class="row">
			<!--Start of Row-->

			<div class="col">
				<!--Start of Feature-->
				<div class="feature-image">
					<a rel="fancybox" href="images/slider/slider-3.jpg">
						<div class="mask-container-thumbnail">
							<img src="images/image-thumbnail-4.jpg" alt="" />
							<div class="mask-gloss-thumbnail"></div>
						</div>
					</a>
				</div>
				<div class="description">
					<h4><spring:message code="home.topfeatures.n1.title" /> </h4>
					<p><spring:message code="home.topfeatures.n1.par1" /></p>

					<p><spring:message code="home.topfeatures.n1.par2" /></p>

					<p><spring:message code="home.topfeatures.n1.par3" /></p>
				</div>
			</div>
			<!--End of Feature-->

			<div class="col">
				<!--Start of Feature-->
				<div class="feature-image">
					<a rel="fancybox" href="images/slider/slider-2.jpg">
						<div class="mask-container-thumbnail">
							<img src="images/image-thumbnail-3.jpg" alt="" />
							<div class="mask-gloss-thumbnail"></div>
						</div>
					</a>
				</div>
				<div class="description">
					<h4><spring:message code="home.topfeatures.n2.title" /></h4>
					<p><spring:message code="home.topfeatures.n2.par1" /></p>

					<p><spring:message code="home.topfeatures.n2.par2" /></p>

					<p><spring:message code="home.topfeatures.n2.par3" /></p>
				</div>
			</div>
			<!--End of Feature-->

			<br class="clear" />
		</div>
		<!--End of Row-->

		<div class="row">
			<!--Start of Row-->

			<div class="col">
				<!--Start of Feature-->
				<div class="feature-image">
					<a rel="fancybox" href="images/slider/slider-1.jpg">
						<div class="mask-container-thumbnail">
							<img src="images/image-thumbnail-1.jpg" alt="" />
							<div class="mask-gloss-thumbnail"></div>
						</div>
					</a>
				</div>
				<div class="description">
					<h4><spring:message code="home.topfeatures.n3.title" /> </h4>
					<p><spring:message code="home.topfeatures.n3.par1" /></p>

					<p><spring:message code="home.topfeatures.n3.par2" /></p>

					<p><spring:message code="home.topfeatures.n3.par3" /></p>
				</div>
			</div>
			<!--End of Feature-->

			<div class="col">
				<!--Start of Feature-->
				<div class="feature-image">
					<a rel="fancybox" href="images/slider/slider-4.jpg">
						<div class="mask-container-thumbnail">
							<img src="images/image-thumbnail-2.jpg" alt="" />
							<div class="mask-gloss-thumbnail"></div>
						</div>
					</a>
				</div>
				<div class="description">
					<h4><spring:message code="home.topfeatures.n4.title" /> </h4>
					<p><spring:message code="home.topfeatures.n4.par1" /></p>

					<p><spring:message code="home.topfeatures.n4.par2" /></p>

					<p><spring:message code="home.topfeatures.n4.par3" /></p>
				</div>
			</div>
			<!--End of Feature-->

			<br class="clear" />
		</div>
		<!--End of Row-->


	</div>
	<div class="inset-btm"></div>
	
	<sec:authorize access="isAnonymous()">
		<a id="registerForFree" class="action-button" href="<%=request.getContextPath()%>/register">
			<spring:message code="header.signupForFree"></spring:message>
		</a>
	</sec:authorize>
</div>

<!-- <div class="testimonials">
	<h3>Testimonials</h3>

	<div class="quote">
		Start of Quote
		<img src="images/quotation-mark.png" alt="quotation mark"
			class="quotation-mark" />
		<p>Cum sociis natoque penatibus et magnis dis parturient montes,
			nascetur ridiculus mus. Nulla vitae elit libero, a pharetra augue.</p>

		<p>Nulla vitae elit libero, a pharetra augue. Vestibulum id ligula
			porta felis euismod semper. Curabitur blandit tempus porttitor. Etiam
			porta sem malesuada magna mollis euismod.</p>

		<p>
			<strong>Full Name</strong>
		</p>
	</div>
	End of Quote

	<div class="quote">
		Start of Quote
		<img src="images/quotation-mark.png" alt="quotation mark"
			class="quotation-mark" />
		<p>Cum sociis natoque penatibus et magnis dis parturient montes,
			nascetur ridiculus mus. Nulla vitae elit libero, a pharetra augue.</p>

		<p>Nulla vitae elit libero, a pharetra augue. Vestibulum id ligula
			porta felis euismod semper. Curabitur blandit tempus porttitor. Etiam
			porta sem malesuada magna mollis euismod.</p>

		<p>
			<strong>Full Name</strong>
		</p>
	</div>
	End of Quote

	<br class="clear" />

</div> -->

<script type="text/javascript"
	src="js/nivoslider/jquery.nivo.slider.pack.js"></script>
<script type="text/javascript"
	src="js/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<script type="text/javascript"
	src="js/fancybox/jquery.easing-1.3.pack.js"></script>
<script type="text/javascript"
	src="js/fancybox/jquery.mousewheel-3.0.4.pack.js"></script>

<script type="text/javascript">
		$(window).load(function() {
		    $('#slider').nivoSlider({
		    	pauseTime: 6000
		    });
		});
		</script>

<script type="text/javascript">
				$(window).load(function() {
				    $("*[rel=fancybox]").fancybox();
				});
		</script>

