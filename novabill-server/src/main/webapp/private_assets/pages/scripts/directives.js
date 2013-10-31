angular.module('directives', [])

.directive('novabillInvoice', function factory(){
	
	return {
			templateUrl: NovabillConf.partialsBaseUrl+'/directives/novabill-invoice.html',
			scope: { 
				invoice : '=',
				bottomUpMenu : '='
			},
			restrict: 'E',
			replace: true,
	};
	
})


.directive('novabillEstimation', function factory(){
	
	return {
			templateUrl: NovabillConf.partialsBaseUrl+'/directives/novabill-estimation.html',
			scope: { 
				estimation : '=',
				bottomUpMenu : '='
			},
			restrict: 'E',
			replace: true,
	};
	
})


.directive('novabillTransportDocument', function factory(){
	
	return {
			templateUrl: NovabillConf.partialsBaseUrl+'/directives/novabill-transport-document.html',
			scope: { 
				transportDocument : '=',
				bottomUpMenu : '='
			},
			restrict: 'E',
			replace: true,
	};
	
})


.directive('novabillCreditNote', function factory(){
	
	return {
			templateUrl: NovabillConf.partialsBaseUrl+'/directives/novabill-credit-note.html',
			scope: { 
				creditNote : '=',
				bottomUpMenu : '='
			},
			restrict: 'E',
			replace: true,
	};
	
});