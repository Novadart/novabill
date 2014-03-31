'use strict';

angular.module('novabill.creditNotes.controllers', 
		['novabill.utils', 'novabill.directives', 'novabill.directives.dialogs', 'novabill.translations'])


/**
 * CREDIT NOTES PAGE CONTROLLER
 */
.controller('CreditNoteCtrl', ['$scope', '$location', 'nConstants', 'nSelectClientDialog', 
                               function($scope, $location, nConstants, nSelectClientDialog){
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
		var instance = nSelectClientDialog.open();
		instance.result.then(
				function (clientId) {
					//workaround to enable scroll
					window.location.assign( nConstants.url.creditNoteNew(clientId) );
					window.location.reload();
//					$location.path('/new/'+clientId);
				},
				function () {
				}
		);
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
.controller('CreditNoteDetailsCtrl', ['$scope', '$routeParams', '$location', '$translate', 'gwtHook',
                                      function($scope, $routeParams, $location, $translate, gwtHook) {
	$scope.pageTitle = $translate('MODIFY_CREDIT_NOTE');
	
    GWT_UI.showModifyCreditNotePage('credit-note-details', $routeParams.creditNoteId, {
    	onSuccess : function(bool){
    	    $scope.$apply(function(){
    	    	$location.path('/');
    	    });  		
    	},
    	onFailure : function(){
			$scope.$apply(function(){
				$location.path('/');
			});
		}
    });
	
	gwtHook.injectSelectCommodityDialogHook();
}])



/**
 * ESTIMATION CREATE PAGE CONTROLLER
 */
.controller('CreditNoteCreateCtrl', ['$scope', '$routeParams', '$location', '$translate', 'gwtHook',
                                     function($scope, $routeParams, $location, $translate, gwtHook) {
	$scope.pageTitle = $translate('NEW_CREDIT_NOTE');
	
	GWT_UI.showNewCreditNotePage('credit-note-details', $routeParams.clientId, {
    	onSuccess : function(bool){
    		$scope.$apply(function(){
    	    	$location.path('/');
    	    });
    	},
    	onFailure : function(){
			$scope.$apply(function(){
				$location.path('/');
			});
		}
    });
	
	gwtHook.injectSelectCommodityDialogHook();
}])


/**
 * ESTIMATION CREATE PAGE CONTROLLER
 */
.controller('CreditNoteFromInvoiceCtrl', ['$scope', '$routeParams', '$location', '$translate', 'gwtHook',
                                          function($scope, $routeParams, $location, $translate, gwtHook) {
	$scope.pageTitle = $translate('NEW_CREDIT_NOTE');
	
	GWT_UI.showFromInvoiceCreditNotePage('credit-note-details', $routeParams.invoiceId, {
    	onSuccess : function(bool){
    		$scope.$apply(function(){
    	    	$location.path('/');
    	    });
    	},
    	onFailure : function(){
			$scope.$apply(function(){
				$location.path('/');
			});
		}
    });
	
	gwtHook.injectSelectCommodityDialogHook();
}]);


