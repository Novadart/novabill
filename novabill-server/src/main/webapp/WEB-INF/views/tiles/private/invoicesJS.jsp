<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="privateAssetsUrl" value="/private_assets" />
<spring:url var="gwtUrl" value="/rpc/rpc.nocache.js" />

<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="${gwtUrl}" type="text/javascript"></script>
<script src="${privateAssetsUrl}/scripts/app.js" type="text/javascript"></script>
<!-- END PAGE LEVEL SCRIPTS -->  

<script type="text/javascript">
jQuery(document).ready(function() {    
   App.init();
});

function onGWTLoaded(){
     /* angular.bootstrap(document, ['clients']); */
	GWT_UI.showNewInvoicePage('test','6',{
		onSuccess : function(bool){
			alert(bool);
		},
		onFailure : function(err){}
	});
}
</script>