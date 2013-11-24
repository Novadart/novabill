'use strict';

angular.module('novabill.directives', ['novabill.utils'])

/*
 * Invoice widget
 */
.directive('novabillInvoice', ['NRemovalDialogAPI', '$rootScope', 
                               function factory(NRemovalDialogAPI, $rootScope){

	return {
		templateUrl: NovabillConf.partialsBaseUrl+'/directives/novabill-invoice.html',
		scope: { 
			invoice : '=',
			bottomUpMenu : '=',
		},
		controller : ['$scope', function($scope){
			$scope.openUrl = NovabillConf.invoicesBaseUrl + '#/details/' + $scope.invoice.id;

			$scope.print = function(){
				GWT_UI.generateInvoicePdf($scope.invoice.id);
			};

			$scope.remove = function(){
				NRemovalDialogAPI.init('Delete '+$scope.invoice.documentID+' Invoice?', {
					onOk : function(){
						GWT_Server.invoice.remove(NovabillConf.businessId, $scope.invoice.client.id, $scope.invoice.id, {
							onSuccess : function(){
								$rootScope.$broadcast('invoice.remove');
							},
							onFailure : function(){}
						});
					},

					onCancel : function(){}
				});
				NRemovalDialogAPI.show();
			};

			$scope.clone = function(){
				GWT_UI.selectClientDialog(NovabillConf.businessId, {

					onSuccess : function(clientId){
						window.location.assign(NovabillConf.invoicesBaseUrl + '#/new/' + clientId + '/clone/' + $scope.invoice.id);
					},

					onFailure : function(){}
				});
			};

			$scope.createCreditNote = function(id){
				window.location.assign(NovabillConf.creditNotesBaseUrl + '#/from-invoice/' + $scope.invoice.id);
			};
		}],
		restrict: 'E',
		replace: true,
	};

}])


/*
 * Estimation Widget
 */
.directive('novabillEstimation', ['NRemovalDialogAPI', '$rootScope', 
                                  function factory(NRemovalDialogAPI, $rootScope){

	return {
		templateUrl: NovabillConf.partialsBaseUrl+'/directives/novabill-estimation.html',
		scope: { 
			estimation : '=',
			bottomUpMenu : '=',
		},
		controller : ['$scope', function($scope){
			$scope.openUrl = NovabillConf.estimationsBaseUrl + '#/details/' + $scope.estimation.id;

			$scope.print = function(){
				GWT_UI.generateEstimationPdf($scope.estimation.id);
			};

			$scope.remove = function(){
				NRemovalDialogAPI.init('Delete '+$scope.estimation.documentID+' Estimation?', {
					onOk : function(){
						GWT_Server.estimation.remove(NovabillConf.businessId, $scope.estimation.client.id, $scope.estimation.id, {
							onSuccess : function(){
								$rootScope.$broadcast('estimation.remove');
							},
							onFailure : function(){}
						});
					},

					onCancel : function(){}
				});
				NRemovalDialogAPI.show();
			};

			$scope.clone = function(){
				GWT_UI.selectClientDialog(NovabillConf.businessId, {

					onSuccess : function(clientId){
						window.location.assign(NovabillConf.estimationsBaseUrl + '#/new/' + clientId + '/clone/' + $scope.invoice.id);
					},

					onFailure : function(){}
				});
			};

			$scope.convertToInvoice = function(id){
				window.location.assign(NovabillConf.invoicesBaseUrl + '#/from-estimation/' + $scope.estimation.id);
			};

		}],
		restrict: 'E',
		replace: true,
	};

}])


/*
 * Transport Document Widget
 */
