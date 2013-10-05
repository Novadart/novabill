var NovabillClientsPage = angular.module("NovabillClientsPage", []);

function ClientsCtrl($scope){
	GWT_Server.business.getClients('1',{
		onSuccess : function(data){
			$scope.$apply(function(){
				$scope.clients = data.clients;
			});
		},
		
		onFailure : function(error){
		}
	});
};

