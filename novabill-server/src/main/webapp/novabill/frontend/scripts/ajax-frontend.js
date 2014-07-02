'use strict';

angular.module('novabill-frontend.ajax', ['novabill-frontend.constants'])


/**
 * The ajax service stores all the resources used in Novabill Frontend area
 */
.factory('nAjaxFrontend', ['nConstantsFrontend', '$http', function(nConstantsFrontend, $http) {

	var baseUrl = nConstantsFrontend.conf.baseUrl;
	
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
		}

	};
}]);