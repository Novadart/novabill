'use strict';

angular.module("novabill.estimations", ['novabill.estimations.controllers', 'novabill.constants', 'ngRoute', 'novabill.analytics'])

.config(['$routeProvider', 'nConstantsProvider', function($routeProvider, nConstantsProvider){

	$routeProvider
	
	.when('/', {
		templateUrl: nConstantsProvider.conf.partialsBaseUrl + '/estimations.html',
		controller: 'EstimationCtrl'
	})
	
	.when('/details/:estimationId', {
		templateUrl: nConstantsProvider.conf.partialsBaseUrl + '/estimations-detail.html',
		controller: 'EstimationDetailsCtrl'
	})
	
	.when('/new/:clientId', {
		templateUrl: nConstantsProvider.conf.partialsBaseUrl + '/estimations-detail.html',
		controller: 'EstimationCreateCtrl'
	})
	
	.when('/new/:clientId/clone/:sourceId', {
		templateUrl: nConstantsProvider.conf.partialsBaseUrl + '/estimations-detail.html',
		controller: 'EstimationCloneEstimationCtrl'
	})
	
	.otherwise ({
		redirectTo: '/'
	});

}])

.run(['nAnalytics', function(nAnalytics){ }]);
