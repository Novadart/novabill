'use strict';

angular.module("novabill.commodities", ['novabill.commodities.controllers','ngRoute'])

.config(['$routeProvider', function($routeProvider){

	$routeProvider

	.when('/', {
		templateUrl: NovabillConf.partialsBaseUrl + '/commodities.html',
		controller: 'CommoditiesCtrl'
	})

//	.when('/details/:clientId', {
//		templateUrl: NovabillConf.partialsBaseUrl + '/clients-detail.html',
//		controller: 'ClientDetailsCtrl'
//	})

	.otherwise ({
		redirectTo: '/'
	});

}]);
