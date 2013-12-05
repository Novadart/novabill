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
 * Update Price Widget
 */
.directive('updatePrice', ['NConstants', function factory(NConstants){

	return {
		templateUrl: NovabillConf.partialsBaseUrl+'/directives/update-price.html',
		scope: { 
			priceListName : '@',
			price : '=',
		},
		controller : ['$scope', function($scope){
			$scope.PRICE_TYPE = NConstants.priceType;
			$scope.DEFAULT_PRICELIST_NAME = NovabillConf.defaultPriceListName;
			$scope.editMode = false;
			
			$scope.$watch('priceType', function(newValue, oldValue){
				if($scope.editMode && newValue && oldValue && newValue !== oldValue) {
					$scope.priceValueDerived = null;
					$scope.priceValueFixed = null;
				}
			});
			
			$scope.edit = function(){
				$scope.editMode = true;
				
				$scope.priceType = $scope.price.priceType;

				if($scope.priceType === $scope.PRICE_TYPE.FIXED){
					$scope.priceValueDerived = null;
					$scope.priceValueFixed = $scope.price.priceValue;
				} else {
					$scope.priceValueDerived = $scope.price.priceValue;
					$scope.priceValueFixed = null;
				}
			};
			
			$scope.cancel = function(){
				$scope.editMode = false;
			};
			
			$scope.save = function(){
				var tempPrice = angular.copy($scope.price);
				tempPrice.priceType = $scope.priceType;
				tempPrice.priceValue = $scope.priceValueDerived !== null ? $scope.priceValueDerived : $scope.priceValueFixed;
				
				GWT_Server.commodity.addOrUpdatePrice(NovabillConf.businessId, JSON.stringify(tempPrice), {
					onSuccess : function(){
						$scope.$apply(function(){
							$scope.price.priceType = tempPrice.priceType;
							$scope.price.priceValue = tempPrice.priceValue;
							$scope.editMode = false;
						});
					},

					onFailure : function(error){}
				});
			};
			
			
		}],
		restrict: 'E',
		replace: true,
	};

}])



/*
 * Smart Percentage attribute. 
 * User can insert , or . to separate decimals
 * Value mast be between 0 and 100
 */
.directive('smartPercentage', ['NRegExp', function(NRegExp) {
	return {
		require: 'ngModel',
		restrict: 'A',
		link: function(scope, elm, attrs, ctrl) {
			ctrl.$parsers.unshift(function(viewValue) {
				if (NRegExp.float.test(viewValue)) {
					ctrl.$setValidity('percentage', true);
					return parseFloat(viewValue.replace(',', '.'));
				} else {
					ctrl.$setValidity('percentage', false);
					return undefined;
				}
			});
			
			ctrl.$formatters.push(function(modelValue) {
				return modelValue ? new String(modelValue).replace('.', ',') : modelValue;
			});
		}
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
			
			ctrl.$formatters.push(function(modelValue) {
				return modelValue ? new String(modelValue).replace('.', ',') : modelValue;
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
			
			ctrl.$formatters.push(function(modelValue) {
				return modelValue ? new String(modelValue).replace('.', ',') : modelValue;
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
}]);
