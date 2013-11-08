<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/frontend_assets" var="frontendAssetsUrl" />

<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<!-- BEGIN CORE PLUGINS -->
<script src="${frontendAssetsUrl}/plugins/jquery-1.10.1.min.js" type="text/javascript"></script>
<script src="${frontendAssetsUrl}/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
<script src="${frontendAssetsUrl}/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${frontendAssetsUrl}/plugins/back-to-top.js"></script>    
<script type="text/javascript" src="${frontendAssetsUrl}/plugins/fancybox/source/jquery.fancybox.pack.js"></script>
<script type="text/javascript" src="${frontendAssetsUrl}/plugins/hover-dropdown.js"></script>         
<!--[if lt IE 9]>
<script src="${frontendAssetsUrl}/plugins/respond.min.js"></script>  
<![endif]-->   
<!-- END CORE PLUGINS -->
<script src="${frontendAssetsUrl}/scripts/app.js"></script>      
<script type="text/javascript">
    jQuery(document).ready(function() {
        App.init();
                    
    });
</script>
<!-- END JAVASCRIPTS -->