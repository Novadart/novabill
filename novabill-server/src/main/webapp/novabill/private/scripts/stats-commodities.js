'use strict';

angular.module("novabill.stats.commodities", ['novabill.stats.controllers', 'novabill.constants', 'ngRoute', 'novabill.utils',
                                     'novabill.notifications'])

.config(['$routeProvider', 'nConstantsProvider', 'nAnalyticsProvider',
         function($routeProvider, nConstantsProvider, nAnalyticsProvider){

	nAnalyticsProvider.urlPath(nConstantsProvider.conf.statsBaseUrl);
	
	$routeProvider

	.when('/', {
		redirectTo: '/0/' + new Date().getFullYear()
	})
	
	.when('/:commodityID/:year', {
		templateUrl: nConstantsProvider.url.htmlFragmentUrl('/stats-commodities.html'),
		controller: 'StatsCommoditiesCtrl'
	})
	
	.otherwise ({
		redirectTo: '/'
	});

}])

.run(['nAnalytics', function(nAnalytics){ }]);
