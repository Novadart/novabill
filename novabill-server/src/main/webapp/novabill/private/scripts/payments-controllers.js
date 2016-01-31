'use strict';

angular.module('novabill.payments.controllers', ['novabill.translations', 'novabill.directives',
	'novabill.directives.dialogs', 'infinite-scroll', 'ui.bootstrap'])


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

			if($location.search().tab) {
				$scope.activeTab[$location.search().tab] = true;
			} else {
				$scope.activeTab.paymentsstatus = true;
			}

			$timeout(function(){
				GWT_UI.showPaymentsPage('payments-page');
			}, 1000);

			$scope.uiBootstrap = {
				filteringDateType : 'PAYMENT_DUEDATE',
				startDate : null,
				endDate : null,
				openSD : false,
				openED : false
			};
			var loadedInvoices = [];
			var PARTITION = 50;

			$scope.loadInvoices = function(filteringDateType, startDate, endDate){
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
			};

			$scope.loadMoreInvoices = function(){
				if($scope.invoices){
					var currentIndex = $scope.invoices.length;
					$scope.invoices = $scope.invoices.concat(loadedInvoices.slice(currentIndex, currentIndex+PARTITION));
				}
			};

			$scope.openStartDate = function($event) {
				$event.preventDefault();
				$event.stopPropagation();
				$scope.uiBootstrap.openSD = true;
			};

			$scope.openEndDate = function($event) {
				$event.preventDefault();
				$event.stopPropagation();
				$scope.uiBootstrap.openED = true;
			};

			$scope.clear = function(){
				$scope.uiBootstrap.startDate = null;
				$scope.uiBootstrap.endDate = null;
				$scope.uiBootstrap.invoices = null;
				$scope.loadInvoices('PAYMENT_DUEDATE', null, null);
			};

			$scope.print = function(){
				nDownload.downloadPaymentsProspect($scope.uiBootstrap.filteringDateType, $scope.uiBootstrap.startDate, $scope.uiBootstrap.endDate);
			};

			$scope.$watch('uiBootstrap.startDate', function(newValue, oldValue){
				if(newValue != null){
					$scope.loadInvoices($scope.uiBootstrap.filteringDateType, newValue, $scope.uiBootstrap.endDate);
				}
			});

			$scope.$watch('uiBootstrap.endDate', function(newValue, oldValue){
				if(newValue != null){
					$scope.loadInvoices($scope.uiBootstrap.filteringDateType, $scope.uiBootstrap.startDate, newValue);
				}
			});

			$scope.$watch('uiBootstrap.filteringDateType', function(newValue, oldValue){
				if(newValue != null){
					$scope.loadInvoices($scope.uiBootstrap.filteringDateType, $scope.uiBootstrap.startDate, $scope.uiBootstrap.endDate);
				}
			});

		}]);