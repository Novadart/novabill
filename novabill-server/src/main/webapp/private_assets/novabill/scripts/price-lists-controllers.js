'use strict';

angular.module('novabill.priceLists.controllers', ['novabill.translations', 'novabill.directives', 'novabill.directives.dialogs', 'novabill.utils'])


/**
 * PRICE LISTS PAGE CONTROLLER
 */
.controller('PriceListsCtrl', ['$scope', '$rootScope', 'nConstants', 'nSorting', 
                               function($scope, $rootScope, nConstants, nSorting){
	
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
	
	$scope.newPriceList = function(){
		$rootScope.$broadcast(nConstants.events.SHOW_EDIT_PRICE_LIST_DIALOG, false, {
			
			onSave : function(priceList, delegation){
				GWT_Server.priceList.add(JSON.stringify(priceList), {
					
					onSuccess : function(newId){
						delegation.finish();
						loadPriceLists();
					},
					
					onFailure : function(error){
						switch(error.exception){
						case nConstants.exception.VALIDATION:
							if(error.data === nConstants.validation.NOT_UNIQUE){
								delegation.invalidIdentifier();
							}
							break;

						default:
							break;
						}
					},
					
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
.controller('PriceListsDetailsCtrl', ['$scope', '$http', '$routeParams', 'nSorting', 'nConstants', '$rootScope', '$location',
                                      function($scope, $http, $routeParams, nSorting, nConstants, $rootScope, $location){
	
	$scope.DEFAULT_PRICELIST_NAME = nConstants.conf.defaultPriceListName;
	
	function loadPriceList(){
		$http.get(nConstants.conf.privateAreaBaseUrl+'pricelists/'+$routeParams.priceListId)
			.success(function(data, status){
				$scope.commodities = data.commodities.sort(nSorting.descriptionComparator);
				$scope.priceList = data;
			});
	}
	
	
	$scope.editPriceList = function(){

		$rootScope.$broadcast(nConstants.events.SHOW_EDIT_PRICE_LIST_DIALOG, true, {

			onSave : function(priceList, delegation){
				GWT_Server.priceList.update(JSON.stringify(priceList), {

					onSuccess : function(newId){
						delegation.finish();
					},

					onFailure : function(){},

				});
			},

			onCancel : function(){}
		});

	};


	$scope.removePriceList = function(){
		$rootScope.$broadcast(nConstants.events.SHOW_REMOVAL_DIALOG, 
				'Are you sure that you want to delete permanently any data associated to "'+$scope.priceList.name+'"', {
			onOk : function(){
				GWT_Server.priceList.remove(nConstants.conf.businessId, $scope.priceList.id, {
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