.directive('novabillTransportDocument', ['NRemovalDialogAPI', '$rootScope', 
                                         function factory(NRemovalDialogAPI, $rootScope){

	return {
		templateUrl: NovabillConf.partialsBaseUrl+'/directives/novabill-transport-document.html',
		scope: { 
			transportDocument : '=',
			bottomUpMenu : '=',
		},
		controller : ['$scope', function($scope){
			$scope.openUrl = NovabillConf.transportDocumentsBaseUrl + '#/details/' + $scope.transportDocument.id;

			$scope.print = function(){
				GWT_UI.generateTransportDocumentPdf($scope.transportDocument.id);
			};

			$scope.remove = function(){
				NRemovalDialogAPI.init('Delete '+$scope.transportDocument.documentID+' Transport Document?', {
					onOk : function(){
						GWT_Server.transportDocument.remove(NovabillConf.businessId, $scope.transportDocument.client.id, $scope.transportDocument.id, {
							onSuccess : function(){
								$rootScope.$broadcast('transportDocument.remove');
							},
							onFailure : function(){}
						});
					},

					onCancel : function(){}
				});
				NRemovalDialogAPI.show();
			};
			
			$scope.createInvoice = function(id){
				window.location.assign(NovabillConf.invoicesBaseUrl + '#/from-transport-document/' + $scope.transportDocument.id);
			};

		}],
		restrict: 'E',
		replace: true,
	};

}])


/*
 * Credit Note Widget
 */
.directive('novabillCreditNote', ['NRemovalDialogAPI', '$rootScope', 
                                  function factory(NRemovalDialogAPI, $rootScope){

	return {
		templateUrl: NovabillConf.partialsBaseUrl+'/directives/novabill-credit-note.html',
		scope: { 
			creditNote : '=',
			bottomUpMenu : '=',
		},
		controller : ['$scope', function($scope){
			$scope.openUrl = NovabillConf.creditNotesBaseUrl + '#/details/' + $scope.creditNote.id;

			$scope.print = function(){
				GWT_UI.generateCreditNotePdf($scope.creditNote.id);
			};
			
			$scope.remove = function(){
				NRemovalDialogAPI.init('Delete '+$scope.creditNote.documentID+' Credit Note?', {
					onOk : function(){
						GWT_Server.creditNote.remove(NovabillConf.businessId, $scope.creditNote.client.id, $scope.creditNote.id, {
							onSuccess : function(){
								$rootScope.$broadcast('creditNote.remove');
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
 * Commodity Widget
 */
.directive('novabillCommodity', ['NRemovalDialogAPI', '$rootScope', 
                                  function factory(NRemovalDialogAPI, $rootScope){

	return {
		templateUrl: NovabillConf.partialsBaseUrl+'/directives/novabill-commodity.html',
		scope: { 
			commodity : '=',
			bottomUpMenu : '=',
		},
		controller : ['$scope', function($scope){

			$scope.remove = function(){};
			
		}],
		restrict: 'E',
		replace: true,
	};

}])



/*
 * Edit Commodity Dialog
 */
.directive('editCommodityDialog', function factory(){

	return {

		templateUrl: NovabillConf.partialsBaseUrl+'/directives/edit-commodity-dialog.html',
		scope: {},

		controller : function($scope, NEditCommodityDialogAPI){
			$scope.api = NEditCommodityDialogAPI;

			$scope.save = function(){
				NEditCommodityDialogAPI.hide();
				
				// workaround - see http://stackoverflow.com/questions/11519660/twitter-bootstrap-modal-backdrop-doesnt-disappear
				$('body').removeClass('modal-open');
				$('.modal-backdrop').remove();
				$scope.api.callback.onOk();
			};

			$scope.cancel = function(){
				NEditCommodityDialogAPI.hide();
				$scope.api.callback.onCancel();
			};
		},

		restrict: 'E',
		replace: true,

	};

})
//APIs
.factory('NEditCommodityDialogAPI', function(){
	return {

		//instance variables
		commodity : null,

		callback : {
			onSave : function(commodity){},
			onCancel : function(){}
		},

		//functions
		init : function(commodity, callback){
			this.commodity = commodity;
			this.callback = callback;
		},

		show : function(){
			$('#editCommodityDialog').modal('show');
		},

		hide : function(){
			$('#editCommodityDialog').modal('hide');
		}

	};
})






/*
 * Removal Dialog
 */
.directive('removalDialog', function factory(){

	return {

		templateUrl: NovabillConf.partialsBaseUrl+'/directives/confirm-removal-dialog.html',
		scope: {},

		controller : function($scope, NRemovalDialogAPI){
			$scope.api = NRemovalDialogAPI;

			$scope.ok = function(){
				NRemovalDialogAPI.hide();
				
				// workaround - see http://stackoverflow.com/questions/11519660/twitter-bootstrap-modal-backdrop-doesnt-disappear
				$('body').removeClass('modal-open');
				$('.modal-backdrop').remove();
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
