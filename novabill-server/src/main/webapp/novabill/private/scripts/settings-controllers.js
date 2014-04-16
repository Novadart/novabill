'use strict';

angular.module('novabill.settings.controllers', ['novabill.directives', 'novabill.directives.dialogs', 'novabill.directives.forms', 
                                                 'novabill.translations', 'novabill.ajax'])


/**
 * SETTINGS PAGE CONTROLLER
 */
.controller('SettingsCtrl', ['$scope', 'nConstants', '$route', 'nAjax', 'nEditSharingPermitDialog', 'nDownload', '$window',
                             function($scope, nConstants, $route, nAjax, nEditSharingPermitDialog, nDownload, $window){

	var Business = nAjax.Business();
	var SharingPermit = nAjax.SharingPermit();
	
	$scope.firstRun =  nConstants.conf.businessId === '-1';

	if(!$scope.firstRun) {
		
		Business.get({ id : nConstants.conf.businessId }, function(business){
			$scope.business = business;
			$scope.priceDisplayInDocsMonolithic = !business.settings.priceDisplayInDocsMonolithic;
		});
		
		
		$scope.update = function(updateLayout){
			$scope.business.$update(function(){
				if(updateLayout){
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

		
	} else {
		
		$scope.business = new Business();
	}

	
}]);


