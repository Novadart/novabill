'use strict';

angular.module("novabill.invoices", ['novabill.invoices.controllers','ngRoute'])

.config(['$routeProvider', function($routeProvider){

	$routeProvider
	
	.when('/', {
		templateUrl: NovabillConf.partialsBaseUrl + '/invoices.html',
		controller: 'InvoicesCtrl'
	})
	
	
	.when('/details/:invoiceId', {
		templateUrl: NovabillConf.partialsBaseUrl + '/invoices-detail.html',
		controller: 'InvoiceDetailsCtrl'
	})
	
	.when('/new/:clientId', {
		templateUrl: NovabillConf.partialsBaseUrl + '/invoices-detail.html',
		controller: 'InvoiceCreateCtrl'
	})
	
	.when('/new/:clientId/clone/:sourceId', {
		templateUrl: NovabillConf.partialsBaseUrl + '/invoices-detail.html',
		controller: 'InvoiceCloneInvoiceCtrl'
	})
	
	.when('/from-estimation/:estimationId', {
		templateUrl: NovabillConf.partialsBaseUrl + '/invoices-detail.html',
		controller: 'InvoiceFromEstimationCtrl'
	})
	
	.when('/from-transport-document/:transportDocumentId', {
		templateUrl: NovabillConf.partialsBaseUrl + '/invoices-detail.html',
		controller: 'InvoiceFromTransportDocumentCtrl'
	})
	
	.when('/from-transport-document-list/:transportDocumentList', {
		templateUrl: NovabillConf.partialsBaseUrl + '/invoices-detail.html',
		controller: 'InvoiceFromTransportDocumentListCtrl'
	})
	
	.otherwise ({
		redirectTo: '/'
	});

}]);
