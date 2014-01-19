'use strict';

angular.module("novabill.transportDocuments", ['novabill.transportDocuments.controllers', 'novabill.constants', 'ngRoute', 'novabill.utils'])

.config(['$routeProvider', 'nConstantsProvider', 'nAnalyticsProvider',
         function($routeProvider, nConstantsProvider, nAnalyticsProvider){

	nAnalyticsProvider.urlPath(nConstantsProvider.conf.transportDocumentsBaseUrl);
	
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
