<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:url var="deleteAccountUrl" value="/private/delete-account" />
<spring:url var="exportDataUrl"
	value="/private/export?${exportClientsParamName}=true&${exportInvoicesParamName}=true&${exportEstimationsParamName}=true&${exportCreditnotesParamName}=true&${exportTransportdocsParamName}=true" />

<div class="page-content">

	<div class="row" style="margin-top: 50px;">
		<div class="col-md-6 col-md-offset-3 col-sm-6 col-sm-offset-3">
		
		  <p class="goodbyeMessage">
            Ci spiace che tu abbia deciso di lasciarci. <br>
            Se vuoi, puoi farci sapere perchè hai deciso di cancellare il tuo account
            utilizzando il pulsante "?" che vedi nell'angolo in basso a sinistra della
            pagina.<br>In ogni caso, <b>grazie</b> per averci dato una
            possibilità
        </p>
        <p class="lastChanceToExport">
          Hai un'ultima possibilità di scaricare i tuoi dati prima che questi vengano definitivamente cancellati dai nostri server.
        </p>
        
        <p class="text-center">
            <a target="_blank" class="btn  blue" href="${exportDataUrl}"><i class="fa fa-cloud-download"></i>Scarica i dati</a>
        </p>

        <br><br><br>

			<form:form action="${deleteAccountUrl}"
				modelAttribute="deleteAccount" method="post">
				<spring:message code="deleteAccount.deleteMessage" />
				<br>
				<div class="form-group">
					<label class="control-label visible-ie8 visible-ie9">Password *</label>
					<div class="input-icon">
						<i class="fa fa-lock"></i>
						<form:password path="password"
							cssClass="form-control password placeholder-no-fix"
							autocomplete="off" id="password" placeholder="Password" />
						<span class="text-danger"><form:errors path="password" />
						</span>
					</div>
				</div>

				<div class="form-actions text-center">
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
					<form:button id="register-submit-btn" class="btn red">
                         <i class="fa fa-trash-o"></i> Elimina il mio account e cancella i dati definitivamente
					</form:button>
				</div>

			</form:form>
		</div>
	</div>
</div>
