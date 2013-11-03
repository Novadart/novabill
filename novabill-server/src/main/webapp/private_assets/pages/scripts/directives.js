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
			$scope.openUrl = NovabillConf.invoicesBaseUrl + '#/details/' + $scope.invoice.id;

			$scope.print = function(){
				GWT_UI.generateInvoicePdf($scope.invoice.id);
			};

			$scope.remove = function(id){
				NRemovalDialogAPI.init('Delete '+$scope.invoice.documentID+' Invoice?', {
					onOk : function(){
						GWT_Server.invoice.remove($scope.invoice.id, {
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
.directive('novabillEstimation', ['NRemovalDialogAPI', function factory(NRemovalDialogAPI){

	return {
		templateUrl: NovabillConf.partialsBaseUrl+'/directives/novabill-estimation.html',
		scope: { 
			estimation : '=',
			bottomUpMenu : '=',
			onRemove : '&'
		},
		controller : ['$scope', function($scope){
			$scope.openUrl = NovabillConf.estimationsBaseUrl + '#/details/' + $scope.estimation.id;

			$scope.print = function(){
				GWT_UI.generateEstimationPdf($scope.estimation.id);
			};

			$scope.remove = function(id){
				NRemovalDialogAPI.init('Delete '+$scope.estimation.documentID+' Estimation?', {
					onOk : function(){
						GWT_Server.estimation.remove($scope.estimation.id, {
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

			$scope.convertToInvoice = function(id){};

		}],
		restrict: 'E',
		replace: true,
	};

}])


/*
 * Transport Document Widget
 */
.directive('novabillTransportDocument', ['NRemovalDialogAPI', function factory(NRemovalDialogAPI){

	return {
		templateUrl: NovabillConf.partialsBaseUrl+'/directives/novabill-transport-document.html',
		scope: { 
			transportDocument : '=',
			bottomUpMenu : '=',
			onRemove : '&'
		},
		controller : ['$scope', function($scope){
			$scope.openUrl = NovabillConf.transportDocumentsBaseUrl + '#/details/' + $scope.transportDocument.id;

			$scope.print = function(){
				GWT_UI.generateTransportDocumentPdf($scope.transportDocument.id);
			};

			$scope.remove = function(id){
				NRemovalDialogAPI.init('Delete '+$scope.transportDocument.documentID+' Transport Document?', {
					onOk : function(){
						GWT_Server.transportDocument.remove($scope.transportDocument.id, {
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

		}],
		restrict: 'E',
		replace: true,
	};

}])


/*
 * Credit Note Widget
 */
.directive('novabillCreditNote', ['NRemovalDialogAPI', function factory(NRemovalDialogAPI){

	return {
		templateUrl: NovabillConf.partialsBaseUrl+'/directives/novabill-credit-note.html',
		scope: { 
			creditNote : '=',
			bottomUpMenu : '=',
			onRemove : '&'
		},
		controller : ['$scope', function($scope){
			$scope.openUrl = NovabillConf.creditNotesBaseUrl + '#/details/' + $scope.creditNote.id;

			$scope.print = function(){
				GWT_UI.generateCreditNotePdf($scope.creditNote.id);
			};

			$scope.remove = function(id){
				NRemovalDialogAPI.init('Delete '+$scope.creditNote.documentID+' Credit Note?', {
					onOk : function(){
						GWT_Server.creditNote.remove($scope.creditNote.id, {
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

		}],
		restrict: 'E',
		replace: true,
	};

}])


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
