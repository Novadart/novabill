'use strict';

angular.module("novabill.commodities", ['novabill.commodities.controllers','novabill.constants','ngRoute', 'novabill.utils'])

.config(['$routeProvider', 'nConstantsProvider', 'nAnalyticsProvider', 
         function($routeProvider, nConstantsProvider, nAnalyticsProvider){

	nAnalyticsProvider.urlPath(nConstantsProvider.conf.commoditiesBaseUrl);
	
	$routeProvider

	.when('/', {
		templateUrl: nConstantsProvider.conf.partialsBaseUrl + '/commodities.html',
		controller: 'CommoditiesCtrl'
	})

	.when('/details/:commodityId', {
		templateUrl: nConstantsProvider.conf.partialsBaseUrl + '/commodities-detail.html',
		controller: 'CommoditiesDetailsCtrl'
	})

	.otherwise ({
		redirectTo: '/'
	});

}])

.run(['nAnalytics', function(nAnalytics){ }]);
