'use strict';

angular.module('novabill.directives.forms', 
		['novabill.constants', 'novabill.utils', 'novabill.ajax', 'novabill.translations', 'angularFileUpload'])


/*
 * Business Form
 */
.directive('nBusinessForm', ['nConstants', function factory(nConstants){

	return {
		templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-business-form.html'),
		scope: { 
			business : '=',
			callback : '&'
		},
		controller : ['$scope', 
		              function($scope){
			
			$scope.saveOrUpdate = function(){
				
				if($scope.business.id){
					$scope.business.$update(function(){
						$scope.callback();
					});
				} else {
					$scope.business.settings = {};
					$scope.business.$save(function(){
						$scope.callback();
					});
				}
				
			};
			
		}],
		restrict: 'E',
		replace: true
	};

}])



/*
 * Business Logo
 */
.directive('nBusinessLogo', ['nConstants', function factory(nConstants){

	return {
		templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-business-logo.html'),

		controller : ['$scope', 'nConstants', 'nAjax', '$filter',
		              function($scope, nConstants, nAjax, $filter){
			var BusinessLogo = nAjax.BusinessLogo();
			
			$scope.logoUrl = nConstants.conf.logoUrl;

			$scope.refreshLogoUrl = function(){
				$scope.logoUrl = nConstants.conf.logoUrl + '?date=' + new Date().getTime();
			};

			$scope.removeLogo = function(){
				BusinessLogo.remove( $scope.refreshLogoUrl );
			};
			
			$scope.uploadLogo = function($files){
				$scope.errorMessage = null;
				
				BusinessLogo.upload($files[0], function(result, status, headers, config) {
					
					var res = result.indexOf('<pre>') != -1 ? result.charAt(5) : result;
					switch (parseInt(res)) {
					case 0:
						$scope.showUploadForm = false;
						$scope.refreshLogoUrl();
						break;

					default:
					case 1:
					case 4:
						$scope.errorMessage = $filter('translate')('LOGO_UPLOAD_ERROR_ILLEGAL_REQUEST');
						$scope.refreshLogoUrl();
						break;
						
					case 2:
						$scope.errorMessage = $filter('translate')('LOGO_UPLOAD_ERROR_ILLEGAL_PAYLOAD');
						break;
						
					case 3:
						$scope.errorMessage = $filter('translate')('LOGO_UPLOAD_ERROR_ILLEGAL_SIZE');
						break;
					}
					
				});

			};

		}],

		restrict: 'E',
		replace: true
	};

}]);
