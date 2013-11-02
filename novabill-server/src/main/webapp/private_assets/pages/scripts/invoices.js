angular.module("novabill.invoices", ['novabill.invoices.controllers','ngRoute'])

.config(function($routeProvider){

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
	
	.otherwise ({
		redirectTo: '/'
	});

});
