'use strict';

angular.module('novabill.directives', ['novabill.utils', 'novabill.translations', 'novabill.constants', 'ngSanitize'])

/*
 * Invoice widget
 */
.directive('nInvoice', ['$rootScope', 'nConstants',
                        function factory($rootScope, nConstants){

	return {
		templateUrl: NovabillConf.partialsBaseUrl+'/directives/n-invoice.html',
		scope: { 
			invoice : '=',
			bottomUpMenu : '=',
		},
		controller : ['$scope', '$element', function($scope, $element){

			$scope.openUrl = function() {
				window.location.assign( nConstants.url.invoiceDetails($scope.invoice.id) );
			};

			$scope.stopProp = function($event){
				$event.stopPropagation();
			};

			$scope.print = function($event){
				GWT_UI.generateInvoicePdf($scope.invoice.id);
			};

			$scope.remove = function(){
				$rootScope.$broadcast(nConstants.events.SHOW_REMOVAL_DIALOG, 
						'Delete '+$scope.invoice.documentID+' Invoice?', {
					onOk : function(){
						GWT_Server.invoice.remove(NovabillConf.businessId, $scope.invoice.client.id, $scope.invoice.id, {
							onSuccess : function(){
								$rootScope.$broadcast(nConstants.events.INVOICE_REMOVED);
							},
							onFailure : function(){}
						});
					},

					onCancel : function(){}
				});

			};

			$scope.clone = function(){
				GWT_UI.selectClientDialog(NovabillConf.businessId, {

					onSuccess : function(clientId){
						window.location.assign(nConstants.url.invoiceClone(clientId, $scope.invoice.id));
					},

					onFailure : function(){}
				});
			};

			$scope.createCreditNote = function(id){
				window.location.assign(nConstants.url.creditNoteFromInvoice($scope.invoice.id));
			};

			//activate the dropdown
			angular.element($element).find('.dropdown-toggle').dropdown();

		}],
		restrict: 'E',
		replace: true,
	};

}])


/*
 * Estimation Widget
 */
.directive('nEstimation', ['$rootScope', 'nConstants', 
                           function factory($rootScope, nConstants){

	return {
		templateUrl: NovabillConf.partialsBaseUrl+'/directives/n-estimation.html',
		scope: { 
			estimation : '=',
			bottomUpMenu : '=',
		},
		controller : ['$scope', '$element', function($scope, $element){

			$scope.openUrl = function() {
				window.location.assign( nConstants.url.estimationDetails($scope.estimation.id) );
			};

			$scope.stopProp = function($event){
				$event.stopPropagation();
			};

			$scope.print = function(){
				GWT_UI.generateEstimationPdf($scope.estimation.id);
				$event.stopPropagation();
			};

			$scope.remove = function(){
				$rootScope.$broadcast(nConstants.events.SHOW_REMOVAL_DIALOG, 
						'Delete '+$scope.estimation.documentID+' Estimation?', {
					onOk : function(){
						GWT_Server.estimation.remove(NovabillConf.businessId, $scope.estimation.client.id, $scope.estimation.id, {
							onSuccess : function(){
								$rootScope.$broadcast(nConstants.events.ESTIMATION_REMOVED);
							},
							onFailure : function(){}
						});
					},

					onCancel : function(){}
				});

			};

			$scope.clone = function(){
				GWT_UI.selectClientDialog(NovabillConf.businessId, {

					onSuccess : function(clientId){
						window.location.assign(nConstants.url.estimationClone(clientId, $scope.estimation.id));
					},

					onFailure : function(){}
				});
			};

			$scope.convertToInvoice = function(id){
				window.location.assign(nConstants.url.invoiceFromEstimation($scope.estimation.id));
			};

			//activate the dropdown
			angular.element($element).find('.dropdown-toggle').dropdown();

		}],
		restrict: 'E',
		replace: true,
	};

}])


/*
 * Transport Document Widget
 */
.directive('nTransportDocument', ['$rootScope', 'nConstants',
                                  function factory($rootScope, nConstants){

	return {
		templateUrl: NovabillConf.partialsBaseUrl+'/directives/n-transport-document.html',
		scope: { 
			transportDocument : '=',
			bottomUpMenu : '=',
		},
		controller : ['$scope', '$element', function($scope, $element){

			$scope.openUrl = function() {
				window.location.assign( nConstants.url.trasportDocumentDetails( $scope.transportDocument.id ) );
			};

			$scope.stopProp = function($event){
				$event.stopPropagation();
			};

			$scope.print = function(){
				GWT_UI.generateTransportDocumentPdf($scope.transportDocument.id);
				$event.stopPropagation();
			};

			$scope.remove = function(){
				$rootScope.$broadcast(nConstants.events.SHOW_REMOVAL_DIALOG, 
						'Delete '+$scope.transportDocument.documentID+' Transport Document?', {
					onOk : function(){
						GWT_Server.transportDocument.remove(NovabillConf.businessId, $scope.transportDocument.client.id, $scope.transportDocument.id, {
							onSuccess : function(){
								$rootScope.$broadcast(nConstants.events.TRANSPORT_DOCUMENT_REMOVED);
							},
							onFailure : function(){}
						});
					},

					onCancel : function(){}
				});
			};

			$scope.createInvoice = function(id){
				$rootScope.$broadcast(nConstants.events.SHOW_TRANSPORT_DOCUMENTS_DIALOG, 
						$scope.transportDocument.client.id, $scope.transportDocument.id);
			};

			//activate the dropdown
			angular.element($element).find('.dropdown-toggle').dropdown();

		}],
		restrict: 'E',
		replace: true,
	};

}])


