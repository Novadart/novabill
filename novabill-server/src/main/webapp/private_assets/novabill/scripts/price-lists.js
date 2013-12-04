'use strict';

angular.module("novabill.priceLists", ['novabill.priceLists.controllers','ngRoute'])

.config(['$routeProvider', function($routeProvider){

	$routeProvider

	.when('/', {
		templateUrl: NovabillConf.partialsBaseUrl + '/price-lists.html',
		controller: 'PriceListsCtrl'
	})

	.when('/details/:priceListId', {
		templateUrl: NovabillConf.partialsBaseUrl + '/price-lists-detail.html',
		controller: 'PriceListsDetailsCtrl'
	})

	.otherwise ({
		redirectTo: '/'
	});

}]);
