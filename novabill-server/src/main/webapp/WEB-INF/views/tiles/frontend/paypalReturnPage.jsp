<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<spring:url var="privateAssetsUrl" value="/private_assets" />
<spring:url var="profilePageUrl" value="/private/settings/#/?tab=profile" />
<spring:url var="homeUrl" value="/" />

<div class="page-container">
  
        <!-- BEGIN BREADCRUMBS -->   
        <div class="row breadcrumbs margin-bottom-40">
            <div class="container">
                <div class="col-md-4 col-sm-4">
                    <h1>Attivazione Premium</h1>
                </div>
                <div class="col-md-8 col-sm-8">
                    <ul class="pull-right breadcrumb">
                        <li><a href="${homeUrl}">Home</a></li>
                        <li class="active">Attivazione Premium</li>
                    </ul>
                </div>
            </div>
        </div>
        <!-- END BREADCRUMBS -->

        <!-- BEGIN CONTAINER -->   
        <div class="container " style="margin-bottom: 150px; margin-top: 50px;"> <!-- margin-bottom-40 -->
          <div class="row">
            <div class="col-md-6 col-md-offset-3 col-sm-6 col-sm-offset-3 alert alert-info">
                 <h3><strong>
                Grazie per aver acquistato la versione Premium di Novabill!
                </strong></h3>
                <br>
                <p style="text-align: justify;">
                    Ti invieremo un'email non appena avremo verificato il completamento dell'acquisto. Puoi verificare lo stato del tuo piano nella <a class="btn btn-sm btn-success" href="${profilePageUrl}">pagina del profilo</a><br><br>
                    In caso di necessità contattaci all'indirizzo <a href="mailto:supporto@novabill.it">supporto@novabill.it</a><br> 
                    Grazie.
                </p>
            </div>
          </div>
        </div>
        
</div>
