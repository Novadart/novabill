angular.module("novabill.creditNotes", ['novabill.creditNotes.controllers','ngRoute'])

.config(function($routeProvider){

	$routeProvider
	
	.when('/', {
		templateUrl: NovabillConf.partialsBaseUrl + '/credit-notes.html',
		controller: 'CreditNoteCtrl'
	})
	
	.when('/details/:creditNoteId', {
		templateUrl: NovabillConf.partialsBaseUrl + '/credit-notes-detail.html',
		controller: 'CreditNoteDetailsCtrl'
	})
	
	.when('/new/:clientId', {
		templateUrl: NovabillConf.partialsBaseUrl + '/credit-notes-detail.html',
		controller: 'CreditNoteCreateCtrl'
	})
	
	.when('/from-invoice/:invoiceId', {
		templateUrl: NovabillConf.partialsBaseUrl + '/credit-notes-detail.html',
		controller: 'CreditNoteFromInvoiceCtrl'
	})
	
	.otherwise ({
		redirectTo: '/'
	});

});
