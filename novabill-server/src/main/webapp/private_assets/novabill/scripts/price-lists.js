'use strict';

angular.module("novabill.priceLists", ['novabill.priceLists.controllers', 'novabill.constants', 'ngRoute', 'novabill.utils'])

.config(['$routeProvider', 'nConstantsProvider', 'nAnalyticsProvider',
         function($routeProvider, nConstantsProvider, nAnalyticsProvider){

	nAnalyticsProvider.urlPath(nConstantsProvider.conf.priceListsBaseUrl);
	
	$routeProvider

	.when('/', {
		templateUrl: nConstantsProvider.conf.partialsBaseUrl + '/price-lists.html',
		controller: 'PriceListsCtrl'
	})

	.when('/details/:priceListId', {
		templateUrl: nConstantsProvider.conf.partialsBaseUrl + '/price-lists-detail.html',
		controller: 'PriceListsDetailsCtrl'
	})

	.otherwise ({
		redirectTo: '/'
	});

}])

.run(['nAnalytics', function(nAnalytics){ }]);
