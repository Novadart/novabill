'use strict';

angular.module("novabill.dashboard", ['novabill.dashboard.controllers', 
                                      'novabill.constants', 'ngRoute', 'novabill.utils', 'novabill.notifications'])

.config(['$routeProvider', 'nConstantsProvider', 'nAnalyticsProvider',
         function($routeProvider, nConstantsProvider, nAnalyticsProvider){
	
	nAnalyticsProvider.urlPath(nConstantsProvider.conf.dashboardUrl);
	
	$routeProvider
	
	.when('/', {
		templateUrl: nConstantsProvider.url.htmlFragmentUrl('/dashboard.html'),
		controller: 'DashboardCtrl'
	})
	
	.otherwise ({
		redirectTo: '/'
	});

}])

.run(['nAnalytics', function(nAnalytics){ }]);
