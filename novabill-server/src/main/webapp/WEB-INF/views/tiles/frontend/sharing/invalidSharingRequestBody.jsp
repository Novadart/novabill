<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/share-ask" var="shareUrl"></spring:url>

 <!-- BEGIN CONTAINER -->   
<div class="container " style="margin-bottom: 150px; margin-top: 40px;"> <!-- margin-bottom-40 -->
  <div class="row">
    <div class="col-md-8 col-md-offset-2 col-sm-6 col-sm-offset-3">
       <div class="alert alert-info" style="text-align: center;">
            <h2>Questo indirizzo non è più attivo</h2>
            <br>
            <a href="${shareUrl}" class="btn btn-primary">Attiva un nuovo indirizzo di accesso</a>
       </div>
    </div>
  </div>
</div>