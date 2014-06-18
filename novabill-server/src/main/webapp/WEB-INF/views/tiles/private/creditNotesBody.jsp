<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="page-content">
	<div ng-view></div>
</div>

<script type="text/javascript">
var documentYears = {
        creditNotes : <%=request.getAttribute("creditNoteYears")%>,
};

function onGWTLoaded(){
    angular.bootstrap(angular.element('.page-content'), ['novabill.creditNotes']);
}
</script>