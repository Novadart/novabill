angular.module("clients", ['clients.controllers','ngRoute'])

.config(function($routeProvider){

	$routeProvider
	
	.when('/', {
		templateUrl: NovabillConf.partialsBaseUrl + '/clients.html',
		controller: 'ClientsCtrl'
	})
	
	
	.when('/:clientId', {
		templateUrl: NovabillConf.partialsBaseUrl + '/clients-detail.html',
		controller: 'ClientDetailsCtrl'
	})
	
	.otherwise ({
		redirectTo: '/'
	});

});
