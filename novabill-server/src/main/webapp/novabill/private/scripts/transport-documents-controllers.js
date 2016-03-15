'use strict';

angular.module('novabill.transportDocuments.controllers',
	['novabill.utils', 'novabill.directives', 'novabill.directives.dialogs', 'novabill.ajax',
		'novabill.translations', 'novabill.constants', 'infinite-scroll', 'novabill.gwtbridge', 'ui.bootstrap'])


/**
 * TRANSPORT DOCUMENTS PAGE CONTROLLER
 */
	.controller('TransportDocumentCtrl', ['$scope', '$location', '$filter', 'nConstants', 'nSelectClientDialog',
		'nEditTransporterDialog', 'nConfirmDialog', 'nAjax',
		function($scope, $location, $filter, nConstants, nSelectClientDialog,
				 nEditTransporterDialog, nConfirmDialog, nAjax){
			var YEAR_PARAM = 'year';
			var FILTER_QUERY_PARAM = 'filter';

			var Transporter = nAjax.Transporter();
			var loadedTransportDocuments = [];
			var filteredTransportDocuments = [];
			var PARTITION = 50;

			$scope.selectedYear = $location.search()[YEAR_PARAM]? $location.search()[[YEAR_PARAM]]: String(new Date().getFullYear());

			$scope.uiBootstrap = {
				query : $location.search()[FILTER_QUERY_PARAM]? $location.search()[[FILTER_QUERY_PARAM]]: ''
			};

			$scope.onTabChange = function(token){
				$location.search('tab',token);
			};

			$scope.activeTab = {
				transportdocuments : false,
				transporters : false
			};

			if($location.search().tab) {
				$scope.activeTab[$location.search().tab] = true;
			} else {
				$scope.activeTab.transportdocuments = true;
			}

			$scope.loadTransporters = function(){
				Transporter.query(function(transporters){
					$scope.transporters = transporters;
				});
			};

			function updateFilteredTransportDocuments(){
				filteredTransportDocuments = $filter('filter')(loadedTransportDocuments, $scope.uiBootstrap.query);
				$scope.transportDocuments = filteredTransportDocuments.slice(0, 15);
			}

			$scope.$watch('uiBootstrap.query', function(newValue, oldValue){
				$location.search(FILTER_QUERY_PARAM, newValue == ''? null : newValue);
				updateFilteredTransportDocuments();
			});

			$scope.loadTransportDocuments = function(year) {
				$scope.selectedYear = year;
				$location.search(YEAR_PARAM, year);
				$scope.transportDocuments = null;

				GWT_Server.transportDocument.getAllInRange(nConstants.conf.businessId, $scope.selectedYear, '0', '1000000', {
					onSuccess : function(page){
						$scope.$apply(function(){
							loadedTransportDocuments = page.items;
							updateFilteredTransportDocuments();
						});
					},

					onFailure : function(error){}
				});
			};

			$scope.loadMoreTransportDocuments = function(){
				if($scope.transportDocuments){
					var currentIndex = $scope.transportDocuments.length;
					$scope.transportDocuments = $scope.transportDocuments.concat(filteredTransportDocuments.slice(currentIndex, currentIndex+PARTITION));
				}
			};

			$scope.newTransportDocumentClick = function(){
				var instance = nSelectClientDialog.open();
				instance.result.then(
					function (clientId) {
						//workaround to enable scroll
						window.location.assign( nConstants.url.transportDocumentNew(clientId) );
						window.location.reload();
//							 $location.path('/new/'+clientId);
					},
					function () {
					}
				);
			};

			$scope.$on(nConstants.events.TRANSPORT_DOCUMENT_REMOVED, function(){
				$scope.$apply(function(){
					$scope.transportDocuments = null;
				});
				$scope.loadTransportDocuments($scope.selectedYear);
			});

			$scope.newTransporter = function(){
				var newTransporter = new Transporter();
				newTransporter.business = { id : nConstants.conf.businessId };

				var instance = nEditTransporterDialog.open(newTransporter);
				instance.result.then(function(transporter){
					transporter.$save( $scope.loadTransporters );
				});
			};

			$scope.editTransporter = function(transporter){
				var instance = nEditTransporterDialog.open(transporter);
				instance.result.then(function(updatedTransporter){
					updatedTransporter.business = { id : nConstants.conf.businessId };
					updatedTransporter.$update(function(){
						$scope.loadTransporters();
					});
				});
			};

			$scope.removeTransporter = function(transporter){
				var instance = nConfirmDialog.open( $filter('translate')('REMOVAL_QUESTION',{data : transporter.name}) );
				instance.result.then(function(value){
					if(value){
						transporter.$delete(function(){
							$scope.loadTransporters();
						});
					}
				});
			};

			$scope.loadTransporters();
		}])




/**
 * TRANSPORT DOCUMENTS MODIFY PAGE CONTROLLER
 */
	.controller('TransportDocumentDetailsCtrl', ['$scope', '$routeParams', '$location', '$translate', 'nSafeHistoryBack',
		function($scope, $routeParams, $location, $translate, nSafeHistoryBack) {
			$scope.pageTitle = $translate('MODIFY_TRANSPORT_DOCUMENT');

			GWT_UI.showModifyTransportDocumentPage('transport-document-details', $routeParams.transportDocumentId, {
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
 * INVOICE CREATE FROM ESTIMATION PAGE CONTROLLER
 */
	.controller('TransportDocumentFromEstimationCtrl', ['$scope', '$routeParams', '$location', '$translate',
		function($scope, $routeParams, $location, $translate) {
			$scope.pageTitle = $translate('NEW_TRANSPORT_DOCUMENT');

			GWT_UI.showFromEstimationTransportDocumentPage('transport-document-details', $routeParams.estimationId, {
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
 * TRANSPORT DOCUMENTS CREATE PAGE CONTROLLER
 */
	.controller('TransportDocumentCreateCtrl', ['$scope', '$routeParams', '$location', '$translate',
		function($scope, $routeParams, $location, $translate) {
			$scope.pageTitle = $translate('NEW_TRANSPORT_DOCUMENT');

			GWT_UI.showNewTransportDocumentPage('transport-document-details', $routeParams.clientId, {
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


