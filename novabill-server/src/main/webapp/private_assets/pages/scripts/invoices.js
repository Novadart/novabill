angular.module("invoices", ['invoices.controllers','ngRoute'])

.config(function($routeProvider){

	$routeProvider
	
	.when('/', {
		templateUrl: NovabillConf.partialsBaseUrl + '/invoices.html',
		controller: 'InvoicesCtrl'
	})
	
	
	.when('/:invoiceId', {
		templateUrl: NovabillConf.partialsBaseUrl + '/invoices-detail.html',
		controller: 'InvoiceModifyCtrl'
	})
	
	.when('/create/:clientId', {
		templateUrl: NovabillConf.partialsBaseUrl + '/invoices-detail.html',
		controller: 'InvoiceCreateCtrl'
	})
	
	.otherwise ({
		redirectTo: '/'
	});

});
