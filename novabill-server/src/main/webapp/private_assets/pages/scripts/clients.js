angular.module("clients", ['clients.controllers','ngRoute']).

config(function($routeProvider){

	$routeProvider.
	
	when('/', {
		templateUrl: partialsBaseUrl + '/clients.html',
		controller: 'ClientsCtrl'
	}).
	
	
	when('/:clientId', {
		templateUrl: partialsBaseUrl + '/clients-detail.html',
		controller: 'ClientDetailsCtrl'
	}).
	
	otherwise ({
		redirectTo: '/'
	});

});
