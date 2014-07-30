<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:url var="changePasswordUrl" value="/private/change-password" />

<div class="page-content">

	<!-- BEGIN CONTAINER -->
	<div class="container login-signup-page" style="margin-bottom: 150px;">
		<div class="row">
			<div class="col-md-6 col-md-offset-3 col-sm-6 col-sm-offset-3">
			    <form:form action="${changePasswordUrl}" modelAttribute="changePassword" cssClass="register-form" method="post" 
			               novalidate="novalidate" style="display: block;">
			        <h3>Modifica la tua Password</h3>
			        <br>
			        <div class="form-group">
                        <label class="control-label visible-ie8 visible-ie9">Vecchia Password *</label>
                        <div class="input-icon">
                            <i class="fa fa-lock"></i> 
                            <form:password path="password" cssClass="form-control password placeholder-no-fix" autocomplete="off" 
                                id="password" placeholder="Vecchia Password" />
                            <span class="text-danger"><form:errors path="password" /> </span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label visible-ie8 visible-ie9">Nuova Password *</label>
                        <div class="input-icon">
                            <i class="fa fa-lock"></i> 
                            <form:password path="newPassword" cssClass="form-control new-password placeholder-no-fix" autocomplete="off" 
                                id="newPassword" placeholder="Nuova Password" 
                                data-toggle="popover" data-placement="left" data-trigger="focus"
                                data-content="La password deve essere lunga almeno 8 caratteri.<br><br><small style='font-style:italic;'><b>Consiglio</b> - per una password sicura includi:<ul><li>caratteri sia maiuscoli che minuscoli</li><li>almeno un numero</li><li>almeno un carattere speciale come : , / % $ ! . # @ *</li></ul></small>"/>
                            <span class="text-danger"><form:errors path="newPassword" /> </span>
                        </div>
                    </div>
                    <script type="text/javascript">
                    $(function(){
                    	$('input.new-password').popover({
                    		html : true
                    	});
                    	
                    	var initialized = false;
                        var input = $("#newPassword");

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
                        <label class="control-label visible-ie8 visible-ie9">Conferma Nuova Password *</label>
                        <div class="input-icon">
                            <i class="fa fa-check"></i> 
                            <form:password path="confirmNewPassword" cssClass="form-control placeholder-no-fix" autocomplete="off" 
                                id="confirmNewPassword" placeholder="Conferma Nuova Password" />
                            <span class="text-danger"><form:errors path="confirmNewPassword" /></span>
                        </div>
                    </div>
                    <div class="form-actions text-center">
                        
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <form:button id="register-submit-btn" class="btn green">
                            Invia <i class="m-icon-swapright m-icon-white"></i>
                        </form:button>
                    </div>
			    
			    
			    </form:form>
			</div>
		</div>
	</div>

</div>
