'use strict';

angular.module('novabill.dashboard.controllers', ['novabill.directives'])


/**
 * DASHBOARD CONTROLLER
 */
.controller('DashboardCtrl', ['$scope', function($scope){

	GWT_Server.business.getStats(NovabillConf.businessId, {
		onSuccess : function(stats){
			$scope.$apply(function(){
				$scope.stats = stats;
			});
		},

		onFailure : function(error){}
	});
	
	
}]);


