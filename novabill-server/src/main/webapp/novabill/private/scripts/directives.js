'use strict';

angular.module('novabill.directives',
	['novabill.directives.validation', 'novabill.directives.dialogs',
		'novabill.utils', 'novabill.translations',
		'novabill.calc', 'novabill.constants', 'ngSanitize', 'ui.bootstrap'])







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
				controller : ['$scope', '$rootScope', '$element', 'nAjax', 'nAlertDialog', '$filter', '$window',
					'nConfirmDialog', 'nSelectClientDialog', 'nDownload', 'nSendEmailDialog',
					function($scope, $rootScope, $element, nAjax, nAlertDialog, $filter, $window,
							 nConfirmDialog, nSelectClientDialog, nDownload, nSendEmailDialog){

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
							$window.location.assign( nConstants.url.invoiceDetails($scope.invoice.id) );
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
							var instance = nConfirmDialog.open( $filter('translate')('REMOVAL_QUESTION') );
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
									$window.location.assign(nConstants.url.invoiceClone(clientId, $scope.invoice.id));
								},
								function () {
								}
							);
						};

						$scope.createCreditNote = function(id){
							$window.location.assign(nConstants.url.creditNoteFromInvoice($scope.invoice.id));
						};

						$scope.sendEmailToClient = function(){
							nAjax.Business().get(function(business){
								var instance = nSendEmailDialog.open(business, $scope.invoice);
								instance.result.then(function(data){
									var InvoiceUtils = nAjax.InvoiceUtils();
									InvoiceUtils.email(data, function(data){
										nAlertDialog.open($filter('translate')(data.value ? 'SEND_EMAIL_TO_CLIENT_SUCCESS' : 'SEND_EMAIL_TO_CLIENT_FAILURE'));
									}, function(){
										nAlertDialog.open($filter('translate')('SEND_EMAIL_TO_CLIENT_FAILURE'));
									});
								});

							})
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
			controller : ['$scope', '$element', '$rootScope', '$translate', 'nSelectClientDialog', 'nConfirmDialog', 'nDownload', '$window',
				function($scope, $element, $rootScope, $translate, nSelectClientDialog, nConfirmDialog, nDownload, $window){

					$scope.openUrl = function() {
						$window.location.assign( nConstants.url.estimationDetails($scope.estimation.id) );
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
								$window.location.assign(nConstants.url.estimationClone(clientId, $scope.estimation.id));
							},
							function () {
							}
						);
					};

					$scope.convertToInvoice = function(id){
						$window.location.assign(nConstants.url.invoiceFromEstimation($scope.estimation.id));
					};

					$scope.convertToTransportDocument = function(id){
						$window.location.assign(nConstants.url.transportDocumentFromEstimation($scope.estimation.id));
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
			controller : ['$scope', '$element', '$translate', 'nConstants', 'nConfirmDialog', '$window',
				'nSelectTransportDocumentsDialog', '$rootScope', 'nDownload',
				function($scope, $element, $translate, nConstants, nConfirmDialog, $window,
						 nSelectTransportDocumentsDialog, $rootScope, nDownload){

					$scope.invoiceRefUrl = $scope.transportDocument.invoice ? nConstants.url.invoiceDetails($scope.transportDocument.invoice) : null;

					$scope.openUrl = function() {
						$window.location.assign( nConstants.url.transportDocumentDetails( $scope.transportDocument.id ) );
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
			controller : ['$scope', '$rootScope', '$element', '$translate', 'nConfirmDialog', 'nDownload', '$window',
				function($scope, $rootScope, $element, $translate, nConfirmDialog, nDownload, $window){

					$scope.openUrl = function() {
						$window.location.assign( nConstants.url.creditNoteDetails( $scope.creditNote.id ) );
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
			controller : ['$scope', 'nConstants', 'nRegExp', '$window',
				function($scope, nConstants, nRegExp, $window){

					$scope.printSku = function(){
						if( !nRegExp.reserved_word.test($scope.commodity.sku)){
							return $scope.commodity.sku;
						} else {
							return '';
						}
					};

					$scope.openUrl = function(){
						$window.location.assign( nConstants.url.commodityDetails($scope.commodity.id) );
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
						$window.location.assign( nConstants.url.priceListDetails($scope.priceList.id) );
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
						$scope.readOnly = false;
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
									function(data){
										//set the id, needed if the price was freshly added
										$scope.price.id = data.value;

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
				documentType : '@',
				selectedYear : '@'
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

				if($scope.selectedYear == null)
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


	/*
	 * Stats year selector
	 */
	.directive('nStatsYearSelector', ['nConstants', function(nConstants) {
		return {
			templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-stats-year-selector.html'),
			scope: {
				callback : '&',
				selectedYear : '='
			},
			controller : ['$scope', 'nConstants', function($scope, nConstants){
				var principalCreationYear = new Date(nConstants.conf.principalCreationDate).getFullYear();
				var currentYear = new Date().getFullYear();

				var years = [];
				for(var i=currentYear; i>=principalCreationYear; i--){
					years.push(i);
				}
				$scope.years = years;

				$scope.onChange = function(){
					$scope.callback({ year : String($scope.selectedYear) });
				};

			}],
			restrict: 'E',
			replace: true
		};
	}])


    /*
     * Country selector
     */
    .directive('nCountrySelector', ['nConstants', function(nConstants) {
        return {
            templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-country-selector.html'),
            scope: {
                //callback : '&',
                selectedCountry : '='
            },
            controller : ['$scope', function($scope){
                //$scope.onChange = function(){
                //	$scope.callback({ year : String($scope.selectedYear) });
                //};

            }],
            restrict: 'E',
            replace: true
        };
    }])


    /*
     * Document ID Class selector
     */
    .directive('nDocumentIdSelector', ['nConstants', function(nConstants) {
        return {
            templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-document-id-class-selector.html'),
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


	/*
	 * Suffix selector
	 */
	.directive('nSuffixSelector', ['nConstants', function(nConstants) {
		return {
			templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-suffix-selector.html'),
			scope: {
				callback : '&',
				selectedClass : '@'
			},
			controller : ['$scope', '$window', '$filter', function($scope, $window, $filter){
				var classes = $window.invoiceSuffixes;
				var sortedClasses = classes.sort();

				$scope.classes = [$filter('translate')('SUFFIX_STANDARD')].concat(sortedClasses);
				if($scope.selectedClass == null)
					$scope.selectedClass = $scope.classes[0];

				$scope.onChange = function(event){
					var index = $scope.classes.indexOf($scope.selectedClass);
					$scope.callback({ documentIdClass : index==0 ? null : String($scope.selectedClass) });
				};

			}],
			restrict: 'E',
			replace: true
		};
	}])



	.directive('nFocusIf', ['$timeout', function ($timeout) {
		return {
			restrict: 'A',
			link: function(scope, element, attrs) {
				scope.$watch(attrs.nFocusIf, function(value, old) {
					if(value === true) {
						$timeout(function(){
							element[0].focus();
							element[0].select();
						}, 100);
					}
				});
			}
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


	.directive("nSlimScroll", [function () {

		return {
			restrict: "A",
			link: function (scope, element, attrs) {
				// instance-specific options
				var opts = angular.extend({}, scope.$eval(attrs.slimScroll));
				angular.element(element).slimScroll(opts);
			}
		};
	}])


	.directive('nEmailKeywordChooser', ['nConstants', function(nConstants) {

		return {
			templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-email-keyword-chooser.html'),
			scope: {
				model : '='
			},
			controller : ['$scope', function($scope){

				$scope.keywords = {
					'RagioneSocialeAzienda' : 'EMAIL_KEYWORD_BUSINESS_NAME',
					'NomeCliente' : 'EMAIL_KEYWORD_CLIENT_NAME',
					'DataFattura' : 'EMAIL_KEYWORD_INVOICE_DATE',
					'NumeroFattura' : 'EMAIL_KEYWORD_INVOICE_NUMBER',
					'TotaleFattura' : 'EMAIL_KEYWORD_INVOICE_TOTAL'
				};

				$scope.click = function(key){
					$scope.model = $scope.model ? $scope.model+("$"+key) : ("$"+key);
				};

			}],
			restrict: 'E',
			replace: true
		};
	}])


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
								if($scope.record.referringToDeletedEntity)
									$scope.description = tr('LR_CLIENT_CREATE_DEL',
										'{clientName: "'+ $sanitize(details.clientName) +'"}');
								else
									$scope.description = tr('LR_CLIENT_CREATE',
										'{clientName: "'+ $sanitize(details.clientName) +'", link: "'+nConstants.url.clientDetails( $scope.record.entityID )+'"}');
								break;

							case nConstants.logRecord.operationType.UPDATE:
								if($scope.record.referringToDeletedEntity)
									$scope.description = tr('LR_CLIENT_UPDATE_DEL',
										'{clientName: "'+ $sanitize(details.clientName) +'"}');
								else
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
								if($scope.record.referringToDeletedEntity)
									$scope.description = tr('LR_COMMODITY_CREATE_DEL',
										'{commodityName : "'+ $sanitize(details.commodityName) +'"}');
								else
									$scope.description = tr('LR_COMMODITY_CREATE',
										'{commodityName : "'+ $sanitize(details.commodityName)	+'", link : "'+nConstants.url.commodityDetails( $scope.record.entityID )+'"}');
								break;

							case nConstants.logRecord.operationType.UPDATE:
								if($scope.record.referringToDeletedEntity)
									$scope.description = tr('LR_COMMODITY_UPDATE_DEL',
										'{commodityName : "'+ $sanitize(details.commodityName)	+'"}');
								else
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
								if($scope.record.referringToDeletedEntity)
									$scope.description = tr('LR_INVOICE_CREATE_DEL',
										'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'"}');
								else
									$scope.description = tr('LR_INVOICE_CREATE',
										'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'", link: "'+nConstants.url.invoiceDetails( $scope.record.entityID )+'"}');
								break;

							case nConstants.logRecord.operationType.UPDATE:
								if($scope.record.referringToDeletedEntity)
									$scope.description = tr('LR_INVOICE_UPDATE_DEL',
										'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'"}');
								else
									$scope.description = tr('LR_INVOICE_UPDATE',
										'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'", link: "'+nConstants.url.invoiceDetails( $scope.record.entityID )+'"}');
								break;

							case nConstants.logRecord.operationType.SET_PAYED:
								if($scope.record.referringToDeletedEntity)
									$scope.description = tr( details.payedStatus==='true' ? 'LR_INVOICE_SET_PAYED_TRUE_DEL' : 'LR_INVOICE_SET_PAYED_FALSE_DEL',
										'{documentID: "'+ details.documentID + '", clientName: "'+ $sanitize(details.clientName) +'"}');
								else
									$scope.description = tr( details.payedStatus==='true' ? 'LR_INVOICE_SET_PAYED_TRUE' : 'LR_INVOICE_SET_PAYED_FALSE',
										'{documentID: "'+ details.documentID + '", clientName: "'+ $sanitize(details.clientName) + '", link: "'+nConstants.url.invoiceDetails( $scope.record.entityID )+'"}');
								break;

							case nConstants.logRecord.operationType.DELETE:
								$scope.description = tr('LR_INVOICE_DELETE',
									'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'"}');
								break;

							case nConstants.logRecord.operationType.EMAIL:
								if($scope.record.referringToDeletedEntity)
									$scope.description = tr('LR_INVOICE_EMAIL_DEL',
										'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'"}');
								else
									$scope.description = tr('LR_INVOICE_EMAIL',
										'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'", link: "'+nConstants.url.invoiceDetails( $scope.record.entityID )+'"}');
								break;

							default:
								break;
						}
						break;

					case nConstants.logRecord.entityType.ESTIMATION:
						switch ($scope.record.operationType) {
							case nConstants.logRecord.operationType.CREATE:
								if($scope.record.referringToDeletedEntity)
									$scope.description = tr('LR_ESTIMATION_CREATE_DEL',
										'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'"}');
								else
									$scope.description = tr('LR_ESTIMATION_CREATE',
										'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'", link: "'+nConstants.url.estimationDetails( $scope.record.entityID )+'"}');
								break;

							case nConstants.logRecord.operationType.UPDATE:
								if($scope.record.referringToDeletedEntity)
									$scope.description = tr('LR_ESTIMATION_UPDATE_DEL',
										'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'"}');
								else
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
								if($scope.record.referringToDeletedEntity)
									$scope.description = tr('LR_CREDIT_NOTE_CREATE_DEL',
										'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'"}');
								else
									$scope.description = tr('LR_CREDIT_NOTE_CREATE',
										'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'", link: "'+nConstants.url.creditNoteDetails( $scope.record.entityID )+'"}');
								break;

							case nConstants.logRecord.operationType.UPDATE:
								if($scope.record.referringToDeletedEntity)
									$scope.description = tr('LR_CREDIT_NOTE_UPDATE_DEL',
										'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'"}');
								else
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
								if($scope.record.referringToDeletedEntity)
									$scope.description = tr('LR_TRANSPORT_DOCUMENT_CREATE_DEL',
										'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'"}');
								else
									$scope.description = tr('LR_TRANSPORT_DOCUMENT_CREATE',
										'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'", link: "'+nConstants.url.transportDocumentDetails( $scope.record.entityID )+'"}');
								break;

							case nConstants.logRecord.operationType.UPDATE:
								if($scope.record.referringToDeletedEntity)
									$scope.description = tr('LR_TRANSPORT_DOCUMENT_UPDATE_DEL',
										'{documentID: "'+details.documentID+'", clientName: "'+ $sanitize(details.clientName) +'"}');
								else
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

					case nConstants.logRecord.entityType.DOCUMENT_ID_CLASS:
						switch ($scope.record.operationType) {
							case nConstants.logRecord.operationType.CREATE:
								if($scope.record.referringToDeletedEntity)
									$scope.description = tr('LR_DOCUMENT_ID_CLASS_CREATE_DEL',
										'{documentIdClassSuffix: "'+details.documentIdClassSuffix+'"}');
								else
									$scope.description = tr('LR_DOCUMENT_ID_CLASS_CREATE',
										'{documentIdClassSuffix: "'+details.documentIdClassSuffix+'", link: "'+nConstants.url.documentIdClasses()+'"}');
								break;

							case nConstants.logRecord.operationType.UPDATE:
								if($scope.record.referringToDeletedEntity)
									$scope.description = tr('LR_DOCUMENT_ID_CLASS_UPDATE_DEL',
										'{documentIdClassSuffix: "'+details.documentIdClassSuffix+'"}');
								else
									$scope.description = tr('LR_DOCUMENT_ID_CLASS_UPDATE',
										'{documentIdClassSuffix: "'+details.documentIdClassSuffix+'", link: "'+nConstants.url.documentIdClasses()+'"}');
								break;

							case nConstants.logRecord.operationType.DELETE:
								$scope.description = tr('LR_DOCUMENT_ID_CLASS_DELETE',
									'{documentIdClassSuffix: "'+details.documentIdClassSuffix+'"}');
								break;

							default:
								break;
						}
						break;

					case nConstants.logRecord.entityType.PRICE_LIST:
						switch ($scope.record.operationType) {
							case nConstants.logRecord.operationType.CREATE:
								if($scope.record.referringToDeletedEntity)
									$scope.description = tr('LR_PRICE_LIST_CREATE_DEL',
										'{priceListName: "'+ $sanitize(details.priceListName) +'"}');
								else
									$scope.description = tr('LR_PRICE_LIST_CREATE',
										'{priceListName: "'+ $sanitize(details.priceListName) +'", link: "'+nConstants.url.priceListDetails( $scope.record.entityID )+'"}');
								break;

							case nConstants.logRecord.operationType.UPDATE:
								if($scope.record.referringToDeletedEntity)
									$scope.description = tr('LR_PRICE_LIST_UPDATE_DEL',
										'{priceListName: "'+ $sanitize(details.priceListName) +'"}');
								else
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
								if($scope.record.referringToDeletedEntity)
									$scope.description = tr('LR_PAYMENT_TYPE_CREATE_DEL',
										'{paymentName: "'+ $sanitize(details.paymentTypeName) +'"}');
								else
									$scope.description = tr('LR_PAYMENT_TYPE_CREATE',
										'{paymentName: "'+ $sanitize(details.paymentTypeName) +'", link: "'+nConstants.url.paymentList()+'"}');
								break;

							case nConstants.logRecord.operationType.UPDATE:
								if($scope.record.referringToDeletedEntity)
									$scope.description = tr('LR_PAYMENT_TYPE_UPDATE_DEL',
										'{paymentName: "'+ $sanitize(details.paymentTypeName) +'"}');
								else
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
								if($scope.record.referringToDeletedEntity)
									$scope.description = tr('LR_SHARING_PERMIT_CREATE_DEL',
										'{sharingPermitDesc: "'+ $sanitize(details.sharingPermitDesc) +'"}');
								else
									$scope.description = tr('LR_SHARING_PERMIT_CREATE',
										'{sharingPermitDesc: "'+ $sanitize(details.sharingPermitDesc) +'", link: "'+nConstants.url.share()+'"}');
								break;

							case nConstants.logRecord.operationType.DELETE:
								$scope.description = tr('LR_SHARING_PERMIT_DELETE',
									'{sharingPermitDesc: "'+ $sanitize(details.sharingPermitDesc) +'"}');
								break;

							default:
								break;
						}
						break;

					case nConstants.logRecord.entityType.TRANSPORTER:
						switch ($scope.record.operationType) {
							case nConstants.logRecord.operationType.CREATE:
								if($scope.record.referringToDeletedEntity)
									$scope.description = tr('LR_TRANSPORTER_CREATE_DEL',
										'{transporterName: "'+ $sanitize(details.transporterName) +'"}');
								else
									$scope.description = tr('LR_TRANSPORTER_CREATE',
										'{transporterName: "'+ $sanitize(details.transporterName) +'", link: "'+nConstants.url.transporters()+'"}');
								break;

							case nConstants.logRecord.operationType.UPDATE:
								if($scope.record.referringToDeletedEntity)
									$scope.description = tr('LR_TRANSPORTER_UPDATE_DEL',
										'{transporterName: "'+ $sanitize(details.transporterName) +'"}');
								else
									$scope.description = tr('LR_TRANSPORTER_UPDATE',
										'{transporterName: "'+ $sanitize(details.transporterName) +'", link: "'+nConstants.url.transporters()+'"}');
								break;

							case nConstants.logRecord.operationType.DELETE:
								$scope.description = tr('LR_TRANSPORTER_DELETE',
									'{transporterName: "'+ $sanitize(details.transporterName) +'"}');
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
