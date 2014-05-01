'use strict';

angular.module('novabill.commodities.controllers', 
		['novabill.directives', 'novabill.directives.dialogs', 'novabill.translations', 'novabill.constants', 'novabill.utils', 'infinite-scroll'])


/**
 * COMMODITIES PAGE CONTROLLER
 */
.controller('CommoditiesCtrl', ['$scope', '$location', '$rootScope', 'nConstants', 'nSorting', '$filter', 'nEditCommodityDialog',
                                function($scope, $location, $rootScope, nConstants, nSorting, $filter, nEditCommodityDialog){

	$scope.commodities = null;
	
	var loadedCommodities = [];
	var filteredCommodities = [];
	var PARTITION = 30;
	
	function updateFilteredCommodities(){
		filteredCommodities = $filter('filter')(loadedCommodities, $scope.query);
		$scope.commodities = filteredCommodities.slice(0, 15);
	}
	
	$scope.$watch('query', function(newValue, oldValue){
		updateFilteredCommodities();
	});


	function loadCommodities() {
		GWT_Server.commodity.getAll(nConstants.conf.businessId, {
			onSuccess : function(data){
				$scope.$apply(function(){
					loadedCommodities = data.commodities.sort(nSorting.descriptionComparator);
					updateFilteredCommodities();
				});
			},

			onFailure : function(error){}
		});
	};

	$scope.loadMoreCommodities = function(){
		if($scope.commodities){
			var currentIndex = $scope.commodities.length;
			$scope.commodities = $scope.commodities.concat(filteredCommodities.slice(currentIndex, currentIndex+PARTITION));
		}
	};

	
	function recursiveCreation(wrongCommodity, invalidSku){
		var instance = null;
		
		if(wrongCommodity){
			instance = nEditCommodityDialog.open(wrongCommodity, invalidSku, true);
		} else {
			instance = nEditCommodityDialog.open();
		}
		
		instance.result.then(function(commodity){
			GWT_Server.commodity.add(angular.toJson(commodity), {
				onSuccess : function(newId){
					loadCommodities();
				},

				onFailure : function(error){
					switch(error.exception){
					case nConstants.exception.VALIDATION:
						if(error.data === nConstants.validation.NOT_UNIQUE){
							recursiveCreation(commodity, true);
						}
						break;

					default:
						break;
					}

				}
			});
		});
	}
	
	
	
	$scope.newCommodity = function(){
		recursiveCreation();
	};

	loadCommodities();

}])



/**
 * COMMODITIES DETAILS PAGE CONTROLLER
 */
.controller('CommoditiesDetailsCtrl', ['$scope', '$location', '$routeParams', '$rootScope', 'nConstants', '$filter', '$route', 'nRegExp', 'nConfirmDialog', 'nEditCommodityDialog',
                                       function($scope, $location, $routeParams, $rootScope, nConstants, $filter, $route, nRegExp, nConfirmDialog, nEditCommodityDialog){
	$scope.DEFAULT_PRICELIST_NAME = nConstants.conf.defaultPriceListName;
	$scope.commodity = null;


	function loadCommodity(){
		GWT_Server.commodity.get(nConstants.conf.businessId, $routeParams.commodityId, {

			onSuccess : function(commodity){
				$scope.$apply(function(){
					$scope.commodity = commodity;
				});
			},

			onFailure : function(){}

		});
	}

	$scope.printSku = function(){
		if($scope.commodity){
			if( !nRegExp.reserved_word.test($scope.commodity.sku)){
				return $scope.commodity.sku;
			} else {
				return '';
			}
		} else {
			return '';
		}
		
	};
	
	
	function recursiveUpdate(wrongCommodity, invalidSku){
		var instance = null;
		
		if(wrongCommodity){
			instance = nEditCommodityDialog.open(wrongCommodity, invalidSku, true);
		} else {
			instance = nEditCommodityDialog.open();
		}
		
		instance.result.then(function(commodity){
			GWT_Server.commodity.update(angular.toJson(commodity), {
				onSuccess : function(newId){
					$route.reload();
				},

				onFailure : function(error){
					switch(error.exception){
					case nConstants.exception.VALIDATION:
						if(error.data === nConstants.validation.NOT_UNIQUE){
							recursiveUpdate(commodity, true);
						}
						break;

					default:
						break;
					}

				}
			});
		});
	}
	

	$scope.editCommodity = function(commodityId){
		recursiveUpdate($scope.commodity, false, true);
	};


	$scope.removeCommodity = function(){
		var instance = nConfirmDialog.open( $filter('translate')('REMOVAL_QUESTION',{data : $scope.commodity.description}) );
		instance.result.then(function(value){
			if(value){
				GWT_Server.commodity.remove(nConstants.conf.businessId, $scope.commodity.id, {
					onSuccess : function(data){
						$scope.$apply(function(){
							$location.path('/');
						});
					},
	
					onFailure : function(error){}
				});
			}
		});
	};


	loadCommodity();

}]);


