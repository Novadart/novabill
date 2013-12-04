'use strict';

angular.module('novabill.commodities.controllers', ['novabill.directives', 'novabill.translations', 'novabill.constants'])


/**
 * COMMODITIES PAGE CONTROLLER
 */
.controller('CommoditiesCtrl', ['$scope', '$location', 'NEditCommodityDialogAPI', 'NConstants',
                                function($scope, $location, NEditCommodityDialogAPI, NConstants){
	
	$scope.commodities = null;
	
	NEditCommodityDialogAPI.init(null, {
		onSave : function(commodity, defaultPrice, isService, delegation){
			
			commodity['pricesMap'] = { prices : {} };
			commodity['pricesMap']['prices'][NovabillConf.defaultPriceListName] = {
					priceValue : defaultPrice,
					priceType : 'FIXED'
			};
			commodity.service = isService;
			
			GWT_Server.commodity.add(JSON.stringify(commodity), {
				onSuccess : function(newId){
					delegation.finish();
					console.log('Added new Commodity '+newId);
					$scope.loadCommodities();
				},

				onFailure : function(error){
					switch(error.exception){
					case NConstants.exception.VALIDATION:
						if(error.data === NConstants.validation.NOT_UNIQUE){
							delegation.invalidSku();
						}
						break;
						
					default:
						break;
					}
					
				}
			});
		},
		
		onCancel : function(){}
	});
	
	$scope.newCommodity = function(){
		NEditCommodityDialogAPI.show();
	};
	
	$scope.loadCommodities = function() {
		GWT_Server.commodity.getAll(NovabillConf.businessId, {
			onSuccess : function(data){
				$scope.$apply(function(){
					$scope.commodities = data.commodities;
				});
			},

			onFailure : function(error){}
		});
	};
	
	$scope.loadCommodities();
	
}])



/**
 * COMMODITIES DETAILS PAGE CONTROLLER
 */
.controller('CommoditiesDetailsCtrl', ['$scope', '$location', '$routeParams', 'NRemovalDialogAPI', 
                                       function($scope, $location, $routeParams, NRemovalDialogAPI){
	
	$scope.editCommodity = function(commodityId){
		
	};
	
	
	$scope.removeCommodity = function(commodityDescription, commodityId){
		NRemovalDialogAPI.init('Are you sure that you want to delete permanently any data associated to "'+commodityDescription+'"', {
			onOk : function(){
				GWT_Server.commodity.remove(NovabillConf.businessId, commodityId, {
					onSuccess : function(data){
						$scope.$apply(function(){
							$location.path('/');
						});
					},

					onFailure : function(error){}
				});
				
			},
			
			onCancel : function(){}
		});
		NRemovalDialogAPI.show();
	};
	
	
	GWT_Server.commodity.get(NovabillConf.businessId, $routeParams.commodityId, {
		
		onSuccess : function(commodity){
			$scope.$apply(function(){
				$scope.commodity = commodity;
			});
		},
		
		onFailure : function(){}
		
	});
	
}]);


