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
	var loadedCreditNotes = [];
	var PARTITION = 50;
	var firstLoad = true;
	
	var now = new Date();
	var DEFAULT_START_DATE = new Date(now.getFullYear(), 0, 1);
	var DEFAULT_END_DATE = new Date(now.getFullYear(), 11, 31);
	
	var HIDDEN_IFRAME = angular.element('<iframe style="display: none;"></iframe>');
	angular.element($document[0].body).append(HIDDEN_IFRAME);
	
	$scope.formatDate = function(date){
		var formatedDate = date.getFullYear() + '-' + ('0'+(date.getMonth()+1)).slice(-2) + '-' + ('0'+date.getDate()).slice(-2);
		return formatedDate;
	};
	
	$scope.isLoading = function(){
		return $scope.loadingInvoices || $scope.loadingCreditNotes;
	};
	
	$scope.loadDocs = function(startDate, endDate){
		$scope.loadingInvoices = true;
		$scope.loadingCreditNotes = true;
		$scope.invoices = null;
		$scope.creditNotes = null;
		
		nAjaxFrontend.getInvoices(nQueryParams.businessID, nQueryParams.token, 
				startDate != null ? $scope.formatDate(startDate) : '', 
				endDate != null ? $scope.formatDate(endDate) : '', 
				function(invoices){
					
					firstLoad = false;
					loadedInvoices = invoices;
					$scope.invoices = loadedInvoices.slice(0, 15);
					$scope.loadingInvoices = false;
					
		});
		
		nAjaxFrontend.getCreditNotes(nQueryParams.businessID, nQueryParams.token, 
				startDate != null ? $scope.formatDate(startDate) : '', 
				endDate != null ? $scope.formatDate(endDate) : '', 
				function(notes){
					
					firstLoad = false;
					loadedCreditNotes = notes;
					$scope.creditNotes = loadedCreditNotes.slice(0, 15);
					$scope.loadingCreditNotes = false;
					
		});
	};
	
	$scope.loadMoreInvoices = function(){
		if($scope.invoices){
			var currentIndex = $scope.invoices.length;
			$scope.invoices = $scope.invoices.concat(loadedInvoices.slice(currentIndex, currentIndex+PARTITION));
		}
	};
	
	$scope.loadMoreCreditNotes = function(){
		if($scope.creditNotes){
			var currentIndex = $scope.creditNotes.length;
			$scope.creditNotes = $scope.creditNotes.concat(loadedCreditNotes.slice(currentIndex, currentIndex+PARTITION));
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
		$scope.loadDocs(DEFAULT_START_DATE, DEFAULT_END_DATE);
	};
	
	$scope.download = function(docsType){
		var sd = $scope.startDate != null ? $scope.formatDate($scope.startDate) : ''; 
		var ed = $scope.endDate != null ? $scope.formatDate($scope.endDate) : '';
		
		var url = nConstantsFrontend.conf.baseUrl + 'share/{businessID}/'+docsType+'/download?token={token}&startDate={startDate}&endDate={endDate}'
		.replace('{businessID}', nQueryParams.businessID)
		.replace('{token}', nQueryParams.token)
		.replace('{startDate}', sd)
		.replace('{endDate}', ed);
		
		HIDDEN_IFRAME.attr('src', url);
	};

	$scope.$watch('startDate', function(newValue, oldValue){
		if(!firstLoad){
			$scope.loadDocs(newValue, $scope.endDate);
		}
	});
	
	$scope.$watch('endDate', function(newValue, oldValue){
		if(!firstLoad){
			$scope.loadDocs($scope.startDate, newValue);
		}
	});

	$scope.clear();
}]);