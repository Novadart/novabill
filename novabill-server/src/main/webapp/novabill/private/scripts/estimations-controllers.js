'use strict';

angular.module('novabill.estimations.controllers', ['novabill.utils', 'novabill.directives', 'novabill.directives.dialogs',
	'novabill.translations', 'infinite-scroll'])


/**
 * ESTIMATIONS PAGE CONTROLLER
 */
.controller('EstimationCtrl', ['$scope', '$location', 'nConstants', 'nSelectClientDialog', '$filter',
                               function($scope, $location, nConstants, nSelectClientDialog, $filter){
	var YEAR_PARAM = 'year';
	var FILTER_QUERY_PARAM = 'filter';
	var loadedEstimations = [];
	var filteredEstimations = [];
	var PARTITION = 50;


	$scope.selectedYear = $location.search()[YEAR_PARAM]? $location.search()[[YEAR_PARAM]]: String(new Date().getFullYear());

	$scope.query = $location.search()[FILTER_QUERY_PARAM]? $location.search()[[FILTER_QUERY_PARAM]]: '';

	function updateFilteredEstimations(){
		filteredEstimations = $filter('filter')(loadedEstimations, $scope.query);
		$scope.estimations = filteredEstimations.slice(0, 15);
	}

	$scope.$watch('query', function(newValue, oldValue){
        $location.search(FILTER_QUERY_PARAM, newValue == ''? null : newValue);
		updateFilteredEstimations();
	});

	$scope.loadEstimations = function(year) {
		$scope.selectedYear = year;
		$location.search(YEAR_PARAM, year);
		$scope.estimations = null;

		GWT_Server.estimation.getAllInRange(nConstants.conf.businessId, $scope.selectedYear, '0', '1000000', {
			onSuccess : function(page){
				$scope.$apply(function(){
					loadedEstimations = page.items;
					updateFilteredEstimations();
				});
			},

			onFailure : function(error){}
		});
	};

	$scope.loadMoreEstimations = function(){
		if($scope.estimations){
			var currentIndex = $scope.estimations.length;
			$scope.estimations = $scope.estimations.concat(filteredEstimations.slice(currentIndex, currentIndex+PARTITION));
		}
	};

	$scope.newEstimationClick = function(){
		var instance = nSelectClientDialog.open(true);
		instance.result.then(
				function (clientId) {
					//workaround to enable scroll
					window.location.assign( nConstants.url.estimationNew(clientId) );
					window.location.reload();
//					$location.path('/new/'+clientId);
				},
				function () {
				}
		);
	};

	$scope.$on(nConstants.events.ESTIMATION_REMOVED, function(){
		$scope.$apply(function(){
			$scope.estimations= null;
		});
		$scope.loadEstimations($scope.selectedYear);
	});

}])




/**
 * ESTIMATION MODIFY PAGE CONTROLLER
 */
.controller('EstimationDetailsCtrl', ['$scope', '$routeParams', '$location', '$translate', 'nSafeHistoryBack',
                                      function($scope, $routeParams, $location, $translate, nSafeHistoryBack) {
	$scope.pageTitle = $translate('MODIFY_ESTIMATION');

	GWT_UI.showModifyEstimationPage('estimation-details', $routeParams.estimationId, {
		onSuccess : function(bool){
			nSafeHistoryBack.safeBack();
		},
		onFailure : function(){
			nSafeHistoryBack.safeBack();
		}
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
			$location.path('/');
		},
		onFailure : function(){
			$location.path('/');
		}
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
			$location.path('/');
		},
		onFailure : function(){
			$location.path('/');
		}
	});

}]);


