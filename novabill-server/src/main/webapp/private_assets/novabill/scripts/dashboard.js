'use strict';

angular.module("novabill.dashboard", ['novabill.dashboard.controllers', 'ngRoute'])

.config(['$routeProvider', function($routeProvider){

	$routeProvider
	
	.when('/', {
		templateUrl: NovabillConf.partialsBaseUrl + '/dashboard.html',
		controller: 'DashboardCtrl'
	})
	
	.otherwise ({
		redirectTo: '/'
	});

}]);
