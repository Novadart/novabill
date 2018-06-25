'use strict';

angular.module("novabill.creditNotes", ['novabill.creditNotes.controllers','novabill.constants',
                                        'ngRoute', 'novabill.utils', 'novabill.notifications'])

.config(['$routeProvider', 'nConstantsProvider', 'nAnalyticsProvider', 
         function($routeProvider, nConstantsProvider, nAnalyticsProvider){

	nAnalyticsProvider.urlPath(nConstantsProvider.conf.creditNotesBaseUrl);
	
	$routeProvider

		.when('/', {
			templateUrl: nConstantsProvider.url.htmlFragmentUrl('/credit-notes.html'),
			controller: 'CreditNoteCtrl',
			reloadOnSearch: false
		})

		.when('/details/:creditNoteId', {
			templateUrl: nConstantsProvider.url.htmlFragmentUrl('/credit-notes-detail.html'),
			controller: 'CreditNoteDetailsCtrl'
		})

		.when('/new/:clientId', {
			templateUrl: nConstantsProvider.url.htmlFragmentUrl('/credit-notes-detail.html'),
			controller: 'CreditNoteCreateCtrl'
		})

		.when('/from-invoice/:invoiceId', {
			templateUrl: nConstantsProvider.url.htmlFragmentUrl('/credit-notes-detail.html'),
			controller: 'CreditNoteFromInvoiceCtrl'
		})

		.otherwise({
		redirectTo: '/'
	});

}])

.run(['nAnalytics', function(nAnalytics){ }]);
