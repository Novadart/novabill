'use strict';

angular.module('novabill.commodities.controllers', 
		['novabill.directives', 'novabill.directives.dialogs', 'novabill.translations', 'novabill.constants', 'novabill.utils', 'infinite-scroll'])


/**
 * COMMODITIES PAGE CONTROLLER
 */
.controller('CommoditiesCtrl', ['$scope', '$location', '$rootScope', 'nConstants', 'nSorting', '$filter',
                                function($scope, $location, $rootScope, nConstants, nSorting, $filter){

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

	$scope.newCommodity = function(){
		$rootScope.$broadcast(nConstants.events.SHOW_EDIT_COMMODITY_DIALOG, false, 
				{
			onSave : function(commodity, delegation){

				GWT_Server.commodity.add(JSON.stringify(commodity), {
					onSuccess : function(newId){
						delegation.finish();
						loadCommodities();
					},

					onFailure : function(error){
						switch(error.exception){
						case nConstants.exception.VALIDATION:
							if(error.data === nConstants.validation.NOT_UNIQUE){
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
	};

	loadCommodities();

}])



/**
 * COMMODITIES DETAILS PAGE CONTROLLER
 */
.controller('CommoditiesDetailsCtrl', ['$scope', '$location', '$routeParams', '$rootScope', 'nConstants', '$filter', 'nRegExp',
                                       function($scope, $location, $routeParams, $rootScope, nConstants, $filter, nRegExp){
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

	$scope.editCommodity = function(commodityId){

		$rootScope.$broadcast(nConstants.events.SHOW_EDIT_COMMODITY_DIALOG, true, 
				{
			onSave : function(commodity, delegation){

				GWT_Server.commodity.update(JSON.stringify(commodity), {
					onSuccess : function(newId){
						delegation.finish(true);
						loadCommodity();
					},

					onFailure : function(error){
						switch(error.exception){
						case nConstants.exception.VALIDATION:
							if(error.data === nConstants.validation.NOT_UNIQUE){
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

	};


	$scope.removeCommodity = function(){
		$rootScope.$broadcast(nConstants.events.SHOW_REMOVAL_DIALOG, $filter('translate')('REMOVAL_QUESTION',{data : $scope.commodity.description}), {
			onOk : function(){
				GWT_Server.commodity.remove(nConstants.conf.businessId, $scope.commodity.id, {
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


	loadCommodity();

}]);


