<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<spring:url var="privateAssetsUrl" value="/private_assets" />
<spring:url var="profilePageUrl" value="/private/settings/#/?tab=profile" />
<spring:url var="homeUrl" value="/" />
<spring:url var="dashboardUrl" value="/private/" />



<!-- BEGIN CONTAINER -->
<div class="container"> <!-- margin-bottom-40 -->
    <div class="row">


        <div class="col-md-8 col-md-offset-2 text-center">

            <h2>Attivazione Premium</h2>
            <br><br>

        </div>

        <div class="col-md-8 col-md-offset-2">
            <div class="alert alert-info">
                <h3 class="text-center"><strong>
                    Grazie per aver acquistato la versione Premium di Novabill!
                </strong></h3>
                <br>
                <p style="text-align: justify;">
                    Ti invieremo un'email non appena avremo verificato il completamento dell'acquisto. Puoi verificare lo stato del tuo piano nella <a href="${profilePageUrl}">pagina del profilo</a><br><br>
                    In caso di necessità contattaci all'indirizzo <a href="mailto:info@novabill.it">info@novabill.it</a><br>
                    Grazie.
                </p>
                <br>
                <p class="text-center">
                    <a class="btn btn-success" href="${dashboardUrl}">Continua</a>

                </p>
            </div>
        </div>

    </div>
</div>
