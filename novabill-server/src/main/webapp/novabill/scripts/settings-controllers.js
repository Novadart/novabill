'use strict';

angular.module('novabill.settings.controllers', ['novabill.translations'])


/**
 * SETTINGS PAGE CONTROLLER
 */
.controller('SettingsCtrl', ['$scope', 'nConstants', '$route', function($scope, nConstants, $route){
	
	GWT_UI.showSettingsPage('settings-page');

	GWT_Server.business.get(nConstants.conf.businessId, {
		onSuccess : function(business){
			$scope.$apply(function(){
				$scope.business = business;
				$scope.priceDisplayInDocsMonolithic = !business.settings.priceDisplayInDocsMonolithic;
			});
		},
		onFailure : function(){}
	});
	
	$scope.update = function(){
		GWT_Server.business.update(angular.toJson($scope.business), {
			onSuccess : function(business){
				$scope.$apply(function(){
					window.location.reload();
				});
			},
			onFailure : function(){}
		});
	};
	
	$scope.$watch('priceDisplayInDocsMonolithic', function(newValue, oldValue) {
		if($scope.business){
			$scope.business.settings.priceDisplayInDocsMonolithic = !newValue;
		}
	});
	
}]);


