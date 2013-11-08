angular.module('novabill.creditNotes.controllers', ['novabill.utils', 'novabill.directives'])


/**
 * CREDIT NOTES PAGE CONTROLLER
 */
.controller('CreditNoteCtrl', ['$scope', '$location', function($scope, $location){
	$scope.loadCreditNotes = function($scope) {
		GWT_Server.creditNote.getAllInRange(NovabillConf.businessId, '0', '1000000', {
			onSuccess : function(page){
				$scope.$apply(function(){
					$scope.creditNotes = page.items;
				});
			},

			onFailure : function(error){}
		});
	};
	
	$scope.newCreditNoteClick = function(){
		GWT_UI.selectClientDialog(NovabillConf.businessId, {
	    	onSuccess : function(clientId){
	    		
	    	    $scope.$apply(function(){
	    	    	$location.path('/new/'+clientId);
	    	    });
	    	    
	    	},
	    	onFailure : function(){},
	    });
	};
	
	$scope.loadCreditNotes($scope);
}])




/**
 * ESTIMATION MODIFY PAGE CONTROLLER
 */
.controller('CreditNoteDetailsCtrl', ['$scope', '$routeParams', '$location', function($scope, $routeParams, $location) {
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
.controller('CreditNoteCreateCtrl', ['$scope', '$routeParams', '$location', function($scope, $routeParams, $location) {
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
.controller('CreditNoteFromInvoiceCtrl', ['$scope', '$routeParams', '$location', function($scope, $routeParams, $location) {
	GWT_UI.showFromInvoiceCreditNotePage('credit-note-details', $routeParams.invoiceId, {
    	onSuccess : function(bool){
    		$scope.$apply(function(){
    	    	$location.path('/');
    	    });
    	},
    	onFailure : function(){},
    });
}]);


