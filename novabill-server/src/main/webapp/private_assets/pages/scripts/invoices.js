angular.module("invoices", ['invoices.controllers','ngRoute'])

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
	
	.when('/create/:clientId', {
		templateUrl: NovabillConf.partialsBaseUrl + '/invoices-create.html',
		controller: 'InvoiceCreateCtrl'
	})
	
	.otherwise ({
		redirectTo: '/'
	});

});
