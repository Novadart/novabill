'use strict';

angular.module('novabill.transportDocuments.controllers', ['novabill.utils', 'novabill.directives', 'novabill.directives.dialogs', 'novabill.translations', 'novabill.constants'])


/**
 * CREDIT NOTES PAGE CONTROLLER
 */
.controller('TransportDocumentCtrl', ['$scope', '$location', 'nConstants', '$rootScope', function($scope, $location, nConstants, $rootScope){
	$scope.loadTransportDocuments = function() {
		GWT_Server.transportDocument.getAllInRange(NovabillConf.businessId, '2013', '0', '1000000', {
			onSuccess : function(page){
				$scope.$apply(function(){
					$scope.transportDocuments = page.items;
				});
			},

			onFailure : function(error){}
		});
	};
	
	$scope.newTransportDocumentClick = function(){
		$rootScope.$broadcast(nConstants.events.SHOW_SELECT_CLIENT_DIALOG, {
			onOk : function(clientId){
				$location.path('/new/'+clientId);
			},
			onCancel : function(){}
		});
	};
	
	$scope.$on(nConstants.events.TRANSPORT_DOCUMENT_REMOVED, function(){
		$scope.$apply(function(){
			$scope.transportDocuments = null;
		});
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


