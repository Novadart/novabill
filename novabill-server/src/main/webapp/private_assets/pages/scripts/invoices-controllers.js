'use strict';

angular.module('novabill.invoices.controllers', ['novabill.utils', 'novabill.directives', 'novabill.translations'])


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
.controller('InvoiceDetailsCtrl', ['$scope', '$routeParams', '$location', '$translate', 
                                   function($scope, $routeParams, $location, $translate) {
	$scope.pageTitle = $translate('MODIFY_INVOICE');
	
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
.controller('InvoiceCreateCtrl', ['$scope', '$routeParams', '$location', '$translate',
                                  function($scope, $routeParams, $location, $translate) {
	$scope.pageTitle = $translate('NEW_INVOICE');
	
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
 * INVOICE CREATE FROM ESTIMATION PAGE CONTROLLER
 */
.controller('InvoiceFromEstimationCtrl', ['$scope', '$routeParams', '$location', '$translate',
                                  function($scope, $routeParams, $location, $translate) {
	$scope.pageTitle = $translate('NEW_INVOICE');
	
	GWT_UI.showFromEstimationInvoicePage('invoice-details', $routeParams.estimationId, {
    	onSuccess : function(bool){
    		$scope.$apply(function(){
    	    	$location.path('/');
    	    });
    	},
    	onFailure : function(){},
    });
}])



/**
 * INVOICE CREATE FROM TRANSPORT DOCUMENT PAGE CONTROLLER
 */
.controller('InvoiceFromTransportDocumentCtrl', ['$scope', '$routeParams', '$location', '$translate',
                                  function($scope, $routeParams, $location, $translate) {
	$scope.pageTitle = $translate('NEW_INVOICE');
	
	GWT_UI.showFromTransportDocumentInvoicePage('invoice-details', $routeParams.transportDocumentId, {
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
.controller('InvoiceCloneInvoiceCtrl', ['$scope', '$routeParams', '$location', '$translate',
                                        function($scope, $routeParams, $location, $translate) {
	$scope.pageTitle = $translate('NEW_INVOICE');
	
	GWT_UI.showCloneInvoicePage('invoice-details', $routeParams.clientId, $routeParams.sourceId, {
    	onSuccess : function(bool){
    	    $scope.$apply(function(){
    	    	$location.path('/');
    	    });  		
    	},
    	onFailure : function(){},
    });
}]);

