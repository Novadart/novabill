<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="page-content">
    <div ng-view></div>
</div>

<script type="text/javascript">
var documentYears = {
        transportDocuments : <%=request.getAttribute("transportDocumentYears")%>,
};

function onGWTLoaded(){
    angular.bootstrap(angular.element('.page-content'), ['novabill.transportDocuments']);
}
</script>