<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>

<div id="socialBox" style="display: none;">
	<div class="entry">
		<div class="fb-like" data-href="https://www.novabill.it/" data-send="true" data-layout="box_count" data-width="450" data-show-faces="false" data-font="arial"></div>
	</div>
	<div class="entry">
		<div class="g-plusone" data-annotation="bubble" data-size="tall"></div>
	</div>
	<div class="entry">
		<a href="https://twitter.com/share" class="twitter-share-button" data-url="https://www.novabill.it/" data-lang="it" data-count="vertical">Tweet</a>
	</div>
</div>

<div class="intro">
	<h2>
		<spring:message code="application_name"></spring:message>
	</h2>
	<p><spring:message code="index.promotion" /> </p>
	
	<sec:authorize access="isAnonymous()">
		<script type="text/javascript" src="<spring:url value="/js/jquery.cycle.all.js" />"></script>
		
		<!-- <div id="shuffleContainer"> -->
			<div id="shuffleImage">
				<div><img class="image" src="<spring:url value="/images/index/clients.png" />"></div>
				<div><img class="image" src="<spring:url value="/images/index/invoice.png" />"></div>
				<div><img class="image" src="<spring:url value="/images/index/estimation.png" />"></div>
				<div><img class="image" src="<spring:url value="/images/index/creditNote.png" />"></div>
				<div><img class="image" src="<spring:url value="/images/index/transportDoc.png" />"></div>
			</div>	
			<div id="shuffleAsset">
				<div class="doc"><spring:message code="shared.clientManagement"></spring:message> </div>
				<div class="doc"><spring:message code="shared.invoices"></spring:message></div>
				<div class="doc"><spring:message code="shared.estimations"></spring:message></div>
				<div class="doc"><spring:message code="shared.creditNotes"></spring:message></div>
				<div class="doc"><spring:message code="shared.transportDocument"></spring:message></div>
			</div>				
		<!-- </div>		 -->
		
		<script type="text/javascript">
			$('#shuffleImage').cycle({
				fx:      'turnDown', 
			    delay: -4000,
			    prevNextEvent: null
			});
			$('#shuffleAsset').cycle({
				fx:      'scrollRight', 
			    next:   '#shuffleAsset', 
			    delay: -4000,
			    prevNextEvent: null
			});
		</script>
		
		<a id="registerForFree" class="action-button" href="<%=request.getContextPath()%>/register">
			<spring:message code="header.signupForFree"></spring:message>
		</a>
	</sec:authorize>
	<sec:authorize access="isAuthenticated()">
		<a class="action2-button goToPrivate" href='<spring:url value="/private"></spring:url>'><spring:message code="shared.private"/></a>
	</sec:authorize>
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
					<spring:message code="home.topfeatures.n1.par1" var="n1_1" />
					<spring:message code="home.topfeatures.n1.par2" var="n1_2" />
					<spring:message code="home.topfeatures.n1.par3" var="n1_3" />
				
					<a rel="fancybox" href="images/slider/slider-3.jpg" title="<spring:message text="${n1_1}|${n1_2}|${n1_3}" />">
						<span class="mask-container-thumbnail">
							<img src="images/image-thumbnail-4.jpg" alt="" />
							<span class="mask-gloss-thumbnail"></span>
						</span>
					</a>
				</div>
				<div class="description">
					<h4><spring:message code="home.topfeatures.n1.title" /></h4>
					<p><spring:message text="${n1_1}" htmlEscape="false" /></p>
					<p><spring:message text="${n1_2}" htmlEscape="false" /></p>
					<p><spring:message text="${n1_3}" htmlEscape="false" /></p>
				</div>
			</div>
			<!--End of Feature-->

			<div class="col">
				<!--Start of Feature-->
				<div class="feature-image">
					<spring:message code="home.topfeatures.n2.par1" var="n2_1" />
					<spring:message code="home.topfeatures.n2.par2" var="n2_2" />
					<spring:message code="home.topfeatures.n2.par3" var="n2_3" />
				
					<a rel="fancybox" href="images/slider/slider-2.jpg" title="<spring:message text="${n2_1}|${n2_2}|${n2_3}" />">
						<span class="mask-container-thumbnail">
							<img src="images/image-thumbnail-3.jpg" alt="" />
							<span class="mask-gloss-thumbnail"></span>
						</span>
					</a>
				</div>
				<div class="description">
					<h4><spring:message code="home.topfeatures.n2.title" /></h4>
					<p><spring:message text="${n2_1}" htmlEscape="false" /></p>
					<p><spring:message text="${n2_2}" htmlEscape="false" /></p>
					<p><spring:message text="${n2_3}" htmlEscape="false" /></p>
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
					<spring:message code="home.topfeatures.n3.par1" var="n3_1" />
					<spring:message code="home.topfeatures.n3.par2" var="n3_2" />
					<spring:message code="home.topfeatures.n3.par3" var="n3_3" />
				
					<a rel="fancybox" href="images/slider/slider-1.jpg" title="<spring:message text="${n3_1}|${n3_2}|${n3_3}" />">
						<span class="mask-container-thumbnail">
							<img src="images/image-thumbnail-1.jpg" alt="" />
							<span class="mask-gloss-thumbnail"></span>
						</span>
					</a>
				</div>
				<div class="description">
					<h4><spring:message code="home.topfeatures.n3.title" /></h4>
					<p><spring:message text="${n3_1}" htmlEscape="false" /></p>
					<p><spring:message text="${n3_2}" htmlEscape="false" /></p>
					<p><spring:message text="${n3_3}" htmlEscape="false" /></p>
				</div>
			</div>
			<!--End of Feature-->

	
			<div class="col">
				<!--Start of Feature-->
				<div class="feature-image">
					<spring:message code="home.topfeatures.n4.par1" var="n4_1" />
					<spring:message code="home.topfeatures.n4.par2" var="n4_2" />
					<spring:message code="home.topfeatures.n4.par3" var="n4_3" />
				
					<a rel="fancybox" href="images/slider/slider-4.jpg" title="<spring:message text="${n4_1}|${n4_2}|${n4_3}" />">
						<span class="mask-container-thumbnail">
							<img src="images/image-thumbnail-2.jpg" alt="" />
							<span class="mask-gloss-thumbnail"></span>
						</span>
					</a>
				</div>
				<div class="description">
					<h4><spring:message code="home.topfeatures.n4.title" /></h4>
					<p><spring:message text="${n4_1}" htmlEscape="false" /></p>
					<p><spring:message text="${n4_2}" htmlEscape="false" /></p>
					<p><spring:message text="${n4_3}" htmlEscape="false" /></p>
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