/*
 * Credit Note Widget
 */
.directive('nCreditNote', ['$rootScope', 'nConstants', 
                           function factory($rootScope, nConstants){

	return {
		templateUrl: NovabillConf.partialsBaseUrl+'/directives/n-credit-note.html',
		scope: { 
			creditNote : '=',
			bottomUpMenu : '=',
		},
		controller : ['$scope', '$element', function($scope, $element){

			$scope.openUrl = function() {
				window.location.assign( nConstants.url.creditNoteDetails( $scope.creditNote.id ) );
			};

			$scope.stopProp = function($event){
				$event.stopPropagation();
			};

			$scope.print = function(){
				GWT_UI.generateCreditNotePdf($scope.creditNote.id);
				$event.stopPropagation();
			};

			$scope.remove = function(){
				$rootScope.$broadcast(nConstants.events.SHOW_REMOVAL_DIALOG, 
						'Delete '+$scope.creditNote.documentID+' Credit Note?', {
					onOk : function(){
						GWT_Server.creditNote.remove(NovabillConf.businessId, $scope.creditNote.client.id, $scope.creditNote.id, {
							onSuccess : function(){
								$rootScope.$broadcast(nConstants.events.CREDIT_NOTE_REMOVED);
							},
							onFailure : function(){}
						});
					},

					onCancel : function(){}
				});
			};

			//activate the dropdown
			angular.element($element).find('.dropdown-toggle').dropdown();

		}],
		restrict: 'E',
		replace: true,
	};

}])



/*
 * Commodity Widget
 */
.directive('nCommodity', ['$rootScope', function factory($rootScope){

	return {
		templateUrl: NovabillConf.partialsBaseUrl+'/directives/n-commodity.html',
		scope: { 
			commodity : '=',
			bottomUpMenu : '=',
		},
		controller : ['$scope', 'nConstants', function($scope, nConstants){

			$scope.openUrl = function(){
				window.location.assign( nConstants.url.commodityDetails($scope.commodity.id) );
			};

		}],
		restrict: 'E',
		replace: true,
	};

}])


/*
 * Update Price Widget
 */
