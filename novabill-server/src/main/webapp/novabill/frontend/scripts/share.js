'use strict';

angular.module('novabill-frontend.share', ['novabill-frontend.ajax', 'novabill-frontend.directives', 'novabill-frontend.translations', 
                                           'novabill-frontend.constants', 'infinite-scroll', 'ui.bootstrap'])

                                           
.factory('nQueryParams', ['$window', function($window){
	var searchStr = $window.location.search.substring(1);
	var toks = searchStr.split('&');
	
	var queryParams = {};
	var tmp = null;
	for(var i in toks){
		tmp = toks[i].split('=');
		queryParams[tmp[0]] = tmp[1];
	}
	
	return queryParams;
}])



.controller('ShareCtrl', ['$scope', '$document', 'nQueryParams', 'nAjaxFrontend', 'nConstantsFrontend',
                          function($scope, $document, nQueryParams, nAjaxFrontend, nConstantsFrontend){
	
	$scope.dateOptions = {
			'starting-day' : '1',
			'clear-text' : 'Ripulisci'
	};
	
	var loadedInvoices = [];
	var PARTITION = 50;
	
	var now = new Date();
	var DEFAULT_START_DATE = new Date(now.getFullYear(), 0, 1);
	var DEFAULT_END_DATE = new Date(now.getFullYear(), 11, 31);
	
	var HIDDEN_IFRAME = angular.element('<iframe style="display: none;"></iframe>');
	angular.element($document[0].body).append(HIDDEN_IFRAME);
	
	function formatDate(date){
		var formatedDate = date.getFullYear() + '-' + ('0'+(date.getMonth()+1)).slice(-2) + '-' + ('0'+date.getDate()).slice(-2);
		return formatedDate;
	}
	
	function loadInvoices(startDate, endDate){
		$scope.loading = true;
		$scope.invoices = null;
		
		nAjaxFrontend.getInvoices(nQueryParams.businessID, nQueryParams.token, 
				startDate != null ? formatDate(startDate) : '', 
				endDate != null ? formatDate(endDate) : '', 
				function(invoices){
					
					loadedInvoices = invoices;
					$scope.invoices = loadedInvoices.slice(0, 15);
					$scope.loading = false;
					
		});
	}
	
	$scope.loadMoreInvoices = function(){
		if($scope.invoices){
			var currentIndex = $scope.invoices.length;
			$scope.invoices = $scope.invoices.concat(loadedInvoices.slice(currentIndex, currentIndex+PARTITION));
		}
	};
	
	$scope.openStartDate = function($event) {
		$event.preventDefault();
		$event.stopPropagation();
		$scope.openSD = true;
	};
	
	$scope.openEndDate = function($event) {
		$event.preventDefault();
		$event.stopPropagation();
		$scope.openED = true;
	};
	
	$scope.clear = function(){
		$scope.startDate = DEFAULT_START_DATE;
		$scope.endDate = DEFAULT_END_DATE;
		$scope.invoices = null;
		loadInvoices(DEFAULT_START_DATE, DEFAULT_END_DATE);
	};
	
	$scope.print = function(){
		var sd = $scope.startDate != null ? formatDate($scope.startDate) : ''; 
		var ed = $scope.endDate != null ? formatDate($scope.endDate) : '';
		
		var url = nConstantsFrontend.conf.baseUrl + 'share/{businessID}/{token}/download?startDate={startDate}&endDate={endDate}'
		.replace('{businessID}', nQueryParams.businessID)
		.replace('{token}', nQueryParams.token)
		.replace('{startDate}', sd)
		.replace('{endDate}', ed);
		
		HIDDEN_IFRAME.attr('src', url);
	};

	$scope.$watch('startDate', function(newValue, oldValue){
		if(newValue != null){
			loadInvoices(newValue, $scope.endDate);
		}
	});
	
	$scope.$watch('endDate', function(newValue, oldValue){
		if(newValue != null){
			loadInvoices($scope.startDate, newValue);
		}
	});
	
	$scope.clear();
	
}]);