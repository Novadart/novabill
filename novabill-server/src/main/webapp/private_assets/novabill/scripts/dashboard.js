'use strict';

angular.module("novabill.dashboard", ['novabill.dashboard.controllers', 'novabill.constants', 'ngRoute', 'novabill.analytics'])

.config(['$routeProvider', 'nConstantsProvider', function($routeProvider, nConstantsProvider){
	
	$routeProvider
	
	.when('/', {
		templateUrl: nConstantsProvider.conf.partialsBaseUrl + '/dashboard.html',
		controller: 'DashboardCtrl'
	})
	
	.otherwise ({
		redirectTo: '/'
	});

}])

.run(['nAnalytics', function(nAnalytics){ }]);
