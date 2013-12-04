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
 * Smart Tax attribute. 
 * User can insert , or . to separate decimals
 * Value mast be between 0 and 100
 */
.directive('smartTax', ['NRegExp', function(NRegExp) {
	return {
		require: 'ngModel',
		restrict: 'A',
		link: function(scope, elm, attrs, ctrl) {
			ctrl.$parsers.unshift(function(viewValue) {
				if (NRegExp.float.test(viewValue)) {
					var floatVal = parseFloat(viewValue.replace(',', '.'));
					if(floatVal >= 0 && floatVal < 100){
						ctrl.$setValidity('tax', true);
						return floatVal;
					} else {
						ctrl.$setValidity('tax', false);
						return undefined;
					}
					
				} else {
					ctrl.$setValidity('tax', false);
					return undefined;
				}
			});
		}
	};
}])


/*
 * Smart Price attribute. 
 * User can insert , or . to separate decimals
 */
.directive('smartPrice', ['NRegExp', function(NRegExp) {
	return {
		require: 'ngModel',
		restrict: 'A',
		link: function(scope, elm, attrs, ctrl) {
			ctrl.$parsers.unshift(function(viewValue) {
				if (NRegExp.float.test(viewValue)) {
					var floatVal = parseFloat(viewValue.replace(',', '.'));
					if(floatVal >= 0){
						ctrl.$setValidity('price', true);
						return floatVal;
					} else {
						ctrl.$setValidity('price', false);
						return undefined;
					}
					
				} else {
					ctrl.$setValidity('price', false);
					return undefined;
				}
			});
		}
	};
}])


/*
 * Check if the text is not a reserved word
 */
.directive('notReserved', ['NRegExp', function(NRegExp) {
	return {
		require: 'ngModel',
		restrict: 'A',
		link: function(scope, elm, attrs, ctrl) {
			ctrl.$parsers.unshift(function(viewValue) {
				if (NRegExp.reserved_word.test(viewValue)) {
					ctrl.$setValidity('notReserved', false);
					return undefined;
					
				} else {
					ctrl.$setValidity('notReserved', true);
					return viewValue;
				}
			});
		}
	};
}])


/*
 * Edit Commodity Dialog
 */
.directive('editCommodityDialog', function factory(){

	return {

		templateUrl: NovabillConf.partialsBaseUrl+'/directives/edit-commodity-dialog.html',
		scope: {
			commodity : '=?',
		},

		controller : function($scope, NEditCommodityDialogAPI){
			$scope.api = NEditCommodityDialogAPI;
			
			$scope.$watch('commodity', function() {
				
				//if prices map is empty, init it
				if( $scope.commodity ){
					$scope.price = $scope.commodity.pricesMap ? $scope.commodity.pricesMap.prices[ NovabillConf.defaultPriceListName ].priceValue : null;
					
					// NOTE we check for id to workaround GWT removing the property when it is false
					$scope.service = ($scope.commodity.service === undefined  
							? ($scope.commodity.id === undefined || $scope.commodity.id === null ? null : 'false') 
									: $scope.commodity.service.toString());
				}
				
			});
			
			function hideAndReset(){
				if(!$scope.api.keepCommodityOnClose){
					$scope.commodity = null;
					$scope.price = null;
					$scope.service = null;
				}
				$scope.api.hide();
				$scope.invalidSku = false;
				$scope.contactingServer = false;
				$scope.form.$setPristine();
			};

			$scope.save = function(){
				$scope.contactingServer = true;
				$scope.commodity = $scope.service==='true';
				if(!$scope.commodity.pricesMap){
					commodity['pricesMap'] = { prices : {} };
					commodity['pricesMap']['prices'][NovabillConf.defaultPriceListName] = {
							priceValue : $scope.price,
							priceType : 'FIXED'
					};
				}
				
				$scope.api.callback.onSave(
						$scope.commodity, 
						{
							finish : function(keepCommodity){ hideAndReset(keepCommodity); },
							
							invalidSku : function(){ 
								$scope.$apply(function(){
									$scope.contactingServer = false;
									$scope.invalidSku = true;
								}); 
							}
						});
			};

			$scope.cancel = function(){
				hideAndReset();
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

		keepCommodityOnClose : false,
		
		callback : {
			onSave : function(commodity){},
			onCancel : function(){}
		},
		
		//functions
		init : function(callback, keepCommodityOnClose){
			this.callback = callback;
			this.keepCommodityOnClose = keepCommodityOnClose;
		},

		show : function(){
			$('#editCommodityDialog').modal('show');
		},

		hide : function(){
			$('#editCommodityDialog').modal('hide');
			
			// workaround - see http://stackoverflow.com/questions/11519660/twitter-bootstrap-modal-backdrop-doesnt-disappear
			$('body').removeClass('modal-open');
			$('.modal-backdrop').remove();
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
			onOk : function(commodity){},
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
			
			// workaround - see http://stackoverflow.com/questions/11519660/twitter-bootstrap-modal-backdrop-doesnt-disappear
			$('body').removeClass('modal-open');
			$('.modal-backdrop').remove();
		}

	};
});
