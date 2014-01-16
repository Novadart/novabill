'use strict';

angular.module('novabill.clients.controllers', 
		['novabill.utils', 'novabill.constants', 'novabill.directives', 'novabill.directives.dialogs', 'novabill.translations', 'infinite-scroll'])


		/**
		 * CLIENTS PAGE CONTROLLER
		 */
		.controller('ClientsCtrl', ['$scope', 'nSorting', '$location', 'nConstants',
		                            function($scope, nSorting, $location, nConstants){

			var loadedClients = [];
			var filteredClients = [];
			var displayedClientsCount = 0;
			var PARTITION = 50;

			function partitionClients(clients){
				//split it alphabetically
				var partitions = [];
				var pt = null;

				var lo = '', 
				l = '';
				var cl;

				for ( var id in clients) {
					cl = clients[id];
					l = cl.name.charAt(0).toUpperCase();

					if(l != lo) {
						pt = {
								letter : l,
								clients : []
						};
						partitions.push(pt);
					}

					pt.clients.push(cl);

					lo = l;
				}

				displayedClientsCount = clients.length;

				return partitions;
			}

			function containsQuery(query, client) {
				var normalizedQuery = query.toLowerCase();
				return (client.name && client.name.toLowerCase().indexOf(normalizedQuery) > -1) ||
				(client.address && client.address.toLowerCase().indexOf(normalizedQuery) > -1) ||
				(client.city && client.city.toLowerCase().indexOf(normalizedQuery) > -1);
			}

			function filterClients(query, clients){
				if(!query){
					return clients;
				}
				var result = [];
				angular.forEach(clients, function(client, index){
					if(containsQuery(query, client)){
						result.push(client);
					}
				});
				return result;
			}

			function updateLettersIndex(clients){
				var letters = {};
				var l = '', lo ='';
				angular.forEach(clients, function(client, index){
					l = client.name.charAt(0).toUpperCase();
					if(l !== lo){
						letters[l] = index;
					}
				});
				$scope.letters = letters;
			}
			
			function updateFilteredClients(generateTillIndex){
				filteredClients = filterClients($scope.query, loadedClients);
				updateLettersIndex(filteredClients);
				if(generateTillIndex){
					$scope.partitions = partitionClients( filteredClients.slice(0, generateTillIndex+1) );
				} else {
					$scope.partitions = partitionClients( filteredClients.slice(0, 20) );
				}
			}
			
			$scope.$watch('query', function(newValue, oldValue){
				updateFilteredClients();
			});

			$scope.loadMoreClients = function(){
				if($scope.partitions){
					var currentIndex = displayedClientsCount;
					$scope.partitions = partitionClients( 
							filteredClients.slice(0, currentIndex+PARTITION) 
					);
				}
			};

			function loadClients() {
				GWT_Server.business.getClients(nConstants.conf.businessId, {
					onSuccess : function(data){
						$scope.$apply(function(){
							//sort the data
							loadedClients = data.clients.sort( nSorting.clientsComparator );
							updateFilteredClients();
						});
					},

					onFailure : function(error){}
				});
			};

			$scope.address = function(client){
				var address = (client.address ? client.address+' ' : '') +
				(client.postcode ? ' - '+client.postcode+' - ' : '') +
				(client.city ? client.city+' ' : '') +
				(client.province ? '('+client.province+') ' : '');
				return address;
			};

			$scope.clientClick = function(client){
				$location.path('/details/'+client.id);
			};

			// fired when new client button is clicked
			$scope.newClientClick = function() {
				GWT_UI.clientDialog(nConstants.conf.businessId, {

					onSuccess : function(){
						loadClients();
					},

					onFailure : function() {}
				});
			};

			$scope.scrollTo = function(letter, index){
				if(displayedClientsCount < index){
					updateFilteredClients(index);
				}
				//needed to let the browser draw the UI before moving to the letter
				setTimeout(function(){
					$('html, body').animate({
						scrollTop: $('#'+letter).offset().top - 42
					}, 1000);
				}, 1);
			};

			loadClients();
		}])




		/**
		 * CLIENT DETAILS PAGE CONTROLLER
		 */
		.controller('ClientDetailsCtrl', ['$scope', '$route', '$routeParams', '$location', '$rootScope', 'nConstants', '$filter',
		                                  function($scope, $route, $routeParams, $location, $rootScope, nConstants, $filter) {

			//fired when edit client is clicked
			$scope.editClient = function(clientId) {
				GWT_UI.modifyClientDialog(nConstants.conf.businessId, clientId, {

					onSuccess : function(){
						$scope.$apply(function(){
							$route.reload();
						});
					},

					onFailure : function() {}
				});
			};


			//fired when remove client is clicked
			$scope.removeClient = function(name, clientId) {

				$rootScope.$broadcast(nConstants.events.SHOW_REMOVAL_DIALOG, 
						$filter('translate')('REMOVAL_QUESTION',{data : name}),
						{

					onOk : function(){

						GWT_Server.client.remove(nConstants.conf.businessId, clientId, {
							onSuccess : function(data){
								$scope.$apply(function(){
									$location.path('/');
								});
							},

							onFailure : function(error){}
						});
					},

					onCancel : function(){}

						});

			};

			// load client data
			GWT_Server.client.get($routeParams.clientId, {

				onSuccess : function(client){
					$scope.$apply(function(){

						$scope.name = client.name;

						$scope.businessDetails = 
							(client.vatID ? client.vatID : '') +
							(client.vatID && client.ssn ? ' - ' : '') +
							(client.ssn ? client.ssn : '');

						$scope.address = 
							(client.address ? client.address+' ' : '') +
							(client.postcode ? ' - '+client.postcode+' - ' : '') +
							(client.city ? client.city+' ' : '') +
							(client.province ? '('+client.province+') ' : '');

						$scope.contactInfo =
							(client.email ? 'Email: '+client.email : '') +
							(client.fax ? 'Fax: '+client.fax : '') +
							(client.mobile ? 'Mobile: '+client.mobile : '') +
							(client.phone ? 'Phone: '+client.phone : '');


						$scope.client = client;
					});
				},

				onFailure : function(error){
				}
			});


			/*
			 * load documents. 
			 * types = {'invoice','estimation','creditNote','transportDocument'}
			 */ 
			var loading = {};
			var selectedYear = {};
			var firstRun = {
					'estimation' : true,
					'creditNote' : true,
					'transportDocument' : true
			};

			var loadDocs = function(year, type){
				selectedYear[type] = year;

				if(loading[type]){
					return;
				}

				loading[type] = true;
				GWT_Server[type].getAllForClient($routeParams.clientId, year, {

					onSuccess : function(clientData){
						$scope.$apply(function(){
							$scope[type+'Data'] = clientData[type+'s'];
						});
						loading[type] = false;
					},

					onFailure : function(error){
						loading[type] = false;
					}
				});
			};


			$scope.loadInvoices = function(year){ 
				loadDocs(year === undefined ? selectedYear['invoice'] : year, 'invoice'); 
			};

			$scope.loadEstimations = function(year){ 
				if(firstRun['estimation']){
					firstRun['estimation'] = false;
					selectedYear['estimation'] = year;
					return;
				}
				loadDocs(year === undefined ? selectedYear['estimation'] : year, 'estimation'); 
			};

			$scope.loadCreditNotes = function(year){ 
				if(firstRun['creditNote']){
					firstRun['creditNote'] = false;
					selectedYear['creditNote'] = year;
					return;
				}
				loadDocs(year === undefined ? selectedYear['creditNote'] : year, 'creditNote'); 
			};

			$scope.loadTransportDocuments = function(year){ 
				if(firstRun['transportDocument']){
					firstRun['transportDocument'] = false;
					selectedYear['transportDocument'] = year;
					return;
				}
				loadDocs(year === undefined ? selectedYear['transportDocument'] : year, 'transportDocument'); 
			};

			$scope.$on(nConstants.events.INVOICE_REMOVED, function(event){
				$scope.invoiceData = null;
				$scope.$apply();
				$scope.loadInvoices();
			});

			$scope.$on(nConstants.events.ESTIMATION_REMOVED, function(event){
				$scope.estimationData = null;
				$scope.$apply();
				$scope.loadEstimations();
			});

			$scope.$on(nConstants.events.CREDIT_NOTE_REMOVED, function(event){
				$scope.creditNoteData = null;
				$scope.$apply();
				$scope.loadCreditNotes();
			});

			$scope.$on(nConstants.events.TRANSPORT_DOCUMENT_REMOVED, function(event){
				$scope.transportDocumentData = null;
				$scope.$apply();
				$scope.loadTransportDocuments();
			});

		}]);


