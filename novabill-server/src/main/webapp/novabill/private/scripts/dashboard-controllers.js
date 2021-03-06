'use strict';

angular.module('novabill.dashboard.controllers', 
		['novabill.directives', 'novabill.directives.dialogs', 'novabill.constants', 'novabill.translations', 'novabill.ajax', 'ngCookies'])


/**
 * DASHBOARD CONTROLLER
 */
.controller('DashboardCtrl', ['$scope', 'nConstants', '$filter', 'nAjax', 'nRecommendByEmailDialog', 'nAlertDialog', '$cookies',
                              function($scope, nConstants, $filter, nAjax, nRecommendByEmailDialog, nAlertDialog, $cookies){

	var Business = nAjax.Business();

	function drawInvoicesPerMonthChart(data) {
		if (!jQuery.plot || !data) {
			return;
		}

		var points = [];
		for(var i=0; i<data.length; i++){
			points.push([i+1, data[i]]);
		}
		
		function showTooltip(valueX, valueY, x, y) {
			$('<div id="tooltip" class="chart-tooltip">' + $filter('translate')(nConstants.months[valueX-1]) + ': ' + $filter('currency')(valueY) + '<\/div>').css({
				position: 'absolute',
				display: 'none',
				top: y - 40,
				left: x - 30,
				'min-width': 30,
				'text-align': 'center',
				border: '0px solid #ccc',
				padding: '2px 2px',
				'background-color': '#fff'
			}).appendTo("body").fadeIn(200);
		}

		if ($('#site_statistics').size() != 0) {

			$('#site_statistics_loading').hide();
			$('#site_statistics_content').show();

			$.plot($("#site_statistics"), [{
				data: points,
				label: $filter('translate')('TOTAL_INVOICING')
			}
			], {
				series: {
					lines: {
						show: true,
						lineWidth: 2,
						fill: true,
						fillColor: {
							colors: [{
								opacity: 0.05
							}, {
								opacity: 0.01
							}
							]
						}
					},
					points: {
						show: true
					},
					shadowSize: 2
				},
				grid: {
					hoverable: true,
					clickable: true,
					tickColor: "#eee",
					borderWidth: 0
				},
				colors: ["#d12610"],
				xaxis: {
					ticks: 11,
					tickDecimals: 0
				},
				yaxis: {
					min: 0,
					ticks: 11,
					tickDecimals: 0
				}
			});

			var previousPoint = null;
			$("#site_statistics").bind("plothover", function (event, pos, item) {
				$("#x").text(pos.x.toFixed(2));
				$("#y").text(pos.y.toFixed(2));
				if (item) {
					if (previousPoint != item.dataIndex) {
						previousPoint = item.dataIndex;

						$("#tooltip").remove();

						showTooltip(item.datapoint[0], item.datapoint[1], item.pageX, item.pageY);
					}
				} else {
					$("#tooltip").remove();
					previousPoint = null;
				}
			});
		}               

	};

	var loadedLogRecords = [];
	$scope.logRecords = [];
	var PARTITION = 30;
	
	$scope.loadMoreLogRecords = function(){
		if($scope.logRecords){
			var currentIndex = $scope.logRecords.length;
			$scope.logRecords = $scope.logRecords.concat(loadedLogRecords.slice(currentIndex, currentIndex+PARTITION));
		}
	};
	
	$scope.recommendNovabill = function(){
		var instance = nRecommendByEmailDialog.open();
		instance.result.then(function(data){
			var RecommendNovabill = nAjax.RecommendNovabill();
			RecommendNovabill.email(data, function(){
				nAlertDialog.open($filter('translate')('SEND_EMAIL_TO_CLIENT_SUCCESS'));
			}, function(){
				nAlertDialog.open($filter('translate')('SEND_EMAIL_TO_CLIENT_FAILURE'));
			});
		});
	};
	
	Business.getStats(function(stats){
		$scope.stats = stats;
		loadedLogRecords = stats.logRecords;
		$scope.logRecords = loadedLogRecords.slice(0, 10);
		drawInvoicesPerMonthChart(stats.invoiceTotalsPerMonth);
		
		$('.scroller').slimScroll({
		    height: '300px'
		});
	});
	
	Business.get({id : nConstants.conf.businessId}, function(business){
		if( !business.vatID || !business.ssn || !business.address || !business.city || !business.postcode 
				|| !business.province || !business.country){
			$scope.incompleteBusinessAlert = $filter('translate')('ALERT_MISSING_BUSINESS_INFO_DASHBOARD', {link : nConstants.url.settingsBusiness()});
		}
	});
	
	
	$scope.showFirstRunAlert =  (new Date().getTime() - nConstants.conf.principalCreationDate) <= 604800000 && !$cookies.hideFirstRunAlert;
	$scope.closeFirstRunAlert = function(){
		$cookies.hideFirstRunAlert = true;
	};
	

}]);


