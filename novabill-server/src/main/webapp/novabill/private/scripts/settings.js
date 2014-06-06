'use strict';

angular.module("novabill.settings", ['novabill.settings.controllers', 'novabill.constants', 'ngRoute', 'novabill.utils'])

.config(['$routeProvider', 'nConstantsProvider', 'nAnalyticsProvider',
         function($routeProvider, nConstantsProvider, nAnalyticsProvider){

	nAnalyticsProvider.urlPath(nConstantsProvider.conf.settingsBaseUrl);
	
	$routeProvider

	.when('/', {
		templateUrl: nConstantsProvider.url.htmlFragmentUrl('/settings.html'),
		controller: 'SettingsCtrl',
		reloadOnSearch : false
	})
	
	.otherwise ({
		redirectTo: '/'
	});

}])

.run(['nAnalytics', function(nAnalytics){ }]);
