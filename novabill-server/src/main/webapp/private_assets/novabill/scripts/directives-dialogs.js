'use strict';

angular.module('novabill.directives.dialogs', ['novabill.utils', 'novabill.constants', 'novabill.calc'])


/*
 * Select Transport Documents Dialog
 */
.directive('nSelectTransportDocumentsDialog', ['nConstants', function factory(nConstants){

	return {

		templateUrl: nConstants.conf.partialsBaseUrl+'/directives/n-select-transport-documents-dialog.html',
		scope: {},

		controller : ['$scope', 'nConstants', function($scope, nConstants){

			$scope.$on(nConstants.events.SHOW_TRANSPORT_DOCUMENTS_DIALOG, 
					function(event, clientId, preSelectedId){

				var currentYear = new Date().getFullYear().toString();

				GWT_Server.transportDocument.getAllForClient(clientId, currentYear, {

					onSuccess : function(result){
						$scope.$apply(function(){
							$scope.selectedSet = {};
							$scope.selectedSet[preSelectedId] = true;
							$scope.docs = result.transportDocuments;

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
				window.open(nConstants.url.trasportDocumentDetails( id ), '_blank');
			};

			$scope.ok = function(){
				var ids = new Array();
				for(var id in $scope.selectedSet){
					ids.push(id);
				}

				window.location.assign(nConstants.url.invoiceFromTransportDocumentList( 
						encodeURIComponent( 
								JSON.stringify({
									list : ids
								}) ) ));
			};


		}],

		restrict: 'E',
		replace: true,

	};

}])


/*
 * Edit Commodity Dialog
 */
.directive('nEditCommodityDialog', ['nConstants', function factory(nConstants){

	return {

		templateUrl: nConstants.conf.partialsBaseUrl+'/directives/n-edit-commodity-dialog.html',
		scope: {
			commodity : '=?',
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
		replace: true,

	};

}])



/*
 * Edit Price List Dialog
 */
.directive('nEditPriceListDialog', ['nConstants', function factory(nConstants){

	return {

		templateUrl: nConstants.conf.partialsBaseUrl+'/directives/n-edit-price-list-dialog.html',
		scope: {
			priceList : '=?',
		},

		controller : ['$scope', 'nConstants', function($scope, nConstants){

			$scope.$on(nConstants.events.SHOW_EDIT_PRICE_LIST_DIALOG, 
					function(event, editMode, callback){
				$scope.editMode = editMode;
				$scope.callback = callback;

				$('#editPriceListDialog').modal('show');
			});

			function hideAndReset(){
				if(!$scope.editMode){
					$scope.priceList = null;
				}

				$('#editPriceListDialog').modal('hide');

				// workaround - see http://stackoverflow.com/questions/11519660/twitter-bootstrap-modal-backdrop-doesnt-disappear
				$('body').removeClass('modal-open');
				$('.modal-backdrop').remove();

				$scope.contactingServer = false;
				$scope.invalidIdentifier = false;
				$scope.form.$setPristine();
			};

			$scope.save = function(){
				$scope.contactingServer = true;

				// persist the commodity
				$scope.callback.onSave(
						$scope.priceList, 
						{
							finish : hideAndReset,

							invalidIdentifier : function(){ 
								$scope.$apply(function(){
									$scope.contactingServer = false;
									$scope.invalidIdentifier = true;
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
		replace: true,

	};

}])


/*
 * Removal Dialog
 */
.directive('nRemovalDialog', ['nConstants', function factory(nConstants){

	return {

		templateUrl: nConstants.conf.partialsBaseUrl+'/directives/n-confirm-removal-dialog.html',
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
		replace: true,

	};

}])


/*
 * Select Client Dialog
 */
.directive('nSelectClientDialog', ['nConstants', function factory(nConstants){

	return {

		templateUrl: nConstants.conf.partialsBaseUrl+'/directives/n-select-client-dialog.html',
		scope: {},

		controller : ['$scope', 'nConstants', 'nSorting', function($scope, nConstants, nSorting){

			$scope.selectedId = null;

			$scope.select = function(id){
				$scope.selectedId = id;
			};

			$scope.$on(nConstants.events.SHOW_SELECT_CLIENT_DIALOG, function(event, callback){
				$scope.callback = callback;

				GWT_Server.business.getClients(nConstants.conf.businessId, {

					onSuccess : function(data){
						$scope.$apply(function(){

							data.clients.sort( nSorting.clientsComparator );
							$scope.clients = data.clients;

							$('#selectClientDialog').modal('show');
							$('#selectClientDialog .scroller').slimScroll({
								height: '400px'
							});
						});
					},

					onFailure : function(){}

				});
			});

			function hide(){
				$scope.selectedId = null;
				$scope.query = null;

				$('#selectClientDialog').modal('hide');

				// workaround - see http://stackoverflow.com/questions/11519660/twitter-bootstrap-modal-backdrop-doesnt-disappear
				$('body').removeClass('modal-open');
				$('.modal-backdrop').remove();
			};

			$scope.ok = function(){
				$scope.callback.onOk($scope.selectedId);
				hide();
			};

			$scope.cancel = function(){
				$scope.callback.onCancel();
				hide();
			};
		}],

		restrict: 'E',
		replace: true,

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
.directive('nSelectCommodityDialog', ['nConstants', function factory(nConstants){

	return {

		templateUrl: nConstants.conf.partialsBaseUrl+'/directives/n-select-commodity-dialog.html',
		scope: {
			gwtHook : '@?'
		},

		controller : ['$scope', 'nConstants', 'nSorting', 'nCalc', function($scope, nConstants, nSorting, nCalc){

			if($scope.gwtHook) {
				window.GWT_Hook_nSelectCommodityDialog = function(clientId, callback){
					$scope.$broadcast(nConstants.events.SHOW_SELECT_COMMODITY_DIALOG, clientId, callback);
				};
			}

			function loadPriceList(id){
				GWT_Server.priceList.get(id, {

					onSuccess : function(priceList){
						$scope.$apply(function(){
							$scope.priceList = priceList;
						});
					},
					onFailure : function(){}
				});
			}

			$scope.selectCommodity = function(commodity){
				$scope.selectedCommodity = commodity;
			};

			$scope.changePriceList = function(){
				loadPriceList($scope.selectedPriceList);
			};

			$scope.$on(nConstants.events.SHOW_SELECT_COMMODITY_DIALOG, function(event, clientId, callback){
				$scope.callback = callback;

				GWT_Server.batchDataFetcher.fetchSelectCommodityForDocItemOpData(String(clientId), {

					onSuccess : function(tupleData){
						$scope.$apply(function(){
							$scope.priceList = tupleData.first;
							$scope.listOfPriceLists = tupleData.second.list.sort(nSorting.priceListsComparator);
							$scope.selectedPriceList = $scope.priceList.id;

							$('#selectCommodityDialog').modal('show');
							$('#selectCommodityDialog .scroller').slimScroll({
								height: '400px'
							});
						});
					},

					onFailure : function(){}
				});

			});


			function hide(){
				$scope.selectedCommodity = null;
				$scope.query = null;
				$scope.priceList = null;
				$scope.listOfPriceLists = null;

				$('#selectCommodityDialog').modal('hide');

				// workaround - see http://stackoverflow.com/questions/11519660/twitter-bootstrap-modal-backdrop-doesnt-disappear
				$('body').removeClass('modal-open');
				$('.modal-backdrop').remove();
			};

			$scope.ok = function(){
				$scope.callback.onOk(
						$scope.selectedCommodity, 
						nCalc.calculatePriceForCommodity($scope.selectedCommodity, $scope.priceList.name)
				);
				hide();
			};

			$scope.cancel = function(){
				$scope.callback.onCancel();
				hide();
			};
		}],

		restrict: 'E',
		replace: true,

	};

}]);
