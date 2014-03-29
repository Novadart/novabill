'use strict';

angular.module('novabill.ajax', ['ngResource'])


/**
 * The ajax service stores all the resources used in Novabill
 */
.factory('nAjax', ['nConstants', '$resource', function(nConstants, $resource) {

	var businesId = nConstants.conf.businessId;
	var baseUrl = nConstants.conf.privateAreaBaseUrl;
	
	return {
		
		/*
		 * Sharing Permit resource
		 */
		SharingPermit : function(){
			return $resource(
					baseUrl + 'businesses/'+ businesId +'/sharepermits/:id',
					{id:'@id'},
					{
						sendEmail : {
							method : 'POST',
							params : {id:'@id'},
							url : baseUrl + 'businesses/'+ businesId +'/sharepermits/:id/email'
						}
					}
			);
		}

	};
}]);
