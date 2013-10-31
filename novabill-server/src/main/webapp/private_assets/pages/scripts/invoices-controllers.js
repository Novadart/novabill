angular.module('invoices.controllers', ['utils', 'directives'])


/**
 * INVOICES PAGE CONTROLLER
 */
.controller('InvoicesCtrl', function($scope, Nsorting, $location){
	
	$scope.loadInvoices = function($scope) {
//		GWT_Server.business.getClients(NovabillConf.businessId, {
//			onSuccess : function(data){
//
//			},
//
//			onFailure : function(error){}
//		});
	};
	
	$scope.loadInvoices($scope);
})




/**
 * INVOICE MODIFY PAGE CONTROLLER
 */
.controller('InvoiceModifyCtrl', function($scope, $route, $routeParams, $location) {
	
	

})



/**
 * INVOICE CREATE PAGE CONTROLLER
 */
.controller('InvoiceCreateCtrl', function($scope, $route, $routeParams, $location) {
	
	

});


