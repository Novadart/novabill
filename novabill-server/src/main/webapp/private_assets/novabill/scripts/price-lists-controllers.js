'use strict';

angular.module('novabill.priceLists.controllers', ['novabill.translations', 'novabill.directives', 'novabill.directives.dialogs', 'novabill.utils'])


/**
 * PRICE LISTS PAGE CONTROLLER
 */
.controller('PriceListsCtrl', ['$scope', '$rootScope', 'nConstants', 'nSorting', function($scope, $rootScope, nConstants, nSorting){
	
	$scope.DEFAULT_PRICELIST_NAME = NovabillConf.defaultPriceListName;
	
	function loadPriceLists(){
		GWT_Server.priceList.getAll(NovabillConf.businessId, {
			
			onSuccess : function(priceLists){
				$scope.$apply(function(){
					$scope.priceLists = priceLists.list.sort(nSorting.priceListsComparator);
				});
			},
			
			onFailure : function() {
				
			}
			
		});
	};
	
	$scope.newPriceList = function(){
		$rootScope.$broadcast(nConstants.events.SHOW_EDIT_PRICE_LIST_DIALOG, false, {
			
			onSave : function(priceList, delegation){
				GWT_Server.priceList.add(JSON.stringify(priceList), {
					
					onSuccess : function(newId){
						delegation.finish();
						loadPriceLists();
					},
					
					onFailure : function(){},
					
				});
			},
			
			onCancel : function(){}
		});
	};
	
	loadPriceLists();
	
}])



/**
 * PRICE LISTS DETAILS PAGE CONTROLLER
 */
.controller('PriceListsDetailsCtrl', ['$scope', '$routeParams', 'nSorting', function($scope, $routeParams, nSorting){
	
	function loadPriceList(){
		GWT_Server.batch.fetchModifyPriceList(NovabillConf.businessId, $routeParams.priceListId, {

			onSuccess : function(data){
				$scope.$apply(function(){
					$scope.priceList = data.priceList;
					$scope.commodityPriceList = data.commodityPriceList.sort(nSorting.modifyPriceListPricesComparator);
					
					var tmp = {};
					var n;
					for(var i=0; i<data.commodityPriceList.length; i++){
						n = Math.random();
						tmp[data.commodityPriceList[i].sku] = (n <= 0.3);
					}
					$scope.tmp = tmp;
				});
			},

			onFailure : function(){}

		});
	}
	
	loadPriceList();
	
}]);


