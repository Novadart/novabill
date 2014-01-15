'use strict';

angular.module("novabill.transportDocuments", ['novabill.transportDocuments.controllers', 'novabill.constants', 'ngRoute', 'novabill.analytics'])

.config(['$routeProvider', 'nConstantsProvider', function($routeProvider, nConstantsProvider){

	$routeProvider
	
	.when('/', {
		templateUrl: nConstantsProvider.conf.partialsBaseUrl + '/transport-documents.html',
		controller: 'TransportDocumentCtrl'
	})
	
	.when('/details/:transportDocumentId', {
		templateUrl: nConstantsProvider.conf.partialsBaseUrl + '/transport-documents-detail.html',
		controller: 'TransportDocumentDetailsCtrl'
	})
	
	.when('/new/:clientId', {
		templateUrl: nConstantsProvider.conf.partialsBaseUrl + '/transport-documents-detail.html',
		controller: 'TransportDocumentCreateCtrl'
	})
	
	.otherwise ({
		redirectTo: '/'
	});

}])

.run(['nAnalytics', function(nAnalytics){ }]);
