'use strict';

angular.module('novabill.creditNotes.controllers', 
		['novabill.utils', 'novabill.directives', 'novabill.directives.dialogs', 'novabill.translations', 'novabill.gwtbridge', 'infinite-scroll'])


		/**
		 * CREDIT NOTES PAGE CONTROLLER
		 */
		.controller('CreditNoteCtrl', ['$scope', '$location', 'nConstants', 'nSelectClientDialog', '$filter', 
		                               function($scope, $location, nConstants, nSelectClientDialog, $filter){

			var YEAR_PARAM = 'year';
			var FILTER_QUERY_PARAM = 'filter';
			var loadedCreditNotes = [];
			var filteredCreditNotes = [];
			var PARTITION = 50;

			$scope.selectedYear = $location.search()[YEAR_PARAM]? $location.search()[[YEAR_PARAM]]: String(new Date().getFullYear());

			$scope.query = $location.search()[FILTER_QUERY_PARAM]? $location.search()[[FILTER_QUERY_PARAM]]: '';

			function updateFilteredCreditNotes(){
				filteredCreditNotes = $filter('filter')(loadedCreditNotes, $scope.query);
				$scope.creditNotes = filteredCreditNotes.slice(0, 15);
			}
			
			$scope.$watch('query', function(newValue, oldValue){
				$location.search(FILTER_QUERY_PARAM, newValue == ''? null : newValue);
				updateFilteredCreditNotes();
			});

			$scope.loadCreditNotes = function(year) {
				$scope.selectedYear = year;
				$location.search(YEAR_PARAM, year);
				$scope.creditNotes = null;

				GWT_Server.creditNote.getAllInRange(nConstants.conf.businessId, $scope.selectedYear, '0', '1000000', {
					onSuccess : function(page){
						$scope.$apply(function(){
							loadedCreditNotes = page.items;
							updateFilteredCreditNotes();
						});
					},

					onFailure : function(error){}
				});
			};

			$scope.loadMoreCreditNotes = function(){
				if($scope.invoices){
					var currentIndex = $scope.creditNotes.length;
					$scope.creditNotes = $scope.creditNotes.concat(filteredCreditNotes.slice(currentIndex, currentIndex+PARTITION));
				}
			};

			$scope.newCreditNoteClick = function(){
				var instance = nSelectClientDialog.open();
				instance.result.then(
						function (clientId) {
							//workaround to enable scroll
							window.location.assign( nConstants.url.creditNoteNew(clientId) );
							window.location.reload();
//							$location.path('/new/'+clientId);
						},
						function () {
						}
				);
			};

			$scope.$on(nConstants.events.CREDIT_NOTE_REMOVED, function(){
				$scope.$apply(function(){
					$scope.creditNotes = null;
				});
				$scope.loadCreditNotes($scope.selectedYear);
			});

		}])




		/**
		 * ESTIMATION MODIFY PAGE CONTROLLER
		 */
		.controller('CreditNoteDetailsCtrl', ['$scope', '$routeParams', '$location', '$translate', 'nSafeHistoryBack',
		                                      function($scope, $routeParams, $location, $translate, nSafeHistoryBack) {
			$scope.pageTitle = $translate('MODIFY_CREDIT_NOTE');

			GWT_UI.showModifyCreditNotePage('credit-note-details', $routeParams.creditNoteId, {
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
		 * ESTIMATION CREATE PAGE CONTROLLER
		 */
		.controller('CreditNoteCreateCtrl', ['$scope', '$routeParams', '$location', '$translate',
		                                     function($scope, $routeParams, $location, $translate) {
			$scope.pageTitle = $translate('NEW_CREDIT_NOTE');

			GWT_UI.showNewCreditNotePage('credit-note-details', $routeParams.clientId, {
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
		 * ESTIMATION CREATE PAGE CONTROLLER
		 */
		.controller('CreditNoteFromInvoiceCtrl', ['$scope', '$routeParams', '$location', '$translate',
		                                          function($scope, $routeParams, $location, $translate) {
			$scope.pageTitle = $translate('NEW_CREDIT_NOTE');

			GWT_UI.showFromInvoiceCreditNotePage('credit-note-details', $routeParams.invoiceId, {
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


