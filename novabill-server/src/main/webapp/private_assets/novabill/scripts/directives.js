'use strict';

angular.module('novabill.directives', ['novabill.utils', 'novabill.directives.dialogs'])

/*
 * Invoice widget
 */
.directive('nInvoice', ['nRemovalDialogAPI', '$rootScope', 
                               function factory(nRemovalDialogAPI, $rootScope){

	return {
		templateUrl: NovabillConf.partialsBaseUrl+'/directives/n-invoice.html',
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
				nRemovalDialogAPI.init('Delete '+$scope.invoice.documentID+' Invoice?', {
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
				nRemovalDialogAPI.show();
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
.directive('nEstimation', ['nRemovalDialogAPI', '$rootScope', 
                                  function factory(nRemovalDialogAPI, $rootScope){

	return {
		templateUrl: NovabillConf.partialsBaseUrl+'/directives/n-estimation.html',
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
				nRemovalDialogAPI.init('Delete '+$scope.estimation.documentID+' Estimation?', {
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
				nRemovalDialogAPI.show();
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
.directive('nTransportDocument', ['nRemovalDialogAPI', '$rootScope', 
                                         function factory(nRemovalDialogAPI, $rootScope){

	return {
		templateUrl: NovabillConf.partialsBaseUrl+'/directives/n-transport-document.html',
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
				nRemovalDialogAPI.init('Delete '+$scope.transportDocument.documentID+' Transport Document?', {
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
				nRemovalDialogAPI.show();
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
.directive('nCreditNote', ['nRemovalDialogAPI', '$rootScope', 
                                  function factory(nRemovalDialogAPI, $rootScope){

	return {
		templateUrl: NovabillConf.partialsBaseUrl+'/directives/n-credit-note.html',
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
				nRemovalDialogAPI.init('Delete '+$scope.creditNote.documentID+' Credit Note?', {
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
				nRemovalDialogAPI.show();
			};
			
		}],
		restrict: 'E',
		replace: true,
	};

}])



/*
 * Commodity Widget
 */
.directive('nCommodity', ['nRemovalDialogAPI', '$rootScope', 
                                  function factory(nRemovalDialogAPI, $rootScope){

	return {
		templateUrl: NovabillConf.partialsBaseUrl+'/directives/n-commodity.html',
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
.directive('nUpdatePrice', ['$route', 'nConstants', 'nRemovalDialogAPI', 
                           function factory($route, nConstants, nRemovalDialogAPI){

	return {
		templateUrl: NovabillConf.partialsBaseUrl+'/directives/n-update-price.html',
		scope: { 
			priceListName : '=',
			price : '=',
		},
		controller : ['$scope', function($scope){
			$scope.PRICE_TYPE = nConstants.priceType;
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
				tempPrice.priceValue = $scope.priceValueDerived !== null && $scope.priceValueDerived !== undefined ? $scope.priceValueDerived : $scope.priceValueFixed;
				
				GWT_Server.commodity.addOrUpdatePrice(NovabillConf.businessId, JSON.stringify(tempPrice), {
					onSuccess : function(){
						$scope.$apply(function(){
							$route.reload();
						});
					},

					onFailure : function(error){}
				});
			};
			
			$scope.remove = function(){
				nRemovalDialogAPI.init('Are you sure that you want to delete the price in this price list?', {
					onOk : function(){
						GWT_Server.commodity.removePrice(NovabillConf.businessId, $scope.price.priceListID, $scope.price.commodityID, {
							onSuccess : function(data){
								$scope.$apply(function(){
									$route.reload();
								});
							},

							onFailure : function(error){}
						});

					},

					onCancel : function(){}
				});
				nRemovalDialogAPI.show();
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
.directive('nSmartPercentage', ['nRegExp', function(nRegExp) {
	return {
		require: 'ngModel',
		restrict: 'A',
		link: function(scope, elm, attrs, ctrl) {
			ctrl.$parsers.unshift(function(viewValue) {
				if (nRegExp.float.test(viewValue)) {
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
.directive('nSmartTax', ['nRegExp', function(nRegExp) {
	return {
		require: 'ngModel',
		restrict: 'A',
		link: function(scope, elm, attrs, ctrl) {
			ctrl.$parsers.unshift(function(viewValue) {
				if (nRegExp.float.test(viewValue)) {
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
.directive('nSmartPrice', ['nRegExp', function(nRegExp) {
	return {
		require: 'ngModel',
		restrict: 'A',
		link: function(scope, elm, attrs, ctrl) {
			ctrl.$parsers.unshift(function(viewValue) {
				if (nRegExp.float.test(viewValue)) {
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
.directive('nNotReserved', ['nRegExp', function(nRegExp) {
	return {
		require: 'ngModel',
		restrict: 'A',
		link: function(scope, elm, attrs, ctrl) {
			ctrl.$parsers.unshift(function(viewValue) {
				if (nRegExp.reserved_word.test(viewValue)) {
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
 * Log Record Widget
 */
.directive('nLogRecord', [function factory(){

	return {
		templateUrl: NovabillConf.partialsBaseUrl+'/directives/n-log-record.html',
		scope: { 
			record : '='
		},
		controller : ['$scope', function($scope){

		}],
		restrict: 'E',
		replace: true,
	};

}]);
