'use strict';

angular.module('novabill.creditNotes.controllers', ['novabill.utils', 'novabill.directives', 'novabill.directives.dialogs', 'novabill.translations'])


/**
 * CREDIT NOTES PAGE CONTROLLER
 */
.controller('CreditNoteCtrl', ['$scope', '$location', 'nConstants', '$rootScope', function($scope, $location, nConstants, $rootScope){
	$scope.loadCreditNotes = function() {
		GWT_Server.creditNote.getAllInRange(nConstants.conf.businessId, '2013', '0', '1000000', {
			onSuccess : function(page){
				$scope.$apply(function(){
					$scope.creditNotes = page.items;
				});
			},

			onFailure : function(error){}
		});
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
		$scope.loadCreditNotes();
	});
	
	$scope.loadCreditNotes();
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


