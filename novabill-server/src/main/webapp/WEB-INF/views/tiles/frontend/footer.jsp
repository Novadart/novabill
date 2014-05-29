<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/frontend_assets" var="frontendAssetsUrl" />
<spring:url value="/tos" var="tosUrl" />
<spring:url var="shareAskUrl" value="/share-ask" />

<!-- BEGIN FOOTER -->
<%-- <div class="footer">
    <div class="container">
        <div class="row">
            <div class="col-md-4 col-sm-4 space-mobile">
                <!-- BEGIN ABOUT -->                    
                <h2>About</h2>
                <p class="margin-bottom-30">Vivamus imperdiet felis consectetur onec eget orci adipiscing nunc. Pellentesque fermentum, ante ac interdum ullamcorper.</p>
                <div class="clearfix"></div>                    
                <!-- END ABOUT -->          

                <h2>Photos Stream</h2>
                <!-- BEGIN BLOG PHOTOS STREAM -->
                <div class="blog-photo-stream margin-bottom-30">
                    <ul class="list-unstyled">
                        <li><a href="#"><img src="${frontendAssetsUrl}/img/people/img5-small.jpg" alt=""></a></li>
                        <li><a href="#"><img src="${frontendAssetsUrl}/img/works/img1.jpg" alt=""></a></li>
                        <li><a href="#"><img src="${frontendAssetsUrl}/img/people/img4-large.jpg" alt=""></a></li>
                        <li><a href="#"><img src="${frontendAssetsUrl}/img/works/img6.jpg" alt=""></a></li>
                        <li><a href="#"><img src="${frontendAssetsUrl}/img/pics/img1-large.jpg" alt=""></a></li>
                        <li><a href="#"><img src="${frontendAssetsUrl}/img/pics/img2-large.jpg" alt=""></a></li>
                        <li><a href="#"><img src="${frontendAssetsUrl}/img/works/img3.jpg" alt=""></a></li>
                        <li><a href="#"><img src="${frontendAssetsUrl}/img/people/img2-large.jpg" alt=""></a></li>
                        <li><a href="#"><img src="${frontendAssetsUrl}/img/works/img2.jpg" alt=""></a></li>
                        <li><a href="#"><img src="${frontendAssetsUrl}/img/works/img5.jpg" alt=""></a></li>
                    </ul>                    
                </div>
                <!-- END BLOG PHOTOS STREAM -->                              
            </div>
            <div class="col-md-4 col-sm-4 space-mobile">
                <!-- BEGIN CONTACTS -->                                    
                <h2>Contact Us</h2>
                <address class="margin-bottom-40">
                    Loop, Inc. <br />
                    795 Park Ave, Suite 120 <br />
                    San Francisco, CA 94107 <br />
                    P: (234) 145-1810 <br />
                    Email: <a href="mailto:info@keenthemes.com">info@keenthemes.com</a>                        
                </address>
                <!-- END CONTACTS -->                                    

                <!-- BEGIN SUBSCRIBE -->                                    
                <h2>Monthly Newsletter</h2>  
                <p>Subscribe to our newsletter and stay up to date with the latest news and deals!</p>
                <form action="#" class="form-subscribe">
                    <div class="input-group input-large">
                        <input class="form-control" type="text">
                        <span class="input-group-btn">
                            <button class="btn theme-btn" type="button">SUBSCRIBE</button>
                        </span>
                    </div>
                </form>

                <!-- END SUBSCRIBE -->                                    
            </div>
            <div class="col-md-4 col-sm-4">
                <!-- BEGIN TWITTER BLOCK -->                                                    
                <h2>Latest Tweets</h2>
                <dl class="dl-horizontal f-twitter">
                    <dt><i class="fa fa-twitter"></i></dt>
                    <dd>
                        <a href="#">@keenthemes</a>
                        Imperdiet condimentum diam dolor lorem sit consectetur adipiscing
                        <span>3 min ago</span>
                    </dd>
                </dl>                    
                <dl class="dl-horizontal f-twitter">
                    <dt><i class="fa fa-twitter"></i></dt>
                    <dd>
                        <a href="#">@keenthemes</a>
                        Sequat ipsum dolor onec eget orci fermentum condimentum lorem sit consectetur adipiscing
                        <span>8 min ago</span>
                    </dd>
                </dl>                    
                <dl class="dl-horizontal f-twitter">
                    <dt><i class="fa fa-twitter"></i></dt>
                    <dd>
                        <a href="#">@keenthemes</a>
                        Remonde sequat ipsum dolor lorem sit consectetur adipiscing
                        <span>12 min ago</span>
                    </dd>
                </dl>                    
                <dl class="dl-horizontal f-twitter">
                    <dt><i class="fa fa-twitter"></i></dt>
                    <dd>
                        <a href="#">@keenthemes</a>
                        Pilsum dolor lorem sit consectetur adipiscing orem sequat
                        <span>16 min ago</span>
                    </dd>
                </dl>                    
                <!-- END TWITTER BLOCK -->                                                                        
            </div>
        </div>
    </div>
</div> --%>
<!-- END FOOTER -->

<!-- BEGIN COPYRIGHT -->
<div class="copyright">
    <div class="container">
        <div class="row">
            <div class="col-md-8 col-sm-8">
                <p style="padding-bottom: 20px;">
                    <span class="margin-right-10">2013-2014 © <a href="http://www.novadart.com" target="_blank">Novadart</a>. ALL Rights Reserved.</span> 
                    <a href="https://www.iubenda.com/privacy-policy/257554" class="iubenda-light iubenda-embed" title="Privacy Policy">Privacy Policy</a>
				    <script type="text/javascript">
				        (function(w, d) {
				            var loader = function() {
				                var s = d.createElement("script"), tag = d
				                        .getElementsByTagName("script")[0];
				                s.src = "https://cdn.iubenda.com/iubenda.js";
				                tag.parentNode.insertBefore(s, tag);
				            };
				            if (w.addEventListener) {
				                w.addEventListener("load", loader, false);
				            } else if (w.attachEvent) {
				                w.attachEvent("onload", loader);
				            } else {
				                w.onload = loader;
				            }
				        })(window, document);
				    </script>
                    
                    | <a href="${tosUrl}">Termini di Servizio</a>
                </p>
            </div>
            <div class="col-md-4 col-sm-4">
                <ul class="social-footer">
                    <li class="datashare"><a class="btn btn-sm blue" href="${shareAskUrl}"><i class="fa fa-group"></i> Sei un Commercialista?</a></li>
                </ul>
                <!-- <ul class="social-footer">
                    <li><a href="#"><i class="fa fa-facebook"></i></a></li>
                    <li><a href="#"><i class="fa fa-google-plus"></i></a></li>
                    <li><a href="#"><i class="fa fa-dribbble"></i></a></li>
                    <li><a href="#"><i class="fa fa-linkedin"></i></a></li>
                    <li><a href="#"><i class="fa fa-twitter"></i></a></li>
                    <li><a href="#"><i class="fa fa-skype"></i></a></li>
                    <li><a href="#"><i class="fa fa-github"></i></a></li>
                    <li><a href="#"><i class="fa fa-youtube"></i></a></li>
                    <li><a href="#"><i class="fa fa-dropbox"></i></a></li>
                </ul> -->                
            </div>
        </div>
    </div>
</div>
<!-- END COPYRIGHT -->