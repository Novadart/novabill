'use strict';

angular.module("novabill.creditNotes", ['novabill.creditNotes.controllers','novabill.constants','ngRoute', 'novabill.utils'])

.config(['$routeProvider', 'nConstantsProvider', 'nAnalyticsProvider', 
         function($routeProvider, nConstantsProvider, nAnalyticsProvider){

	nAnalyticsProvider.urlPath(nConstantsProvider.conf.creditNotesBaseUrl);
	
	$routeProvider
	
	.when('/', {
		templateUrl: nConstantsProvider.conf.partialsBaseUrl + '/credit-notes.html',
		controller: 'CreditNoteCtrl'
	})
	
	.when('/details/:creditNoteId', {
		templateUrl: nConstantsProvider.conf.partialsBaseUrl + '/credit-notes-detail.html',
		controller: 'CreditNoteDetailsCtrl'
	})
	
	.when('/new/:clientId', {
		templateUrl: nConstantsProvider.conf.partialsBaseUrl + '/credit-notes-detail.html',
		controller: 'CreditNoteCreateCtrl'
	})
	
	.when('/from-invoice/:invoiceId', {
		templateUrl: nConstantsProvider.conf.partialsBaseUrl + '/credit-notes-detail.html',
		controller: 'CreditNoteFromInvoiceCtrl'
	})
	
	.otherwise ({
		redirectTo: '/'
	});

}])

.run(['nAnalytics', function(nAnalytics){ }]);
