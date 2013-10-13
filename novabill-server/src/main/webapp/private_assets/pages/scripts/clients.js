angular.module("clients", ['clients.controllers','ngRoute']).

config(function($routeProvider, $locationProvider){

	$routeProvider.
	
	when('/:clientId', {
		templateUrl: partialsBaseUrl + '/clients-detail.html',
		controller: 'ClientDetailsCtrl'
	});
	
//	otherwise ({
//		redirectTo: '/'
//	});


});
