'use strict';

angular.module('novabill.directives', 
		['novabill.directives.validation', 'novabill.directives.dialogs', 'novabill.utils', 'novabill.translations', 
		 'novabill.calc', 'novabill.constants', 'ngSanitize', 'ngAnimate'])

/*
 * Invoice widget
 */
.directive('nInvoice', ['nConstants',
                        function factory(nConstants){

	return {
		templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-invoice.html'),
		scope: { 
			invoice : '=',
			bottomUpMenu : '='
		},
		controller : ['$scope', '$rootScope', '$element', '$translate', 'nConfirmDialog', 'nSelectClientDialog', 'nDownload',
		              function($scope, $rootScope, $element, $translate, nConfirmDialog, nSelectClientDialog, nDownload){

			function isExpired(){
				var today = new Date();
				today.setHours(0);
				today.setMinutes(0);
				today.setSeconds(0);
				var paymentDueDate = new Date(parseInt($scope.invoice.paymentDueDate));
				return paymentDueDate < today;
			}; 
			
			$scope.expired = isExpired();
			
			$scope.togglePayed = function(){
				var value = !$scope.invoice.payed;
				GWT_Server.invoice.setPayed(nConstants.conf.businessId, $scope.invoice.client.id, $scope.invoice.id, value, {
					onSuccess : function(){
						$scope.$apply(function(){
							$scope.invoice.payed = value;
							$scope.expired = isExpired();
						});
					},
					onFailure : function(){}
				});
			};
			
			$scope.openUrl = function() {
				window.location.assign( nConstants.url.invoiceDetails($scope.invoice.id) );
			};

			$scope.stopProp = function($event){
				$event.stopPropagation();
			};
			
			$scope.download = function(){
				nDownload.downloadInvoicePdf($scope.invoice.id);
			};

			$scope.print = function(){
				nDownload.printInvoicePdf($scope.invoice.id);
			};

			$scope.remove = function(){
				var instance = nConfirmDialog.open( $translate('REMOVAL_QUESTION') );
				instance.result.then(function(value){
					if(value){
						GWT_Server.invoice.remove(nConstants.conf.businessId, $scope.invoice.client.id, $scope.invoice.id, {
							onSuccess : function(){
								$rootScope.$broadcast(nConstants.events.INVOICE_REMOVED);
							},
							onFailure : function(){}
						});
					}
				});
				
			};

			$scope.clone = function(){
				var instance = nSelectClientDialog.open();
				instance.result.then(
						function (clientId) {
							window.location.assign(nConstants.url.invoiceClone(clientId, $scope.invoice.id));
						},
						function () {
						}
				);
			};

			$scope.createCreditNote = function(id){
				window.location.assign(nConstants.url.creditNoteFromInvoice($scope.invoice.id));
			};

			//activate the dropdown
			angular.element($element).find('.dropdown-toggle').dropdown();

		}],
		restrict: 'E',
		replace: true
	};

}])


/*
 * Estimation Widget
 */
