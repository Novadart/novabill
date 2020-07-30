<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:url var="frontendAssetsUrl" value="/frontend_assets" />
<spring:url var="homeUrl" value="/" />
<spring:url var="forgotPasswordUrl" value="/forgot-password" />


<!-- BEGIN CONTAINER -->
<div class="container"> <!-- margin-bottom-40 -->
    <div class="row">


        <div class="col-md-8 col-md-offset-2 text-center">

            <h2>Creazione Nuova Password</h2>
            <p>Inserisci la tua email. Ti invieremo le istruzioni per creare una nuova password.</p>
            <br><br>

        </div>

        <div class="col-md-8 col-md-offset-2">

            <form:form modelAttribute="forgotPassword" cssClass="form-horizontal" action="${forgotPasswordUrl}" method="post" >


                <div class="form-group">
                    <label for="email" class="col-sm-4 control-label">Email</label>
                    <div class="col-sm-8">
                        <form:input path="email" cssClass="form-control" type="email" placeholder="La tua e-mail" />
                        <span class="text-danger"><form:errors path="email" /></span>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-md-12 text-center">
                        <form:button class="btn btn-success">
                            Invia
                        </form:button>
                    </div>
                </div>

            </form:form>
        </div>

    </div>
</div>
