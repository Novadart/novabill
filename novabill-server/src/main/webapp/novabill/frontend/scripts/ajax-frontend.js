'use strict';

angular.module('novabill-frontend.ajax', ['novabill-frontend.constants'])


/**
 * The ajax service stores all the resources used in Novabill Frontend area
 */
.factory('nAjaxFrontend', ['nConstantsFrontend', '$http', function(nConstantsFrontend, $http) {

	var baseUrl = nConstantsFrontend.conf.baseUrl;
	
	return {

		/*
		 * Stats resource
		 */
		Sharing : function() {

			return {

				getInvoices : function(businessID, token, startDate, endDate, onSuccess){
					var url = baseUrl + 'share/{businessID}/invoices/filter?token={token}&startDate={startDate}&endDate={endDate}'
							.replace('{businessID}', businessID)
							.replace('{token}', token)
							.replace('{startDate}', startDate)
							.replace('{endDate}', endDate);
					$http.get(url).success( onSuccess );
				},

				getCreditNotes : function(businessID, token, startDate, endDate, onSuccess){
					var url = baseUrl + 'share/{businessID}/creditnotes/filter?token={token}&startDate={startDate}&endDate={endDate}'
							.replace('{businessID}', businessID)
							.replace('{token}', token)
							.replace('{startDate}', startDate)
							.replace('{endDate}', endDate);
					$http.get(url).success( onSuccess );
				},

				getClients : function(businessID, token, onSuccess){
					var url = baseUrl + 'share/{businessID}/clients?token={token}'
							.replace('{businessID}', businessID)
							.replace('{token}', token);
					$http.get(url).success( onSuccess );
				},

				getGeneralBIStats: function(businessId, year, token, successFn){
					var url = baseUrl + 'share/{businessID}/bizintel/genstats/{year}?token={token}'
							.replace('{businessID}', businessId)
							.replace('{year}', year)
							.replace('{token}', token);
					$http.get(url).success( successFn );
				},

				getClientBIStats: function(businessId, clientId, year, token, successFn){
					var url = baseUrl + 'share/{businessID}/bizintel/clientstats/{clientId}/{year}?token={token}'
							.replace('{businessID}', businessId)
							.replace('{clientId}', clientId)
							.replace('{year}', year)
							.replace('{token}', token);
					$http.get(url).success( successFn );
				}

			};
		}

	};
}]);