<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/" var="homeUrl"/>

<!-- BEGIN CONTAINER -->
<div class="container"> <!-- margin-bottom-40 -->
    <div class="row">


        <div class="col-md-8 col-md-offset-2 text-center">

            <h2>Risorsa non Disponibile</h2>
            <br><br>

        </div>

        <div class="col-md-8 col-md-offset-2">
            <div class="alert alert-warning text-center">
                <h3>404</h3>
                <p>
                    Sembra che ciò che stai cercando non sia più disponibile!<br>
                    <a href="${homeUrl}">Ritorna alla Home</a>
                </p>
            </div>
        </div>

    </div>
</div>
