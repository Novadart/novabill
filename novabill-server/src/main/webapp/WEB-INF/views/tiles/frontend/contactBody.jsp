<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>

<spring:url var="indexPageUrl" value="/" />

<div class="page-container">
    
        <!-- BEGIN BREADCRUMBS -->   
        <div class="row breadcrumbs">
            <div class="container">
                <div class="col-md-4 col-sm-4">
                    <h1>Contact</h1>
                </div>
                <div class="col-md-8 col-sm-8">
                    <ul class="pull-right breadcrumb">
                        <li><a href="index.html">Home</a></li>
                        <li><a href="">Pages</a></li>
                        <li class="active">Contact</li>
                    </ul>
                </div>
            </div>
        </div>
        <!-- END BREADCRUMBS -->

        <!-- BEGIN GOOGLE MAP -->
<!--         <div class="row">
            <div id="map" class="gmaps margin-bottom-40" style="height:400px;"></div>
        </div> -->
        <!-- END GOOGLE MAP -->

        <!-- BEGIN CONTAINER -->   
        <div class="container min-hight">
            <div class="row">
                <div class="col-md-9 col-sm-9">
                    <h2>Contact Form</h2>
                    <p>Lorem ipsum dolor sit amet, Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat consectetuer adipiscing elit, sed diam nonummy nibh euismod tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.</p>
                    <div class="space20"></div>
                    <!-- BEGIN FORM-->
                    <form action="#" class="horizontal-form margin-bottom-40" role="form">
                        <div class="form-group">
                            <label class="control-label">Name</label>
                            <div class="col-lg-12">
                                <input type="text" class="form-control" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label" >Email <span class="color-red">*</span></label>
                            <div class="col-lg-12">
                                <input type="text" class="form-control" >
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label" >Message</label>
                            <div class="col-lg-12">
                                <textarea class="form-control" rows="8"></textarea>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-default theme-btn"><i class="icon-ok"></i> Send</button>
                        <button type="button" class="btn btn-default">Cancel</button>
                    </form>
                    <!-- END FORM-->                  
                </div>

                <div class="col-md-3 col-sm-3">
                    <h2>Our Contacts</h2>
                    <address>
                        <strong>Loop, Inc.</strong><br>
                        795 Park Ave, Suite 120<br>
                        San Francisco, CA 94107<br>
                        <abbr title="Phone">P:</abbr> (234) 145-1810
                    </address>
                    <address>
                        <strong>Email</strong><br>
                        <a href="mailto:#">info@email.com</a><br>
                        <a href="mailto:#">support@example.com</a>
                    </address>
                    <ul class="social-icons margin-bottom-10">
                        <li><a href="#" data-original-title="facebook" class="facebook"></a></li>
                        <li><a href="#" data-original-title="github" class="github"></a></li>
                        <li><a href="#" data-original-title="Goole Plus" class="googleplus"></a></li>
                        <li><a href="#" data-original-title="linkedin" class="linkedin"></a></li>
                        <li><a href="#" data-original-title="rss" class="rss"></a></li>
                    </ul>

                    <div class="clearfix margin-bottom-30"></div>

                    <h2>About Us</h2>
                    <p>Sediam nonummy nibh euismod tation ullamcorper suscipit</p>
                    <ul class="list-unstyled">
                        <li><i class="fa fa-check"></i> Officia deserunt molliti</li>
                        <li><i class="fa fa-check"></i> Consectetur adipiscing </li>
                        <li><i class="fa fa-check"></i> Deserunt fpicia</li>
                    </ul>                                
                </div>            
            </div>
        </div>
        <!-- END CONTAINER -->

    </div>