.directive('nUpdatePrice', ['$route', 'nConstants', 
                            function factory($route, nConstants){

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
				$rootScope.$broadcast(nConstants.events.SHOW_REMOVAL_DIALOG, 
						'Are you sure that you want to delete the price in this price list?', {
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
		controller : ['$scope', 'nConstants', '$filter', function($scope, nConstants, $filter){
			$scope.ENTITY_TYPE = nConstants.logRecord.entityType;
			$scope.OPERATION_TYPE = nConstants.logRecord.operationType;

			var tr = $filter('translate');
			var details = angular.fromJson($scope.record.details);

			switch ($scope.record.entityType) {

			case nConstants.logRecord.entityType.CLIENT:
				switch ($scope.record.operationType) {
				case nConstants.logRecord.operationType.CREATE:
					$scope.description = tr('LR_CLIENT_CREATE',
							'{clientName: "'+ details.clientName +'", link: "'+nConstants.url.clientDetails( $scope.record.entityID )+'"}');
					break;

				case nConstants.logRecord.operationType.UPDATE:
					$scope.description = tr('LR_CLIENT_UPDATE',
							'{clientName: "'+ details.clientName +'", link: "'+nConstants.url.clientDetails( $scope.record.entityID )+'"}');
					break;

				case nConstants.logRecord.operationType.DELETE:
					$scope.description = tr('LR_CLIENT_DELETE',
							'{clientName: "'+ details.clientName +'"}');
					break;

				default:
					break;
				}
				break;

			case nConstants.logRecord.entityType.COMMODITY:
				switch ($scope.record.operationType) {
				case nConstants.logRecord.operationType.CREATE:
					$scope.description = tr('LR_COMMODITY_CREATE',
							'{commodityName : "'+ details.commodityName	+'", link : "'+nConstants.url.commodityDetails( $scope.record.entityID )+'"}');
					break;

				case nConstants.logRecord.operationType.UPDATE:
					$scope.description = tr('LR_COMMODITY_UPDATE',
							'{commodityName : "'+ details.commodityName	+'", link : "'+nConstants.url.commodityDetails( $scope.record.entityID )+'"}');
					break;


				case nConstants.logRecord.operationType.DELETE:
					$scope.description = tr('LR_COMMODITY_DELETE',
							'{commodityName : "'+ details.commodityName	+'"}');
					break;

				default:
					break;
				}

				break;

			case nConstants.logRecord.entityType.INVOICE:
				switch ($scope.record.operationType) {
				case nConstants.logRecord.operationType.CREATE:
					$scope.description = tr('LR_INVOICE_CREATE',
							'{documentID: "'+details.documentID+'", clientName: "'+ details.clientName +'", link: "'+nConstants.url.invoiceDetails( $scope.record.entityID )+'"}');
					break;

				case nConstants.logRecord.operationType.UPDATE:
					$scope.description = tr('LR_INVOICE_UPDATE',
							'{documentID: "'+details.documentID+'", clientName: "'+ details.clientName +'", link: "'+nConstants.url.invoiceDetails( $scope.record.entityID )+'"}');
					break;

				case nConstants.logRecord.operationType.SET_PAYED:
//					$scope.description = tr('LR_INVOICE_SET_PAYED');
//					$scope.link = nConstants.url.invoiceDetails( $scope.record.entityID );
					break;

				case nConstants.logRecord.operationType.DELETE:
					$scope.description = tr('LR_INVOICE_DELETE',
							'{documentID: "'+details.documentID+'", clientName: "'+ details.clientName +'"}');
					break;

				default:
					break;
				}
				break;

			case nConstants.logRecord.entityType.ESTIMATION:
				switch ($scope.record.operationType) {
				case nConstants.logRecord.operationType.CREATE:
					$scope.description = tr('LR_ESTIMATION_CREATE',
							'{documentID: "'+details.documentID+'", clientName: "'+ details.clientName +'", link: "'+nConstants.url.estimationDetails( $scope.record.entityID )+'"}');
					break;

				case nConstants.logRecord.operationType.UPDATE:
					$scope.description = tr('LR_ESTIMATION_UPDATE',
							'{documentID: "'+details.documentID+'", clientName: "'+ details.clientName +'", link: "'+nConstants.url.estimationDetails( $scope.record.entityID )+'"}');
					break;

				case nConstants.logRecord.operationType.DELETE:
					$scope.description = tr('LR_ESTIMATION_DELETE',
							'{documentID: "'+details.documentID+'", clientName: "'+ details.clientName +'"}');
					break;

				default:
					break;
				}
				break;

			case nConstants.logRecord.entityType.CREDIT_NOTE:
				switch ($scope.record.operationType) {
				case nConstants.logRecord.operationType.CREATE:
					$scope.description = tr('LR_CREDIT_NOTE_CREATE',
							'{documentID: "'+details.documentID+'", clientName: "'+ details.clientName +'", link: "'+nConstants.url.creditNoteDetails( $scope.record.entityID )+'"}');
					break;

				case nConstants.logRecord.operationType.UPDATE:
					$scope.description = tr('LR_CREDIT_NOTE_UPDATE',
							'{documentID: "'+details.documentID+'", clientName: "'+ details.clientName +'", link: "'+nConstants.url.creditNoteDetails( $scope.record.entityID )+'"}');
					break;

				case nConstants.logRecord.operationType.DELETE:
					$scope.description = tr('LR_CREDIT_NOTE_DELETE',
							'{documentID: "'+details.documentID+'", clientName: "'+ details.clientName +'"}');
					break;

				default:
					break;
				}
				break;

			case nConstants.logRecord.entityType.TRANSPORT_DOCUMENT:
				switch ($scope.record.operationType) {
				case nConstants.logRecord.operationType.CREATE:
					$scope.description = tr('LR_TRANSPORT_DOCUMENT_CREATE',
							'{documentID: "'+details.documentID+'", clientName: "'+ details.clientName +'", link: "'+nConstants.url.trasportDocumentDetails( $scope.record.entityID )+'"}');
					break;

				case nConstants.logRecord.operationType.UPDATE:
					$scope.description = tr('LR_TRANSPORT_DOCUMENT_UPDATE',
							'{documentID: "'+details.documentID+'", clientName: "'+ details.clientName +'", link: "'+nConstants.url.trasportDocumentDetails( $scope.record.entityID )+'"}');
					break;

				case nConstants.logRecord.operationType.DELETE:
					$scope.description = tr('LR_TRANSPORT_DOCUMENT_DELETE',
							'{documentID: "'+details.documentID+'", clientName: "'+ details.clientName +'"}');
					break;

				default:
					break;
				}
				break;

			case nConstants.logRecord.entityType.PAYMENT_TYPE:
				switch ($scope.record.operationType) {
				case nConstants.logRecord.operationType.CREATE:
					$scope.description = tr('LR_PAYMENT_TYPE_CREATE',
							'{paymentName: "'+ details.paymentTypeName +'", link: "'+nConstants.url.paymentList()+'"}');
					break;

				case nConstants.logRecord.operationType.UPDATE:
					$scope.description = tr('LR_PAYMENT_TYPE_UPDATE',
							'{paymentName: "'+ details.paymentTypeName +'", link: "'+nConstants.url.paymentList()+'"}');
					break;

				case nConstants.logRecord.operationType.DELETE:
					$scope.description = tr('LR_PAYMENT_TYPE_DELETE',
							'{paymentName: "'+ details.paymentTypeName +'"}');
					break;

				default:
					break;
				}
				break;

			default:
				break;
			}

		}],
		restrict: 'E',
		replace: true,
	};

}]);
