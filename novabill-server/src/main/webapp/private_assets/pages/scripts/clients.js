angular.module("clients", ['clients.controllers','ngRoute'])

.config(['$routeProvider', function($routeProvider){

	$routeProvider

	.when('/', {
		templateUrl: NovabillConf.partialsBaseUrl + '/clients.html',
		controller: 'ClientsCtrl'
	})


	.when('/details/:clientId', {
		templateUrl: NovabillConf.partialsBaseUrl + '/clients-detail.html',
		controller: 'ClientDetailsCtrl'
	})

	.otherwise ({
		redirectTo: '/'
	});

}]);
