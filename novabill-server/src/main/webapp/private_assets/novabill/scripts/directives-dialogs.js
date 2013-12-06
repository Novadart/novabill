'use strict';

angular.module('novabill.directives.dialogs', ['novabill.utils'])

/*
 * Edit Commodity Dialog
 */
.directive('editCommodityDialog', function factory(){

	return {

		templateUrl: NovabillConf.partialsBaseUrl+'/directives/edit-commodity-dialog.html',
		scope: {
			commodity : '=?',
		},

		controller : function($scope, NEditCommodityDialogAPI){
			$scope.api = NEditCommodityDialogAPI;
			
			//init commodity, if not present, to avoid calls to $watch that will reset service and price
			$scope.commodity = $scope['commodity'] === undefined ? {} : $scope.commodity;
						
			$scope.$watch('commodity', function() {
				
				//if prices map is empty, init it
				if( $scope.commodity ){
					$scope.price = $scope.commodity.pricesMap ? $scope.commodity.pricesMap.prices[ NovabillConf.defaultPriceListName ].priceValue : null;
					
					// NOTE we check for id to workaround GWT removing the property when it is false
					$scope.service = ($scope.commodity.service === undefined  
							? ($scope.commodity.id === undefined || $scope.commodity.id === null ? null : 'false') 
									: $scope.commodity.service.toString());
				}
				
			});
			
			function hideAndReset(){
				if(!$scope.api.keepCommodityOnClose){
					$scope.commodity = null;
					$scope.price = null;
					$scope.service = null;
				}
				$scope.api.hide();
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
				$scope.api.callback.onSave(
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
				$scope.api.callback.onCancel();
			};
			
		},

		restrict: 'E',
		replace: true,

	};

})
//APIs
.factory('NEditCommodityDialogAPI', function(){
	return {

		keepCommodityOnClose : false,
		
		callback : {
			onSave : function(commodity){},
			onCancel : function(){}
		},
		
		//functions
		init : function(callback, keepCommodityOnClose){
			this.callback = callback;
			this.keepCommodityOnClose = keepCommodityOnClose;
		},

		show : function(){
			$('#editCommodityDialog').modal('show');
		},

		hide : function(){
			$('#editCommodityDialog').modal('hide');
			
			// workaround - see http://stackoverflow.com/questions/11519660/twitter-bootstrap-modal-backdrop-doesnt-disappear
			$('body').removeClass('modal-open');
			$('.modal-backdrop').remove();
		}

	};
})






/*
 * Removal Dialog
 */
.directive('removalDialog', function factory(){

	return {

		templateUrl: NovabillConf.partialsBaseUrl+'/directives/confirm-removal-dialog.html',
		scope: {},

		controller : function($scope, NRemovalDialogAPI){
			$scope.api = NRemovalDialogAPI;

			$scope.ok = function(){
				NRemovalDialogAPI.hide();
				$scope.api.callback.onOk();
			};

			$scope.cancel = function(){
				NRemovalDialogAPI.hide();
				$scope.api.callback.onCancel();
			};
		},

		restrict: 'E',
		replace: true,

	};

})
//APIs
.factory('NRemovalDialogAPI', function(){
	return {

		//instance variables
		message : '',

		callback : {
			onOk : function(commodity){},
			onCancel : function(){}
		},

		//functions
		init : function(message, callback){
			this.message = message;
			this.callback = callback;
		},

		show : function(){
			$('#removalDialog').modal('show');
		},

		hide : function(){
			$('#removalDialog').modal('hide');
			
			// workaround - see http://stackoverflow.com/questions/11519660/twitter-bootstrap-modal-backdrop-doesnt-disappear
			$('body').removeClass('modal-open');
			$('.modal-backdrop').remove();
		}

	};
});
