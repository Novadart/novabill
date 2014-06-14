<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:url var="deleteAccountUrl" value="/private/delete-account" />
<spring:url var="exportDataUrl" value="/private/export?${exportClientsParamName}=true&${exportInvoicesParamName}=true&${exportEstimationsParamName}=true&${exportCreditnotesParamName}=true&${exportTransportdocsParamName}=true"/>

<div class="page-content">
	Last chance to export your data! you can do it <a href="${exportDataUrl}">here</a>.
	<div class="row">
		<div class="col-md-6 col-md-offset-3 col-sm-6 col-sm-offset-3">
			<form:form action="${deleteAccountUrl}" modelAttribute="deleteAccount" method="post">
				<h3><spring:message code="deleteAccount.deleteMessage"/></h3>
				<br>
				<div class="form-group">
                     <label class="control-label visible-ie8 visible-ie9">Password *</label>
                     <div class="input-icon">
                         <i class="fa fa-lock"></i> 
                         <form:password path="password" cssClass="form-control password placeholder-no-fix" autocomplete="off" 
                             id="password" placeholder="Password" />
                         <span class="text-danger"><form:errors path="password" /> </span>
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
