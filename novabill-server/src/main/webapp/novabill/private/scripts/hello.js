'use strict';

angular.module('novabill.hello', ['novabill.ajax', 'novabill.directives.dialogs'])


/**
 * HELLO PAGE CONTROLLER
 */
.controller('HelloCtrl', ['$scope', 'nHelloDialog', 'nAjax', function($scope, nHelloDialog, nAjax){

	var Business = nAjax.Business();
	
	var business = new Business();
	business.country = 'IT';
	
	nHelloDialog.open( business );
	
}]);


