'use strict';

angular.module('novabill.settings.controllers', ['novabill.directives', 'novabill.directives.dialogs', 'novabill.translations', 'novabill.ajax'])


/**
 * SETTINGS PAGE CONTROLLER
 */
.controller('SettingsCtrl', ['$scope', 'nConstants', '$route', 'nAjax', 'nEditSharingPermitDialog',
                             function($scope, nConstants, $route, nAjax, nEditSharingPermitDialog){
	
	var SharingPermit = nAjax.SharingPermit();
	
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
	
	$scope.loadSharingPermits = function(){
		if($scope.sharingPermits == null){
			$scope.sharingPermits = SharingPermit.query();
		}
		
	};
	
	$scope.newShare = function(){
		// open the dialog to create a new sharing permit with an empty resource
		var instance = nEditSharingPermitDialog.open( new SharingPermit() );
		instance.result.then(function( sharingPermit ){
			//add missing parameters
			sharingPermit.business = {
					id : nConstants.conf.businessId
			};
			sharingPermit.createdOn = new Date().getTime();
			
			// save the sharing permit
			sharingPermit.$save(function(){
				$scope.sharingPermits = SharingPermit.query();
			});
		});
	};
	
	$scope.$watch('priceDisplayInDocsMonolithic', function(newValue, oldValue) {
		if($scope.business){
			$scope.business.settings.priceDisplayInDocsMonolithic = !newValue;
		}
	});
	
	$scope.$on(nConstants.events.SHARING_PERMIT_REMOVED, function(){
		$scope.sharingPermits = SharingPermit.query();
	});
	
}]);


