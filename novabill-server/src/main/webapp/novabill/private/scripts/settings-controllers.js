'use strict';

angular.module('novabill.settings.controllers', ['novabill.directives', 'novabill.directives.dialogs', 'novabill.directives.forms', 
                                                 'novabill.translations', 'novabill.ajax'])


/**
 * SETTINGS PAGE CONTROLLER
 */
.controller('SettingsCtrl', ['$scope', 'nConstants', 'nAjax', 'nEditSharingPermitDialog', 
                             'nDownload', '$window', '$location', 'nEmailPreviewDialog',
                             function($scope, nConstants, nAjax, nEditSharingPermitDialog, 
                            		 nDownload, $window, $location, nEmailPreviewDialog){

	var Business = nAjax.Business();
	var SharingPermit = nAjax.SharingPermit();
	
	$scope.changePasswordUrl = nConstants.conf.changePasswordBaseUrl;
	
	$scope.onTabChange = function(token){
		$location.search('tab',token);
	};
	
	$scope.activeTab = {
			business : false,
			profile : false,
			options : false,
			share : false
	};
	$scope.activeTab[$location.search().tab] = true;
	
	
	Business.get(function(business){
		$scope.business = business;
		$scope.priceDisplayInDocsMonolithic = !business.settings.priceDisplayInDocsMonolithic;
	});
	
	
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

	$scope.loadSharingPermits = function(){
		if($scope.sharingPermits == null){
			SharingPermit.query(function(result){
				$scope.sharingPermits = result;
			});
		}
	};
	
	$scope.exportZip = nDownload.downloadExportZip;
	
	$scope.recursiveCreation = function(wrongShare){
		
		var invalidEmail = wrongShare ? true : false;
		var sp = wrongShare ? wrongShare : new SharingPermit();
		
		// open the dialog to create a new sharing permit with an empty resource
		var instance = nEditSharingPermitDialog.open( sp, invalidEmail );
		instance.result.then(function( result ){
			var sharingPermit = result.sharingPermit;
			
			//add missing parameters
			sharingPermit.business = {
					id : nConstants.conf.businessId
			};
			sharingPermit.createdOn = new Date().getTime();

			// save the sharing permit
			sharingPermit.$save({ sendEmail : result.sendEmail }, function(){
				SharingPermit.query(function(result){
					$scope.sharingPermits = result;
				});
			}, function(exception){
				switch (exception.data.error) {
				case "VALIDATION ERROR":
					$scope.recursiveCreation( sharingPermit );
					break;

				default:
					break;
				}
			});
		});
	};
	
	$scope.viewPreview = function(){
		nEmailPreviewDialog.open($scope.business);
	};

	$scope.newShare = function(){
		$scope.recursiveCreation();
	};

	
	$scope.$watch('priceDisplayInDocsMonolithic', function(newValue, oldValue) {
		if($scope.business){
			$scope.business.settings.priceDisplayInDocsMonolithic = !newValue;
		}
	});

	$scope.$on(nConstants.events.SHARING_PERMIT_REMOVED, function(){
		SharingPermit.query(function(result){
			$scope.sharingPermits = result;
		});
	});

}]);


