'use strict';

angular.module('novabill.directives.dialogs', 
		['novabill.directives.validation', 'novabill.directives.forms', 
		 'novabill.utils', 'novabill.ajax', 'novabill.constants', 'novabill.calc', 'ui.bootstrap'])


/*
 * Edit Price List Dialog
 */
.factory('nAlertDialog', ['nConstants', '$modal', function (nConstants, $modal){

	return {
		open : function( message ) {

			return $modal.open({

				templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-alert-dialog.html'),

				controller: ['$scope', '$modalInstance',
				             function($scope, $modalInstance){

					$scope.message = message;

					$scope.ok = function(){
						$modalInstance.close();
					};

				}]
			});
		}
	};
}])


/*
 * Edit Address Dialog
 */
.factory('nSelectTransportDocumentsDialog', ['nConstants', '$modal', function (nConstants, $modal){

	return {
		open : function( clientId, preSelectedId ) {

			return $modal.open({

				templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-select-transport-documents-dialog.html'),

				controller: ['$scope', '$modalInstance',
				             function($scope, $modalInstance){
					
					var currentYear = new Date().getFullYear().toString();

					GWT_Server.transportDocument.getAllForClient(clientId, currentYear, {

						onSuccess : function(result){
							$scope.$apply(function(){
								$scope.selectedSet = {};
								$scope.selectedSet[preSelectedId] = true;
								$scope.docs = partitionTransportDocuments(result.transportDocuments);

								$('.n-select-transport-documents-dialog .scroller').slimScroll({
									height: '400px'
								});
							});
						},

						onFailure : function(){}

					});
					
					
					function partitionTransportDocuments(docs){
						var result = {
								neverIncludedInInvoice : [],
								alreadyIncludedInInvoice : []
						};

						angular.forEach(docs, function(tDoc, key){
							if(tDoc.invoice){
								result.alreadyIncludedInInvoice.push(tDoc);
							} else {
								result.neverIncludedInInvoice.push(tDoc);
							}
						});

						return result;
					}

					/**
					 * This function keeps updated a set of selected values.
					 * If the set is empty, it sets it to null for easier evaluation in the view 
					 */
					$scope.toggleElement = function(id){
						if(!$scope.selectedSet){
							$scope.selectedSet = {};
						}

						if($scope.selectedSet[id]){
							delete $scope.selectedSet[id];

							if(angular.element.isEmptyObject($scope.selectedSet)){
								$scope.selectedSet = null;
							}
						} else {
							$scope.selectedSet[id] = true;
						}
					};

					$scope.openUrl = function($event, id){
						$event.stopPropagation();
						window.open(nConstants.url.transportDocumentDetails( id ), '_blank');
					};

					$scope.openInvoice = function($event, invoiceId){
						$event.stopPropagation();
						window.open(nConstants.url.invoiceDetails( invoiceId ), '_blank');
					};

					$scope.ok = function(){
						var ids = new Array();
						for(var id in $scope.selectedSet){
							ids.push(id);
						}

						$modalInstance.close();
						
						window.location.assign(nConstants.url.invoiceFromTransportDocumentList( 
								encodeURIComponent( 
										angular.toJson({
											list : ids
										}) ) ));
					};
					
					$scope.cancel = function(){
						$modalInstance.dismiss();
					};

				}]
			});
		}
	};
}])



/*
 * Send Email Dialog
 */
.factory('nSendEmailDialog', ['nConstants', '$modal', function (nConstants, $modal){

	return {
		open : function( invoice ) {

			return $modal.open({

				templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-send-email-dialog.html'),

				controller: ['$scope', '$modalInstance', '$filter',
				             function($scope, $modalInstance, $filter){
					
					$scope.data = {
							to : invoice.client.email,
							replyTo : invoice.business.email,
							message : $filter('nEmailKeywords')(invoice.business.settings.emailText, invoice),
							subject : $filter('nEmailKeywords')(invoice.business.settings.emailSubject, invoice)
					};
					
					$scope.save = function(){
						$modalInstance.close({
							invoiceID : invoice.id,
							payload : $scope.data
						});
					};

					$scope.cancel = function(){
						$modalInstance.dismiss();
					};
				}]
			});
		}
	};
}])



