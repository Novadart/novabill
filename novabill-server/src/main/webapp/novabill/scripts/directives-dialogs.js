'use strict';

angular.module('novabill.directives.dialogs', ['novabill.utils', 'novabill.constants', 'novabill.calc', 'ui.bootstrap'])


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
 * Select Transport Documents Dialog
 */
.directive('nSelectTransportDocumentsDialog', ['nConstants', function factory(nConstants){

	return {

		templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-select-transport-documents-dialog.html'),
		scope: {},

		controller : ['$scope', 'nConstants', function($scope, nConstants){

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

			$scope.$on(nConstants.events.SHOW_TRANSPORT_DOCUMENTS_DIALOG, 
					function(event, clientId, preSelectedId){

				var currentYear = new Date().getFullYear().toString();

				GWT_Server.transportDocument.getAllForClient(clientId, currentYear, {

					onSuccess : function(result){
						$scope.$apply(function(){
							$scope.selectedSet = {};
							$scope.selectedSet[preSelectedId] = true;
							$scope.docs = partitionTransportDocuments(result.transportDocuments);

							$('#selectTransportDocumentsDialog').modal('show');
							$('#selectTransportDocumentsDialog .scroller').slimScroll({
								height: '400px'
							});
						});
					},

					onFailure : function(){}

				});
			});


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

				window.location.assign(nConstants.url.invoiceFromTransportDocumentList( 
						encodeURIComponent( 
								angular.toJson({
									list : ids
								}) ) ));
			};


		}],

		restrict: 'E',
		replace: true

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
					
					$scope.onCountryChange = function(){
						if($scope.address.country !== 'IT'){
							$scope.address.province = null;
						}
					};

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
 * Edit Commodity Dialog
 */
.directive('nEditCommodityDialog', ['nConstants', function factory(nConstants){

	return {

		templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-edit-commodity-dialog.html'),
		scope: {
			commodity : '=?'
		},

		controller : ['$scope', 'nConstants', function($scope, nConstants){

			$scope.$on(nConstants.events.SHOW_EDIT_COMMODITY_DIALOG, 
					function(event, editMode, callback){
				$scope.editMode = editMode;
				$scope.callback = callback;

				$('#editCommodityDialog').modal('show');
			});

			//init commodity, if not present, to avoid calls to $watch that will reset service and price
			$scope.commodity = $scope['commodity'] === undefined ? {} : $scope.commodity;

			$scope.$watch('commodity', function() {

				//if prices map is empty, init it
				if( $scope.commodity ){
					$scope.price = $scope.commodity.pricesMap && $scope.commodity.pricesMap.prices[ nConstants.conf.defaultPriceListName ]
					? $scope.commodity.pricesMap.prices[ nConstants.conf.defaultPriceListName ].priceValue 
							: null;

					// NOTE we check for id to workaround GWT removing the property when it is false
					$scope.service = ($scope.commodity.service === undefined  
							? ($scope.commodity.id === undefined || $scope.commodity.id === null ? null : 'false') 
									: $scope.commodity.service.toString());
				}

			});

			function hideAndReset(){
				if(!$scope.editMode){
					$scope.commodity = null;
					$scope.price = null;
					$scope.service = null;
				}

				$('#editCommodityDialog').modal('hide');

				// workaround - see http://stackoverflow.com/questions/11519660/twitter-bootstrap-modal-backdrop-doesnt-disappear
				$('body').removeClass('modal-open');
				$('.modal-backdrop').remove();

				$scope.invalidSku = false;
				$scope.contactingServer = false;
				$scope.form.$setPristine();
			};

			$scope.save = function(){
				$scope.contactingServer = true;

				// update service property
				$scope.commodity.service = $scope.service==='true';

				//set weight to null in case we are dealing with a service
				$scope.commodity.weight = $scope.commodity.service ? null : ($scope.commodity.weight ? new String($scope.commodity.weight) : null);

				// if default price is not present, build the structure for storing it
				if(!$scope.commodity.pricesMap){
					$scope.commodity['pricesMap'] = { prices : {} };
					$scope.commodity['pricesMap']['prices'][nConstants.conf.defaultPriceListName] = {
							priceValue : null,
							priceType : 'FIXED'
					};
				}

				// update default price
				$scope.commodity['pricesMap']['prices'][nConstants.conf.defaultPriceListName].priceValue = $scope.price;

				// persist the commodity
				$scope.callback.onSave(
						$scope.commodity, 
						{
							finish : hideAndReset,

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
				$scope.callback.onCancel();
			};

		}],

		restrict: 'E',
		replace: true

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
.factory('nEditSharingPermitDialog', ['nConstants', '$modal', function (nConstants, $modal){

	return {
		open : function( sharingPermit ) {

			return $modal.open({

				templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-edit-sharing-permit-dialog.html'),

				controller: ['$scope', '$modalInstance',
				             function($scope, $modalInstance){
					
					$scope.sharingPermit = sharingPermit;
					$scope.sendEmail = false;

					$scope.save = function(){
						$modalInstance.close({
							sharingPermit : $scope.sharingPermit,
							sendEmail : $scope.sendEmail
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
 * Removal Dialog
 */
.directive('nRemovalDialog', ['nConstants', function factory(nConstants){

	return {

		templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-confirm-removal-dialog.html'),
		scope: {},

		controller : ['$scope', 'nConstants', function($scope, nConstants){

			$scope.$on(nConstants.events.SHOW_REMOVAL_DIALOG, function(event, message, callback){
				$scope.message = message;
				$scope.callback = callback;

				$('#removalDialog').modal('show');
			});

			function hide(){
				$('#removalDialog').modal('hide');

				// workaround - see http://stackoverflow.com/questions/11519660/twitter-bootstrap-modal-backdrop-doesnt-disappear
				$('body').removeClass('modal-open');
				$('.modal-backdrop').remove();
			};

			$scope.ok = function(){
				hide();
				$scope.callback.onOk();
			};

			$scope.cancel = function(){
				hide();
				$scope.callback.onCancel();
			};
		}],

		restrict: 'E',
		replace: true

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

				controller: ['$scope', 'nConstants', 'nSorting', '$filter', '$modalInstance',
				             function($scope, nConstants, nSorting, $filter, $modalInstance){

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
						if($scope.newClientMode) {
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
							
						} else {
							$scope.newClientMode = true;
							filteredClients = [];
							$scope.query = '';
							updateFilteredClients();
						}
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

					GWT_Server.business.getClients(nConstants.conf.businessId, {

						onSuccess : function(data){
							$scope.$apply(function(){

								loadedClients = data.clients.sort( nSorting.clientsComparator );
								updateFilteredClients();

							});
						},

						onFailure : function(){}

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
							defaultPriceValue : defaultPrice.priceValue,
							priceListName : $scope.priceList.name,
							priceType : price.priceType,
							priceValue : price.priceValue
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


.factory('gwtHook', ['nSelectCommodityDialog', function(nSelectCommodityDialog) {
	return {

		injectSelectCommodityDialogHook : function(){
			window.GWT_Hook_nSelectCommodityDialog = function(clientId){
				return nSelectCommodityDialog.open(clientId);
			};
		}
	};
}]);
