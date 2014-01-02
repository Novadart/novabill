'use strict';

angular.module('novabill.commodities.controllers', ['novabill.directives', 'novabill.directives.dialogs', 'novabill.translations', 'novabill.constants', 'novabill.utils'])


/**
 * COMMODITIES PAGE CONTROLLER
 */
.controller('CommoditiesCtrl', ['$scope', '$location', '$rootScope', 'nConstants', 'nSorting',
                                function($scope, $location, $rootScope, nConstants, nSorting){

	$scope.commodities = null;


	function loadCommodities() {
		GWT_Server.commodity.getAll(nConstants.conf.businessId, {
			onSuccess : function(data){
				$scope.$apply(function(){
					$scope.commodities = data.commodities.sort(nSorting.modifyPriceListPricesComparator);
				});
			},

			onFailure : function(error){}
		});
	};


	$scope.newCommodity = function(){
		$rootScope.$broadcast(nConstants.events.SHOW_EDIT_COMMODITY_DIALOG, false, 
				{
			onSave : function(commodity, delegation){

				GWT_Server.commodity.add(JSON.stringify(commodity), {
					onSuccess : function(newId){
						delegation.finish();
						console.log('Added new Commodity '+newId);
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
.controller('CommoditiesDetailsCtrl', ['$scope', '$location', '$routeParams', '$rootScope', 'nConstants', 
                                       function($scope, $location, $routeParams, $rootScope, nConstants){
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


	$scope.editCommodity = function(commodityId){

		$rootScope.$broadcast(nConstants.events.SHOW_EDIT_COMMODITY_DIALOG, true, 
				{
			onSave : function(commodity, delegation){

				GWT_Server.commodity.update(JSON.stringify(commodity), {
					onSuccess : function(newId){
						delegation.finish(true);
						console.log('Updated Commodity '+newId);
						loadCommodity();
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

	};


	$scope.removeCommodity = function(){
		$rootScope.$broadcast(nConstants.events.SHOW_REMOVAL_DIALOG, 
				'Are you sure that you want to delete permanently any data associated to "'+$scope.commodity.description+'"', {
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


