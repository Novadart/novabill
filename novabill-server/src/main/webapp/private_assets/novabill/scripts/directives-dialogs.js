'use strict';

angular.module('novabill.directives.dialogs', ['novabill.utils', 'novabill.constants'])


/*
 * Select Transport Documents Dialog
 */
.directive('nSelectTransportDocumentsDialog', function factory(){

	return {

		templateUrl: NovabillConf.partialsBaseUrl+'/directives/n-select-transport-documents-dialog.html',
		scope: {},

		controller : ['$scope', 'nConstants', '$element', function($scope, nConstants, $element){
			
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
				var ids = [];
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

})


/*
 * Edit Commodity Dialog
 */
.directive('nEditCommodityDialog', function factory(){

	return {

		templateUrl: NovabillConf.partialsBaseUrl+'/directives/n-edit-commodity-dialog.html',
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
					$scope.price = $scope.commodity.pricesMap && $scope.commodity.pricesMap.prices[ NovabillConf.defaultPriceListName ]
						? $scope.commodity.pricesMap.prices[ NovabillConf.defaultPriceListName ].priceValue 
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
					$scope.commodity['pricesMap']['prices'][NovabillConf.defaultPriceListName] = {
							priceValue : null,
							priceType : 'FIXED'
					};
				}

				// update default price
				$scope.commodity['pricesMap']['prices'][NovabillConf.defaultPriceListName].priceValue = $scope.price;

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

})



/*
 * Edit Price List Dialog
 */
.directive('nEditPriceListDialog', function factory(){

	return {

		templateUrl: NovabillConf.partialsBaseUrl+'/directives/n-edit-price-list-dialog.html',
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
				
				$scope.form.$setPristine();
			};

			$scope.save = function(){

				// persist the commodity
				$scope.callback.onSave(
						$scope.priceList, 
						{
							finish : hideAndReset
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

})


/*
 * Removal Dialog
 */
.directive('nRemovalDialog', function factory(){

	return {

		templateUrl: NovabillConf.partialsBaseUrl+'/directives/n-confirm-removal-dialog.html',
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

});
