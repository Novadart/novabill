<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="privateAssetsUrl" value="/private_assets" />
<spring:url var="gwtUrl" value="/rpc/rpc.nocache.js" />

<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="${gwtUrl}" type="text/javascript"></script>
<script src="${privateAssetsUrl}/scripts/app.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/novabill/scripts/utils.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/novabill/scripts/price-lists-controllers.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/novabill/scripts/price-lists.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/novabill/scripts/directives.js" type="text/javascript"></script>
<!-- END PAGE LEVEL SCRIPTS -->  

<script type="text/javascript">
jQuery(document).ready(function() {    
   App.init();
});

function onGWTLoaded(){
     angular.bootstrap(document, ['novabill.priceLists']);
}
</script>