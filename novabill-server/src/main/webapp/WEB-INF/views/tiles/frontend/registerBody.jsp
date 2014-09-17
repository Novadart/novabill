<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:url var="frontendAssetsUrl" value="/frontend_assets" />
<spring:url var="registerUrl" value="/register" />
<spring:url var="homeUrl" value="/" />
<spring:url var="tosUrl" value="/tos-minimal" />

<div class="page-container">

	<!-- BEGIN BREADCRUMBS -->
	<div class="row breadcrumbs margin-bottom-40">
		<div class="container">
			<div class="col-md-4 col-sm-4">
				<h1>Registrazione</h1>
			</div>
			<div class="col-md-8 col-sm-8">
				<ul class="pull-right breadcrumb">
					<li><a href="${homeUrl}">Home</a></li>
					<li class="active">Registrazione</li>
				</ul>
			</div>
		</div>
	</div>
	<!-- END BREADCRUMBS -->

	<!-- BEGIN CONTAINER -->
	<div class="container login-signup-page" style="margin-bottom: 150px;">
		<div class="row">
			<div class="col-md-6 col-md-offset-3 col-sm-6 col-sm-offset-3">
			    <form:form action="${registerUrl}" modelAttribute="registration" cssClass="register-form" method="post" 
			               novalidate="novalidate" style="display: block;">
			        <h3>Inserisci i tuoi dati <small style="margin-left: 10px;">(Dubbi? guarda il <a data-toggle="lightbox" data-width="853" href="https://www.youtube.com/watch?v=a490oulaMX0">video di esempio</a>)</small></h3>
                    <div class="form-group">
                        <!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
                        <label class="control-label visible-ie8 visible-ie9">La tua e-mail</label>
                        <div class="input-icon">
                            <i class="fa fa-envelope"></i> 
                            <form:input path="email" cssClass="form-control placeholder-no-fix" type="text"
                                placeholder="La tua e-mail" />
                            <span class="text-danger"><form:errors path="email" /></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label visible-ie8 visible-ie9">Crea una Password</label>
                        <div class="input-icon">
                            <i class="fa fa-lock"></i> 
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
                    
                    <div class="form-group">
                        <label>
                            <form:checkbox path="agreementAccepted"/> 
                                Accetto i <a data-toggle="modal" data-target="#tosModal" href="javascript:void(0);">
                                Termini di Servizio</a> e 
                                <a href="https://www.iubenda.com/privacy-policy/257554" class="iubenda-light iubenda-embed" 
                                title="Privacy Policy">Privacy Policy</a>
                        </label>
                        <div class="text-danger"><form:errors path="agreementAccepted" /></div>
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

<!-- Modal -->
<div class="modal fade" id="tosModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-wide">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title">Termini di Servizio</h4>
			</div>
			<div class="modal-body">
				<iframe style="width: 100%; height: 100%; border: 1px solid #DDD;" src="${tosUrl}"></iframe>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
$(document).delegate('*[data-toggle="lightbox"]', 'click', function(event) {
    event.preventDefault();
    $(this).ekkoLightbox();
}); 

$('#tosModal').on('show.bs.modal', function () {
    $('#tosModal .modal-body iframe').css('height', $( window ).height()*0.8);
});
</script>

