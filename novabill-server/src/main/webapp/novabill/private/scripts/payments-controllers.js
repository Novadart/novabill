'use strict';

angular.module('novabill.payments.controllers', ['novabill.translations', 'novabill.directives', 
                                                 'novabill.directives.dialogs', 
                                                 'infinite-scroll', 'ui.bootstrap'])


/**
 * SETTINGS PAGE CONTROLLER
 */
.controller('PaymentsCtrl', ['$scope', 'nConstants', 'nDownload', '$location', '$timeout',
                             function($scope, nConstants, nDownload, $location, $timeout){
	
	$scope.dateOptions = {
			'starting-day' : '1',
			'clear-text' : 'Ripulisci'
	};
	
	$scope.onTabChange = function(token){
		$location.search('tab',token);
	};
	
	$scope.activeTab = {
			paymentsstatus : false,
			paymenttypes : false
	};
	$scope.activeTab[$location.search().tab] = true;

	$timeout(function(){
		GWT_UI.showPaymentsPage('payments-page');
	}, 1000);

	$scope.filteringDateType = 'PAYMENT_DUEDATE';
	var loadedInvoices = [];
	var PARTITION = 50;
	
	function loadInvoices(filteringDateType, startDate, endDate){
		$scope.loading = true;
		$scope.invoices = null;
		
		var sd = startDate ? String(startDate.getTime()) : null;
		var ed = endDate ? String(endDate.getTime()) : null;
		
		GWT_Server.invoice.getAllUnpaidInDateRange(filteringDateType, sd, ed, {
			onSuccess : function(list){
				$scope.$apply(function(){
					loadedInvoices = list.invoices;
					$scope.invoices = loadedInvoices.slice(0, 15);
					$scope.loading = false;
				});
			},
			onFailure : function(){
				$scope.$apply(function(){
					$scope.loading = false;
				});
			}
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
		$scope.startDate = null;
		$scope.endDate = null;
		$scope.invoices = null;
		loadInvoices('PAYMENT_DUEDATE', null, null);
	};
	
	$scope.print = function(){
		nDownload.downloadPaymentsProspect($scope.filteringDateType, $scope.startDate, $scope.endDate);
	};

	$scope.$watch('startDate', function(newValue, oldValue){
		if(newValue != null){
			loadInvoices($scope.filteringDateType, newValue, $scope.endDate);
		}
	});
	
	$scope.$watch('endDate', function(newValue, oldValue){
		if(newValue != null){
			loadInvoices($scope.filteringDateType, $scope.startDate, newValue);
		}
	});
	
	$scope.$watch('filteringDateType', function(newValue, oldValue){
		if(newValue != null){
			loadInvoices($scope.filteringDateType, $scope.startDate, $scope.endDate);
		}
	});
	
	$scope.clear();
	
}]);