<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/frontend_assets" var="frontendAssetsUrl" />

<!-- BEGIN PAGE LEVEL JAVASCRIPTS(REQUIRED ONLY FOR CURRENT PAGE) -->
<script type="text/javascript" src="${frontendAssetsUrl}/plugins/fancybox/source/jquery.fancybox.pack.js"></script>

<script src="http://maps.google.com/maps/api/js?sensor=true" type="text/javascript"></script>
<script src="${frontendAssetsUrl}/plugins/gmaps/gmaps.js" type="text/javascript"></script>
<script src="${frontendAssetsUrl}/scripts/contact-us.js"></script>  
<script src="${frontendAssetsUrl}/scripts/app.js"></script>  
<script type="text/javascript">
    jQuery(document).ready(function() {    
       App.init();
       ContactUs.init();
    });
</script>
<!-- END PAGE LEVEL JAVASCRIPTS -->