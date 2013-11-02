angular.module('novabill.directives', ['novabill.utils'])

/*
 * Invoice widget
 */
.directive('novabillInvoice', ['NRemovalDialogAPI', function factory(NRemovalDialogAPI){
	
	return {
			templateUrl: NovabillConf.partialsBaseUrl+'/directives/novabill-invoice.html',
			scope: { 
				invoice : '=',
				bottomUpMenu : '=',
				onRemove : '&'
			},
			controller : ['$scope', function($scope){
				$scope.openUrl = NovabillConf.invoicesBaseUrl + '#/details/' + $scope.invoice.documentID;
				
				$scope.print = function(){
					GWT_UI.generateInvoicePdf($scope.invoice.documentID);
				};
				
				$scope.remove = function(id){
					NRemovalDialogAPI.init('Delete '+$scope.invoice.documentID+' Invoice?', {
						onOk : function(){
							GWT_Server.invoice.remove({
								onSuccess : function(){
									$scope.onRemove();
								},
								onFailure : function(){}
							});
						},
						
						onCancel : function(){}
					});
					NRemovalDialogAPI.show();
				};
				
				$scope.clone = function(){};
				
				$scope.createCreditNote = function(id){};
			}],
			restrict: 'E',
			replace: true,
	};
	
}])


/*
 * Estimation Widget
 */
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


/*
 * Transport Document Widget
 */
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


/*
 * Credit Note Widget
 */
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
	
})


/*
 * Removal Dialog
 */
.directive('removalDialog', function factory(){
	
	return {
		
		templateUrl: NovabillConf.partialsBaseUrl+'/directives/confirm-removal.html',
		scope: {},
		
		controller : function($scope, NRemovalDialogAPI){
			$scope.api = NRemovalDialogAPI;
			
			$scope.ok = function(){
				NRemovalDialogAPI.hide();
				$scope.api.callback.onOk();
			};
			
			$scope.cancel = function(){
				NRemovalDialogAPI.hide();
				$scope.api.callback.onCancel();
			};
		},
		
		restrict: 'E',
		replace: true,
		
	};
	
})
//APIs
.factory('NRemovalDialogAPI', function(){
	return {
		
		//instance variables
		message : '',
		
		callback : {
			onOk : function(){},
			onCancel : function(){}
		},
		
		//functions
		init : function(message, callback){
			this.message = message;
			this.callback = callback;
		},
		
		show : function(){
			$('#removalDialog').modal('show');
		},
		
		hide : function(){
			$('#removalDialog').modal('hide');
		}
		
	};
});