.directive('nEstimation', ['nConstants', function factory(nConstants){

	return {
		templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-estimation.html'),
		scope: { 
			estimation : '=',
			bottomUpMenu : '='
		},
		controller : ['$scope', '$element', '$rootScope', '$translate', 'nSelectClientDialog', 'nConfirmDialog', 'nDownload',
		              function($scope, $element, $rootScope, $translate, nSelectClientDialog, nConfirmDialog, nDownload){

			$scope.openUrl = function() {
				window.location.assign( nConstants.url.estimationDetails($scope.estimation.id) );
			};

			$scope.stopProp = function($event){
				$event.stopPropagation();
			};

			$scope.download = function(){
				nDownload.downloadEstimationPdf($scope.estimation.id);
			};
			
			$scope.print = function(){
				nDownload.printEstimationPdf($scope.estimation.id);
			};

			$scope.remove = function(){
				var instance = nConfirmDialog.open( $translate('REMOVAL_QUESTION') );
				instance.result.then(function(value){
					if(value){
						GWT_Server.estimation.remove(nConstants.conf.businessId, $scope.estimation.client.id, $scope.estimation.id, {
							onSuccess : function(){
								$rootScope.$broadcast(nConstants.events.ESTIMATION_REMOVED);
							},
							onFailure : function(){}
						});
					}
				});
			};

			$scope.clone = function(){
				var instance = nSelectClientDialog.open();
				instance.result.then(
						function (clientId) {
							window.location.assign(nConstants.url.estimationClone(clientId, $scope.estimation.id));
						},
						function () {
						}
				);
			};

			$scope.convertToInvoice = function(id){
				window.location.assign(nConstants.url.invoiceFromEstimation($scope.estimation.id));
			};
			
			$scope.convertToTransportDocument = function(id){
				window.location.assign(nConstants.url.transportDocumentFromEstimation($scope.estimation.id));
			};

			//activate the dropdown
			angular.element($element).find('.dropdown-toggle').dropdown();

		}],
		restrict: 'E',
		replace: true
	};

}])


/*
 * Transport Document Widget
 */
.directive('nTransportDocument', ['nConstants', function factory(nConstants){

	return {
		templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-transport-document.html'),
		scope: { 
			transportDocument : '=',
			bottomUpMenu : '='
		},
		controller : ['$scope', '$element', '$translate', 'nConstants', 'nConfirmDialog', 'nSelectTransportDocumentsDialog', '$rootScope', 'nDownload',
		              function($scope, $element, $translate, nConstants, nConfirmDialog, nSelectTransportDocumentsDialog, $rootScope, nDownload){

			$scope.invoiceRefUrl = $scope.transportDocument.invoice ? nConstants.url.invoiceDetails($scope.transportDocument.invoice) : null;
			
			$scope.openUrl = function() {
				window.location.assign( nConstants.url.transportDocumentDetails( $scope.transportDocument.id ) );
			};

			$scope.stopProp = function($event){
				$event.stopPropagation();
			};

			$scope.download = function(){
				nDownload.downloadTransportDocumentPdf($scope.transportDocument.id);
			};
			
			$scope.print = function(){
				nDownload.printTransportDocumentPdf($scope.transportDocument.id);
			};

			$scope.remove = function(){
				var instance = nConfirmDialog.open( $translate('REMOVAL_QUESTION') );
				instance.result.then(function(value){
					if(value){
						GWT_Server.transportDocument.remove(nConstants.conf.businessId, $scope.transportDocument.client.id, $scope.transportDocument.id, {
							onSuccess : function(){
								$rootScope.$broadcast(nConstants.events.TRANSPORT_DOCUMENT_REMOVED);
							},
							onFailure : function(){}
						});
					}
				});
			};

			$scope.createInvoice = function(id){
				nSelectTransportDocumentsDialog.open($scope.transportDocument.client.id, $scope.transportDocument.id);
			};

			//activate the dropdown
			angular.element($element).find('.dropdown-toggle').dropdown();

		}],
		restrict: 'E',
		replace: true
	};

}])


/*
 * Credit Note Widget
 */
.directive('nCreditNote', ['nConstants', function factory(nConstants){

	return {
		templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-credit-note.html'),
		scope: { 
			creditNote : '=',
			bottomUpMenu : '='
		},
		controller : ['$scope', '$rootScope', '$element', '$translate', 'nConfirmDialog', 'nDownload',
		              function($scope, $rootScope, $element, $translate, nConfirmDialog, nDownload){

			$scope.openUrl = function() {
				window.location.assign( nConstants.url.creditNoteDetails( $scope.creditNote.id ) );
			};

			$scope.stopProp = function($event){
				$event.stopPropagation();
			};
			
			$scope.download = function(){
				nDownload.downloadCreditNotePdf($scope.creditNote.id);
			};

			$scope.print = function(){
				nDownload.printCreditNotePdf($scope.creditNote.id);
			};

			$scope.remove = function(){
				var instance = nConfirmDialog.open( $translate('REMOVAL_QUESTION') );
				instance.result.then(function(value){
					if(value){
						GWT_Server.creditNote.remove(nConstants.conf.businessId, $scope.creditNote.client.id, $scope.creditNote.id, {
							onSuccess : function(){
								$rootScope.$broadcast(nConstants.events.CREDIT_NOTE_REMOVED);
							},
							onFailure : function(){}
						});
					}
				});
			};

			//activate the dropdown
			angular.element($element).find('.dropdown-toggle').dropdown();

		}],
		restrict: 'E',
		replace: true
	};

}])



