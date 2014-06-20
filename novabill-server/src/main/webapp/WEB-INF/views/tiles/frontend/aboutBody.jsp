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
                <div class="col-md-12 space-mobile">
                    <div class="row">
	                    <div class="text-center well col-md-8 col-md-offset-2">
	                        <h2>Our mission is to deliver an easy-to-use, secure and robust service for managing your documents</h2>
	                    </div>
                    </div>
                    <div class="row">
                        <div class="col-md-8 col-md-offset-2">
	                        <h2>Why Novabill?</h2>
	                        <p>
		                        Novabill is an online invoicing system tailored for small businesses and professionals.<br>
		                        We started building Novabill to use it internally in our company, with three principles in mind:                       
		                        <div class="row front-lists-v1">
	                                <div class="col-md-12">
		                                <ul class="list-unstyled margin-bottom-20">
		                                    <li><i class="fa fa-check"></i> Ease of use, in fact existing solutions were either too simple or too complex</li>
		                                    <li><i class="fa fa-check"></i> The possibility to access the data from anywhere, at any time</li>
		                                    <li><i class="fa fa-check"></i> Safety and security of the data via continuous backups and using state of the art cryptography</li>
		                                </ul>
		                            </div>
		                        </div>
		                        And this is how the first version of Novabill was born.<br>
		                        We showed it to friends and colleagues and it soon became evident that we were not the only ones that could benefit from a solution like.<br>
		                        They encouraged us to share it with them and that was the moment when we made Novabill public and available to anyone.<br>Since that day more and more professionals and companies are joining us in using Novabill.                     
		                    </p>
		                
		                
		                
		                    <h2>The people behind Novabill</h2>
		                    <p>
                                We are a company based in Padova but with a very international heart and mindset.<br>
                                In fact everything started in Copenhagen where the two founders were doing a MSc in Software Security and since then they decided to work side by side on innovative projects based on web technologies and data analysis.<br>
                                We love Linux, Java and the Open Source world.<br>
                                We enjoy a lot pasta and coffee, in fact Novabill required quite a lot of both to be developed.<br>
                                If you want to know more about us or drop us a line, come to visit us at <a href="http://www.novadart.com">novadart.com</a>.
                            </p> 
	                    </div>
                    </div>
                </div>
                <!-- END INFO BLOCK -->   

            </div>
            <!-- END ABOUT INFO -->   
        </div>
        <!-- END CONTAINER -->

</div>
