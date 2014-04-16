'use strict';

angular.module('novabill.ajax', ['ngResource'])


/**
 * The ajax service stores all the resources used in Novabill
 */
.factory('nAjax', ['nConstants', '$resource', function(nConstants, $resource) {

	var businessId = nConstants.conf.businessId;
	var baseUrl = nConstants.conf.ajaxBaseUrl;

	return {

		/*
		 * Business resource
		 */
		Business : function(){
			return $resource(
					baseUrl + 'businesses/:id',
					{id:'@id'},
					{
						'getClients': { 
							isArray: true,
							method:'GET',
							url : baseUrl + 'businesses/'+ businessId +'/clients'
						},
						
						'getStats' : {
							method:'GET',
							url : baseUrl + 'businesses/'+ businessId +'/stats'
						},
						
						'setDefaultLayout' : {
							method:'PUT',
							url : baseUrl + 'businesses/'+ businessId +'/defaulttemplate/:defaultLayoutType',
							params : {defaultLayoutType : '@defaultLayoutType'}
						},
						
						'update': { 
							method:'PUT' 
						}
					}
			);
		},


		/*
		 * Sharing Permit resource
		 */
		SharingPermit : function(){
			return $resource(
					baseUrl + 'businesses/'+ businessId +'/sharepermits/:id',
					{id:'@id'},
					{
						sendEmail : {
							method : 'POST',
							params : {id:'@id'},
							url : baseUrl + 'businesses/'+ businessId +'/sharepermits/:id/email'
						}
					}
			);
		}

	};
}]);
