<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="frontendAssetsUrl" value="/frontend_assets" />
<spring:url var="homeUrl" value="/" />
<spring:url var="forgotPasswordUrl" value="/forgot-password" />


<!-- BEGIN CONTAINER -->
<div class="container"> <!-- margin-bottom-40 -->
    <div class="row">


        <div class="col-md-8 col-md-offset-2 text-center">

            <h2>Creazione Nuova Password</h2>
            <br><br>

        </div>

        <div class="col-md-8 col-md-offset-2">
            <div class="alert alert-danger text-center">
                Questo link non è più valido. <br>
                Se vuoi creare una nuova password esegui nuovamente la procedura a <a href="${forgotPasswordUrl}">questo indirizzo</a>. Grazie.
            </div>
        </div>

    </div>
</div>
