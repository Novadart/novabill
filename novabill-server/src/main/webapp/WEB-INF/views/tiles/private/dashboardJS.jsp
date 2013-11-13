<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="privateAssetsUrl" value="/private_assets" />

<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="${privateAssetsUrl}/plugins/jqvmap/jqvmap/jquery.vmap.js" type="text/javascript"></script>   
<script src="${privateAssetsUrl}/plugins/jqvmap/jqvmap/maps/jquery.vmap.russia.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/plugins/jqvmap/jqvmap/maps/jquery.vmap.world.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/plugins/jqvmap/jqvmap/maps/jquery.vmap.europe.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/plugins/jqvmap/jqvmap/maps/jquery.vmap.germany.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/plugins/jqvmap/jqvmap/maps/jquery.vmap.usa.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/plugins/jqvmap/jqvmap/data/jquery.vmap.sampledata.js" type="text/javascript"></script>  
<script src="${privateAssetsUrl}/plugins/flot/jquery.flot.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/plugins/flot/jquery.flot.resize.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/plugins/jquery.pulsate.min.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/plugins/bootstrap-daterangepicker/moment.min.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/plugins/bootstrap-daterangepicker/daterangepicker.js" type="text/javascript"></script>     
<script src="${privateAssetsUrl}/plugins/gritter/js/jquery.gritter.js" type="text/javascript"></script>
<!-- IMPORTANT! fullcalendar depends on jquery-ui-1.10.3.custom.min.js for drag & drop support -->
<script src="${privateAssetsUrl}/plugins/fullcalendar/fullcalendar/fullcalendar.min.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/plugins/jquery-easy-pie-chart/jquery.easy-pie-chart.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/plugins/jquery.sparkline.min.js" type="text/javascript"></script>  
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="${privateAssetsUrl}/scripts/app.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/scripts/index.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/scripts/tasks.js" type="text/javascript"></script>        
<!-- END PAGE LEVEL SCRIPTS -->  
<script>
    jQuery(document).ready(function() {    
       App.init(); // initlayout and core plugins
       Index.init();
       Index.initJQVMAP(); // init index page's custom scripts
       Index.initCalendar(); // init index page's custom scripts
       Index.initCharts(); // init index page's custom scripts
       Index.initChat();
       Index.initMiniCharts();
       Index.initDashboardDaterange();
       Index.initIntro();
       Tasks.initDashboardWidget();
    });
</script>
<!-- END JAVASCRIPTS -->