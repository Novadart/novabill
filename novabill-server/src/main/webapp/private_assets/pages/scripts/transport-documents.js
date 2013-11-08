'use strict';

angular.module("novabill.transportDocuments", ['novabill.transportDocuments.controllers','ngRoute'])

.config(function($routeProvider){

	$routeProvider
	
	.when('/', {
		templateUrl: NovabillConf.partialsBaseUrl + '/transport-documents.html',
		controller: 'TransportDocumentCtrl'
	})
	
	.when('/details/:transportDocumentId', {
		templateUrl: NovabillConf.partialsBaseUrl + '/transport-documents-detail.html',
		controller: 'TransportDocumentDetailsCtrl'
	})
	
	.when('/new/:clientId', {
		templateUrl: NovabillConf.partialsBaseUrl + '/transport-documents-detail.html',
		controller: 'TransportDocumentCreateCtrl'
	})
	
	.otherwise ({
		redirectTo: '/'
	});

});
