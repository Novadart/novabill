<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:url var="frontendAssetsUrl" value="/frontend_assets" />
<spring:url var="homeUrl" value="/" />
<spring:url var="invoicePdfUrl" value="${pdfUrl}" />

<script type="text/javascript">
window.skipIEAlert = true;
</script>

<div class="page-container">
  
        <!-- BEGIN BREADCRUMBS -->   
        <div class="row breadcrumbs margin-bottom-40">
            <div class="container">
                <div class="col-md-4 col-sm-4">
                    <h1>Accesso alla Fattura</h1>
                </div>
                <div class="col-md-8 col-sm-8">
                    <ul class="pull-right breadcrumb">
                        <li><a href="${homeUrl}">Home</a></li>
                        <li class="active">Accesso alla Fattura</li>
                    </ul>
                </div>
            </div>
        </div>
        <!-- END BREADCRUMBS -->

        <!-- BEGIN CONTAINER -->   
        <div class="container " style="margin-bottom: 150px;">
          <div class="row">
            <div class="col-md-6 col-md-offset-3 col-sm-6 col-sm-offset-3 text-center">
                    <h2>${businessName}</h2>
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
        
</div>
