angular.module('novabill.transportDocuments.controllers', ['novabill.utils', 'novabill.directives'])


/**
 * CREDIT NOTES PAGE CONTROLLER
 */
.controller('TransportDocumentCtrl', ['$scope', '$location', function($scope, $location){
	$scope.loadTransportDocuments = function($scope) {
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
	
	$scope.loadTransportDocuments($scope);
}])




/**
 * ESTIMATION MODIFY PAGE CONTROLLER
 */
.controller('TransportDocumentDetailsCtrl', ['$scope', '$routeParams', '$location', function($scope, $routeParams, $location) {
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
.controller('TransportDocumentCreateCtrl', ['$scope', '$routeParams', '$location', function($scope, $routeParams, $location) {
	GWT_UI.showNewTransportDocumentPage('transport-document-details', $routeParams.clientId, {
    	onSuccess : function(bool){
    		$scope.$apply(function(){
    	    	$location.path('/');
    	    });
    	},
    	onFailure : function(){},
    });
}]);


