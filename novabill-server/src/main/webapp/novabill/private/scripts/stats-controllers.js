'use strict';

angular.module('novabill.stats.controllers', ['novabill.directives', 'novabill.translations', 'novabill.ajax'])


/**
 * GENERAL STATS PAGE CONTROLLER
 */
.controller('StatsGeneralCtrl', ['$scope', 'nConstants', 'nAjax', '$location', '$routeParams',
                          function($scope, nConstants, nAjax, $location, $routeParams){

	var Stats = nAjax.Stats();
	var year = parseInt( $routeParams.year );
	var prevYear = year-1;
	$scope.year = year.toString();
	
	$scope.loadStats = function(year){
		Stats.genStats({year : year}, function(stats){

			// calculate totals per months
			var tpm = [];
			var prevYearStr = prevYear.toString();
			for(var i=0; i<12; i++){
				tpm.push({
					month: i.toString(), 
					value: stats.totalsPerMonths[year][i].toString(), 
					pastValue: stats.totalsPerMonths[prevYearStr][i].toString()
				});
			}
			$scope.totalsPerMonths = tpm;
			
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
			$scope.clients = {
					totalCount : stats.clientsVsReturningClients.numberOfClients,
					returning : stats.clientsVsReturningClients.numberOfReturningClients
			};
			
			// calculate commodities stats
			$scope.commodities = {
					totalCount : 123,
					services : 11,
					products : 112
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
.controller('StatsClientsCtrl', ['$scope', 'nConstants', 'nAjax', '$location', '$routeParams',
                          function($scope, nConstants, nAjax, $location, $routeParams){

	var year = parseInt( $routeParams.year );
	var prevYear = year-1;
	$scope.year = year.toString();
	
	var Stats = nAjax.Stats();

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


