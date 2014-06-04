<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>

<spring:url var="indexPageUrl" value="/" />
<spring:url value="/frontend_assets" var="frontendAssetsUrl" />

<div class="page-container">
    
        <!-- BEGIN BREADCRUMBS -->   
        <div class="row breadcrumbs margin-bottom-40">
            <div class="container">
                <div class="col-md-4 col-sm-4">
                    <h1>Chi Siamo</h1>
                </div>
                <div class="col-md-8 col-sm-8">
                    <ul class="pull-right breadcrumb">
                        <li><a href="${indexPageUrl}">Home</a></li>
                        <li class="active">Chi Siamo</li>
                    </ul>
                </div>
            </div>
        </div>
        <!-- END BREADCRUMBS -->

        <!-- BEGIN CONTAINER -->   
        <div class="container min-hight">
            <!-- BEGIN ABOUT INFO -->   
            <div class="row margin-bottom-30">
                <!-- BEGIN INFO BLOCK -->               
                <div class="col-md-7 space-mobile">
                    <h2>Vero eos et accusamus</h2>
                    <p>At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi.</p> 
                    <p>Idest laborum et dolorum fuga. Et harum quidem rerum et quas molestias excepturi sint occaecati facilis est et expedita distinctio lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut non libero consectetur adipiscing elit magna. Sed et quam lacus.</p>
                    <!-- BEGIN LISTS -->
                    <div class="row front-lists-v1">
                        <div class="col-md-6">
                            <ul class="list-unstyled margin-bottom-20">
                                <li><i class="fa fa-check"></i> Officia deserunt molliti</li>
                                <li><i class="fa fa-check"></i> Consectetur adipiscing </li>
                                <li><i class="fa fa-check"></i> Deserunt fpicia</li>
                            </ul>
                        </div>
                        <div class="col-md-6">
                            <ul class="list-unstyled">
                                <li><i class="fa fa-check"></i> Officia deserunt molliti</li>
                                <li><i class="fa fa-check"></i> Consectetur adipiscing </li>
                                <li><i class="fa fa-check"></i> Deserunt fpicia</li>
                            </ul>
                        </div>
                    </div>
                    <!-- END LISTS -->
                </div>
                <!-- END INFO BLOCK -->   

                <!-- BEGIN CAROUSEL -->            
                <div class="col-md-5 front-carousel">
                    <div id="myCarousel" class="carousel slide">
                        <!-- Carousel items -->
                        <div class="carousel-inner">
                            <div class="active item">
                                <img src="${frontendAssetsUrl}/img/pics/img2-medium.jpg" alt="">
                                <div class="carousel-caption">
                                    <p>Excepturi sint occaecati cupiditate non provident</p>
                                </div>
                            </div>
                            <div class="item">
                                <img src="${frontendAssetsUrl}/img/pics/img1-medium.jpg" alt="">
                                <div class="carousel-caption">
                                    <p>Ducimus qui blanditiis praesentium voluptatum</p>
                                </div>
                            </div>
                            <div class="item">
                                <img src="${frontendAssetsUrl}/img/pics/img2-medium.jpg" alt="">
                                <div class="carousel-caption">
                                    <p>Ut non libero consectetur adipiscing elit magna</p>
                                </div>
                            </div>
                        </div>
                        <!-- Carousel nav -->
                        <a class="carousel-control left" href="#myCarousel" data-slide="prev">
                            <i class="fa fa-angle-left"></i>
                        </a>
                        <a class="carousel-control right" href="#myCarousel" data-slide="next">
                            <i class="fa fa-angle-right"></i>
                        </a>
                    </div>                
                </div>
                <!-- END CAROUSEL -->             
            </div>
            <!-- END ABOUT INFO -->   
        </div>
        <!-- END CONTAINER -->

