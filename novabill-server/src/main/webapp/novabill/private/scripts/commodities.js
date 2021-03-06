'use strict';

angular.module("novabill.commodities", ['novabill.commodities.controllers','novabill.constants','ngRoute', 
                                        'novabill.utils', 'novabill.notifications'])

.config(['$routeProvider', 'nConstantsProvider', 'nAnalyticsProvider', 
         function($routeProvider, nConstantsProvider, nAnalyticsProvider){

	nAnalyticsProvider.urlPath(nConstantsProvider.conf.commoditiesBaseUrl);
	
	$routeProvider

		.when('/', {
			templateUrl: nConstantsProvider.url.htmlFragmentUrl('/commodities.html'),
			controller: 'CommoditiesCtrl',
			reloadOnSearch: false
		})

		.when('/details/:commodityId', {
			templateUrl: nConstantsProvider.url.htmlFragmentUrl('/commodities-detail.html'),
			controller: 'CommoditiesDetailsCtrl'
		})

		.otherwise({
		redirectTo: '/'
	});

}])

.run(['nAnalytics', function(nAnalytics){ }]);
