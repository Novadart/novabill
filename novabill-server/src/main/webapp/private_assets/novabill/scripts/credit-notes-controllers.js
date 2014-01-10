'use strict';

angular.module('novabill.creditNotes.controllers', ['novabill.utils', 'novabill.directives', 'novabill.directives.dialogs', 'novabill.translations'])


/**
 * CREDIT NOTES PAGE CONTROLLER
 */
.controller('CreditNoteCtrl', ['$scope', '$location', 'nConstants', '$rootScope', 
                               function($scope, $location, nConstants, $rootScope){
	var selectedYear = String(new Date().getFullYear());
	var loadedCreditNotes = [];
	var PARTITION = 50;
	
	$scope.loadCreditNotes = function(year) {
		selectedYear = year;
		
		GWT_Server.creditNote.getAllInRange(nConstants.conf.businessId, selectedYear, '0', '1000000', {
			onSuccess : function(page){
				$scope.$apply(function(){
					loadedCreditNotes = page.items;
					$scope.creditNotes = loadedCreditNotes.slice(0, 15);
				});
			},

			onFailure : function(error){}
		});
	};
	
	$scope.loadMoreCreditNotes = function(){
		if($scope.invoices){
			var currentIndex = $scope.creditNotes.length;
			$scope.creditNotes = $scope.creditNotes.concat(loadedCreditNotes.slice(currentIndex, currentIndex+PARTITION));
		}
	};
	
	$scope.newCreditNoteClick = function(){
		$rootScope.$broadcast(nConstants.events.SHOW_SELECT_CLIENT_DIALOG, {
			onOk : function(clientId){
				$location.path('/new/'+clientId);
			},
			onCancel : function(){}
		});
	};
	
	$scope.$on(nConstants.events.CREDIT_NOTE_REMOVED, function(){
		$scope.$apply(function(){
			$scope.creditNotes = null;
		});
		$scope.loadCreditNotes(selectedYear);
	});
	
}])




/**
 * ESTIMATION MODIFY PAGE CONTROLLER
 */
.controller('CreditNoteDetailsCtrl', ['$scope', '$routeParams', '$location', '$translate',
                                      function($scope, $routeParams, $location, $translate) {
	$scope.pageTitle = $translate('MODIFY_CREDIT_NOTE');
	
    GWT_UI.showModifyCreditNotePage('credit-note-details', $routeParams.creditNoteId, {
    	onSuccess : function(bool){
    	    $scope.$apply(function(){
    	    	$location.path('/');
    	    });  		
    	},
    	onFailure : function(){},
    });
}])



/**
 * ESTIMATION CREATE PAGE CONTROLLER
 */
.controller('CreditNoteCreateCtrl', ['$scope', '$routeParams', '$location', '$translate',
                                     function($scope, $routeParams, $location, $translate) {
	$scope.pageTitle = $translate('NEW_CREDIT_NOTE');
	
	GWT_UI.showNewCreditNotePage('credit-note-details', $routeParams.clientId, {
    	onSuccess : function(bool){
    		$scope.$apply(function(){
    	    	$location.path('/');
    	    });
    	},
    	onFailure : function(){},
    });
}])


/**
 * ESTIMATION CREATE PAGE CONTROLLER
 */
.controller('CreditNoteFromInvoiceCtrl', ['$scope', '$routeParams', '$location', '$translate',
                                          function($scope, $routeParams, $location, $translate) {
	$scope.pageTitle = $translate('NEW_CREDIT_NOTE');
	
	GWT_UI.showFromInvoiceCreditNotePage('credit-note-details', $routeParams.invoiceId, {
    	onSuccess : function(bool){
    		$scope.$apply(function(){
    	    	$location.path('/');
    	    });
    	},
    	onFailure : function(){},
    });
}]);


