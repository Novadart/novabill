'use strict';

angular.module('novabill.estimations.controllers', ['novabill.utils', 'novabill.directives', 'novabill.directives.dialogs', 'novabill.translations'])


/**
 * ESTIMATIONS PAGE CONTROLLER
 */
.controller('EstimationCtrl', ['$scope', '$location', 'nConstants', 'nSelectClientDialog',
                               function($scope, $location, nConstants, nSelectClientDialog){
	var selectedYear = String(new Date().getFullYear());
	var loadedEstimations = [];
	var PARTITION = 50;
	
	$scope.loadEstimations = function(year) {
		selectedYear = year;
		
		GWT_Server.estimation.getAllInRange(nConstants.conf.businessId, selectedYear, '0', '1000000', {
			onSuccess : function(page){
				$scope.$apply(function(){
					loadedEstimations = page.items;
					$scope.estimations = loadedEstimations.slice(0, 15);
				});
			},

			onFailure : function(error){}
		});
	};
	
	$scope.loadMoreEstimations = function(){
		if($scope.estimations){
			var currentIndex = $scope.estimations.length;
			$scope.estimations = $scope.estimations.concat(loadedEstimations.slice(currentIndex, currentIndex+PARTITION));
		}
	};
	
	$scope.newEstimationClick = function(){
		var instance = nSelectClientDialog.open();
		instance.result.then(
				function (clientId) {
					$location.path('/new/'+clientId);
				},
				function () {
				}
		);
	};
	
	$scope.$on(nConstants.events.ESTIMATION_REMOVED, function(){
		$scope.$apply(function(){
			$scope.estimations= null;
		});
		$scope.loadEstimations(selectedYear);
	});
	
}])




/**
 * ESTIMATION MODIFY PAGE CONTROLLER
 */
.controller('EstimationDetailsCtrl', ['$scope', '$routeParams', '$location', '$translate', 'gwtHook',
                                      function($scope, $routeParams, $location, $translate, gwtHook) {
	$scope.pageTitle = $translate('MODIFY_ESTIMATION');
	
    GWT_UI.showModifyEstimationPage('estimation-details', $routeParams.estimationId, {
    	onSuccess : function(bool){
    	    $scope.$apply(function(){
    	    	$location.path('/');
    	    });  		
    	},
    	onFailure : function(){},
    });
	gwtHook.injectSelectCommodityDialogHook();
}])



/**
 * ESTIMATION CREATE PAGE CONTROLLER
 */
.controller('EstimationCreateCtrl', ['$scope', '$routeParams', '$location', '$translate', 'gwtHook',
                                     function($scope, $routeParams, $location, $translate, gwtHook) {
	$scope.pageTitle = $translate('NEW_ESTIMATION');
	
	GWT_UI.showNewEstimationPage('estimation-details', $routeParams.clientId, {
    	onSuccess : function(bool){
    		$scope.$apply(function(){
    	    	$location.path('/');
    	    });
    	},
    	onFailure : function(){},
    });
	gwtHook.injectSelectCommodityDialogHook();
}])



/**
 * ESTIMATION CLONE PAGE CONTROLLER
 */
.controller('EstimationCloneEstimationCtrl', ['$scope', '$routeParams', '$location', '$translate', 'gwtHook',
                                        function($scope, $routeParams, $location, $translate, gwtHook) {
	$scope.pageTitle = $translate('NEW_ESTIMATION');
	
	GWT_UI.showCloneEstimationPage('estimation-details', $routeParams.clientId, $routeParams.sourceId, {
    	onSuccess : function(bool){
    	    $scope.$apply(function(){
    	    	$location.path('/');
    	    });  		
    	},
    	onFailure : function(){},
    });
	gwtHook.injectSelectCommodityDialogHook();
}]);


