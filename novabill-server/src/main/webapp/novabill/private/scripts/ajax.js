'use strict';

angular.module('novabill.ajax', ['ngResource', 'angularFileUpload'])

.config(['$httpProvider', function($httpProvider){

	/*
	 *  CSRF protection
	 */
	var csrfHeaderName = angular.element("meta[name='_csrf_header']").attr("content");
	var csrfHeaderValue = angular.element("meta[name='_csrf']").attr("content");

	// delete usually is not present
	$httpProvider.defaults.headers['delete'] = 
		$httpProvider.defaults.headers['delete'] ? $httpProvider.defaults.headers['delete'] : {};

		// set the header for DELETE / POST / PUT calls
		$httpProvider.defaults.headers['delete'][ csrfHeaderName ] = csrfHeaderValue;
		$httpProvider.defaults.headers['post'][ csrfHeaderName ] = csrfHeaderValue;
		$httpProvider.defaults.headers['put'][ csrfHeaderName ] = csrfHeaderValue;

}])


/**
 * The ajax service stores all the resources used in Novabill
 */
.factory('nAjax', ['nConstants', '$resource', '$upload', '$http', function(nConstants, $resource, $upload, $http) {

	var businessId = nConstants.conf.businessId;
	var baseUrl = nConstants.conf.ajaxBaseUrl;

	return {

		/*
		 * Business resource
		 */
		Business : function(){
			return $resource(
					baseUrl + 'businesses/:id',
					{ id : businessId },
					{
						'getClients': { 
							isArray: true,
							method:'GET',
							url : baseUrl + 'businesses/:id/clients',
						},

						'getStats' : {
							method:'GET',
							url : baseUrl + 'businesses/:id/stats',
						},

						'setDefaultLayout' : {
							method:'PUT',
							url : baseUrl + 'businesses/:id/defaulttemplate/:defaultLayoutType',
							params : {
								defaultLayoutType : '@defaultLayoutType'
							}
						},

						'update': { 
							method:'PUT'
						}
					}
			);
		},


		/*
		 * Business Logo
		 */
		BusinessLogo : function(){
			return {

				upload : function(file, successFn, failureFn){
					$upload.upload({
						url: nConstants.conf.logoUrl,
						file: file,
					}).success( successFn );
				},

				remove : function(successFn, failureFn){
					$http({
						method : 'DELETE',
						url : nConstants.conf.logoUrl
					}).success( successFn );
				}
			};

		},



		/*
		 * Commodity resource
		 */
		Commodity : function(){
			return $resource(
					baseUrl + 'businesses/:businessId/commodities/:id',
					{
						businessId: businessId,
						id : '@id'
					},
					{
						
						'update': { 
							method:'PUT'
						}
					}
			);
		},
		
		
		CommodityUtils : function() {
			return {
				
				addOrUpdatePrice: function(params, successFn, failureFn){
					$http({
						method : 'POST',
						url : baseUrl + 'businesses/'+ businessId +'/commodities/'+ params.commodityID +'/pricelists/'+params.priceListID+'/prices',
						data : params
					}).success( successFn );
				},
				
				addOrUpdatePrices: function(params, successFn, failureFn){
					$http({
						method : 'POST',
						url : baseUrl + 'businesses/'+ businessId +'/commodities/prices/batch',
						data : params
					}).success( successFn );
				},
				
				removePrice: function(params, successFn, failureFn){
					$http({
						method : 'DELETE',
						url : baseUrl + 'businesses/'+ businessId +'/commodities/'+ params.commodityID +'/pricelists/'+params.priceListID+'/prices'
					}).success( successFn );
				}
			};
		},
		
		
		
		/*
		 * PriceList resource
		 */
		PriceList : function(){
			return $resource(
					baseUrl + 'businesses/:businessId/pricelists/:id',
					{
						businessId: businessId,
						id : '@id'
					},
					{
						
						'update': { 
							method: 'PUT'
						},
						
						'getPrices': { 
							method: 'GET',
							url : baseUrl + 'businesses/:businessId/pricelists/:id/prices'
						},
						
						'clonePriceList': { 
							method: 'POST',
							url : baseUrl + 'businesses/:businessId/pricelists/:id/clone?priceListName=:priceListName',
							params : {
								priceListName : '@priceListName'
							}
						}
					}
			);
		},
		


		/*
		 * Sharing Permit resource
		 */
		SharingPermit : function(){
			return $resource(
					baseUrl + 'businesses/:businessId/sharepermits/:id',
					{
						businessId : businessId,
						id : '@id'
					},
					{
						sendEmail : {
							method : 'POST',
							url : baseUrl + 'businesses/:businessId/sharepermits/:id/email'
						}
					}
			);
		}

	};
}]);
