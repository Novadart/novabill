'use strict';

angular.module('novabill.share.controllers', ['novabill.directives', 'novabill.directives.dialogs', 
                                                 'novabill.translations', 'novabill.ajax'])


/**
 * SHARE PAGE CONTROLLER
 */
.controller('ShareCtrl', ['$scope', 'nConstants', 'nAjax', 'nEditSharingPermitDialog', '$filter', '$sce',
                             function($scope, nConstants, nAjax, nEditSharingPermitDialog, $filter, $sce){

	var SharingPermit = nAjax.SharingPermit();
	
	$scope.sharingInfo2 = $sce.trustAsHtml( $filter('translate')('SHARING_PERMIT_INFO2', {link : nConstants.conf.publicShareBaseUrl}) );
	
	$scope.loadSharingPermits = function(){
		if($scope.sharingPermits == null){
			SharingPermit.query(function(result){
				$scope.sharingPermits = result;
			});
		}
	};
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

	$scope.$on(nConstants.events.SHARING_PERMIT_REMOVED, function(){
		SharingPermit.query(function(result){
			$scope.sharingPermits = result;
		});
	});
	
	$scope.loadSharingPermits();

}]);


