<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="privateAssetsUrl" value="/private_assets" />
<spring:url var="gwtUrl" value="/rpc/rpc.nocache.js" />

<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="${gwtUrl}" type="text/javascript"></script>
<script src="${privateAssetsUrl}/scripts/app.js" type="text/javascript"></script>
<script src="${privateAssetsUrl}/scripts/pages/clients.js" type="text/javascript"></script>
<!-- END PAGE LEVEL SCRIPTS -->  

<script type="text/javascript">
jQuery(document).ready(function() {    
   App.init();
});

var callback = {
		onSuccess : function(data){
			alert(data);
		},
		
		onFailure : function(error){
			
		}
};

function onGWTLoaded(){
	GWT_Server.business.getClients('1',callback);
}
</script>