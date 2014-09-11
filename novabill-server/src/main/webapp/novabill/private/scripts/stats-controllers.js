'use strict';

angular.module('novabill.stats.controllers', ['novabill.directives', 'novabill.translations', 'novabill.ajax', 'googlechart'])


/**
 * GENERAL STATS PAGE CONTROLLER
 */
.controller('StatsGeneralCtrl', ['$scope', 'nConstants', 'nAjax', '$window', 
                                 '$routeParams', '$filter', '$location',
                                 function($scope, nConstants, nAjax, $window,
                                		 $routeParams, $filter, $location){

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
							title: $filter('translate')('TOTAL_INVOICING_BEFORE_TAXES'),
							viewWindow:{ 
								min: 0 
							}
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
	
	$scope.openClientStats = function(clientID){
		$window.location.assign( nConstants.url.statsClients(clientID, $scope.year) );
	};

	$scope.openCommodityStats = function(sku){
		$window.location.assign( nConstants.url.statsCommodities(sku, $scope.year) );
	};

	$scope.loadStats( $scope.year );

}])



/**
 * CLIENTS STATS PAGE CONTROLLER
 */
.controller('StatsClientsCtrl', ['$scope', 'nConstants', 'nAjax', '$location', '$routeParams', 'nSorting', '$filter',
                                 function($scope, nConstants, nAjax, $location, $routeParams, nSorting, $filter){

	var year = parseInt( $routeParams.year );
	var prevYear = year-1;
	$scope.year = year.toString();
	$scope.commodities = [];

	var clientID = $routeParams.clientID;

	var Stats = nAjax.Stats();
	var Business = nAjax.Business(); 

	$scope.loadStats = function(clientID, year){
		Stats.getClientBIStats({clientID : clientID, year : year}, function(stats){

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
							title: $filter('translate')('TOTAL_INVOICING_BEFORE_TAXES'),
							viewWindow:{ 
								min: 0 
							}
						},
						hAxis: {
							title: $filter('translate')('MONTH')
						}
					},
			};

			
			// calculate totals
			$scope.clientDetails = {
					timestamp : stats.creationTime,
					totalBeforeTaxes : stats.totalBeforeTaxes
			};
			
			// commodities ranking
			$scope.commodities = stats.commodityStatsForCurrentYear;

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

		$scope.loadStats( $scope.selectedClient, $scope.year );

	});

	$scope.openCommodityStats = function(sku){
		$window.location.assign( nConstants.url.statsCommodities(sku, $scope.year) );
	};
	
	$scope.clientChanged = function(clientID){
		$location.path('/' + $scope.selectedClient + '/' + $scope.year);
	};

}])




/**
 * COMMODITIES STATS PAGE CONTROLLER
 */
.controller('StatsCommoditiesCtrl', ['$scope', 'nConstants', 'nAjax', '$location', '$routeParams', 'nSorting', '$filter', '$window',
                                     function($scope, nConstants, nAjax, $location, $routeParams, nSorting, $filter, $window){

	var year = parseInt( $routeParams.year );
	var prevYear = year-1;
	$scope.year = year.toString();
	$scope.commodities = [];

	var sku = $routeParams.sku;

	var Stats = nAjax.Stats();
	var Commodity = nAjax.Commodity(); 

	$scope.loadStats = function(sku, year){
		Stats.getCommodityBIStats({sku : sku, year : year}, function(stats){

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
							title: $filter('translate')('TOTAL_INVOICING_BEFORE_TAXES'),
							viewWindow:{ 
								min: 0 
							}
						},
						hAxis: {
							title: $filter('translate')('MONTH')
						}
					},
			};

			
			// calculate totals
			$scope.commodityDetails = {
					totalBeforeTaxes : stats.totalBeforeTaxes,
					totalBeforeTaxesCurrentYear : stats.totalBeforeTaxesCurrentYear
			};
			
			// commodities ranking
			$scope.clients = stats.clientStatsForCurrentYear;

		});
	};

	// if clientID is provided we can load the stats already, otherwise we'll load them once we got the clients list

	Commodity.query(function(commodities){
		$scope.commodities = commodities.sort( nSorting.descriptionComparator );

		if(sku !== '0'){

			$scope.selectedSku = sku;

		} else {
			if($scope.commodities.length > 0){

				// if user didn't select any client, pick the first one in the row
				$scope.selectedSku = $scope.commodities[0].sku;

			} else {

				// in this case there are no clients and we can't load anything
				//TODO display some message
				return;
			}
		}

		$scope.loadStats( $scope.selectedSku, $scope.year );

	});
	
	
	$scope.openClientStats = function(clientID){
		$window.location.assign( nConstants.url.statsClients(clientID, $scope.year) );
	};

	$scope.commodityChanged = function(clientID){
		$location.path('/' + $scope.selectedSku + '/' + $scope.year);
	};

}]);


