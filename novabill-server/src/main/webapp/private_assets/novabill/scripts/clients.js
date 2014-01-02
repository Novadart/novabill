'use strict';

angular.module("novabill.clients", ['novabill.clients.controllers','novabill.constants','ngRoute'])

.config(['$routeProvider', 'nConstantsProvider', function($routeProvider, nConstantsProvider){

	$routeProvider

	.when('/', {
		templateUrl: nConstantsProvider.conf.partialsBaseUrl + '/clients.html',
		controller: 'ClientsCtrl'
	})


	.when('/details/:clientId', {
		templateUrl: nConstantsProvider.conf.partialsBaseUrl + '/clients-detail.html',
		controller: 'ClientDetailsCtrl'
	})

	.otherwise ({
		redirectTo: '/'
	});

}]);
