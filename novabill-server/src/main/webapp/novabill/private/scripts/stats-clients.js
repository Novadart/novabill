'use strict';

angular.module("novabill.stats.clients", ['novabill.stats.controllers', 'novabill.constants', 'ngRoute', 'novabill.utils',
                                     'novabill.notifications'])

.config(['$routeProvider', 'nConstantsProvider', 'nAnalyticsProvider',
         function($routeProvider, nConstantsProvider, nAnalyticsProvider){

	nAnalyticsProvider.urlPath(nConstantsProvider.conf.statsBaseUrl);
	
	$routeProvider

	.when('/', {
		redirectTo: '/0/' + new Date().getFullYear()
	})
	
	.when('/:clientID/:year', {
		templateUrl: nConstantsProvider.url.htmlFragmentUrl('/stats-clients.html'),
		controller: 'StatsClientsCtrl'
	})
	
	.otherwise ({
		redirectTo: '/'
	});

}])

.run(['nAnalytics', function(nAnalytics){ }]);
