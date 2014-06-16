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
                <div class="col-md-offset-2 col-md-3">
                    <div class="pricing hover-effect">
                        <div class="pricing-head">
                            <h3>1 Anno</h3>
                            <h4><i>€</i>60<i>.00</i></h4>
                        </div>
                        
                        <div class="pricing-footer">
                        <br>
                            <form action="${paypalAction}" method="post">
			                    <input type="hidden" name="cmd" value="_s-xclick">
			                    <input type="hidden" name="hosted_button_id" value="${hostedButtonIDOneYear}">
			                    <input type="image"
			                        src="https://www.sandbox.paypal.com/it_IT/i/btn/btn_buynowCC_LG.gif"
			                        border="0" name="submit"
			                        alt="PayPal - The safer, easier way to pay online!">
			                    <img alt="" border="0" src="https://www.sandbox.paypal.com/it_IT/i/scr/pixel.gif" width="1" height="1">
			                    <input type="hidden" name="return" value="${returnUrl}">
			                    <input type="hidden" name="custom" value="${email}">
			                </form>
                        </div>
                    </div>
                </div>
                <div class="col-md-offset-2 col-md-3">
                    <div class="pricing hover-effect">
                        <div class="pricing-head">
                            <h3>2 Anni</h3>
                            <h4><i>€</i>114<i>.00</i></h4>
                        </div>
                        <div class="pricing-footer">
                        <br>
                            <form action="${paypalAction}" method="post" target="_top">
			                    <input type="hidden" name="cmd" value="_s-xclick">
			                    <input type="hidden" name="hosted_button_id" value="${hostedButtonIDTwoYears}">
			                    <input type="image"
			                        src="https://www.sandbox.paypal.com/it_IT/i/btn/btn_buynowCC_LG.gif"
			                        border="0" name="submit"
			                        alt="PayPal - The safer, easier way to pay online!">
			                    <img alt="" border="0" src="https://www.sandbox.paypal.com/it_IT/i/scr/pixel.gif" width="1" height="1">
			                    <input type="hidden" name="return" value="${returnUrl}">
			                    <input type="hidden" name="custom" value="${email}">
			                </form>
                        </div>
                    </div>
                </div>
		      </div>
                
	
	       </div>
	   </div> 
	   
	   <br><br>
	   
	   <div class="row">
	   
	       <div class="col-md-12">
	           <h4>
                    Seleziona uno dei due piani.<br>
                    Se decidi di aderire al piano da 2 anni potrai beneficiare di uno sconto.
               </h4>
	           
	           <br>
	           
	           <div class="col-md-4">
	               <h4><strong>I vantaggi di diventare utenti Premium</strong></h4>
                   <ul class="pricing-content feats-list list-unstyled">
                        <li><strong>Più modelli di documento selezionabili</strong></li>
                        <li><strong>Statistiche avanzate</strong></li>
                        <li><strong>Invio automatico della fattura al cliente via e-mail</strong></li>
                        <li><strong>Gestione articoli</strong></li>
                        <li><strong>Gestione listini</strong></li>
                        <li><strong>Condivisione dei documenti con il commercialista</strong></li>
                        <li><strong>Priorità nel supporto tecnico</strong></li>
                        <li><strong>Maggiore priorità allo sviluppo di funzionalità suggerite da utenti premium</strong></li>
                        <li><strong>Supporti il nostro lavoro e ci aiuti a fornire un servizio sempre migliore</strong></li>
                    </ul>
               </div>
	       
	           <div class="col-md-4 col-md-offset-1">
	               <h4>Le funzionalità già attive nel piano Standard</h4>
	               <ul class="pricing-content">
	                    <li>Creazione di offerte in numero illimitato</li>
	                    <li>Creazione di fatture in numero illimitato</li>
	                    <li>Creazione di DDT in numero illimitato</li>
	                    <li>Creazione di note di credito in numero illimitato</li>
	                    <li>Gestione di un numero illimitato di clienti</li>
	                    <li>Esportazione dei documenti in PDF</li>
	                    <li>Esportazione dei dati in archivio ZIP</li>
	                    <li>Personalizzazione dei documenti</li>
	                    <li>Gestione dei pagamenti</li>
	                    <li>Backup regolare dei dati</li>
	                </ul>
	           </div>
	           
	       </div>
	       
	   </div>
	   
	   
	   
    </div>
</div>
