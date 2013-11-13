<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>

<spring:url var="indexPageUrl" value="/" />

<div class="page-container">
    
        <!-- BEGIN BREADCRUMBS -->   
        <div class="row breadcrumbs margin-bottom-40">
            <div class="container">
                <div class="col-md-4 col-sm-4">
                    <h1>Prices</h1>
                </div>
                <div class="col-md-8 col-sm-8">
                    <ul class="pull-right breadcrumb">
                        <li><a href="index.html">Home</a></li>
                        <li><a href="">Pages</a></li>
                        <li class="active">Prices</li>
                    </ul>
                </div>
            </div>
        </div>
        <!-- END BREADCRUMBS -->

        <!-- BEGIN CONTAINER -->   
        <div class="container min-hight">
            <!-- BEGIN PRICING OPTION1 -->
            <div class="row margin-bottom-40">
                <div class="col-md-3">
                    <div class="pricing">
                        <div class="pricing-head">
                            <h3>Begginer <span>Officia deserunt mollitia</span></h3>
                            <h4><i>$</i>5<i>.49</i> <span>Per Month</span></h4>
                        </div>
                        <ul class="pricing-content list-unstyled">
                            <li><i class="icon-tags"></i> At vero eos</li>
                            <li><i class="icon-asterisk"></i> No Support</li>
                            <li><i class="icon-heart"></i> Fusce condimentum</li>
                            <li><i class="icon-star"></i> Ut non libero</li>
                            <li><i class="icon-shopping-cart"></i> Consecte adiping elit</li>
                        </ul>
                        <div class="pricing-footer">
                            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut non libero magna psum olor .</p>
                            <a href="#" class="btn theme-btn">Sign Up</a>  
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="pricing">
                        <div class="pricing-head">
                            <h3>Pro<span>Officia deserunt mollitia</span></h3>
                            <h4><i>$</i>8<i>.69</i> <span>Per Month</span></h4>
                        </div>
                        <ul class="pricing-content list-unstyled">
                            <li><i class="icon-tags"></i> At vero eos</li>
                            <li><i class="icon-asterisk"></i> No Support</li>
                            <li><i class="icon-heart"></i> Fusce condimentum</li>
                            <li><i class="icon-star"></i> Ut non libero</li>
                            <li><i class="icon-shopping-cart"></i> Consecte adiping elit</li>
                        </ul>
                        <div class="pricing-footer">
                            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut non libero magna psum olor .</p>
                            <a href="#" class="btn theme-btn">Sign Up</a>  
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="pricing pricing-active">
                        <div class="pricing-head pricing-head-active">
                            <h3>Expert <span>Officia deserunt mollitia</span></h3>
                            <h4><i>$</i>13<i>.99</i> <span>Per Month</span></h4>
                        </div>
                        <ul class="pricing-content list-unstyled">
                            <li><i class="icon-tags"></i> At vero eos</li>
                            <li><i class="icon-asterisk"></i> No Support</li>
                            <li><i class="icon-heart"></i> Fusce condimentum</li>
                            <li><i class="icon-star"></i> Ut non libero</li>
                            <li><i class="icon-shopping-cart"></i> Consecte adiping elit</li>
                        </ul>
                        <div class="pricing-footer">
                            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut non libero magna psum olor .</p>
                            <a href="#" class="btn theme-btn">Sign Up</a>  
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="pricing">
                        <div class="pricing-head">
                            <h3>Hi-Tech<span>Officia deserunt mollitia</span></h3>
                            <h4><i>$</i>99<i>.00</i> <span>Per Month</span></h4>
                        </div>
                        <ul class="pricing-content list-unstyled">
                            <li><i class="icon-tags"></i> At vero eos</li>
                            <li><i class="icon-asterisk"></i> No Support</li>
                            <li><i class="icon-heart"></i> Fusce condimentum</li>
                            <li><i class="icon-star"></i> Ut non libero</li>
                            <li><i class="icon-shopping-cart"></i> Consecte adiping elit</li>
                        </ul>
                        <div class="pricing-footer">
                            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut non libero magna psum olor .</p>
                            <a href="#" class="btn theme-btn">Sign Up</a>  
                        </div>
                    </div>
                </div>
            </div>
            <!-- END PRICING OPTION1 -->

            <!-- BEGIN PRICING OPTION2 -->
            <div class="row margin-bottom-40">
                <div class="col-md-3">
                    <div class="pricing-table">
                        <h3>Basic</h3>
                        <div class="desc">
                            Duis mollis, est non commodo luctus, nisi erat 
                            porttitor ligula, eget lacinia.
                        </div>
                        <ul>
                            <li><i class="icon-angle-right"></i> 10 Email Accounts</li>
                            <li><i class="icon-angle-right"></i> 10 User Accounts</li>
                            <li><i class="icon-angle-right"></i> 100 GB Storage</li>
                            <li><i class="icon-angle-right"></i> 24X7 Support</li>
                            <li>&nbsp;</li>
                            <li>&nbsp;</li>
                            <li>&nbsp;</li>
                        </ul>
                        <div class="rate">
                            <div class="price">
                                <div class="currency ">
                                    $<br />
                                    Monthly
                                </div>
                                <div class="amount ">
                                    99    
                                </div>
                            </div>
                            <a href="#" class="btn theme-btn">Sign Up</a>  
                        </div>
                    </div>
                </div>
                <div class="spance10 visible-phone"></div>
                <div class="col-md-3">
                    <div class="pricing-table">
                        <h3>Standard</h3>
                        <div class="desc">
                            Duis mollis, est non commodo luctus, nisi erat 
                            porttitor ligula, eget lacinia.
                        </div>
                        <ul>
                            <li><i class="icon-angle-right"></i> 100 Email Accounts</li>
                            <li><i class="icon-angle-right"></i> 100 User Accounts</li>
                            <li><i class="icon-angle-right"></i> 1 TB Storage</li>
                            <li><i class="icon-angle-right"></i> 24X7 Support</li>
                            <li><i class="icon-angle-right"></i> Full Backup</li>
                            <li><i class="icon-angle-right"></i> Free Setup</li>
                            <li>&nbsp;</li>
                        </ul>
                        <div class="rate">
                            <div class="price">
                                <div class="currency ">
                                    $<br />
                                    Monthly
                                </div>
                                <div class="amount ">
                                    299      
                                </div>
                            </div>
                            <a href="#" class="btn theme-btn">Sign Up</a>  
                        </div>
                    </div>
                </div>
                <div class="spance10 visible-phone"></div>
                <div class="col-md-3">
                    <div class="pricing-table selected">
                        <h3>Professional</h3>
                        <div class="desc">
                            Duis mollis, est non commodo luctus, nisi erat 
                            porttitor ligula, eget lacinia.
                        </div>
                        <ul>
                            <li><i class="icon-angle-right"></i> Unlimited Email Accounts</li>
                            <li><i class="icon-angle-right"></i> Unlimited User Accounts</li>
                            <li><i class="icon-angle-right"></i> 10 TB Storage</li>
                            <li><i class="icon-angle-right"></i> 24X7 Support</li>
                            <li><i class="icon-angle-right"></i> Full Backup</li>
                            <li><i class="icon-angle-right"></i> Free Setup</li>
                            <li><i class="icon-angle-right"></i> Free CDN</li>
                        </ul>
                        <div class="rate">
                            <div class="price">
                                <div class="currency ">
                                    $<br />
                                    Monthly
                                </div>
                                <div class="amount ">
                                    399      
                                </div>
                            </div>
                            <a href="#" class="btn default">Sign Up</a>  
                        </div>
                    </div>
                </div>
                <div class="spance10 visible-phone"></div>
                <div class="col-md-3">
                    <div class="pricing-table">
                        <h3>Enterprise</h3>
                        <div class="desc">
                            Duis mollis, est non commodo luctus, nisi erat 
                            porttitor ligula, eget lacinia.
                        </div>
                        <ul>
                            <li><i class="icon-angle-right"></i> Unlimited Email Accounts</li>
                            <li><i class="icon-angle-right"></i> Unlimited User Accounts</li>
                            <li><i class="icon-angle-right"></i> Unlimited Storage</li>
                            <li><i class="icon-angle-right"></i> 24X7 Support</li>
                            <li><i class="icon-angle-right"></i> Full Backup</li>
                            <li><i class="icon-angle-right"></i> Free Setup</li>
                            <li><i class="icon-angle-right"></i> Free CDN</li>
                        </ul>
                        <div class="rate">
                            <div class="price">
                                <div class="currency ">
                                    $<br />
                                    Monthly
                                </div>
                                <div class="amount ">
                                    999      
                                </div>
                            </div>
                            <a href="#" class="btn theme-btn">Sign Up</a>  
                        </div>
                    </div>
                </div>
            </div>
            <!-- END PRICING OPTION2 -->

            <div class="clearfix margin-bottom-10"></div>

            <!-- BEGIN PRICING OPTION3 -->
            <div class="row margin-bottom-40">
                <!-- BEGIN INLINE NOTIFICATIONS PORTLET-->
                <div class="col-md-3">
                    <div class="pricing-table2">
                        <h3>Basic</h3>
                        <div class="desc">
                            Duis mollis, est non commodo luctus, nisi erat 
                            porttitor ligula, eget lacinia odio sem nec elit.
                        </div>
                        <div class="rate">
                            <div class="price">
                                <div class="currency ">
                                    $<br />
                                    Monthly
                                </div>
                                <div class="amount ">
                                    99    
                                </div>
                            </div>
                            <a href="#" class="btn default">
                            Sign Up <i class="m-icon-swapright"></i>
                            </a>  
                        </div>
                        <ul>
                            <li><i class="icon-angle-right"></i> 10 Email Accounts</li>
                            <li><i class="icon-angle-right"></i> 10 User Accounts</li>
                            <li><i class="icon-angle-right"></i> 100 GB Storage</li>
                            <li><i class="icon-angle-right"></i> 24X7 Support</li>
                            <li>&nbsp;</li>
                            <li>&nbsp;</li>
                            <li>&nbsp;</li>
                        </ul>
                    </div>
                </div>
                <div class="spance10 visible-phone"></div>
                <div class="col-md-3">
                    <div class="pricing-table2">
                        <h3>Standard</h3>
                        <div class="desc">
                            Duis mollis, est non commodo luctus, nisi erat 
                            porttitor ligula, eget lacinia odio sem nec elit.
                        </div>
                        <div class="rate">
                            <div class="price">
                                <div class="currency ">
                                    $<br />
                                    Monthly
                                </div>
                                <div class="amount ">
                                    299      
                                </div>
                            </div>
                            <a href="#" class="btn default">
                            Sign Up <i class="m-icon-swapright"></i>
                            </a>  
                        </div>
                        <ul>
                            <li><i class="icon-angle-right"></i> 100 Email Accounts</li>
                            <li><i class="icon-angle-right"></i> 100 User Accounts</li>
                            <li><i class="icon-angle-right"></i> 1 TB Storage</li>
                            <li><i class="icon-angle-right"></i> 24X7 Support</li>
                            <li><i class="icon-angle-right"></i> Full Backup</li>
                            <li><i class="icon-angle-right"></i> Free Setup</li>
                            <li>&nbsp;</li>
                        </ul>
                    </div>
                </div>
                <div class="spance10 visible-phone"></div>
                <div class="col-md-3">
                    <div class="pricing-table2 selected">
                        <h3>Professional</h3>
                        <div class="desc">
                            Duis mollis, est non commodo luctus, nisi erat 
                            porttitor ligula, eget lacinia odio sem nec elit.
                        </div>
                        <div class="rate">
                            <div class="price">
                                <div class="currency ">
                                    $<br />
                                    Monthly
                                </div>
                                <div class="amount ">
                                    399      
                                </div>
                            </div>
                            <a href="#" class="btn theme-btn">
                            Sign Up <i class="m-icon-swapright m-icon-white"></i>
                            </a>  
                        </div>
                        <ul>
                            <li><i class="icon-angle-right"></i> Unlimited Email Accounts</li>
                            <li><i class="icon-angle-right"></i> Unlimited User Accounts</li>
                            <li><i class="icon-angle-right"></i> 10 TB Storage</li>
                            <li><i class="icon-angle-right"></i> 24X7 Support</li>
                            <li><i class="icon-angle-right"></i> Full Backup</li>
                            <li><i class="icon-angle-right"></i> Free Setup</li>
                            <li><i class="icon-angle-right"></i> Free CDN</li>
                        </ul>
                    </div>
                </div>
                <div class="spance10 visible-phone"></div>
                <div class="col-md-3">
                    <div class="pricing-table2">
                        <h3>Enterprise</h3>
                        <div class="desc">
                            Duis mollis, est non commodo luctus, nisi erat 
                            porttitor ligula, eget lacinia odio sem nec elit.
                        </div>
                        <div class="rate">
                            <div class="price">
                                <div class="currency ">
                                    $<br />
                                    Monthly
                                </div>
                                <div class="amount ">
                                    999      
                                </div>
                            </div>
                            <a href="#" class="btn default">
                            Sign Up <i class="m-icon-swapright"></i>
                            </a>  
                        </div>
                        <ul>
                            <li><i class="icon-angle-right"></i> Unlimited Email Accounts</li>
                            <li><i class="icon-angle-right"></i> Unlimited User Accounts</li>
                            <li><i class="icon-angle-right"></i> Unlimited Storage</li>
                            <li><i class="icon-angle-right"></i> 24X7 Support</li>
                            <li><i class="icon-angle-right"></i> Full Backup</li>
                            <li><i class="icon-angle-right"></i> Free Setup</li>
                            <li><i class="icon-angle-right"></i> Free CDN</li>
                        </ul>
                    </div>
                </div>
                <!-- END INLINE NOTIFICATIONS PORTLET-->
            </div>        
            <!-- END PRICING OPTION3 -->
        </div>
        <!-- END CONTAINER -->

    </div>