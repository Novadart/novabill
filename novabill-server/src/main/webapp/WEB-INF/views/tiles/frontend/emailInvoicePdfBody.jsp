<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:url var="frontendAssetsUrl" value="/frontend_assets" />
<spring:url var="homeUrl" value="/" />
<spring:url var="invoicePdfUrl" value="/pdf/invoices/${id}?token=${token}" />

<div class="page-container">
  
        <!-- BEGIN BREADCRUMBS -->   
        <div class="row breadcrumbs margin-bottom-40">
            <div class="container">
                <div class="col-md-4 col-sm-4">
                    <h1>Scarica Fattura</h1>
                </div>
                <div class="col-md-8 col-sm-8">
                    <ul class="pull-right breadcrumb">
                        <li><a href="${homeUrl}">Home</a></li>
                        <li class="active">Scarica Fattura</li>
                    </ul>
                </div>
            </div>
        </div>
        <!-- END BREADCRUMBS -->

		Download the pdf <a href="${invoicePdfUrl}">here</a>.

        <!-- BEGIN CONTAINER -->   
<!--         <div class="container " style="margin-bottom: 150px;"> margin-bottom-40 -->
<!--           <div class="row"> -->
<!--             <div class="col-md-6 col-md-offset-3 col-sm-6 col-sm-offset-3 login-signup-page"> -->
<%--                 <form:form modelAttribute="forgotPassword" action="${forgotPasswordUrl}" method="post" >            --%>
<!--                     <h2>Creazione Nuova Password</h2> -->
<!--                     <p>Inserisci la tua email. Ti invieremo le istruzioni per creare una nuova password.</p> -->

<!--                     <div class="input-group"> -->
<!--                         <span class="input-group-addon"><i class="fa fa-envelope"></i></span> -->
<%--                         <form:input path="email" type="text" class="form-control" name="email" placeholder="E-mail"></form:input> --%>
<!--                     </div> -->
<%--                     <span class="text-danger"><form:errors path="email" /> </span>         --%>

<!--                     <div class="row margin-top-20"> -->
<!--                         <div class="col-md-12 col-sm-12 text-center"> -->
<!--                             <button type="submit" class="btn theme-btn">Invia</button>                         -->
<!--                         </div> -->
<!--                     </div> -->

<%--                 </form:form> --%>
<!--             </div> -->
<!--           </div> -->
<!--         </div> -->
        
</div>