<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="dashboardUrl" value="/private/" />

<div class="page-content">

    <div class="container-fluid">
        <div class="row">
            <div class="col-md-12">
            
                <!-- BEGIN PAGE TITLE & BREADCRUMB-->
	            <h3 class="page-title">Passa a Premium</h3>
	            <ul class="breadcrumb">
	                <li><i class="fa fa-dashboard"></i> <a href="${dashboardUrl}">Dashboard</a></li>
	                <li><span>Passa a Premium</span></li>
	            </ul>
	            <!-- END PAGE TITLE & BREADCRUMB-->

                <div class="row">
                
                <div class="col-md-12">
	                <h3>Seleziona il piano di tuo interesse</h3>
	                
	                <br>
	                
	                <div class="col-md-offset-2 col-md-3">
	                    <div class="pricing hover-effect">
	                        <div class="pricing-head">
	                            <h3>1 Anno</h3>
	                            <h4><i>€</i>60<i>.00</i> <span>pari a € 5,00 al mese</span></h4>
	                        </div>
	                        
	                        <div class="pricing-footer">
	                        <br>
									<form action="${paypalAction}" method="post" target="_top">
										<input type="hidden" name="cmd" value="_s-xclick"> <input
											type="hidden" name="hosted_button_id" value="${hostedButtonIDOneYear}">
										<input type="image"
											src="https://www.paypalobjects.com/it_IT/IT/i/btn/btn_buynowCC_LG.gif"
											border="0" name="submit"
											alt="PayPal - Il metodo rapido, affidabile e innovativo per pagare e farsi pagare.">
										<img alt="" border="0"
											src="https://www.paypalobjects.com/it_IT/i/scr/pixel.gif"
											width="1" height="1">
                                        <input type="hidden" name="custom" value="${email}">
									</form>
	                        </div>
	                    </div>
	                </div>
	                <div class="col-md-offset-2 col-md-3">
	                    <div class="pricing hover-effect">
	                        <div class="pricing-head">
	                            <span class="label label-danger" style="position: absolute;right: 10px;top: 10px;font-size: larger;"><small>SCONTO!</small></span>
	                            <h3>2 Anni</h3>
	                            <h4><i>€</i>114<i>.00</i><span>pari a € 4,75 al mese</span></h4>
	                        </div>
	                        <div class="pricing-footer">
	                        <br>
									<form action="${paypalAction}"
										method="post" target="_top">
										<input type="hidden" name="cmd" value="_s-xclick"> <input
											type="hidden" name="hosted_button_id" value="${hostedButtonIDTwoYears}">
										<input type="image"
											src="https://www.paypalobjects.com/it_IT/IT/i/btn/btn_buynowCC_LG.gif"
											border="0" name="submit"
											alt="PayPal - Il metodo rapido, affidabile e innovativo per pagare e farsi pagare.">
										<img alt="" border="0"
											src="https://www.paypalobjects.com/it_IT/i/scr/pixel.gif"
											width="1" height="1">
										<input type="hidden" name="custom" value="${email}">
									</form>
	                        </div>
	                    </div>
	                </div>
                </div>
		      </div>
	       </div>
	   </div> 
	   
	   <br><br>
	   
	   <div class="row">
	   
	       <div class="col-md-12">
	           
	           <h3>I benefici dedicati agli utenti Premium</h3>
	           
	           <br>
	           
	           <div class="col-md-10 col-md-offset-1">
	           <!-- BEGIN SERVICE BLOCKS -->               
                    <div class="row margin-bottom-20">
                        <div class="col-md-4 service-box-v1">
                            <div><i class="fa fa-file-o color-grey"></i></div>
                            <h4>Più modelli di documento selezionabili</h4>
                        </div>
                        <div class="col-md-4 service-box-v1">
                            <div><i class="fa fa-bar-chart-o color-grey"></i></div>
                            <h4>Statistiche avanzate</h4>
                        </div>
                         <div class="col-md-4 service-box-v1">
                            <div><i class="fa fa-envelope-o color-grey"></i></div>
                            <h4>Invio automatico della fattura al cliente via e-mail</h4>
                        </div>
                    </div>
                    <div class="row margin-bottom-20">
                        <div class="col-md-4 service-box-v1">
                            <div><i class="fa fa-th color-grey"></i></div>
                            <h4>Gestione articoli</h4>
                        </div>
                        <div class="col-md-4 service-box-v1">
                            <div><i class="fa fa-dollar color-grey"></i></div>
                            <h4>Gestione listini</h4>
                        </div>
                         <div class="col-md-4 service-box-v1">
                            <div><i class="fa fa-group color-grey"></i></div>
                            <h4>Condivisione dei documenti con il commercialista</h4>
                        </div>
                    </div>
                    <div class="row margin-bottom-20">
                        <div class="col-md-4 service-box-v1">
                            <div><i class="fa fa-comments-o color-grey"></i></div>
                            <h4>Priorità nel supporto tecnico</h4>
                        </div>
                        <div class="col-md-4 service-box-v1">
                            <div><i class="fa fa-code color-grey"></i></div>
                            <h4>Maggiore priorità allo sviluppo di funzionalità suggerite da utenti premium</h4>
                        </div>
                         <div class="col-md-4 service-box-v1">
                            <div><i class="fa fa-smile-o color-grey"></i></div>
                            <h4>Supporti il nostro lavoro e ci aiuti a fornire un servizio sempre migliore</h4>
                        </div>
                    </div>
                <!-- END SERVICE BLOCKS --> 
                </div>
	       </div>
	       
	   </div>
	   
	   
	   
    </div>
</div>
