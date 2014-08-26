'use strict';

angular.module('novabill.stats.controllers', ['novabill.directives', 'novabill.translations', 'novabill.ajax'])


/**
 * STATS PAGE CONTROLLER
 */
.controller('StatsCtrl', ['$scope', 'nConstants', 'nAjax', '$location',
                          function($scope, nConstants, nAjax, $location){

	var year = new Date().getFullYear();
	var prevYear = year-1;
	$scope.year = year.toString();

	$scope.onTabChange = function(token){
		$location.search('tab',token);
	};

	$scope.activeTab = {
			general : false,
			clients : false,
			commodities : false
	};
	$scope.activeTab[$location.search().tab] = true;


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


