angular.module('novabill.invoices.controllers', ['novabill.utils', 'novabill.directives'])


/**
 * INVOICES PAGE CONTROLLER
 */
.controller('InvoicesCtrl', ['$scope', '$location', function($scope, $location){
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
	
	$scope.newInvoiceClick = function(){
		GWT_UI.selectClientDialog(NovabillConf.businessId, {
	    	onSuccess : function(clientId){
	    		
	    	    $scope.$apply(function(){
	    	    	$location.path('/new/'+clientId);
	    	    });
	    	    
	    	},
	    	onFailure : function(){},
	    });
	};
	
	$scope.loadInvoices($scope);
}])




/**
 * INVOICE MODIFY PAGE CONTROLLER
 */
.controller('InvoiceDetailsCtrl', ['$scope', '$routeParams', '$location', function($scope, $routeParams, $location) {
    GWT_UI.showModifyInvoicePage('invoice-details', $routeParams.invoiceId, {
    	onSuccess : function(bool){
    	    $scope.$apply(function(){
    	    	$location.path('/');
    	    });  		
    	},
    	onFailure : function(){},
    });
}])



/**
 * INVOICE CREATE PAGE CONTROLLER
 */
.controller('InvoiceCreateCtrl', ['$scope', '$routeParams', '$location', function($scope, $routeParams, $location) {
	GWT_UI.showNewInvoicePage('invoice-details', $routeParams.clientId, {
    	onSuccess : function(bool){
    		$scope.$apply(function(){
    	    	$location.path('/');
    	    });
    	},
    	onFailure : function(){},
    });
}])



/**
 * INVOICE CLONE PAGE CONTROLLER
 */
.controller('InvoiceCloneInvoiceCtrl', ['$scope', '$routeParams', '$location', function($scope, $routeParams, $location) {
	GWT_UI.showCloneInvoicePage('invoice-details', $routeParams.clientId, $routeParams.sourceId, {
    	onSuccess : function(bool){
    	    $scope.$apply(function(){
    	    	$location.path('/');
    	    });  		
    	},
    	onFailure : function(){},
    });
}]);


