'use strict';

angular.module('novabill.ajax', ['ngResource', 'angularFileUpload', 'novabill.logging'])

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
		
		
	/**
     * this interceptor uses the application logging service to
     * log server-side any errors from $http requests
     */
	$httpProvider.responseInterceptors.push(
			['$rootScope', '$q', '$injector','$location','nApplicationLogger',
			 function($rootScope, $q, $injector, $location, nApplicationLogger){

				return function(promise){
					return promise.then(function(response){
						// http on success
						return response;
					}, function (response) {
						// http on failure
						// in this example im just looking for 500, in production
						// you'd obviously need to be more discerning
						if(response.status === null || response.status === 500) {
							var error = {
									method: response.config.method,
									url: response.config.url,
									message: response.data,
									status: response.status
							};
							nApplicationLogger.error( angular.fromJson(error) );
						}
						
						return $q.reject(response);
					});
				};
			}
			]);	
	

}])


/**
 * The ajax service stores all the resources used in Novabill
 */
.factory('nAjax', ['nConstants', '$resource', '$upload', '$http', function(nConstants, $resource, $upload, $http) {

	var businessId = nConstants.conf.businessId;
	var baseUrl = nConstants.conf.ajaxBaseUrl;

	return {
		
		
		/*
		 * BatchDataFetcher
		 */
		BatchDataFetcherUtils : function() {
			return {
				
				fetchSelectCommodityForDocItemOpData: function(params, successFn, failureFn){
					$http({
						method : 'GET',
						url : baseUrl + 'businesses/'+ businessId +'/commodityselectdata/'+ params.clientID,
						data : params
					}).success( successFn );
				}
				
			};
		},

		/*
		 * Business resource
		 */
		Business : function(){
			return $resource(
					baseUrl + 'businesses/:id',
					{ id : (businessId==='-1' ? null : businessId)},
					{
						'getClients': { 
							isArray: true,
							method:'GET',
							url : baseUrl + 'businesses/:id/clients'
						},

						'getStats' : {
							method:'GET',
							url : baseUrl + 'businesses/:id/stats'
						},
						
						'getNotifications': { 
							isArray: true,
							method:'GET',
							url : baseUrl + 'businesses/:id/notifications'
						},
						
						'markNotificationAsSeen' : {
							method:'POST',
							url : baseUrl + 'businesses/:id/notifications/:notificationId/markseen',
							params : {
								notificationId : '@notificationId'
							}
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
						file: file
					}).success( successFn );
				},

				remove : function(successFn, failureFn){
					$http({
						method : 'DELETE',
						url : nConstants.conf.logoUrl
					}).success( successFn ).error( failureFn );
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
		 * Invoice resource
		 */
		InvoiceUtils : function() {
			return {
				
				email: function(params, successFn, failureFn){
					$http({
						method : 'POST',
						url : baseUrl + 'businesses/'+ businessId +'/invoices/'+ params.invoiceID +'/email',
						data : params.payload
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
		},
		
		
		
		/*
		 * Transporter resource
		 */
		Transporter : function(){
			return $resource(
					baseUrl + 'businesses/:businessId/transporters/:id',
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
		}

	};
}]);
