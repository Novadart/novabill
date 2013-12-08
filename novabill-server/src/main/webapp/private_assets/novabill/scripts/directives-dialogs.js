'use strict';

angular.module('novabill.directives.dialogs', ['novabill.utils', 'novabill.constants'])


/*
 * Select Transport Documents Dialog
 */
.directive('nSelectTransportDocumentsDialog', function factory(){

	return {

		templateUrl: NovabillConf.partialsBaseUrl+'/directives/n-select-transport-documents-dialog.html',
		scope: {},

		controller : ['$scope', 'nConstants', function($scope, nConstants){
			
			function show(){
				$('#editCommodityDialog').modal('show');
			};
			
			
			$scope.$on(nConstants.events.SHOW_TRANSPORT_DOCUMENTS_DIALOG, 
					function(event, clientId, preSelectedId){
				
				var currentYear = new Date().getFullYear();
				
				GWT_Server.transportDocument.getAllForClient(clientId, currentYear, {
					
					onSuccess : function(result){
						$scope.$apply(function(){
							$scope.docs = result;
							show();
						});
					},
					
					onFailure : function(){}
					
				});
			});
			
			
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
					function(event, keepCommodityOnClose, callback){
				$scope.keepCommodityOnClose = keepCommodityOnClose;
				$scope.callback = callback;
				
				$('#editCommodityDialog').modal('show');
			});
			
			//init commodity, if not present, to avoid calls to $watch that will reset service and price
			$scope.commodity = $scope['commodity'] === undefined ? {} : $scope.commodity;
						
			$scope.$watch('commodity', function() {
				
				//if prices map is empty, init it
				if( $scope.commodity ){
					$scope.price = $scope.commodity.pricesMap 
						? $scope.commodity.pricesMap.prices[ NovabillConf.defaultPriceListName ].priceValue 
								: null;
					
					// NOTE we check for id to workaround GWT removing the property when it is false
					$scope.service = ($scope.commodity.service === undefined  
							? ($scope.commodity.id === undefined || $scope.commodity.id === null ? null : 'false') 
									: $scope.commodity.service.toString());
				}
				
			});
			
			function hide(){
				$('#editCommodityDialog').modal('hide');
				
				// workaround - see http://stackoverflow.com/questions/11519660/twitter-bootstrap-modal-backdrop-doesnt-disappear
				$('body').removeClass('modal-open');
				$('.modal-backdrop').remove();
			};
			
			function hideAndReset(){
				if(!$scope.keepCommodityOnClose){
					$scope.commodity = null;
					$scope.price = null;
					$scope.service = null;
				}
				hide();
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
