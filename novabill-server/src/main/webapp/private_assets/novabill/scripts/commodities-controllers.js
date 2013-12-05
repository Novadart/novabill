'use strict';

angular.module('novabill.commodities.controllers', ['novabill.directives', 'novabill.directives.dialogs', 'novabill.translations', 'novabill.constants'])


/**
 * COMMODITIES PAGE CONTROLLER
 */
.controller('CommoditiesCtrl', ['$scope', '$location', 'NEditCommodityDialogAPI', 'NConstants',
                                function($scope, $location, NEditCommodityDialogAPI, NConstants){

	$scope.commodities = null;


	function loadCommodities() {
		GWT_Server.commodity.getAll(NovabillConf.businessId, {
			onSuccess : function(data){
				$scope.$apply(function(){
					$scope.commodities = data.commodities;
				});
			},

			onFailure : function(error){}
		});
	};



	NEditCommodityDialogAPI.init({
		onSave : function(commodity, delegation){

			GWT_Server.commodity.add(JSON.stringify(commodity), {
				onSuccess : function(newId){
					delegation.finish();
					console.log('Added new Commodity '+newId);
					loadCommodities();
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

	loadCommodities();

}])



/**
 * COMMODITIES DETAILS PAGE CONTROLLER
 */
.controller('CommoditiesDetailsCtrl', ['$scope', '$location', '$routeParams', 'NEditCommodityDialogAPI', 'NRemovalDialogAPI', 
                                       function($scope, $location, $routeParams, NEditCommodityDialogAPI, NRemovalDialogAPI){

	$scope.commodity = null;


	function loadCommodity(){
		GWT_Server.commodity.get(NovabillConf.businessId, $routeParams.commodityId, {

			onSuccess : function(commodity){
				$scope.$apply(function(){
					$scope.commodity = commodity;
				});
			},

			onFailure : function(){}

		});
	}


	$scope.editCommodity = function(commodityId){

		NEditCommodityDialogAPI.init({
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
		}, true);

		NEditCommodityDialogAPI.show();

	};


	$scope.removeCommodity = function(){
		NRemovalDialogAPI.init('Are you sure that you want to delete permanently any data associated to "'+$scope.commodity.description+'"', {
			onOk : function(){
				GWT_Server.commodity.remove(NovabillConf.businessId, $scope.commodity.id, {
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


	loadCommodity();

}]);


