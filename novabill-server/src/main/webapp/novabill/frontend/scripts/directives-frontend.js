'use strict';

angular.module('novabill-frontend.directives', [])

/*
 * Share Invoice Widget
 */
.directive('nShareInvoice', ['nConstantsFrontend', function factory(nConstantsFrontend){

	return {
		templateUrl: nConstantsFrontend.url.htmlFragmentUrl('/directives/n-share-invoice.html'),
		scope: { 
			invoice : '='
		},
		controller : ['$scope', function($scope){


		}],
		restrict: 'E',
		replace: true
	};

}]);