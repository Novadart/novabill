'use strict';

angular.module("novabill.creditNotes", ['novabill.creditNotes.controllers','novabill.constants','ngRoute'])

.config(['$routeProvider', 'nConstantsProvider', function($routeProvider, nConstantsProvider){

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

}]);
