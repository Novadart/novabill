<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="page-content">
	<div ng-view></div>
	<toaster-container toaster-options="{'time-out': 0, 'position-class': 'toast-top-right', 'close-button':true}"></toaster-container>
</div>

<script type="text/javascript">
var documentYears = {
		invoices : <%=request.getAttribute("invoiceYears")%>,
		estimations : <%=request.getAttribute("estimationYears")%>,
		transportDocuments : <%=request.getAttribute("transportDocumentYears")%>,
		creditNotes : <%=request.getAttribute("creditNoteYears")%>,
};

var invoiceSuffixes = <%=request.getAttribute("invoiceSuffixes")%>;

function onGWTLoaded(){
     angular.bootstrap(angular.element('.page-content'), ['novabill.clients']);
}
</script>