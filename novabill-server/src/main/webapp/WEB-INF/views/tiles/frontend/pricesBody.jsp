<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>

<spring:url var="indexPageUrl" value="/" />
<spring:url value="/register" var="registerPageUrl"/>

<div class="page-container">
    
        <!-- BEGIN BREADCRUMBS -->   
        <div class="row breadcrumbs margin-bottom-40">
            <div class="container">
                <div class="col-md-4 col-sm-4">
                    <h1>Quanto Costa?</h1>
                </div>
                <div class="col-md-8 col-sm-8">
                    <ul class="pull-right breadcrumb">
                        <li><a href="${indexPageUrl}">Home</a></li>
                        <li class="active">Quanto Costa?</li>
                    </ul>
                </div>
            </div>
        </div>
        <!-- END BREADCRUMBS -->

        <!-- BEGIN CONTAINER -->   
        <div class="container min-hight">
            <!-- BEGIN PRICING OPTION1 -->
            <div class="row margin-bottom-40">
                <div class="col-md-5 col-md-offset-1">
                    <div class="pricing">
                        <div class="pricing-head">
                            <h3>Standard <span>Per aziende o professionisti senza grosse pretese</span></h3>
                            <h4><i>€</i>0<i>,00</i> <span>Al Mese</span></h4>
                        </div>
                        <ul class="pricing-content feats-list list-unstyled">
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
                            <li>Modello di documento non modificabile</li>
                            <li>Statistiche di base</li>
                        </ul>
                        <div class="pricing-footer">
                            <a href="${registerPageUrl}" class="btn green">Registrati</a>  
                        </div>
                    </div>
                </div>
                <div class="col-md-5">
                    <div class="pricing">
                        <div class="pricing-head">
                            <h3>Premium<span>Per aziende o professionisti che necessitano di più servizi</span></h3>
                            <h4><i>€</i>5<i>,00</i> <span>Al Mese</span></h4>
                        </div>
                        <ul class="pricing-content feats-list list-unstyled">
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
                            <li><strong>Più modelli di documento selezionabili</strong></li>
                            <li><strong>Statistiche avanzate</strong></li>
                            <li><strong>Invio automatico della fattura al cliente via e-mail</strong></li>
                            <li><strong>Gestione articoli</strong></li>
                            <li><strong>Gestione listini</strong></li>
                            <li><strong>Condivisione dei documenti con il commercialista</strong></li>
                        </ul>
                        <div class="pricing-footer">
                            <a href="${registerPageUrl}" class="btn green">Registrati</a>  
                        </div>
                    </div>
                </div>
            </div>
            <!-- END PRICING OPTION1 -->
           
        </div>
        <!-- END CONTAINER -->

    </div>