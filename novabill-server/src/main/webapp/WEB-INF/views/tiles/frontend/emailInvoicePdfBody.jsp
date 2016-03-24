<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:url var="frontendAssetsUrl" value="/frontend_assets" />
<spring:url var="homeUrl" value="/" />
<spring:url var="invoicePdfUrl" value="${pdfUrl}" />

<script type="text/javascript">
    window.skipIEAlert = true;
</script>


<!-- BEGIN CONTAINER -->
<div class="container"> <!-- margin-bottom-40 -->
    <div class="row">


        <div class="col-md-8 col-md-offset-2 text-center">

            <h2>Accesso alla Fattura</h2>
            <br><br>

        </div>

        <div class="col-md-8 col-md-offset-2 text-center">
            <div class="well"><h2>${businessName}</h2></div>
            <p>
                    <%
                        Object pdfUrl = request.getAttribute("pdfUrl");
                        if(pdfUrl != null) {
                        %>
                <br>
                Per scaricare il file PDF della fattura premere il pulsante sottostante
                <br>
                <br>
                <br>
                <a class="btn btn-success btn-lg"  href="${invoicePdfUrl}">Scarica la Fattura</a>
                    <%} else { %>
                <br>
            <p class="alert alert-warning">
                Il documento cui stai cercando di accedere non è disponibile.
            </p>
            <br>
            <a class="btn btn-success" href="${homeUrl}">Home</a>
            <%} %>
            </p>
        </div>

    </div>
</div>