/*
 * Commodity Widget
 */
.directive('nCommodity', ['nConstants', function factory(nConstants){

	return {
		templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-commodity.html'),
		scope: { 
			commodity : '='
		},
		controller : ['$scope', 'nConstants', 'nRegExp', function($scope, nConstants, nRegExp){
			
			$scope.printSku = function(){
				if( !nRegExp.reserved_word.test($scope.commodity.sku)){
					return $scope.commodity.sku;
				} else {
					return '';
				}
			};
			
			$scope.openUrl = function(){
				window.location.assign( nConstants.url.commodityDetails($scope.commodity.id) );
			};

		}],
		restrict: 'E',
		replace: true
	};

}])



/*
 * Price List Widget
 */
.directive('nPriceList', ['$rootScope', 'nConstants', function factory($rootScope, nConstants){

	return {
		templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-price-list.html'),
		scope: { 
			priceList : '='
		},
		controller : ['$scope', 'nConstants', '$element', 'nEditPriceListDialog', '$window', 'nAjax',
		              function($scope, nConstants, $element, nEditPriceListDialog, $window, nAjax){
			$scope.DEFAULT_PRICELIST_NAME = nConstants.conf.defaultPriceListName;
			
			$scope.openUrl = function(){
				window.location.assign( nConstants.url.priceListDetails($scope.priceList.id) );
			};
			
			$scope.stopProp = function($event){
				$event.stopPropagation();
			};
			
			function recursiveCloning(wrongPriceList){
				var instance = null;
				
				if(wrongPriceList){
					instance = nEditPriceListDialog.open(wrongPriceList, true);
				} else {
					instance = nEditPriceListDialog.open();
				}
				
				instance.result.then(function(priceList){
					var PriceList = nAjax.PriceList();
					PriceList.clonePriceList(
							{
								id : $scope.priceList.id,
								priceListName: priceList.name 
							},
							function(newPriceList){
								$window.location.assign( nConstants.url.priceListDetails(newPriceList.id) );
							}, 
							function(exceptionData){
								switch(exceptionData.data.error){
								case nConstants.exception.VALIDATION:
									if(exceptionData.data.message[0].errorCode === nConstants.validation.NOT_UNIQUE){
										recursiveCloning(priceList);
									}
									break;

								default:
									break;
								}
							});
				});
			}
			
			$scope.clone = function(){
				recursiveCloning();
			};
			
			//activate the dropdown
			angular.element($element).find('.dropdown-toggle').dropdown();

		}],
		restrict: 'E',
		replace: true
	};

}])


/*
 * Share Widget
 */
