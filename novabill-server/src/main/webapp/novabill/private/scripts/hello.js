'use strict';

angular.module('novabill.hello', ['novabill.ajax', 'novabill.directives.dialogs'])


/**
 * HELLO PAGE CONTROLLER
 */
.controller('HelloCtrl', ['$scope', 'nHelloDialog', 'nAjax','$location','nConstants',
                          function($scope, nHelloDialog, nAjax, $location, nConstants){

	var Business = nAjax.Business();
	
	var business = new Business();
	business.country = 'IT';
	
	var instance = null;
	instance = nHelloDialog.open( business, function(){
		instance.close();
		$location.path(nConstants.url.dashboard());
	} );
	
}]);


