'use strict';

angular.module("novabill.stats", ['novabill.stats.controllers', 'novabill.constants', 'ngRoute', 'novabill.utils',
                                     'novabill.notifications'])

.config(['$routeProvider', 'nConstantsProvider', 'nAnalyticsProvider',
         function($routeProvider, nConstantsProvider, nAnalyticsProvider){

	nAnalyticsProvider.urlPath(nConstantsProvider.conf.statsBaseUrl);
	
	$routeProvider

	.when('/', {
		templateUrl: nConstantsProvider.url.htmlFragmentUrl('/stats.html'),
		controller: 'StatsCtrl',
		reloadOnSearch : false
	})
	
	.otherwise ({
		redirectTo: '/'
	});

}])

.run(['nAnalytics', function(nAnalytics){ }]);
