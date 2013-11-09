'use strict';

angular.module('novabill.transportDocuments.controllers', ['novabill.utils', 'novabill.directives', 'novabill.translations'])


/**
 * CREDIT NOTES PAGE CONTROLLER
 */
.controller('TransportDocumentCtrl', ['$scope', '$location', function($scope, $location){
	$scope.loadTransportDocuments = function() {
		GWT_Server.transportDocument.getAllInRange(NovabillConf.businessId, '0', '1000000', {
			onSuccess : function(page){
				$scope.$apply(function(){
					$scope.transportDocuments = page.items;
				});
			},

			onFailure : function(error){}
		});
	};
	
	$scope.newTransportDocumentClick = function(){
		GWT_UI.selectClientDialog(NovabillConf.businessId, {
	    	onSuccess : function(clientId){
	    		
	    	    $scope.$apply(function(){
	    	    	$location.path('/new/'+clientId);
	    	    });
	    	    
	    	},
	    	onFailure : function(){},
	    });
	};
	
	$scope.$on('transportDocument.remove', function(){
		$scope.transportDocuments = null;
		$scope.loadTransportDocuments();
	});
	
	$scope.loadTransportDocuments();
}])




/**
 * ESTIMATION MODIFY PAGE CONTROLLER
 */
.controller('TransportDocumentDetailsCtrl', ['$scope', '$routeParams', '$location', '$translate', 
                                             function($scope, $routeParams, $location, $translate) {
	$scope.pageTitle = $translate('MODIFY_TRANSPORT_DOCUMENT');
	
    GWT_UI.showModifyTransportDocumentPage('transport-document-details', $routeParams.transportDocumentId, {
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
.controller('TransportDocumentCreateCtrl', ['$scope', '$routeParams', '$location', '$translate', 
                                            function($scope, $routeParams, $location, $translate) {
	$scope.pageTitle = $translate('NEW_TRANSPORT_DOCUMENT');
	
	GWT_UI.showNewTransportDocumentPage('transport-document-details', $routeParams.clientId, {
    	onSuccess : function(bool){
    		$scope.$apply(function(){
    	    	$location.path('/');
    	    });
    	},
    	onFailure : function(){},
    });
}]);


