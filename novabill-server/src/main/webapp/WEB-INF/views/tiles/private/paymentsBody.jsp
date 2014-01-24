<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<script type="text/javascript">
if(window.ga){
    ga('send', 'pageview');
}

function onGWTLoaded(){
    GWT_UI.showPaymentsPage('payments-page');
}
</script>

<div class="page-content">
	<div class="container-fluid">
		<!-- BEGIN PAGE TITLE & BREADCRUMB-->
		<h3 class="page-title">Tipologie di Pagamento</h3>
		<ul class="breadcrumb">
			<li><i class="fa fa-dashboard"></i> <a href="../">Dashboard</a></li>
			<li><span>Tipologie di Pagamento</span></li>
		</ul>
		<!-- END PAGE TITLE & BREADCRUMB-->
		<div id="payments-page"></div>
	</div>
</div>