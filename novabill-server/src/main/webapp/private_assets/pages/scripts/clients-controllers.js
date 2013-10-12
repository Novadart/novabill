angular.module('clients.controllers', []).


controller('ClientsCtrl', function($scope){
	
	$scope.loadClients = function($scope) {
		GWT_Server.business.getClients(NovabillConf.businessId, {
			onSuccess : function(data){
				$scope.$apply(function(){
					$scope.clients = data.clients;
				});
			},

			onFailure : function(error){}
		});
	};
	
	$scope.loadClients($scope);

	// fired when new client button is clicked
	$scope.newClientClicked = function() {
		GWT_UI.clientDialog(NovabillConf.businessId, {

			onSuccess : function(){
				$scope.loadClients($scope);
			},

			onFailure : function() {}
		});
	};

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


