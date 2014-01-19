<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="privateAssetsUrl" value="/private_assets" />
<spring:url var="gwtUrl" value="/rpc/rpc.nocache.js" />

<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="${gwtUrl}" type="text/javascript"></script>
<script src="${privateAssetsUrl}/plugins/angular-xeditable-0.1.7/js/xeditable.min.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/scripts/app.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/novabill/scripts/commodities-controllers.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/novabill/scripts/commodities.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/novabill/scripts/directives.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/novabill/scripts/directives-dialogs.js" type="text/javascript"></script>
<!-- END PAGE LEVEL SCRIPTS -->  

<script type="text/javascript">

jQuery(document).ready(function() {    
   App.init();
});

function onGWTLoaded(){
     angular.bootstrap(document, ['novabill.commodities']);
}
</script>