'use strict';

angular.module("novabill.commodities", ['novabill.commodities.controllers','ngRoute'])

.config(['$routeProvider', function($routeProvider){

	$routeProvider

	.when('/', {
		templateUrl: NovabillConf.partialsBaseUrl + '/commodities.html',
		controller: 'CommoditiesCtrl'
	})

	.when('/details/:commodityId', {
		templateUrl: NovabillConf.partialsBaseUrl + '/commodities-detail.html',
		controller: 'CommoditiesDetailsCtrl'
	})

	.otherwise ({
		redirectTo: '/'
	});

}]);
