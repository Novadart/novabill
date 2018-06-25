'use strict';

angular.module('novabill.invoices.controllers',
	['novabill.utils', 'novabill.directives', 'novabill.directives.dialogs', 'novabill.translations', 'infinite-scroll', 'novabill.gwtbridge'])


/**
 * INVOICES PAGE CONTROLLER
 */
	.controller('InvoicesCtrl', ['$scope', '$location', 'nConstants', 'nSelectClientDialog', '$filter', 'nEditDocumentIdClassDialog', 'nAjax', 'nConfirmDialog', 'nAlertDialog',
		function($scope, $location, nConstants, nSelectClientDialog, $filter, nEditDocumentIdClassDialog, nAjax, nConfirmDialog, nAlertDialog){
			var YEAR_PARAM = 'year';
			var DOC_CLASS_PARAM = 'doc-class';
			var FILTER_QUERY_PARAM = 'filter';
			var loadedInvoices = [];
			var filteredInvoices = [];
			var PARTITION = 50;
			var DocumentIDClass = nAjax.DocumentIDClass();

			$scope.selectedYear = $location.search()[YEAR_PARAM]? $location.search()[[YEAR_PARAM]]: String(new Date().getFullYear());

			$scope.selectedClass = $location.search()[DOC_CLASS_PARAM]? $location.search()[[DOC_CLASS_PARAM]]: null;

			$scope.uiBootstrap = {
				query : $location.search()[FILTER_QUERY_PARAM]? $location.search()[[FILTER_QUERY_PARAM]]: ''
			};

			$scope.onTabChange = function(token){
				$location.search('tab',token);
			};

			$scope.activeTab = {
				invoices : false,
				docIdClasses : false
			};

			if($location.search().tab) {
				$scope.activeTab[$location.search().tab] = true;
			} else {
				$scope.activeTab.invoices = true;
			}

			function updateFilteredInvoices(){
				filteredInvoices = $filter('filter')(loadedInvoices, $scope.uiBootstrap.query);
				$scope.invoices = filteredInvoices.slice(0, 15);
			}

			$scope.$watch('uiBootstrap.query', function(newValue, oldValue){
				$location.search(FILTER_QUERY_PARAM, newValue == ''? null : newValue);
				updateFilteredInvoices();
			});

			$scope.loadInvoicesByYear = function(year) {
				$scope.selectedYear = year;
				$location.search(YEAR_PARAM, year);
				$scope.loadInvoices($scope.selectedYear, $scope.selectedClass);
			};

			$scope.loadInvoicesForClass = function(claz) {
				$scope.selectedClass = claz;
				$location.search(DOC_CLASS_PARAM, claz);
				$scope.loadInvoices($scope.selectedYear, $scope.selectedClass);
			};


			$scope.loadInvoices = function(year, claz) {
				$scope.invoices = null;

				GWT_Server.invoice.getAllInRange(nConstants.conf.businessId, year, claz, '0', '1000000', {
					onSuccess : function(page){
						$scope.$apply(function(){
							loadedInvoices = page.items;
							updateFilteredInvoices();
						});
					},

					onFailure : function(error){}
				});
			};

			$scope.loadMoreInvoices = function(){
				if($scope.invoices){
					var currentIndex = $scope.invoices.length;
					$scope.invoices = $scope.invoices.concat(filteredInvoices.slice(currentIndex, currentIndex+PARTITION));
				}
			};

			$scope.newInvoiceClick = function(){
				var instance = nSelectClientDialog.open();
				instance.result.then(
					function (clientId) {
						//workaround to enable scroll
						window.location.assign( nConstants.url.invoiceNew(clientId) );
						window.location.reload();
//							$location.path('/new/'+clientId);
					},
					function () {
					}
				);
			};

			$scope.$on(nConstants.events.INVOICE_REMOVED, function(){
				$scope.$apply(function(){
					$scope.invoices= null;
				});
				$scope.loadInvoices($scope.selectedYear, $scope.selectedClass);
			});


			/*
			* DOCUMENT ID CLASS TAB
			*/
			$scope.loadDocIDClasses = function(){
				DocumentIDClass.query(function(docIDClasses){
					$scope.docIdClasses = docIDClasses;
				});
			};

			$scope.newDocIDClass = function(){
				var newDocIDClass = new DocumentIDClass();
				newDocIDClass.business = { id : nConstants.conf.businessId };

				var instance = nEditDocumentIdClassDialog.open(newDocIDClass);
				instance.result.then(function(docIdClass){
					docIdClass.$save( $scope.loadDocIDClasses );
				});
			};

			$scope.editDocIDClass = function(docIdClass){
				var instance = nEditDocumentIdClassDialog.open(docIdClass, true);
				instance.result.then(function(updatedDocIdClass){
					updatedDocIdClass.business = { id : nConstants.conf.businessId };
					updatedDocIdClass.$update(function(){
						$scope.loadDocIDClasses();
					});
				});
			};

			$scope.removeDocIDClass = function(docIdClass){
				var instance = nConfirmDialog.open( $filter('translate')('REMOVAL_QUESTION',{data : docIdClass.suffix}) );
				instance.result.then(function(value){
					if(value){
						docIdClass.$delete(function(result){
							if(result.value){
								nAlertDialog.open( $filter('translate')('DOCUMENT_ID_CLASS_DELETION_ALERT') );
							}
							$scope.loadDocIDClasses();
						});
					}
				});
			};


			$scope.loadDocIDClasses();

		}])




