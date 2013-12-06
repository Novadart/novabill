<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="privateAssetsUrl" value="/private_assets" />
<spring:url var="gwtUrl" value="/rpc/rpc.nocache.js" />

<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="${gwtUrl}" type="text/javascript"></script>
<script src="${privateAssetsUrl}/plugins/flot/jquery.flot.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/plugins/flot/jquery.flot.resize.js" type="text/javascript"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="${privateAssetsUrl}/scripts/app.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/scripts/index.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/novabill/scripts/utils.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/novabill/scripts/directives-dialogs.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/novabill/scripts/directives.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/novabill/scripts/dashboard-controllers.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/novabill/scripts/dashboard.js" type="text/javascript"></script>
<!-- END PAGE LEVEL SCRIPTS -->  
<script type="text/javascript">
    jQuery(document).ready(function() {    
       App.init(); // initlayout and core plugins
    });
    
    function onGWTLoaded(){
        angular.bootstrap(document, ['novabill.dashboard']);
        Index.init();
        Index.initCharts(); // init index page's custom scripts
    }
</script>
<!-- END JAVASCRIPTS -->