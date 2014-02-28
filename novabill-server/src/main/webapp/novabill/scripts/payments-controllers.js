'use strict';

angular.module('novabill.payments.controllers', ['novabill.translations', 'novabill.directives', 'novabill.directives.dialogs', 'infinite-scroll', 'ui.bootstrap'])


/**
 * SETTINGS PAGE CONTROLLER
 */
.controller('PaymentsCtrl', ['$scope', 'nConstants', function($scope, nConstants){
	
	$scope.dateOptions = {
			'starting-day' : '1',
			'clear-text' : 'Ripulisci'
	};

	GWT_UI.showPaymentsPage('payments-page');

	
	var loadedInvoices = [];
	var PARTITION = 50;
	
	function loadInvoices(startDate,endDate){
		$scope.loading = true;
		
		var sd = startDate ? String(startDate.getTime()) : null;
		var ed = endDate ? String(endDate.getTime()) : null;
		
		GWT_Server.invoice.getAllUnpaidInDateRange(sd, ed, {
			onSuccess : function(list){
				$scope.$apply(function(){
					loadedInvoices = list.invoices;
					$scope.invoices = loadedInvoices.slice(0, 15);
					$scope.loading = false;
				});
			},
			onFailure : function(){}
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
		loadInvoices(null, null);
	};
	
	$scope.print = function(){
		var sd = $scope.startDate ? String($scope.startDate.getTime()) : null;
		var ed = $scope.endDate ? String($scope.endDate.getTime()) : null;
		GWT_UI.generatePaymentsProspectPdf(sd, ed);
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