<script type="text/javascript">
		$(function() {
		    $("*[rel=fancybox]").fancybox({
		    	'titleFormat': function(title, currentArray, currentIndex, currentOpts){
		    		temp=title.split('|');
		    		if(!temp[2]){temp[2]="";};
		    		return '<p>'+temp[0]+'</p><p>'+temp[1]+'</p><p>'+temp[2]+'</p>'
		    		},
		    	'titlePosition' : 'over'
		    });
		    
		    
		    setTimeout(function(){
		    	$("#socialBox").fadeIn('slow');
		    }, 4000);
		});
</script>

<!-- Google Plus -->
<!-- +1 button -->
<script type="text/javascript">
  window.___gcfg = {lang: 'it'};

  (function() {
    var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;
    po.src = 'https://apis.google.com/js/plusone.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);
  })();
</script>
<!--  -->

<!-- Twitter -->
<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0];if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src="//platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>
<!--  -->

<!-- Facebook -->
<div id="fb-root"></div>
<script>(function(d, s, id) {
  var js, fjs = d.getElementsByTagName(s)[0];
  if (d.getElementById(id)) return;
  js = d.createElement(s); js.id = id;
  js.src = "//connect.facebook.net/it_IT/all.js#xfbml=1&appId=217986825010193";
  fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));</script>
<!--  -->


