'use strict';

angular.module('novabill.priceLists.controllers', 
		['novabill.translations', 'novabill.directives', 'novabill.ajax', 'novabill.directives.dialogs', 
		 'novabill.utils', 'infinite-scroll'])


/**
 * PRICE LISTS PAGE CONTROLLER
 */
.controller('PriceListsCtrl', ['$scope', 'nConstants', 'nSorting', 'nEditPriceListDialog', 'nAjax',
                               function($scope, nConstants, nSorting, nEditPriceListDialog, nAjax){
	
	$scope.DEFAULT_PRICELIST_NAME = nConstants.conf.defaultPriceListName;
	$scope.PREMIUM = nConstants.conf.premium;
	var PriceList = nAjax.PriceList(); 
	
	function loadPriceLists(){
		PriceList.query(function(priceLists){
			$scope.priceLists = priceLists.sort(nSorting.priceListsComparator);
		});
	};
	
	function recursiveCreation(wrongPriceList){
		var instance = null;
		
		if(wrongPriceList){
			instance = nEditPriceListDialog.open(wrongPriceList, true);
		} else {
			var newPriceList = new PriceList();
			newPriceList.business = { id : nConstants.conf.businessId };
			instance = nEditPriceListDialog.open( newPriceList );
		}
		
		instance.result.then(function(priceList){
			priceList.$save(function(newId){
				loadPriceLists();
			}, function(exceptionData){
				switch(exceptionData.data.error){
				case nConstants.exception.VALIDATION:
					if(exceptionData.data.message[0].errorCode === nConstants.validation.NOT_UNIQUE){
						recursiveCreation(priceList);
					}
					break;

				default:
					break;
				}
			});
		});
	}
	
	$scope.newPriceList = function(){
		recursiveCreation();
	};
	
	loadPriceLists();
	
}])



/**
 * PRICE LISTS DETAILS PAGE CONTROLLER
 */
.controller('PriceListsDetailsCtrl', ['$scope', '$http', '$routeParams', 'nSorting', 'nConstants', '$rootScope', 'nConfirmDialog',
                                      '$location', '$filter', 'nEditPriceListDialog', 'nCommodityPriceDialog', '$route', 'nAjax',
                                      function($scope, $http, $routeParams, nSorting, nConstants, $rootScope, nConfirmDialog,
                                    		  $location, $filter, nEditPriceListDialog, nCommodityPriceDialog, $route, nAjax){
	
	$scope.DEFAULT_PRICELIST_NAME = nConstants.conf.defaultPriceListName;
	$scope.selectedCommodities = {};
	$scope.selectedCommoditiesCount = 0;
	var PriceList = nAjax.PriceList();
	var CommodityUtils = nAjax.CommodityUtils();
	
	var loadedCommodities = [];
	var filteredCommodities = [];
	var PARTITION = 50;
	
	function updateFilteredCommodities(){
		$scope.selectedCommodities = {};
		$scope.selectedCommoditiesCount=0;
		filteredCommodities = $filter('filter')(loadedCommodities, $scope.query);
		$scope.commodities = filteredCommodities.slice(0, 15);
	}
	
	$scope.$watch('query', function(newValue, oldValue){
		updateFilteredCommodities();
	});
	
	function loadPriceList(){
		PriceList.get({id: $routeParams.priceListId}, function(priceList){
			loadedCommodities = priceList.commodities.sort(nSorting.descriptionComparator);
			$scope.priceList = priceList;
			$scope.priceList.business = { id : nConstants.conf.businessId };
			updateFilteredCommodities();
		});
	}
	
	$scope.loadMoreCommodities = function(){
		if($scope.commodities){
			var currentIndex = $scope.commodities.length;
			$scope.commodities = $scope.commodities.concat(filteredCommodities.slice(currentIndex, currentIndex+PARTITION));
		}
	};
	
	
	function recursiveUpdate(updatedPriceList, invalidIdentifier){
		var instance = nEditPriceListDialog.open(updatedPriceList, invalidIdentifier);

		instance.result.then(function(priceList){
			priceList.$update(function(newId){
				$scope.priceList.name = priceList.name;
			}, function(exceptionData){
				switch(exceptionData.data.error){
				case nConstants.exception.VALIDATION:
					if(exceptionData.data.message[0].errorCode === nConstants.validation.NOT_UNIQUE){
						recursiveUpdate(priceList, true);
					}
					break;

				default:
					break;
				}
			});
		});
	}
	
	$scope.selectionButtonClick = function(){
		if($scope.selectedCommoditiesCount === 0){
			// make sure that all the expected commodities are on screen
			$scope.commodities = filteredCommodities;
			
			angular.forEach($scope.commodities, function(com, _){
				$scope.selectedCommodities[com.id] = true;
			});
			$scope.selectedCommoditiesCount = $scope.commodities.length;
			
		} else {
			$scope.selectedCommodities = {};
			$scope.selectedCommoditiesCount = 0;
		}
	};
	
	$scope.onSelectionChange = function(id){
		$scope.selectedCommoditiesCount += $scope.selectedCommodities[id] ? 1 : -1;
	};
	
	$scope.editPriceList = function(){
		recursiveUpdate($scope.priceList, false);
	};


	$scope.removePriceList = function(){
		var instance = nConfirmDialog.open( $filter('translate')('REMOVAL_QUESTION', {data: $scope.priceList.name}) );
		instance.result.then(function(value){
			if(value){
				$scope.priceList.$delete(function(){
					$location.path('/');
				});
			}
		});
	};
	
	$scope.showCommodityPriceDialog = function(){
		var instance = nCommodityPriceDialog.open($scope.priceList.name);
		instance.result.then(
				function (result) {

					// find the set of commodities that were selected. We can restrict ourselves only to commodities that are on display
					var set = [];
					angular.forEach($scope.commodities, function(com, _){
						if($scope.selectedCommodities[com.id]){
							set.push(com);
						}
					});
					
					// extract and update the prices
					var prices = [];
					var price = null;
					angular.forEach(set, function(com, _){
						price = angular.copy(com.prices[$scope.priceList.name]);
						price.priceType = result.priceType;
						price.priceValue = result.priceValue;
						prices.push(price);
					});
					
					CommodityUtils.addOrUpdatePrices(prices, function(newPrices){
//						var price = null;
//						angular.forEach(set, function(com, i){
//							price = prices.list[i];
//							price.id = parseInt(result.list[i]);
//							com.prices[$scope.priceList.name] = price;
//						});
						$route.reload();
					});
					
				},
				function () {
				}
		);
	};
	
	loadPriceList();
	
}]);


