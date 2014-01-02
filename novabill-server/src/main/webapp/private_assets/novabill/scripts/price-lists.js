'use strict';

angular.module("novabill.priceLists", ['novabill.priceLists.controllers', 'novabill.constants', 'ngRoute'])

.config(['$routeProvider', 'nConstantsProvider', function($routeProvider, nConstantsProvider){

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

}]);
