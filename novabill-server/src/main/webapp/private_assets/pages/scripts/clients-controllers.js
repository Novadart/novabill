angular.module('clients.controllers', ['utils']).


controller('ClientsCtrl', function($scope, Nsorting){
	
	var partitionsCache = [];
	
	$scope.loadClients = function($scope) {
		GWT_Server.business.getClients(NovabillConf.businessId, {
			onSuccess : function(data){
				$scope.$apply(function(){
					//sort the data
					data.clients.sort( Nsorting.clientsComparator );
					
					//split it alphabetically
					var partitions = [];
					var pt = null;
					
					var lo = '', 
						l = '';
					var cl;
					
					for ( var id in data.clients) {
						cl = data.clients[id];
						l = cl.name.charAt(0).toUpperCase();
						
						if(l != lo) {
							if(pt != null) {
								partitions.push(pt);
							}
							pt = {
									letter : l,
									clients : []
							};
						}
						
						pt.clients.push(cl);
						
						lo = l;
					}
					
					partitionsCache = partitions;
					$scope.filterKeyUp();
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
		alert(client.name);
	};
	
	// fired when new client button is clicked
	$scope.newClientClick = function() {
		GWT_UI.clientDialog(NovabillConf.businessId, {

			onSuccess : function(){
				$scope.loadClients($scope);
			},

			onFailure : function() {}
		});
	};

	$scope.filterKeyUp = function() {
		var query = $scope.query;
		
		if(!query || query.length < 2) {
			$scope.partitions = partitionsCache;
			return;
		}
		
		var result = [];
		var normalizedQuery = query.toLowerCase();
		
		function containsQuery(client) {
			return (client.name && client.name.toLowerCase().indexOf(normalizedQuery) > -1) ||
			(client.address && client.address.toLowerCase().indexOf(normalizedQuery) > -1) ||
			(client.city && client.city.toLowerCase().indexOf(normalizedQuery) > -1);
		}
		
		var ptOrig, ptNew, cl;
		for(var i in partitionsCache){
			ptOrig = partitionsCache[i];
			ptNew = {
					letter : ptOrig.letter,
					clients : []
			};
			
			for(var i2 in ptOrig.clients) {
				cl = ptOrig.clients[i2];
				if(containsQuery(cl)){
					ptNew.clients.push(cl);
				}
			}
			
			if(ptNew.clients.length > 0){
				result.push(ptNew);
			}
		}
		
		$scope.partitions = result;
	};
	
	
	$scope.loadClients($scope);
}).


controller('ClientDetailsCtrl', function($scope, $route, $routeParams, $location) {
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

				
				$scope.clientId = client.id;
			});
		},

		onFailure : function(error){
		}
	});

	
	//fired when edit client is clicked
	$scope.editClient = function(clientId) {
		GWT_UI.modifyClientDialog(NovabillConf.businessId, clientId, {

			onSuccess : function(){
				$scope.$apply(function(){
					$route.reload();
				});
			},

			onFailure : function() {}
		});
	};
	
	
	//fired when edit client is clicked
	$scope.removeClient = function(clientId) {
		GWT_Server.client.remove(NovabillConf.businessId, clientId, {
			onSuccess : function(data){
				$scope.$apply(function(){
					$scope.clients = data.clients;
				});
			},

			onFailure : function(error){}
		});
	};

});


