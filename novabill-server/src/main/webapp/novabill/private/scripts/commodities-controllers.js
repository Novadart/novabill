'use strict';

angular.module('novabill.commodities.controllers', 
		['novabill.directives', 'novabill.directives.dialogs', 'novabill.ajax', 
		 'novabill.translations', 'novabill.constants', 'novabill.utils', 'infinite-scroll'])


/**
 * COMMODITIES PAGE CONTROLLER
 */
.controller('CommoditiesCtrl', ['$scope', '$location', '$rootScope', 'nConstants', 'nSorting', '$filter', 'nAjax', 'nEditCommodityDialog',
                                function($scope, $location, $rootScope, nConstants, nSorting, $filter, nAjax, nEditCommodityDialog){

	$scope.commodities = null;
	var Commodity = nAjax.Commodity();

	var FILTER_QUERY_PARAM = 'filter';
	var loadedCommodities = [];
	var filteredCommodities = [];
	var PARTITION = 30

	$scope.query = $location.search()[FILTER_QUERY_PARAM] ? $location.search()[[FILTER_QUERY_PARAM]] : '';

	function updateFilteredCommodities(){
		filteredCommodities = $filter('filter')(loadedCommodities, $scope.query);
		$scope.commodities = filteredCommodities.slice(0, 15);
	}
	
	$scope.$watch('query', function(newValue, oldValue){
		$location.search(FILTER_QUERY_PARAM, newValue == ''? null : newValue);
		updateFilteredCommodities();
	});


	function loadCommodities() {
		Commodity.query(function(commodities){
			loadedCommodities = commodities.sort(nSorting.descriptionComparator);
			updateFilteredCommodities();
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
			var commodity = new Commodity();
			commodity.business = { id : nConstants.conf.businessId };
			instance = nEditCommodityDialog.open(commodity);
		}
		
		instance.result.then(function(commodity){
			commodity.$save(
					function(newId){
						loadCommodities();
					}, 
					function(exceptionData){
						switch(exceptionData.data.error){
						case nConstants.exception.VALIDATION:
							if(exceptionData.data.message[0].errorCode === nConstants.validation.NOT_UNIQUE){
								recursiveCreation(commodity, true);
							}
							break;

						default:
							break;
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
.controller('CommoditiesDetailsCtrl', ['$scope', '$location', '$routeParams', '$rootScope', 'nConstants', '$filter', 
                                       '$route', 'nRegExp', 'nConfirmDialog', 'nEditCommodityDialog', 'nAjax',
                                       function($scope, $location, $routeParams, $rootScope, nConstants, $filter, 
                                    		   $route, nRegExp, nConfirmDialog, nEditCommodityDialog, nAjax){
	$scope.DEFAULT_PRICELIST_NAME = nConstants.conf.defaultPriceListName;
	$scope.commodity = null;
	var Commodity = nAjax.Commodity();


	function loadCommodity(){
		Commodity.get({id : $routeParams.commodityId}, function(commodity){
			$scope.commodity = commodity;
			$scope.commodity.business = { id : nConstants.conf.businessId };
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
			commodity.$update(function(){
				$route.reload();
			},
			function(exceptionData){
				switch(exceptionData.data.error){
				case nConstants.exception.VALIDATION:
					if(exceptionData.data.message[0].errorCode === nConstants.validation.NOT_UNIQUE){
						recursiveUpdate(commodity, true);
					}
					break;

				default:
					break;
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
				$scope.commodity.$delete(function(){
					$location.path('/');
				});
			}
		});
	};


	loadCommodity();

}]);


