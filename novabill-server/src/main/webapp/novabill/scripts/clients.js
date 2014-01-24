'use strict';

angular.module("novabill.clients", 
		['novabill.clients.controllers', 'novabill.constants', 'ngRoute', 'novabill.utils'])

.config(['$routeProvider', 'nConstantsProvider', 'nAnalyticsProvider', 
         function($routeProvider, nConstantsProvider, nAnalyticsProvider){
	
	nAnalyticsProvider.urlPath(nConstantsProvider.conf.clientsBaseUrl);
	
	$routeProvider

	.when('/', {
		templateUrl: nConstantsProvider.url.htmlFragmentUrl('/clients.html'),
		controller: 'ClientsCtrl'
	})


	.when('/details/:clientId', {
		templateUrl: nConstantsProvider.url.htmlFragmentUrl('/clients-detail.html'),
		controller: 'ClientDetailsCtrl'
	})

	.otherwise ({
		redirectTo: '/'
	});

}])

.run(['nAnalytics', function(nAnalytics){ }]);