/*
 * Email Preview Dialog
 */
.factory('nEmailPreviewDialog', ['nConstants', '$modal', function (nConstants, $modal){

	return {
		open : function( business ) {

			return $modal.open({

				templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-email-preview-dialog.html'),

				controller: ['$scope', '$modalInstance', '$filter', '$sce',
				             function($scope, $modalInstance, $filter, $sce){
					
					$scope.subject = business.settings.emailSubject;
					$scope.text = business.settings.emailText;
					
					$scope.save = function(){
						$modalInstance.close();
					};

					$scope.cancel = function(){
						$modalInstance.dismiss();
					};
				}]
			});
		}
	};
}])




/*
 * Edit Address Dialog
 */
.factory('nEditAddressDialog', ['nConstants', '$modal', function (nConstants, $modal){

	return {
		open : function( address ) {

			return $modal.open({

				templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-edit-address-dialog.html'),

				controller: ['$scope', '$modalInstance',
				             function($scope, $modalInstance){
					
					$scope.address = angular.copy(address);
					
					$scope.save = function(){
						$modalInstance.close($scope.address);
					};

					$scope.cancel = function(){
						$modalInstance.dismiss();
					};
				}]
			});
		}
	};
}])



/*
 * Edit Address Dialog
 */
.factory('nEditTransporterDialog', ['nConstants', '$modal', function (nConstants, $modal){

	return {
		open : function( transporter ) {

			return $modal.open({

				templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-edit-transporter-dialog.html'),

				controller: ['$scope', '$modalInstance',
				             function($scope, $modalInstance){
					
					$scope.transporter = angular.copy(transporter);
					
					$scope.save = function(){
						$modalInstance.close($scope.transporter);
					};

					$scope.cancel = function(){
						$modalInstance.dismiss();
					};
				}]
			});
		}
	};
}])



/*
 * Edit Commodity Dialog
 */


.factory('nEditCommodityDialog', ['nConstants', '$modal', function (nConstants, $modal){

	return {
		open : function( commodity, invalidSku, editMode ) {

			return $modal.open({

				templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-edit-commodity-dialog.html'),

				controller: ['$scope', '$modalInstance',
				             function($scope, $modalInstance){
					
					$scope.editMode = editMode;
					$scope.invalidSku = invalidSku;

					$scope.$watch('commodity', function() {

						//if prices map is empty, init it
						if( $scope.commodity ){
							$scope.price = $scope.commodity.prices ? $scope.commodity.prices[ nConstants.conf.defaultPriceListName ].priceValue : null;
							$scope.service = String( $scope.commodity.service );
						}

					});
					
					//init commodity
					$scope.commodity = angular.copy( commodity );

					$scope.save = function(){
						// update service property
						$scope.commodity.service = $scope.service==='true';

						//set weight to null in case we are dealing with a service
						$scope.commodity.weight = $scope.commodity.service 
								? null 
								: ($scope.commodity.weight ? new String($scope.commodity.weight) : null);

						// if default price is not present, build the structure for storing it
						if(!$scope.commodity.prices){
							$scope.commodity.prices = {};
							$scope.commodity.prices[nConstants.conf.defaultPriceListName] = {
									priceValue : null,
									priceType : 'FIXED'
							};
						}

						// update default price
						$scope.commodity.prices[nConstants.conf.defaultPriceListName].priceValue = $scope.price;

						// persist the commodity
						$modalInstance.close( $scope.commodity );
					};

					$scope.cancel = function(){
						$modalInstance.dismiss();
					};

				}]
			});
		}
	};
}])


/*
 * Edit Price List Dialog
 */
