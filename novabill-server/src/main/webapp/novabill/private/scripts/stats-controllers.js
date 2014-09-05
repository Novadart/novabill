'use strict';

angular.module('novabill.stats.controllers', ['novabill.directives', 'novabill.translations', 'novabill.ajax', 'googlechart'])


/**
 * GENERAL STATS PAGE CONTROLLER
 */
.controller('StatsGeneralCtrl', ['$scope', 'nConstants', 'nAjax', '$location', '$routeParams', '$filter',
                                 function($scope, nConstants, nAjax, $location, $routeParams, $filter){

	var Stats = nAjax.Stats();
	var year = parseInt( $routeParams.year );
	var prevYear = year-1;
	$scope.year = year.toString();

	$scope.loadStats = function(year){
		Stats.getGeneralBIStats({year : year}, function(stats){

			// calculate totals per months
			var rows = [];
			var prevYearStr = prevYear.toString();
			for(var i=0; i<12; i++){
				rows.push({
					c : [
					     {v: $filter('translate')(nConstants.months[i])},
					     {v: stats.totalsPerMonths[year][i]},
					     {v: stats.totalsPerMonths[prevYearStr][i]}
					     ]
				});
			}

			$scope.totalsPerMonthsChart = {
					type: "ColumnChart",
					displayed: true,
					data: {
						cols: [
						       { id: 'month', label: $filter('translate')('MONTH'), type: 'string'},
						       { id: 'invoicing-cur', label: $scope.year, type: 'number'},
						       { id: 'invoicing-past', label: prevYearStr, type: 'number'}
						       ],
						       rows: rows
					},
					options: {
						title: $filter('translate')('INVOICING_PER_MONTH'),
						displayExactValues: true,
						vAxis: {
							title: $filter('translate')('TOTAL_INVOICING_BEFORE_TAXES')
						},
						hAxis: {
							title: $filter('translate')('MONTH')
						}
					},
			};

			// calculate totals
			var totalBeforeTaxes = stats.totals.totalBeforeTax;
			var totalAfterTaxes = stats.totals.totalAfterTax;
			var totalTaxes = ( new BigNumber(totalAfterTaxes).minus(new BigNumber(totalBeforeTaxes)) ).toString();


			$scope.totals = {
					totalBeforeTaxes : totalBeforeTaxes,
					totalTaxes : totalTaxes,
					totalAfterTaxes : totalAfterTaxes
			};

			// calculate clients stats
			$scope.clientsChart = {
					type: "PieChart",
					displayed: true,
					data: {
						cols: [
						       { id: 'set', label: 'Clients Set', type: 'string'},
						       { id: 'value', label: 'Value', type: 'number'},
						       ],
						rows: [
						       { c: [{v: $filter('translate')('STATS_CLIENTS_RETURNING', {year : $scope.year})}, 
						             {v: stats.clientsVsReturningClients.numberOfReturningClients}] },
						       { c: [{v: $filter('translate')('STATS_CLIENTS_NOT_RETURNING', {year : $scope.year})}, 
						             {v: stats.clientsVsReturningClients.numberOfClients - stats.clientsVsReturningClients.numberOfReturningClients}] },
						       ]	
						},
						options : {
					        
						}
			};

			$scope.clients = {
					totalCount : stats.clientsVsReturningClients.numberOfClients
			};

			// calculate commodities stats
			var servicesCount = 0;
			var commoditiesCount = stats.commodityRankingByRevenue.length;
			for(var i=0; i<commoditiesCount; i++){
				if(stats.commodityRankingByRevenue[i].service){
					servicesCount++;
				}
			}
			
			$scope.commoditiesChart = {
					type: "PieChart",
					displayed: true,
					data: {
						cols: [
						       { id: 'set', label: 'Commodities Set', type: 'string'},
						       { id: 'value', label: 'Value', type: 'number'},
						       ],
						rows: [
						       { c: [{v: $filter('translate')('SERVICES')}, 
						             {v: servicesCount}] },
						       { c: [{v: $filter('translate')('PRODUCTS')}, 
						             {v: commoditiesCount - servicesCount}] },
						       ]	
						},
						options : {
					        
						}
			};
			$scope.commodities = {
					totalCount : commoditiesCount
			};

			// rankings
			$scope.ranks = {
					clients : stats.clientRankingByRevenue,
					commodities : stats.commodityRankingByRevenue
			};

		});
	};


	$scope.loadStats( $scope.year );

}])



/**
 * CLIENTS STATS PAGE CONTROLLER
 */
.controller('StatsClientsCtrl', ['$scope', 'nConstants', 'nAjax', '$location', '$routeParams', 'nSorting',
                                 function($scope, nConstants, nAjax, $location, $routeParams, nSorting){

	var year = parseInt( $routeParams.year );
	$scope.year = year.toString();

	var clientID = $routeParams.clientID;

	var Stats = nAjax.Stats();
	var Business = nAjax.Business(); 

	$scope.loadStats = function(clientID, year){
		Stats.getClientBIStats({clientID : clientID, year : year}, function(stats){

			// calculate totals per months
			var tpm = [];
//			var prevYearStr = prevYear.toString();
//			for(var i=0; i<12; i++){
//			tpm.push({
//			month: i.toString(), 
//			value: stats.totalsPerMonths[year][i].toString(), 
//			pastValue: stats.totalsPerMonths[prevYearStr][i].toString()
//			});
//			}
//			$scope.totalsPerMonths = tpm;



		});
	};

	// if clientID is provided we can load the stats already, otherwise we'll load them once we got the clients list

	Business.getClients(function(clients){
		$scope.clients = clients.sort( nSorting.clientsComparator );

		if(clientID !== '0'){

			$scope.selectedClient = parseInt( clientID );

		} else {
			if($scope.clients.length > 0){

				// if user didn't select any client, pick the first one in the row
				$scope.selectedClient = $scope.clients[0].id;

			} else {

				// in this case there are no clients and we can't load anything
				//TODO display some message
				return;
			}
		}

		$scope.loadStats( clientID, $scope.year );

	});



}])




/**
 * COMMODITIES STATS PAGE CONTROLLER
 */
.controller('StatsCommoditiesCtrl', ['$scope', 'nConstants', 'nAjax', '$location', '$routeParams',
                                     function($scope, nConstants, nAjax, $location, $routeParams){

	var year = parseInt( $routeParams.year );
	var prevYear = year-1;
	$scope.year = year.toString();

	var Stats = nAjax.Stats();

}]);