/**
 * INVOICE MODIFY PAGE CONTROLLER
 */
	.controller('InvoiceDetailsCtrl', ['$scope', '$routeParams', '$location', '$translate', 'nSafeHistoryBack',
		function($scope, $routeParams, $location, $translate, nSafeHistoryBack) {
			$scope.pageTitle = $translate('MODIFY_INVOICE');

			GWT_UI.showModifyInvoicePage('invoice-details', $routeParams.invoiceId, {
				onSuccess : function(bool){
					nSafeHistoryBack.safeBack();
				},
				onFailure : function(){
					$scope.$apply(function(){
						nSafeHistoryBack.safeBack();
					});
				}
			});



		}])



/**
 * INVOICE CREATE PAGE CONTROLLER
 */
	.controller('InvoiceCreateCtrl', ['$scope', '$routeParams', '$location', '$translate',
		function($scope, $routeParams, $location, $translate) {
			$scope.pageTitle = $translate('NEW_INVOICE');

			GWT_UI.showNewInvoicePage('invoice-details', $routeParams.clientId, {
				onSuccess : function(bool){
					$location.path('/');
				},
				onFailure : function(){
					$scope.$apply(function(){
						$location.path('/');
					});
				}
			});


		}])



/**
 * INVOICE CREATE FROM ESTIMATION PAGE CONTROLLER
 */
	.controller('InvoiceFromEstimationCtrl', ['$scope', '$routeParams', '$location', '$translate',
		function($scope, $routeParams, $location, $translate) {
			$scope.pageTitle = $translate('NEW_INVOICE');

			GWT_UI.showFromEstimationInvoicePage('invoice-details', $routeParams.estimationId, {
				onSuccess : function(bool){
					$location.path('/');

				},
				onFailure : function(error){
					$scope.$apply(function(){
						$location.path('/');
					});
				}
			});


		}])


/**
 * INVOICE CREATE FROM TRANSPORT DOCUMENT LIST PAGE CONTROLLER
 */
	.controller('InvoiceFromTransportDocumentListCtrl', ['$scope', '$routeParams', '$location', '$translate',
		function($scope, $routeParams, $location, $translate) {
			$scope.pageTitle = $translate('NEW_INVOICE');

			GWT_UI.showFromTransportDocumentListInvoicePage('invoice-details', $routeParams.transportDocumentList, {
				onSuccess : function(bool){
					$location.path('/');
				},
				onFailure : function(){
					$scope.$apply(function(){
						$location.path('/');
					});
				}
			});


		}])


/**
 * INVOICE CLONE PAGE CONTROLLER
 */
	.controller('InvoiceCloneInvoiceCtrl', ['$scope', '$routeParams', '$location', '$translate',
		function($scope, $routeParams, $location, $translate) {
			$scope.pageTitle = $translate('NEW_INVOICE');

			GWT_UI.showCloneInvoicePage('invoice-details', $routeParams.clientId, $routeParams.sourceId, {
				onSuccess : function(bool){
					$location.path('/');
				},
				onFailure : function(){
					$scope.$apply(function(){
						$location.path('/');
					});
				}
			});


		}]);