.factory('nEditPriceListDialog', ['nConstants', '$modal', function (nConstants, $modal){

	return {
		open : function( priceList, invalidIdentifier ) {

			return $modal.open({

				templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-edit-price-list-dialog.html'),

				controller: ['$scope', '$modalInstance',
				             function($scope, $modalInstance){

					$scope.invalidIdentifier = invalidIdentifier;
					$scope.priceList = angular.copy(priceList);

					$scope.save = function(){
						$modalInstance.close($scope.priceList);
					};

					$scope.cancel = function(){
						$modalInstance.dismiss();
					};
				}]
			});
		}
	};
}])


/*
 * Edit Sharing Permit Dialog
 */
.factory('nEditSharingPermitDialog', ['nConstants', '$modal', '$sce', '$filter',
                                      function (nConstants, $modal, $sce, $filter){

	return {
		open : function( sharingPermit, invalidEmail ) {

			return $modal.open({

				templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-edit-sharing-permit-dialog.html'),

				controller: ['$scope', '$modalInstance',
				             function($scope, $modalInstance){
					
					$scope.sharingPermit = sharingPermit;
					$scope.invalidEmail = invalidEmail;
					$scope.sendEmail = 'false';
					
					$scope.dialogInfo = $sce.trustAsHtml( $filter('translate')('SHARING_PERMIT_DIALOG_INFO') );

					$scope.save = function(){
						$modalInstance.close({
							sharingPermit : $scope.sharingPermit,
							sendEmail : $scope.sendEmail !== 'false'
						});
					};

					$scope.cancel = function(){
						$modalInstance.dismiss();
					};
				}]
			});
		}
	};
}])


/*
 * Confirm Dialog
 */
.factory('nConfirmDialog', ['nConstants', '$modal', function factory(nConstants,$modal){
	
	return {
		open : function( message ) {

			return $modal.open({

				templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-confirm-dialog.html'),

				controller: ['$scope', '$modalInstance',
				             function($scope, $modalInstance){
					
					$scope.message = message;
					
					$scope.ok = function(){
						$modalInstance.close(true);
					};

					$scope.cancel = function(){
						$modalInstance.close(false);
					};

				}]
			});
		}
	};

}])


/*
 * Select Client Dialog
 */
.factory('nSelectClientDialog', ['nConstants', '$modal', function (nConstants, $modal){

	return {
		open : function(allowNewClient) {

			var instance = $modal.open({

				templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-select-client-dialog.html'),

				controller: ['$scope', 'nConstants', 'nSorting', '$filter', '$modalInstance', 'nAjax',
				             function($scope, nConstants, nSorting, $filter, $modalInstance, nAjax){

					var loadedClients = new Array();
					var filteredClients = new Array();
					$scope.allowNewClient = allowNewClient;

					function updateFilteredClients(){
						if($scope.newClientMode){
							if(!$scope.query) {
								filteredClients = [];
							} else {
								filteredClients = $filter('filter')(loadedClients, $scope.query);
							}
						} else {
							filteredClients = $filter('filter')(loadedClients, $scope.query);
						}
						$scope.clients = filteredClients.slice(0, 15);
					}

					$scope.$watch('query', function(newValue, oldValue){
						updateFilteredClients();
					});

					$scope.loadMoreClients = function(){
						if($scope.clients){
							var currentIndex = $scope.clients.length;
							$scope.clients = $scope.clients.concat(filteredClients.slice(currentIndex, currentIndex+30));
						}
					};

					$scope.newClientClick = function(){
						$scope.newClientMode = true;
						filteredClients = [];
						$scope.query = '';
						updateFilteredClients();
					};
					
					$scope.createNewClient = function(){
						var newClient = {
								name : $scope.query,
								contact : {}
						};
						GWT_Server.client.add(nConstants.conf.businessId, angular.toJson(newClient), {

							onSuccess : function(newId){
								$scope.$apply(function(){
									$modalInstance.close(newId);
								});
							},

							onFailure : function(){}

						});
					};

					$scope.cancelNewClientClick = function(){
						$scope.newClientMode = false;
						updateFilteredClients();
					};

					$scope.select = function(id){
						$modalInstance.close(id);
					};

					$scope.cancel = function(){
						$modalInstance.dismiss();
					};

					nAjax.Business().getClients(function(clients){
						loadedClients = clients.sort( nSorting.clientsComparator );
						updateFilteredClients();
					});

				}]
			});

			instance.opened.then(function(){
				//workaround: this code gets executed after the dialog has been drawn on the screen
				setTimeout(function(){
					$('.n-select-client-dialog .scroller').slimScroll({
						height: '350px'
					});
				}, 1);

			});

			return instance;

		}
	};
}])




