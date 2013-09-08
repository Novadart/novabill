<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<!-- BEGIN CORE PLUGINS -->
<script src="frontend/plugins/jquery-1.10.1.min.js" type="text/javascript"></script>
<script src="frontend/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
<script src="frontend/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="frontend/plugins/back-to-top.js"></script>    
<script type="text/javascript" src="frontend/plugins/bxslider/jquery.bxslider.js"></script>
<script type="text/javascript" src="frontend/plugins/fancybox/source/jquery.fancybox.pack.js"></script>
<script type="text/javascript" src="frontend/plugins/hover-dropdown.js"></script>         
<!--[if lt IE 9]>
<script src="frontend/plugins/respond.min.js"></script>  
<![endif]-->   
<!-- END CORE PLUGINS -->
<script src="frontend/scripts/app.js"></script>      
<script type="text/javascript">
    jQuery(document).ready(function() {
        App.init();
                    
        App.initBxSlider();
    });
</script>
<!-- END JAVASCRIPTS -->