'use strict';

angular.module('novabill.commodities.controllers', ['novabill.directives', 'novabill.translations'])


/**
 * COMMODITIES PAGE CONTROLLER
 */
.controller('CommoditiesCtrl', ['$scope', '$location', function($scope, $location){
	
	$scope.commodities = null;
	
	$scope.loadCommodities = function() {
		GWT_Server.commodity.getAll(NovabillConf.businessId, {
			onSuccess : function(data){
				$scope.$apply(function(){
					$scope.commodities = data;
				});
			},

			onFailure : function(error){}
		});
	};
	
}]);


