'use strict';

angular.module("novabill.estimations", ['novabill.estimations.controllers','ngRoute'])

.config(function($routeProvider){

	$routeProvider
	
	.when('/', {
		templateUrl: NovabillConf.partialsBaseUrl + '/estimations.html',
		controller: 'EstimationCtrl'
	})
	
	.when('/details/:estimationId', {
		templateUrl: NovabillConf.partialsBaseUrl + '/estimations-detail.html',
		controller: 'EstimationDetailsCtrl'
	})
	
	.when('/new/:clientId', {
		templateUrl: NovabillConf.partialsBaseUrl + '/estimations-detail.html',
		controller: 'EstimationCreateCtrl'
	})
	
	.otherwise ({
		redirectTo: '/'
	});

});
