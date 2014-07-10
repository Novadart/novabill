<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="page-content">
	<div ng-view></div>
	<toaster-container toaster-options="{'time-out': 0, 'position-class': 'toast-top-right', 'close-button':true}"></toaster-container>
</div>

<script type="text/javascript">
var documentYears = {
        creditNotes : <%=request.getAttribute("creditNoteYears")%>,
};

function onGWTLoaded(){
    angular.bootstrap(angular.element('.page-content'), ['novabill.creditNotes']);
}
</script>