/*
 * Commodity Price Dialog
 */
.factory('nCommodityPriceDialog', ['nConstants', '$modal', function (nConstants, $modal){

	return {
		open : function(priceListName) {

			var instance = $modal.open({

				templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-commodity-price-dialog.html'),

				controller: ['$scope', 'nConstants', '$modalInstance',
				             function($scope, nConstants, $modalInstance){

					$scope.isDefaultPriceList = nConstants.conf.defaultPriceListName === priceListName;
					$scope.priceType = $scope.isDefaultPriceList ? nConstants.priceType.FIXED : null;

					$scope.cancel = function(){
						$modalInstance.dismiss();
					};

					$scope.ok = function(){
						$modalInstance.close({
							priceType : $scope.priceType,
							priceValue : $scope.priceValue
						});
					};

				}]
			});

			return instance;

		}
	};
}])




/*
 * Select Commodity Dialog
 * 
 * This dialog is used to select a commodity that is then used in the creation of an invoice or another document.
 * The dialog loads the prices for the default price list assigned to the user.
 * Once the user has selected a commodity, the details of such commodity are inserted in the document (sku, description, tax, price).
 * It is possible to change price list within the dialog, to browse other price lists.
 */
.factory('nSelectCommodityDialog', ['nConstants', '$modal', function (nConstants, $modal){

	return {
		open : function(clientId) {

			var instance = $modal.open({

				templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-select-commodity-dialog.html'),

				controller : ['$scope', 'nConstants', 'nSorting', 'nCalc', '$filter', '$http', '$modalInstance',
				              function($scope, nConstants, nSorting, nCalc, $filter, $http, $modalInstance) {

					var loadedCommodities = new Array();
					var filteredCommodities = new Array();

					function updateFilteredCommodities(){
						filteredCommodities = $filter('filter')(loadedCommodities, $scope.query);
						$scope.commodities = filteredCommodities.slice(0, 15);
						$scope.selectedCommodity = null;
					}

					$scope.$watch('query', function(newValue, oldValue){
						updateFilteredCommodities();
					});

					$scope.loadMoreCommodities = function(){
						if($scope.commodities){
							var currentIndex = $scope.commodities.length;
							$scope.commodities = $scope.commodities.concat(filteredCommodities.slice(currentIndex, currentIndex+50));
						}
					};

					function loadPriceList(id){
						$http.get(nConstants.conf.privateAreaBaseUrl+'json/pricelists/'+id)
						.success(function(data, status){
							loadedCommodities = data.commodities.sort(nSorting.descriptionComparator);
							$scope.priceList = data;
							updateFilteredCommodities();
						});
					}

					$scope.selectCommodity = function(commodity){
						var defaultPrice = commodity.prices[nConstants.conf.defaultPriceListName];
						var price = commodity.prices[$scope.priceList.name];

						$modalInstance.close({
							commodity : commodity,
							priceListName : $scope.priceList.name
						});
					};

					$scope.changePriceList = function(){
						loadPriceList($scope.selectedPriceList);
					};

					function hide(){
						$scope.selectedCommodity = null;
						$scope.query = null;
						$scope.priceList = null;
						$scope.listOfPriceLists = null;
					};

					$scope.cancel = function(){
						$modalInstance.dismiss();
					};

					$http.get(nConstants.conf.privateAreaBaseUrl+'json/comm-select-data/'+String(clientId))
					.success(function(data, status){
						//orig
						var priceList = data.first;
						loadedCommodities = priceList.commodities.sort(nSorting.descriptionComparator);
						$scope.priceList = priceList;
						updateFilteredCommodities();

						$scope.listOfPriceLists = data.second.sort(nSorting.priceListsComparator);
						$scope.selectedPriceList = $scope.priceList.id;
					});

				}]
			});

			instance.opened.then(function(){
				//workaround: this code gets executed after the dialog has been drawn on the screen
				setTimeout(function(){
					$('.n-select-commodity-dialog .scroller').slimScroll({
						height: '350px'
					});
				}, 1);

			});

			return instance;

		}
	};
}])


