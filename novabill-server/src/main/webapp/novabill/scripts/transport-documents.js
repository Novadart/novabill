'use strict';

angular.module("novabill.transportDocuments", ['novabill.transportDocuments.controllers', 'novabill.constants', 'ngRoute', 'novabill.utils'])

.config(['$routeProvider', 'nConstantsProvider', 'nAnalyticsProvider',
         function($routeProvider, nConstantsProvider, nAnalyticsProvider){

	nAnalyticsProvider.urlPath(nConstantsProvider.conf.transportDocumentsBaseUrl);
	
	$routeProvider
	
	.when('/', {
		templateUrl: nConstantsProvider.url.htmlFragmentUrl('/transport-documents.html'),
		controller: 'TransportDocumentCtrl'
	})
	
	.when('/details/:transportDocumentId', {
		templateUrl: nConstantsProvider.url.htmlFragmentUrl('/transport-documents-detail.html'),
		controller: 'TransportDocumentDetailsCtrl'
	})
	
	.when('/from-estimation/:estimationId', {
		templateUrl: nConstantsProvider.url.htmlFragmentUrl('/transport-documents-detail.html'),
		controller: 'TransportDocumentFromEstimationCtrl'
	})
	
	.when('/new/:clientId', {
		templateUrl: nConstantsProvider.url.htmlFragmentUrl('/transport-documents-detail.html'),
		controller: 'TransportDocumentCreateCtrl'
	})
	
	.otherwise ({
		redirectTo: '/'
	});

}])

.run(['nAnalytics', function(nAnalytics){ }]);
