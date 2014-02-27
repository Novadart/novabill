'use strict';

angular.module("novabill.payments", ['novabill.payments.controllers', 'novabill.constants', 'ngRoute', 'novabill.utils'])

.config(['$routeProvider', 'nConstantsProvider', 'nAnalyticsProvider',
         function($routeProvider, nConstantsProvider, nAnalyticsProvider){

	nAnalyticsProvider.urlPath(nConstantsProvider.conf.paymentsBaseUrl);
	
	$routeProvider

	.when('/', {
		templateUrl: nConstantsProvider.url.htmlFragmentUrl('/payments.html'),
		controller: 'PaymentsCtrl'
	})

	.otherwise ({
		redirectTo: '/'
	});

}])

.run(['nAnalytics', function(nAnalytics){ }]);