.directive('nSharingPermit', ['nConstants', function factory(nConstants){

	return {
		templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-sharing-permit.html'),
		scope: { 
			sharingPermit : '='
		},
		controller : ['$scope', 'nConstants', '$rootScope', '$translate', 'nConfirmDialog', 'nAlertDialog',
		              function($scope, nConstants, $rootScope, $translate, nConfirmDialog, nAlertDialog){
			
			$scope.stopProp = function($event){
				$event.stopPropagation();
			};
			
			$scope.sendEmail = function(){
				var instance = nConfirmDialog.open( $translate('SHARING_PERMIT_SEND_EMAIL_CONFIRM', {email : $scope.sharingPermit.email}) );
				instance.result.then(function(value){
					if(value){
						$scope.sharingPermit.$sendEmail(function(){
							nAlertDialog.open($translate('SHARING_PERMIT_EMAIL_SENT'));
						});
					}
				});
			};
			
			$scope.remove = function(){
				var instance = nConfirmDialog.open( $translate('SHARING_PERMIT_REMOVAL_QUESTION', {email : $scope.sharingPermit.email}) );
				instance.result.then(function(value){
					if(value){
						$scope.sharingPermit.$delete(function(){
							$rootScope.$broadcast(nConstants.events.SHARING_PERMIT_REMOVED);
						});
					}
				});
			};

		}],
		restrict: 'E',
		replace: true
	};

}])



/*
 * Commodity Price Widget
 * 
 * REQUIRES: removal dialog
 */
.directive('nCommodityPrice', ['$route', 'nConstants', 
                               function factory($route, nConstants){

	return {
		templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-commodity-price.html'),
		scope: { 
			// the price list we are dealing with
			priceListName : '=',

			// the commodity
			commodity : '=',

			// if true the price cannot be changed, buttons are hidden
			readOnly : '@?',
			
			//should we reload if the price has been updated?
			reloadOnUpdate : '='
		},
		controller : ['$scope', '$rootScope', 'nCalc', '$filter', 'nConfirmDialog', 'nAjax', 
		              function($scope, $rootScope, nCalc, $filter, nConfirmDialog, nAjax){
			$scope.PRICE_TYPE = nConstants.priceType;
			$scope.DEFAULT_PRICELIST_NAME = nConstants.conf.defaultPriceListName;

			// making a copy because we want to drop changes in case they are not saved remotely
			$scope.price = angular.copy( $scope.commodity.prices[$scope.priceListName]);

			function updatePriceInfo(price){

				$scope.priceValue = nCalc.calculatePriceForCommodity($scope.commodity, $scope.priceListName).toString();

				if($scope.priceListName === $scope.DEFAULT_PRICELIST_NAME || !$scope.price.id){

					$scope.priceDetails = $filter('translate')('DEFAULT_PRICE_LIST');

				} else {
					var details = $filter('translate')('DEFAULT_PRICE_LIST')
					+'    '+$filter('currency')($scope.commodity.prices[$scope.DEFAULT_PRICELIST_NAME].priceValue);

					switch (price.priceType) {
					case nConstants.priceType.DISCOUNT_PERCENT:
						$scope.priceDetails = details +'<br>'
						+ $filter('translate')('COMMODITY_PRICE_DISCOUNT')
						+'  ' + String(price.priceValue).replace('\.', ',') +'%';
						break;
						
					case nConstants.priceType.DISCOUNT_FIXED:
						$scope.priceDetails = details +'<br>'
						+ $filter('translate')('COMMODITY_PRICE_DISCOUNT')
						+'  ' + String(price.priceValue).replace('\.', ',') +'€';
						break;
						
					case nConstants.priceType.OVERCHARGE_PERCENT:
						$scope.priceDetails = details +'<br>'
						+ $filter('translate')('COMMODITY_PRICE_OVERCHARGE')
						+'  ' + String(price.priceValue).replace('\.', ',') +'%';
						break;
						
						
					case nConstants.priceType.OVERCHARGE_FIXED:
						$scope.priceDetails = details +'<br>'
						+ $filter('translate')('COMMODITY_PRICE_OVERCHARGE')
						+'  ' + String(price.priceValue).replace('\.', ',') +'€';
						break;

					default:
					case nConstants.priceType.FIXED:
						$scope.priceDetails = details;
						break;
					}
				}
			}

			// for displaying info
			updatePriceInfo($scope.commodity.prices[$scope.priceListName]);

			if(!$scope.readOnly) {
				
				var CommodityUtils = nAjax.CommodityUtils();
				
				$scope.$watch('price', function(newPrice, oldPrice){
					if(newPrice.priceType !== oldPrice.priceType){
						$scope.price.priceValue = null;
					}
				}, true);
	
				$scope.save = function(){
					CommodityUtils.addOrUpdatePrice($scope.price,
							function(newPriceId){
								//set the id, needed if the price was freshly added
								$scope.price.id = newPriceId;
		
								// and update the model
								$scope.commodity.prices[$scope.priceListName] = $scope.price;
								
								// data has been saved, update info...
								updatePriceInfo( $scope.price );
								
								$scope.editMode = false;
		
								if($scope.reloadOnUpdate){
									$route.reload();
								}
							});
				};
	
				$scope.edit = function(){
					$scope.price = angular.copy( $scope.commodity.prices[$scope.priceListName] );
					$scope.editMode = true;
				};
	
				$scope.cancel = function(){
					$scope.editMode = false;
				};
	
				$scope.remove = function(){
					var instance = nConfirmDialog.open( $filter('translate')('REMOVAL_QUESTION') );
					instance.result.then(function(value){
						if(value){
							var params = {
									commodityID : $scope.price.commodityID,
									priceListID : $scope.price.priceListID
								};
							CommodityUtils.removePrice(
									params,
									function(){
										var deletedPrice = $scope.commodity.prices[$scope.priceListName];
										var emptyPrice = {
												id: null, 
												priceValue: null, 
												priceType: null, 
												commodityID: deletedPrice.commodityID, 
												priceListID: deletedPrice.priceListID
										};
										$scope.commodity.prices[$scope.priceListName] = emptyPrice;
										$scope.price = emptyPrice;
										updatePriceInfo( $scope.price );
										$scope.editMode = false;
									});
						}
					});
				};
				
			}

		}],
		restrict: 'E',
		replace: true
	};

}])


