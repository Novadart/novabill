'use strict';

angular.module("novabill.priceLists", ['novabill.priceLists.controllers', 'novabill.constants', 
                                       'ngRoute', 'novabill.utils', 'novabill.notifications'])

.config(['$routeProvider', 'nConstantsProvider', 'nAnalyticsProvider',
         function($routeProvider, nConstantsProvider, nAnalyticsProvider){

	nAnalyticsProvider.urlPath(nConstantsProvider.conf.priceListsBaseUrl);
	
	$routeProvider

		.when('/', {
			templateUrl: nConstantsProvider.url.htmlFragmentUrl('/price-lists.html'),
			controller: 'PriceListsCtrl',
			reloadOnSearch: false
		})

		.when('/details/:priceListId', {
			templateUrl: nConstantsProvider.url.htmlFragmentUrl('/price-lists-detail.html'),
			controller: 'PriceListsDetailsCtrl'
		})

		.otherwise({
		redirectTo: '/'
	});

}])

.run(['nAnalytics', function(nAnalytics){ }]);
