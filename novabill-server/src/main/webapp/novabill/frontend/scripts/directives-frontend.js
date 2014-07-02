'use strict';

angular.module('novabill-frontend.directives', [])

/*
 * Share Invoice Widget
 */
.directive('nShareDoc', ['nConstantsFrontend', function factory(nConstantsFrontend){

	return {
		templateUrl: nConstantsFrontend.url.htmlFragmentUrl('/directives/n-share-doc.html'),
		scope: { 
			doc : '='
		},
		controller : ['$scope', function($scope){


		}],
		restrict: 'E',
		replace: true
	};

}]);