/*
 * Edit Price List Dialog
 */
.factory('nHelloDialog', ['nConstants', '$modal', function (nConstants, $modal){

	return {
		open : function( business, callback ) {

			return $modal.open({

				templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-hello-dialog.html'),
				size : 'lg',
				keyboard : false,
				backdrop : 'static',
				controller: ['$scope', '$modalInstance', '$sce', '$filter',
				             function($scope, $modalInstance, $sce, $filter){

					$scope.helloAlert = $sce.trustAsHtml( $filter('translate')('HELLO_DIALOG_ALERT') );
					$scope.business = business;

					$scope.businessUpdateCallback = function(){
						callback();
					};
					
					$scope.ok = function(){
						$modalInstance.close();
					};

				}]
			});
		}
	};
}])



/*
 * Edit Price List Dialog
 */
.factory('nUpgradeSuggestionDialog', ['nConstants', '$modal', function (nConstants, $modal){

	return {
		open : function() {

			return $modal.open({

				templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-upgrade-suggestion-dialog.html'),
				keyboard : false,
				backdrop : 'static',
				controller: ['$scope', '$modalInstance', 'nConstants', '$window',
				             function($scope, $modalInstance, nConstants, $window){
					
					
					$scope.premiumUrl = nConstants.conf.premiumUrl;
					
					$scope.skip = function(){
						$window.location.reload();
					};

				}]
			});
		}
	};
}])


/*
 * Recommend By Email Dialog
 */
.factory('nRecommendByEmailDialog', ['nConstants', '$modal', function (nConstants, $modal){

	return {
		open : function() {

			return $modal.open({

				templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-recommend-dialog.html'),

				controller: ['$scope', '$modalInstance', '$filter',
				             function($scope, $modalInstance, $filter){
					
					$scope.data = {};
					
					$scope.save = function(){
						$modalInstance.close({
							payload : $scope.data
						});
					};

					$scope.cancel = function(){
						$modalInstance.dismiss();
					};
				}]
			});
		}
	};
}])


/*
 * Exposing few dialogs used by GWT
 */
.run(['nAlertDialog', 'nConfirmDialog', 'nSelectCommodityDialog', '$window', '$rootScope', '$compile',
      function(nAlertDialog, nConfirmDialog, nSelectCommodityDialog, $window, $rootScope, $compile){
	$window.Angular_Dialogs = {
			
			confirm : function(message, callback){
				var instance = nConfirmDialog.open(message);
				instance.result.then(function(value){
					if(callback){
						callback(value);
					}
				});
			},
			
			alert : function(message, callback){
				var instance = nAlertDialog.open(message);
				instance.result.then(function(){
					if(callback){
						callback();
					}
				});
			},
			
			selectCommodityDialog : function(clientId){
				return nSelectCommodityDialog.open(clientId);
			}
			
	};
	
	
	$window.Angular_ItemFormInit = function(){
		var elm = angular.element('n-item-form');
		var html = $compile( elm )($rootScope);
		angular.element('.angularJSItemForm').html(html);
	};
	
}]);
