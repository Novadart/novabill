'use strict';

angular.module('novabill.stats.controllers', ['novabill.directives', 'novabill.translations', 'novabill.ajax'])


/**
 * GENERAL STATS PAGE CONTROLLER
 */
.controller('StatsGeneralCtrl', ['$scope', 'nConstants', 'nAjax', '$location', '$routeParams',
                          function($scope, nConstants, nAjax, $location, $routeParams){

	var year = parseInt( $routeParams.year );
	var prevYear = year-1;
	$scope.year = year.toString();
	
	var Stats = nAjax.Stats();
	Stats.genStats({year : $scope.year}, function(stats){

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
	});

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
	Stats.genStats({year : $scope.year}, function(stats){

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
	Stats.genStats({year : $scope.year}, function(stats){

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
	});

}]);


