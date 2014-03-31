<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>



<div class="container" style="margin: 50px auto 100px;">
	<div class="row">
		<div class="col-md-6 col-md-offset-3">
			<div class="well">
				<h4 class="block">Condivisione dei Dati</h4>
				<p>Inserire qui il proprio <strong>indirizzo email</strong> e la <strong>partita IVA</strong> dell'azienda che ha autorizzato la condivisione dei propri dati con te.</p>
				<p>Dopo aver verificato la correttezza dei dati, ti verrà inviata una email contenente le istruzioni per accedere ai documenti.</p>
				<p>Grazie</p>
			</div>


			<form:form role="form" modelAttribute="sharingRequest">
				<div class="form-body">

					<div class="form-group">
						<label>Indirizzo Email</label>
						<div class="input-group">
							<span class="input-group-addon"><i class="fa fa-envelope"></i></span>
							<form:input path="email" type="text" class="form-control"
								placeholder="Il proprio indirizzo email"></form:input>
							<form:errors path="email" />
						</div>
					</div>

					<div class="form-group">
						<label>Partita IVA</label>
						<div class="input-group">
							<span class="input-group-addon"><i class="fa fa-building"></i></span>
							<form:input path="vatID" type="text" class="form-control"
								placeholder="Partita IVA dell'azienda"></form:input>
							<form:errors path="vatID" />
						</div>
					</div>
				</div>
				<div class="form-actions" style="text-align: center;">
					<button type="submit" class="btn btn-lg blue">Invia</button>
				</div>

			</form:form>
		</div>
	</div>
</div>