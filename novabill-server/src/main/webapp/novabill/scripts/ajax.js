'use strict';

angular.module('novabill.ajax', ['ngResource'])


//.config(['$httpProvider', function($httpProvider) {
//	$httpProvider.defaults.headers.patch = {
//			'Content-Type': 'application/json;charset=utf-8'
//	};
//}])

/**
 * The ajax service stores all the resources used in Novabill
 */
.factory('nAjax', ['nConstants', '$resource', function(nConstants, $resource) {

	return {

		SharingPermit : function(){
			return $resource(
					nConstants.conf.privateAreaBaseUrl + 'businesses/'+nConstants.conf.businessId+'/sharepermits/:id',
					{id:'@id'}
			);
		}

	};
}]);
