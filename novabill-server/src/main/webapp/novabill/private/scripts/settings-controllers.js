'use strict';

angular.module('novabill.settings.controllers', ['novabill.directives', 'novabill.directives.dialogs', 'novabill.directives.forms', 
                                                 'novabill.translations', 'novabill.ajax'])


/**
 * SETTINGS PAGE CONTROLLER
 */
.controller('SettingsCtrl', ['$scope', 'nConstants', 'nAjax', 
                             'nDownload', '$window', '$location', 'nEmailPreviewDialog',
                             function($scope, nConstants, nAjax, 
                            		 nDownload, $window, $location, nEmailPreviewDialog){

	var Business = nAjax.Business();
	
	$scope.changePasswordUrl = nConstants.conf.changePasswordBaseUrl;
	$scope.deleteAccountUrl = nConstants.conf.deleteAccountUrl;
	$scope.premiumUrl = nConstants.conf.premiumUrl;
	$scope.principalEmail = nConstants.conf.principalEmail;
	$scope.principalCreationDate = nConstants.conf.principalCreationDate;
	$scope.premium = nConstants.conf.premium;
	
	$scope.onTabChange = function(token){
		$location.search('tab',token);
	};
	
	$scope.activeTab = {
			business : false,
			profile : false,
			options : false,
	};
	$scope.activeTab[$location.search().tab] = true;
	
	
	Business.get(function(business){
		$scope.business = business;
		$scope.priceDisplayInDocsMonolithic = !business.settings.priceDisplayInDocsMonolithic;
	});
	
	$scope.businessUpdateCallback = function(){
		$window.location.reload();
	};
	
	$scope.update = function(){
		$scope.business.$update(function(){
			
			if(nConstants.conf.premium) {
				Business.setDefaultLayout({defaultLayoutType : $scope.business.settings.defaultLayoutType}, function(){
					$window.location.reload();
				});
			} else {
				$window.location.reload();
			}
		});
	};

	$scope.exportZip = nDownload.downloadExportZip;
	
	$scope.viewPreview = function(){
		nEmailPreviewDialog.open($scope.business);
	};

	$scope.$watch('priceDisplayInDocsMonolithic', function(newValue, oldValue) {
		if($scope.business){
			$scope.business.settings.priceDisplayInDocsMonolithic = !newValue;
		}
	});

}]);


