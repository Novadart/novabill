<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:url var="frontendAssetsUrl" value="/frontend_assets" />
<spring:url var="homeUrl" value="/" />
<spring:url var="passwordRecoveryUrl" value="/password-recovery" />

<div class="page-container">
  
        <!-- BEGIN BREADCRUMBS -->   
        <div class="row breadcrumbs margin-bottom-40">
            <div class="container">
                <div class="col-md-4 col-sm-4">
                    <h1>Creazione Nuova Password</h1>
                </div>
                <div class="col-md-8 col-sm-8">
                    <ul class="pull-right breadcrumb">
                        <li><a href="${homeUrl}">Home</a></li>
                        <li class="active">Creazione Nuova Password</li>
                    </ul>
                </div>
            </div>
        </div>
        <!-- END BREADCRUMBS -->

        <!-- BEGIN CONTAINER -->   
        <div class="container " style="margin-bottom: 150px;"> <!-- margin-bottom-40 -->
          <div class="row">
            <div class="col-md-6 col-md-offset-3 col-sm-6 col-sm-offset-3 login-signup-page">
                <form:form action="${passwordRecoveryUrl}" modelAttribute="forgotPassword" cssClass="register-form" method="post" 
                           novalidate="novalidate" style="display: block;">
                    <h3>Inserisci i tuoi dati</h3>
                    <div class="form-group">
                        <!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
                        <label class="control-label visible-ie8 visible-ie9">E-mail</label>
                        <div class="input-icon">
                            <i class="fa fa-envelope"></i> 
                            <input class="form-control placeholder-no-fix" readonly="readonly" type="text" value="${forgotPassword.email}" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label visible-ie8 visible-ie9">Password</label>
                        <div class="input-icon">
                            <i class="fa fa-lock"></i> 
                            <form:password path="password" cssClass="form-control placeholder-no-fix password" autocomplete="off" 
                                id="register_password" placeholder="Password"
                                data-toggle="popover" data-placement="left" 
                                data-content="La password deve essere lunga almeno 8 caratteri.<br><br>La password dovrebbe contenere almeno un carattere maiuscolo, un carattere minuscolo, un numero e un carattere speciale come : , / % $ ! . # @ *<br>Scegliere una buona password è fondamentale per la sicurezza dei tuoi dati."/>
                            <span class="text-danger"><form:errors path="password" /> </span>
                        </div>
                    </div>
                    <script type="text/javascript">
                    $(function(){
                        $('input.password').popover({
                            html : true
                        });
                        
                        
                        var initialized = false;
                        var input = $("#register_password");

                        input.keydown(function () {
                            if (initialized === false) {
                                // set base options
                                input.pwstrength({
                                    raisePower: 1.4,
                                    minChar: 8,
                                    verdicts: ["!!", "Ok ma debole", "Discretamente Sicura", "Abbastanza Sicura", "Sicura"],
                                    scores: [17, 26, 40, 50, 60]
                                });

                                // add your own rule to calculate the password strength
                                input.pwstrength("addRule", "demoRule", function (options, word, score) {
                                    return word.match(/[a-z].[0-9]/) && score;
                                }, 10, true);

                                // set as initialized 
                                initialized = true;
                            }
                        });
                    });
                    </script>
                    
                    <div class="form-group">
                        <label class="control-label visible-ie8 visible-ie9">Conferma Password</label>
                        <div class="input-icon">
                            <i class="fa fa-check"></i> 
                            <form:password path="confirmPassword" cssClass="form-control placeholder-no-fix" autocomplete="off" 
                                id="register_password" placeholder="Conferma Password" />
                            <span class="text-danger"><form:errors path="confirmPassword" /></span>
                        </div>
                    </div>
                    
                    <div class="form-actions text-center">
                        <form:button id="register-submit-btn" class="btn green">
                            Invia <i class="m-icon-swapright m-icon-white"></i>
                        </form:button>
                    </div>
                
                
                </form:form>
            </div>
          </div>
        </div>
        
</div>