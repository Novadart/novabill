angular.module('invoices.controllers', ['utils', 'directives'])


/**
 * INVOICES PAGE CONTROLLER
 */
.controller('InvoicesCtrl', ['$scope', function($scope){
	$scope.loadInvoices = function($scope) {
		GWT_Server.invoice.getAllInRange(NovabillConf.businessId, '0', '1000000', {
			onSuccess : function(page){
				$scope.$apply(function(){
					$scope.invoices = page.items;
				});
			},

			onFailure : function(error){}
		});
	};
	
	$scope.loadInvoices($scope);
}])




/**
 * INVOICE MODIFY PAGE CONTROLLER
 */
.controller('InvoiceDetailsCtrl', ['$scope', '$routeParams', function($scope, $routeParams) {
    GWT_UI.showModifyInvoicePage('invoice-details', $routeParams.invoiceId, {
    	onSuccess : function(bool){
    	    window.alert(bool ? 'You saved!' : 'You clicked Cancel');  		
    	},
    	onFailure : function(){},
    });
}])



/**
 * INVOICE CREATE PAGE CONTROLLER
 */
.controller('InvoiceCreateCtrl', ['$scope', '$routeParams', function($scope, $routeParams) {
	
	

}]);


