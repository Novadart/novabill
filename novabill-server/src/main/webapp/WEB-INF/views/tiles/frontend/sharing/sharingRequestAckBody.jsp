<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/" var="homeUrl"></spring:url>

<div class="container" style="margin: 50px auto 100px;">
	<div class="row">
		<div class="col-md-6 col-md-offset-3">
			<div class="well">
				<h4 class="block">Grazie!</h4>
				<p>Stiamo verificando i dati inseriti...</p> 
				<p>Se i dati sono corretti riceverai a breve una email con le istruzioni per continuare.</p>
			</div>
            <div style="text-align: center;">
                <a class="btn btn-lg blue" href="${homeUrl}">Home</a>
            </div>
		</div>
	</div>
</div>