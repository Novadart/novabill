'use strict';

angular.module('novabill.priceLists.controllers', 
		['novabill.translations', 'novabill.directives', 'novabill.directives.dialogs', 'novabill.utils', 'infinite-scroll'])


/**
 * PRICE LISTS PAGE CONTROLLER
 */
.controller('PriceListsCtrl', ['$scope', 'nConstants', 'nSorting', 'nEditPriceListDialog', 
                               function($scope, nConstants, nSorting, nEditPriceListDialog){
	
	$scope.DEFAULT_PRICELIST_NAME = nConstants.conf.defaultPriceListName;
	
	function loadPriceLists(){
		GWT_Server.priceList.getAll(nConstants.conf.businessId, {
			
			onSuccess : function(priceLists){
				$scope.$apply(function(){
					$scope.priceLists = priceLists.list.sort(nSorting.priceListsComparator);
				});
			},
			
			onFailure : function() {
				
			}
			
		});
	};
	
	function recursiveCreation(wrongPriceList){
		var instance = null;
		
		if(wrongPriceList){
			instance = nEditPriceListDialog.open(wrongPriceList, true);
		} else {
			instance = nEditPriceListDialog.open();
		}
		
		instance.result.then(function(priceList){
			GWT_Server.priceList.add(JSON.stringify(priceList), {

				onSuccess : function(newId){
					loadPriceLists();
				},

				onFailure : function(error){
					switch(error.exception){
					case nConstants.exception.VALIDATION:
						if(error.data === nConstants.validation.NOT_UNIQUE){
							recursiveCreation(priceList);
						}
						break;

					default:
						break;
					}
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
.controller('PriceListsDetailsCtrl', ['$scope', '$http', '$routeParams', 'nSorting', 'nConstants', '$rootScope', '$location', '$filter', 'nEditPriceListDialog',
                                      function($scope, $http, $routeParams, nSorting, nConstants, $rootScope, $location, $filter, nEditPriceListDialog){
	
	$scope.DEFAULT_PRICELIST_NAME = nConstants.conf.defaultPriceListName;
	
	var loadedCommodities = [];
	var filteredCommodities = [];
	var PARTITION = 50;
	
	function updateFilteredCommodities(){
		filteredCommodities = $filter('filter')(loadedCommodities, $scope.query);
		$scope.commodities = filteredCommodities.slice(0, 15);
	}
	
	$scope.$watch('query', function(newValue, oldValue){
		updateFilteredCommodities();
	});
	
	function loadPriceList(){
		$http.get(nConstants.conf.privateAreaBaseUrl+'json/pricelists/'+$routeParams.priceListId)
			.success(function(data, status){
				loadedCommodities = data.commodities.sort(nSorting.descriptionComparator);
				$scope.priceList = data;
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
			GWT_Server.priceList.update(JSON.stringify(priceList), {

				onSuccess : function(newId){
					$scope.$apply(function(){
						$scope.priceList.name = priceList.name;
					});
				},

				onFailure : function(error){
					switch(error.exception){
					case nConstants.exception.VALIDATION:
						if(error.data === nConstants.validation.NOT_UNIQUE){
							recursiveUpdate(priceList, true);
						}
						break;

					default:
						break;
					}
				}
			});
		});
	}
	
	$scope.editPriceList = function(){
		recursiveUpdate($scope.priceList, false);
	};


	$scope.removePriceList = function(){
		$rootScope.$broadcast(nConstants.events.SHOW_REMOVAL_DIALOG, $filter('translate')('REMOVAL_QUESTION', {data: $scope.priceList.name}), {
			onOk : function(){
				GWT_Server.priceList.remove(nConstants.conf.businessId, String($scope.priceList.id), {
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
	};
	
	loadPriceList();
	
}]);


