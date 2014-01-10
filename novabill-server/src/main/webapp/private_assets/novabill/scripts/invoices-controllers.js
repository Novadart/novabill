'use strict';

angular.module('novabill.invoices.controllers', 
		['novabill.utils', 'novabill.directives', 'novabill.directives.dialogs', 'novabill.translations', 'infinite-scroll'])


/**
 * INVOICES PAGE CONTROLLER
 */
.controller('InvoicesCtrl', ['$scope', '$location', 'nConstants', '$rootScope', function($scope, $location, nConstants, $rootScope){
	var selectedYear = String(new Date().getFullYear());
	var loadedInvoices = [];
	var PARTITION = 50;
	
	
	$scope.loadInvoices = function(year) {
		selectedYear = year;
		
		GWT_Server.invoice.getAllInRange(nConstants.conf.businessId, selectedYear, '0', '1000000', {
			onSuccess : function(page){
				$scope.$apply(function(){
					loadedInvoices = page.items;
					$scope.invoices = loadedInvoices.slice(0, 15);
				});
			},

			onFailure : function(error){}
		});
	};
	
	$scope.loadMoreInvoices = function(){
		if($scope.invoices){
			var currentIndex = $scope.invoices.length;
			$scope.invoices = $scope.invoices.concat(loadedInvoices.slice(currentIndex, currentIndex+PARTITION));
		}
	};

	$scope.newInvoiceClick = function(){
		$rootScope.$broadcast(nConstants.events.SHOW_SELECT_CLIENT_DIALOG, {
			onOk : function(clientId){
				$location.path('/new/'+clientId);
			},
			onCancel : function(){}
		});
	};

	$scope.$on(nConstants.events.INVOICE_REMOVED, function(){
		$scope.$apply(function(){
			$scope.invoices= null;
		});
		$scope.loadInvoices(selectedYear);
	});

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
 * INVOICE CREATE FROM TRANSPORT DOCUMENT LIST PAGE CONTROLLER
 */
.controller('InvoiceFromTransportDocumentListCtrl', ['$scope', '$routeParams', '$location', '$translate',
                                                 function($scope, $routeParams, $location, $translate) {
	$scope.pageTitle = $translate('NEW_INVOICE');
	
	GWT_UI.showFromTransportDocumentListInvoicePage('invoice-details', $routeParams.transportDocumentList, {
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


