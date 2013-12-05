'use strict';

angular.module('novabill.estimations.controllers', ['novabill.utils', 'novabill.directives', 'novabill.directives.dialogs', 'novabill.translations'])


/**
 * ESTIMATIONS PAGE CONTROLLER
 */
.controller('EstimationCtrl', ['$scope', '$location', function($scope, $location){
	$scope.loadEstimations = function() {
		GWT_Server.estimation.getAllInRange(NovabillConf.businessId, '2013', '0', '1000000', {
			onSuccess : function(page){
				$scope.$apply(function(){
					$scope.estimations = page.items;
				});
			},

			onFailure : function(error){}
		});
	};
	
	$scope.newEstimationClick = function(){
		GWT_UI.selectClientDialog(NovabillConf.businessId, {
	    	onSuccess : function(clientId){
	    		
	    	    $scope.$apply(function(){
	    	    	$location.path('/new/'+clientId);
	    	    });
	    	    
	    	},
	    	onFailure : function(){},
	    });
	};
	
	$scope.$on('estimation.remove', function(){
		$scope.$apply(function(){
			$scope.estimations= null;
		});
		$scope.loadEstimations();
	});
	
	$scope.loadEstimations();
}])




/**
 * ESTIMATION MODIFY PAGE CONTROLLER
 */
.controller('EstimationDetailsCtrl', ['$scope', '$routeParams', '$location', '$translate',
                                      function($scope, $routeParams, $location, $translate) {
	$scope.pageTitle = $translate('MODIFY_ESTIMATION');
	
    GWT_UI.showModifyEstimationPage('estimation-details', $routeParams.estimationId, {
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
.controller('EstimationCreateCtrl', ['$scope', '$routeParams', '$location', '$translate',
                                     function($scope, $routeParams, $location, $translate) {
	$scope.pageTitle = $translate('NEW_ESTIMATION');
	
	GWT_UI.showNewEstimationPage('estimation-details', $routeParams.clientId, {
    	onSuccess : function(bool){
    		$scope.$apply(function(){
    	    	$location.path('/');
    	    });
    	},
    	onFailure : function(){},
    });
}])



/**
 * ESTIMATION CLONE PAGE CONTROLLER
 */
.controller('EstimationCloneEstimationCtrl', ['$scope', '$routeParams', '$location', '$translate',
                                        function($scope, $routeParams, $location, $translate) {
	$scope.pageTitle = $translate('NEW_ESTIMATION');
	
	GWT_UI.showCloneEstimationPage('estimation-details', $routeParams.clientId, $routeParams.sourceId, {
    	onSuccess : function(bool){
    	    $scope.$apply(function(){
    	    	$location.path('/');
    	    });  		
    	},
    	onFailure : function(){},
    });
}]);


