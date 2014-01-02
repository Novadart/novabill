'use strict';

angular.module("novabill.commodities", ['novabill.commodities.controllers','novabill.constants','ngRoute'])

.config(['$routeProvider', 'nConstantsProvider', function($routeProvider, nConstantsProvider){

	$routeProvider

	.when('/', {
		templateUrl: nConstantsProvider.conf.partialsBaseUrl + '/commodities.html',
		controller: 'CommoditiesCtrl'
	})

	.when('/details/:commodityId', {
		templateUrl: nConstantsProvider.conf.partialsBaseUrl + '/commodities-detail.html',
		controller: 'CommoditiesDetailsCtrl'
	})

	.otherwise ({
		redirectTo: '/'
	});

}]);
