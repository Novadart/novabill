<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:url var="frontendAssetsUrl" value="/frontend_assets" />
<spring:url var="homeUrl" value="/" />
<spring:url var="passwordRecoveryUrl" value="/password-recovery" />


<script src="${frontendAssetsUrl}/bower_components/zxcvbn/dist/zxcvbn.js"></script>
<script src="${frontendAssetsUrl}/bower_components/pwstrength-bootstrap/dist/pwstrength-bootstrap-1.2.10.min.js"></script>

<style>

    .progress {
        margin-bottom: 0px;
    }

</style>

<div class="container">


    <div class="row">

        <div class="col-md-8 col-md-offset-2 text-center">
            <h3>Creazione Nuova Password</h3>
            <br><br>
        </div>



        <div class="col-md-8 col-md-offset-2">

            <form:form action="${passwordRecoveryUrl}" modelAttribute="forgotPassword" cssClass="form-horizontal" method="post" class="form-horizontal">

                <div class="form-group">
                    <label class="col-sm-4 control-label">Email</label>
                    <div class="col-sm-8">
                        <input class="form-control" readonly="readonly" type="text" value="${forgotPassword.email}" />
                    </div>
                </div>

                <div class="form-group">
                    <label for="register_password" class="col-sm-4 control-label">Password</label>
                    <div class="col-sm-8">
                        <form:password path="password" cssClass="form-control password placeholder-no-fix password-strength" autocomplete="off"
                                       id="register_password" placeholder="Crea una Password"
                                       data-toggle="popover" data-placement="left" data-trigger="focus"
                                       data-content="La password deve essere lunga almeno 8 caratteri.<br><br><small style='font-style:italic;'><b>Consiglio</b> - per una password sicura includi:<ul><li>caratteri sia maiuscoli che minuscoli</li><li>almeno un numero</li><li>almeno un carattere speciale come : , / % $ ! . # @ *</li></ul></small>"/>
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


                        input.pwstrength({
                            common: {
                                minChar: 8,
                                zxcvbn: true
                            },

                            ui: {
                                showVerdictsInsideProgressBar: true,
                                verdicts: ["", "Ok ma debole", "Discretamente Sicura", "Abbastanza Sicura", "Sicura"],
                                scores: [17, 26, 40, 50, 60]
                            }

                        });



                    });
                </script>


                <div class="form-group">
                    <label for="confirmPassword" class="col-sm-4 control-label">Conferma Password</label>
                    <div class="col-sm-8">
                        <form:password path="confirmPassword" cssClass="form-control placeholder-no-fix" autocomplete="off"
                                       id="register_password" placeholder="Conferma Password" />
                        <span class="text-danger"><form:errors path="confirmPassword" /></span>
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