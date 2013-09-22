<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/frontend_assets" var="frontendAssetsUrl" />

<!-- BEGIN FOOTER -->
<div class="front-footer">
    <div class="container">
        <div class="row-fluid">
            <div class="span4 space-mobile">
                <!-- BEGIN ABOUT -->                    
                <h2>About</h2>
                <p class="margin-bottom-30">Vivamus imperdiet felis consectetur onec eget orci adipiscing nunc. Pellentesque fermentum, ante ac interdum ullamcorper.</p>
                <div class="clearfix"></div>                    
                <!-- END ABOUT -->          

                <h2>Photos Stream</h2>
                <!-- BEGIN BLOG PHOTOS STREAM -->
                <div class="blog-photo-stream margin-bottom-30">
                    <ul class="unstyled">
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
            <div class="span4 space-mobile">
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
                <form action="#" class="form-search">
                    <div class="input-append">
                        <input style="background:#fff;" class="m-wrap" type="text"><button class="btn theme-btn" type="button">SUBSCRIBE</button>
                    </div>
                </form>
                <!-- END SUBSCRIBE -->                                    
            </div>
            <div class="span4">
                <!-- BEGIN TWITTER BLOCK -->                                                    
                <h2>Latest Tweets</h2>
                <dl class="dl-horizontal f-twitter">
                    <dt><i class="icon-twitter"></i></dt>
                    <dd>
                        <a href="#">@keenthemes</a>
                        Imperdiet condimentum diam dolor lorem sit consectetur adipiscing
                        <span>3 min ago</span>
                    </dd>
                </dl>                    
                <dl class="dl-horizontal f-twitter">
                    <dt><i class="icon-twitter"></i></dt>
                    <dd>
                        <a href="#">@keenthemes</a>
                        Sequat ipsum dolor onec eget orci fermentum condimentum lorem sit consectetur adipiscing
                        <span>8 min ago</span>
                    </dd>
                </dl>                    
                <dl class="dl-horizontal f-twitter">
                    <dt><i class="icon-twitter"></i></dt>
                    <dd>
                        <a href="#">@keenthemes</a>
                        Remonde sequat ipsum dolor lorem sit consectetur adipiscing
                        <span>12 min ago</span>
                    </dd>
                </dl>                    
                <dl class="dl-horizontal f-twitter">
                    <dt><i class="icon-twitter"></i></dt>
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
</div>
<!-- END FOOTER -->

<!-- BEGIN COPYRIGHT -->
<div class="front-copyright">
    <div class="container">
        <div class="row-fluid">
            <div class="span8">
                <p>
                    <span class="margin-right-10">2013 © Metronic. ALL Rights Reserved.</span> 
                    <a href="#">Privacy Policy</a> | <a href="#">Terms of Service</a>
                </p>
            </div>
            <div class="span4">
                <ul class="social-footer">
                    <li><a href="#"><i class="icon-facebook"></i></a></li>
                    <li><a href="#"><i class="icon-google-plus"></i></a></li>
                    <li><a href="#"><i class="icon-dribbble"></i></a></li>
                    <li><a href="#"><i class="icon-linkedin"></i></a></li>
                    <li><a href="#"><i class="icon-twitter"></i></a></li>
                    <li><a href="#"><i class="icon-skype"></i></a></li>
                    <li><a href="#"><i class="icon-github"></i></a></li>
                    <li><a href="#"><i class="icon-youtube"></i></a></li>
                    <li><a href="#"><i class="icon-dropbox"></i></a></li>
                </ul>                
            </div>
        </div>
    </div>
</div>
<!-- END COPYRIGHT -->