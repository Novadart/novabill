'use strict';

angular.module("novabill.invoices", ['novabill.invoices.controllers', 'novabill.constants', 
                                     'ngRoute', 'novabill.utils', 'novabill.notifications'])

.config(['$routeProvider', 'nConstantsProvider', 'nAnalyticsProvider',
         function($routeProvider, nConstantsProvider, nAnalyticsProvider){
	
	nAnalyticsProvider.urlPath(nConstantsProvider.conf.invoicesBaseUrl);
	
	$routeProvider
	
	.when('/', {
		templateUrl: nConstantsProvider.url.htmlFragmentUrl('/invoices.html'),
		controller: 'InvoicesCtrl'
	})
	
	
	.when('/details/:invoiceId', {
		templateUrl: nConstantsProvider.url.htmlFragmentUrl('/invoices-detail.html'),
		controller: 'InvoiceDetailsCtrl'
	})
	
	.when('/new/:clientId', {
		templateUrl: nConstantsProvider.url.htmlFragmentUrl('/invoices-detail.html'),
		controller: 'InvoiceCreateCtrl'
	})
	
	.when('/new/:clientId/clone/:sourceId', {
		templateUrl: nConstantsProvider.url.htmlFragmentUrl('/invoices-detail.html'),
		controller: 'InvoiceCloneInvoiceCtrl'
	})
	
	.when('/from-estimation/:estimationId', {
		templateUrl: nConstantsProvider.url.htmlFragmentUrl('/invoices-detail.html'),
		controller: 'InvoiceFromEstimationCtrl'
	})
	
	.when('/from-transport-document-list/:transportDocumentList', {
		templateUrl: nConstantsProvider.url.htmlFragmentUrl('/invoices-detail.html'),
		controller: 'InvoiceFromTransportDocumentListCtrl'
	})
	
	.otherwise ({
		redirectTo: '/'
	});

}])

.run(['nAnalytics', function(nAnalytics){ }]);
