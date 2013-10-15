<%@page import="com.novadart.novabill.web.mvc.Urls"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="privateAssetsUrl" value="/private_assets" />
<spring:url var="gwtUrl" value="/rpc/rpc.nocache.js" />
<spring:url var="clientsBaseUrl" value="<%=Urls.PRIVATE_CLIENTS%>" />

<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="${gwtUrl}" type="text/javascript"></script>
<script src="${privateAssetsUrl}/scripts/app.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/pages/scripts/utils.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/pages/scripts/clients-controllers.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/pages/scripts/clients.js" type="text/javascript"></script>
<!-- END PAGE LEVEL SCRIPTS -->  

<script type="text/javascript">
var partialsBaseUrl = '${privateAssetsUrl}/pages/partials/';
var clientsBaseUrl = '${clientsBaseUrl}';

jQuery(document).ready(function() {    
   App.init();
});

function onGWTLoaded(){
     angular.bootstrap(document, ['clients']);
}
</script>