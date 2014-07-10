'use strict';

angular.module("novabill.estimations", ['novabill.estimations.controllers', 
                                        'novabill.constants', 'ngRoute', 'novabill.utils', 'novabill.notifications'])

.config(['$routeProvider', 'nConstantsProvider', 'nAnalyticsProvider',
         function($routeProvider, nConstantsProvider, nAnalyticsProvider){

	nAnalyticsProvider.urlPath(nConstantsProvider.conf.estimationsBaseUrl);
	
	$routeProvider
	
	.when('/', {
		templateUrl: nConstantsProvider.url.htmlFragmentUrl('/estimations.html'),
		controller: 'EstimationCtrl'
	})
	
	.when('/details/:estimationId', {
		templateUrl: nConstantsProvider.url.htmlFragmentUrl('/estimations-detail.html'),
		controller: 'EstimationDetailsCtrl'
	})
	
	.when('/new/:clientId', {
		templateUrl: nConstantsProvider.url.htmlFragmentUrl('/estimations-detail.html'),
		controller: 'EstimationCreateCtrl'
	})
	
	.when('/new/:clientId/clone/:sourceId', {
		templateUrl: nConstantsProvider.url.htmlFragmentUrl('/estimations-detail.html'),
		controller: 'EstimationCloneEstimationCtrl'
	})
	
	.otherwise ({
		redirectTo: '/'
	});

}])

.run(['nAnalytics', function(nAnalytics){ }]);
