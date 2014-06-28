'use strict';

angular.module('novabill.clients.controllers', 
		['novabill.utils', 'novabill.constants', 'novabill.directives', 'novabill.ajax',
		 'novabill.directives.dialogs', 'novabill.translations', 'infinite-scroll'])


		/**
		 * CLIENTS PAGE CONTROLLER
		 */
		.controller('ClientsCtrl', ['$scope', 'nSorting', '$location', 'nConstants', 'nAjax',
		                            function($scope, nSorting, $location, nConstants, nAjax){

			var loadedClients = [];
			var filteredClients = [];
			var displayedClientsCount = 0;
			var PARTITION = 50;
			
			var Business = nAjax.Business();

			$scope.partitionClients = function(clients){
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
			};

			$scope.containsQuery = function(query, client) {
				var normalizedQuery = query.toLowerCase();
				return (client.name && client.name.toLowerCase().indexOf(normalizedQuery) > -1) ||
				(client.address && client.address.toLowerCase().indexOf(normalizedQuery) > -1) ||
				(client.city && client.city.toLowerCase().indexOf(normalizedQuery) > -1);
			};

			$scope.filterClients = function(query, clients){
				if(!query){
					return clients;
				}
				var result = [];
				angular.forEach(clients, function(client, index){
					if($scope.containsQuery(query, client)){
						result.push(client);
					}
				});
				return result;
			};

			$scope.updateLettersIndex = function(clients){
				var letters = {};
				var l = '', lo ='';
				angular.forEach(clients, function(client, index){
					l = client.name.charAt(0).toUpperCase();
					if(l !== lo){
						letters[l] = index;
					}
				});
				$scope.letters = letters;
			};

			$scope.updateFilteredClients = function(generateTillIndex){
				filteredClients = $scope.filterClients($scope.query, loadedClients);
				$scope.updateLettersIndex(filteredClients);
				if(generateTillIndex){
					$scope.partitions = $scope.partitionClients( filteredClients.slice(0, generateTillIndex+1) );
				} else {
					$scope.partitions = $scope.partitionClients( filteredClients.slice(0, 20) );
				}
			};

			$scope.$watch('query', function(newValue, oldValue){
				$scope.updateFilteredClients();
			});

			$scope.loadMoreClients = function(){
				if($scope.partitions){
					var currentIndex = displayedClientsCount;
					$scope.partitions = $scope.partitionClients( 
							filteredClients.slice(0, currentIndex+PARTITION) 
					);
				}
			};

			$scope.loadClients = function() {
				Business.getClients(function(clients){
					//sort the data
					loadedClients = clients.sort( nSorting.clientsComparator );
					$scope.updateFilteredClients();
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
						$scope.loadClients();
					},

					onFailure : function() {}
				});
			};

			$scope.scrollTo = function(letter, index){
				if(displayedClientsCount < index){
					$scope.updateFilteredClients(index);
				}
				//needed to let the browser draw the UI before moving to the letter
				setTimeout(function(){
					$('html, body').animate({
						scrollTop: $('#'+letter).offset().top - 42
					}, 1000);
				}, 1);
			};

			$scope.loadClients();
		}])




		/**
		 * CLIENT DETAILS PAGE CONTROLLER
		 */
		.controller('ClientDetailsCtrl', ['$scope', '$route', '$routeParams', '$location', '$rootScope', 'nConstants', '$filter', 'nAlertDialog', 'nConfirmDialog', 'nEditAddressDialog', 'nSorting',
		                                  function($scope, $route, $routeParams, $location, $rootScope, nConstants, $filter, nAlertDialog, nConfirmDialog, nEditAddressDialog, nSorting) {

			$scope.onTabChange = function(token){
				$location.search('tab',token);
				
				if(token === 'addresses'){
					$scope.loadClientAddresses();
				}
			};
			
			$scope.activeTab = {
					documents : false,
					addresses : false
			};
			$scope.activeTab[$location.search().tab] = true;

			$scope.docsView = 'invoices';
			
			
			$scope.editClient = function() {
				GWT_UI.modifyClientDialog(nConstants.conf.businessId, $scope.client.id, {

					onSuccess : function(){
						$scope.$apply(function(){
							$route.reload();
						});
					},

					onFailure : function() {}
				});
			};


			//fired when remove client is clicked
			$scope.removeClient = function() {
				var instance = nConfirmDialog.open( $filter('translate')('REMOVAL_QUESTION',{data : $scope.client.name}) );
				instance.result.then(function(value){
					if(value){
						GWT_Server.client.remove(nConstants.conf.businessId, $scope.client.id, {
							onSuccess : function(data){
								if(data === 'true') {
									$scope.$apply(function(){
										$location.path('/');
									});
								} else {
									nAlertDialog.open($filter('translate')('CLIENT_DELETION_ALERT'));
								}
							},
	
							onFailure : function(error){}
						});
					}
				});
			};

			$scope.newInvoiceClick = function(){
				window.location.assign( nConstants.url.invoiceNew($routeParams.clientId) );
			};

			$scope.newTransportDocumentClick = function(){
				window.location.assign( nConstants.url.transportDocumentNew($routeParams.clientId) );
			};

			$scope.newEstimationClick = function(){
				window.location.assign( nConstants.url.estimationNew($routeParams.clientId) );
			};

			$scope.newCreditNoteClick = function(){
				window.location.assign( nConstants.url.creditNoteNew($routeParams.clientId) );
			};


			$scope.loadClientAddresses = function(force){
				if(!$scope.clientAddresses || force) {
					GWT_Server.client.getClientAddresses($routeParams.clientId, {
						onSuccess: function(addresses){
							$scope.$apply(function(){
								$scope.clientAddresses = addresses.clientAddresses.sort( nSorting.clientAddressesComparator );
							});
						},
						onFailure : function(){}
					});
				}
			};

			$scope.newAddressClick = function(){
				var instance = nEditAddressDialog.open({
					companyName : $scope.client.name,
					country : 'IT',
					client : { id : $routeParams.clientId }
				});
				instance.result.then(function(address){
					GWT_Server.client.addClientAddress(angular.toJson(address), {
						onSuccess: function(newId){
							$scope.loadClientAddresses(true);
						},
						onFailure : function(){}
					});
				});
			};


			$scope.editLegalAddress = function(){

				var address = { 
						isLegalAddress : true,
						companyName : $scope.client.name,
						address : $scope.client.address,
						postcode: $scope.client.postcode,
						city: $scope.client.city,
						province: $scope.client.province,
						country: $scope.client.country
				};

				var instance = nEditAddressDialog.open(address);
				instance.result.then(function(addr){
					//making a copy to display the results only if saving the data succeeded
					$scope.client.name = addr.companyName;
					$scope.client.address = addr.address;
					$scope.client.postcode = addr.postcode;
					$scope.client.city = addr.city;
					$scope.client.province = addr.province;
					$scope.client.country = addr.country;

					GWT_Server.client.update(nConstants.conf.businessId, angular.toJson($scope.client), {
						onSuccess: function(){},
						onFailure : function(){
							$route.reload();
						}
					});
				});
			};


			$scope.editClientAddress = function(id){
				var address = null;
				for(var i in $scope.clientAddresses){
					if($scope.clientAddresses[i].id === id ){
						address = $scope.clientAddresses[i];
						break;
					}
				}

				if(address == null){
					return;
				}

				address.client = { id : $routeParams.clientId };

				var instance = nEditAddressDialog.open(address);
				instance.result.then(function(addr){
					GWT_Server.client.updateClientAddress(angular.toJson(addr), {
						onSuccess: function(){
							$scope.loadClientAddresses(true);
						},
						onFailure : function(){}
					});
				});
			};

			$scope.removeClientAddress = function(id){
				var address = null;
				for(var i in $scope.clientAddresses){
					if($scope.clientAddresses[i].id === id ){
						address = $scope.clientAddresses[i];
						break;
					}
				}

				if(address == null){
					return;
				}

				var instance = nConfirmDialog.open( $filter('translate')('REMOVAL_QUESTION',{data : address.name}) );
				instance.result.then(function(value){
					if(value){
						GWT_Server.client.removeClientAddress($routeParams.clientId, String(address.id), {
							onSuccess : function(data){
								$scope.$apply(function(){
									$scope.loadClientAddresses(true);
								});
							},
	
							onFailure : function(error){}
						});
					}
				});

			};


			$scope.updateClientDetails = function(){
				if(!$scope.client){
					return;
				}

				$scope.businessDetails = 
					($scope.client.vatID ? $filter('translate')('VATID')+': '+$scope.client.vatID : '') +
					($scope.client.vatID && $scope.client.ssn ? ' - ' : '') +
					($scope.client.ssn ? $filter('translate')('SSN')+': '+$scope.client.ssn : '');

				$scope.address = 
					($scope.client.address ? $scope.client.address+' ' : '') +
					($scope.client.postcode ? ' - '+$scope.client.postcode+' - ' : '') +
					($scope.client.city ? $scope.client.city+' ' : '') +
					($scope.client.province ? '('+$scope.client.province+') ' : '');

				var a1 = [
				          ($scope.client.email ? 'Email: '+$scope.client.email : ''),
				          ($scope.client.fax ? 'Fax: '+$scope.client.fax : ''),
				          ($scope.client.mobile ? 'Cell: '+$scope.client.mobile : ''),
				          ($scope.client.phone ? 'Tel: '+$scope.client.phone : '') ];

				var a2 = [];
				angular.forEach(a1, function(val, _){
					if(val){ a2.push(val); };
				});

				var contactInfo = "";
				for(var i=0; i<a2.length-1; i++){
					contactInfo += a2[i] + ' - '; 
				}
				if(a2.length > 0){
					contactInfo += a2[a2.length-1];
				}
				$scope.contactInfo = contactInfo;

				$scope.website = $scope.client.web;
				$scope.websiteUrl =  $scope.client.web ? ($scope.client.web.indexOf('http') == 0 ? $scope.client.web : 'http://'+$scope.client.web) : null;
			};

			// load client data
			GWT_Server.client.get($routeParams.clientId, {

				onSuccess : function(client){
					$scope.$apply(function(){
						$scope.client = client;
						$scope.updateClientDetails();
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

			$scope.loadDocs = function(year, type){
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
				$scope.loadDocs(year === undefined ? selectedYear['invoice'] : year, 'invoice'); 
			};

			$scope.loadEstimations = function(year){ 
				if(firstRun['estimation']){
					firstRun['estimation'] = false;
					selectedYear['estimation'] = year;
					return;
				}
				$scope.loadDocs(year === undefined ? selectedYear['estimation'] : year, 'estimation'); 
			};

			$scope.loadCreditNotes = function(year){ 
				if(firstRun['creditNote']){
					firstRun['creditNote'] = false;
					selectedYear['creditNote'] = year;
					return;
				}
				$scope.loadDocs(year === undefined ? selectedYear['creditNote'] : year, 'creditNote'); 
			};

			$scope.loadTransportDocuments = function(year){ 
				if(firstRun['transportDocument']){
					firstRun['transportDocument'] = false;
					selectedYear['transportDocument'] = year;
					return;
				}
				$scope.loadDocs(year === undefined ? selectedYear['transportDocument'] : year, 'transportDocument'); 
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

			$scope.$watchCollection('client', function(){ $scope.updateClientDetails(); });

		}]);


