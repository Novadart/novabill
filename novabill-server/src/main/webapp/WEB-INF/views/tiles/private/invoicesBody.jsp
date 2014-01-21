<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="page-content">
    <div ng-view></div>
</div>

<script type="text/javascript">
var documentYears = {
        invoices : <%=request.getAttribute("invoiceYears")%>,
};

function onGWTLoaded(){
    angular.bootstrap(document, ['novabill.invoices']);
}
</script>