/*
 * Year selector
 */
.directive('nYearSelector', ['nConstants', function(nConstants) {
	return {
		templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-year-selector.html'),
		scope: { 
			callback : '&',
			documentType : '@'
		},
		controller : ['$scope', function($scope){
			var years = documentYears[$scope.documentType];
			var currentYear = new Date().getFullYear();
			if(years.indexOf(currentYear) == -1){
				years.push(currentYear);
			}
			years.sort(function(a,b){
				return b-a; 
			});
			
			$scope.years = years;
			$scope.selectedYear = $scope.years.length > 0 ?  $scope.years[0] : null;

			$scope.onChange = function(){
				$scope.callback({ year : String($scope.selectedYear) });
			};

			if($scope.selectedYear){
				$scope.callback({ year : String($scope.selectedYear) });
			}

		}],
		restrict: 'E',
		replace: true
	};
}])



.directive('nDivInfiniteScroll', function() {
	
    return function(scope, elm, attr) {
        var raw = elm[0];
        
        elm.bind('scroll', function() {
            if (raw.scrollTop + raw.offsetHeight >= raw.scrollHeight) {
                scope.$apply(attr.nDivInfiniteScroll);
            }
        });
    };
})


/*
 * Log Record Widget
 */
.directive('nLogRecord', ['nConstants', '$sanitize', function factory(nConstants, $sanitize){

	return {
		templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-log-record.html'),
		scope: { 
			record : '='
		},
		controller : ['$scope', '$filter', function($scope, $filter){
			$scope.ENTITY_TYPE = nConstants.logRecord.entityType;
			$scope.OPERATION_TYPE = nConstants.logRecord.operationType;

			var tr = $filter('translate');
			var details = angular.fromJson($scope.record.details);

			switch ($scope.record.entityType) {

			case nConstants.logRecord.entityType.CLIENT:
				switch ($scope.record.operationType) {
				case nConstants.logRecord.operationType.CREATE:
					$scope.description = tr('LR_CLIENT_CREATE',
							'{clientName: "'+ $sanitize(details.clientName) +'", link: "'+nConstants.url.clientDetails( $scope.record.entityID )+'"}');
					break;

				case nConstants.logRecord.operationType.UPDATE:
					$scope.description = tr('LR_CLIENT_UPDATE',
							'{clientName: "'+ $sanitize(details.clientName) +'", link: "'+nConstants.url.clientDetails( $scope.record.entityID )+'"}');
					break;

				case nConstants.logRecord.operationType.DELETE:
					$scope.description = tr('LR_CLIENT_DELETE',
							'{clientName: "'+ $sanitize(details.clientName) +'"}');
					break;

				default:
					break;
				}
				break;

			case nConstants.logRecord.entityType.COMMODITY:
				switch ($scope.record.operationType) {
				case nConstants.logRecord.operationType.CREATE:
					$scope.description = tr('LR_COMMODITY_CREATE',
							'{commodityName : "'+ $sanitize(details.commodityName)	+'", link : "'+nConstants.url.commodityDetails( $scope.record.entityID )+'"}');
					break;

				case nConstants.logRecord.operationType.UPDATE:
					$scope.description = tr('LR_COMMODITY_UPDATE',
							'{commodityName : "'+ $sanitize(details.commodityName)	+'", link : "'+nConstants.url.commodityDetails( $scope.record.entityID )+'"}');
					break;


				case nConstants.logRecord.operationType.DELETE:
					$scope.description = tr('LR_COMMODITY_DELETE',
							'{commodityName : "'+ $sanitize(details.commodityName)	+'"}');
					break;

				default:
					break;
				}

				break;

			case nConstants.logRecord.entityType.INVOICE:
				switch ($scope.record.operationType) {
				case nConstants.logRecord.operationType.CREATE:
					$scope.description = tr('LR_INVOICE_CREATE',
							'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'", link: "'+nConstants.url.invoiceDetails( $scope.record.entityID )+'"}');
					break;

				case nConstants.logRecord.operationType.UPDATE:
					$scope.description = tr('LR_INVOICE_UPDATE',
							'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'", link: "'+nConstants.url.invoiceDetails( $scope.record.entityID )+'"}');
					break;

				case nConstants.logRecord.operationType.SET_PAYED:
					$scope.description = tr( details.payedStatus==='true' ? 'LR_INVOICE_SET_PAYED_TRUE' : 'LR_INVOICE_SET_PAYED_FALSE',
							'{documentID: "'+ details.documentID + '", clientName: "'+ $sanitize(details.clientName) + '", link: "'+nConstants.url.invoiceDetails( $scope.record.entityID )+'"}');
					break;

				case nConstants.logRecord.operationType.DELETE:
					$scope.description = tr('LR_INVOICE_DELETE',
							'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'"}');
					break;

				default:
					break;
				}
				break;

			case nConstants.logRecord.entityType.ESTIMATION:
				switch ($scope.record.operationType) {
				case nConstants.logRecord.operationType.CREATE:
					$scope.description = tr('LR_ESTIMATION_CREATE',
							'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'", link: "'+nConstants.url.estimationDetails( $scope.record.entityID )+'"}');
					break;

				case nConstants.logRecord.operationType.UPDATE:
					$scope.description = tr('LR_ESTIMATION_UPDATE',
							'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'", link: "'+nConstants.url.estimationDetails( $scope.record.entityID )+'"}');
					break;

				case nConstants.logRecord.operationType.DELETE:
					$scope.description = tr('LR_ESTIMATION_DELETE',
							'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'"}');
					break;

				default:
					break;
				}
				break;

			case nConstants.logRecord.entityType.CREDIT_NOTE:
				switch ($scope.record.operationType) {
				case nConstants.logRecord.operationType.CREATE:
					$scope.description = tr('LR_CREDIT_NOTE_CREATE',
							'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'", link: "'+nConstants.url.creditNoteDetails( $scope.record.entityID )+'"}');
					break;

				case nConstants.logRecord.operationType.UPDATE:
					$scope.description = tr('LR_CREDIT_NOTE_UPDATE',
							'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'", link: "'+nConstants.url.creditNoteDetails( $scope.record.entityID )+'"}');
					break;

				case nConstants.logRecord.operationType.DELETE:
					$scope.description = tr('LR_CREDIT_NOTE_DELETE',
							'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'"}');
					break;

				default:
					break;
				}
				break;

			case nConstants.logRecord.entityType.TRANSPORT_DOCUMENT:
				switch ($scope.record.operationType) {
				case nConstants.logRecord.operationType.CREATE:
					$scope.description = tr('LR_TRANSPORT_DOCUMENT_CREATE',
							'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'", link: "'+nConstants.url.transportDocumentDetails( $scope.record.entityID )+'"}');
					break;

				case nConstants.logRecord.operationType.UPDATE:
					$scope.description = tr('LR_TRANSPORT_DOCUMENT_UPDATE',
							'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'", link: "'+nConstants.url.transportDocumentDetails( $scope.record.entityID )+'"}');
					break;

				case nConstants.logRecord.operationType.DELETE:
					$scope.description = tr('LR_TRANSPORT_DOCUMENT_DELETE',
							'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'"}');
					break;

				default:
					break;
				}
				break;

			case nConstants.logRecord.entityType.PRICE_LIST:
				switch ($scope.record.operationType) {
				case nConstants.logRecord.operationType.CREATE:
					$scope.description = tr('LR_PRICE_LIST_CREATE',
							'{priceListName: "'+ $sanitize(details.priceListName) +'", link: "'+nConstants.url.priceListDetails( $scope.record.entityID )+'"}');
					break;

				case nConstants.logRecord.operationType.UPDATE:
					$scope.description = tr('LR_PRICE_LIST_UPDATE',
							'{priceListName: "'+ $sanitize(details.priceListName) +'", link: "'+nConstants.url.priceListDetails( $scope.record.entityID )+'"}');
					break;

				case nConstants.logRecord.operationType.DELETE:
					$scope.description = tr('LR_PRICE_LIST_DELETE',
							'{priceListName: "'+ $sanitize(details.priceListName) +'"}');
					break;

				default:
					break;
				}
				break;

			case nConstants.logRecord.entityType.PAYMENT_TYPE:
				switch ($scope.record.operationType) {
				case nConstants.logRecord.operationType.CREATE:
					$scope.description = tr('LR_PAYMENT_TYPE_CREATE',
							'{paymentName: "'+ $sanitize(details.paymentTypeName) +'", link: "'+nConstants.url.paymentList()+'"}');
					break;

				case nConstants.logRecord.operationType.UPDATE:
					$scope.description = tr('LR_PAYMENT_TYPE_UPDATE',
							'{paymentName: "'+ $sanitize(details.paymentTypeName) +'", link: "'+nConstants.url.paymentList()+'"}');
					break;

				case nConstants.logRecord.operationType.DELETE:
					$scope.description = tr('LR_PAYMENT_TYPE_DELETE',
							'{paymentName: "'+ $sanitize(details.paymentTypeName) +'"}');
					break;

				default:
					break;
				}
				break;
				
			case nConstants.logRecord.entityType.SHARING_PERMIT:
				switch ($scope.record.operationType) {
				case nConstants.logRecord.operationType.CREATE:
					$scope.description = tr('LR_SHARING_PERMIT_CREATE',
							'{sharingPermitDesc: "'+ $sanitize(details.sharingPermitDesc) +'"}');
					break;

				case nConstants.logRecord.operationType.DELETE:
					$scope.description = tr('LR_SHARING_PERMIT_DELETE',
							'{sharingPermitDesc: "'+ $sanitize(details.sharingPermitDesc) +'"}');
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
		replace: true
	};

}]);
