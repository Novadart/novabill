'use strict';

angular.module('novabill.dashboard.controllers', ['novabill.directives', 'novabill.constants', 'novabill.translations'])


/**
 * DASHBOARD CONTROLLER
 */
.controller('DashboardCtrl', ['$scope', 'nConstants', '$filter', function($scope, nConstants, $filter){


	function drawInvoicesPerMonthChart(data) {
		if (!jQuery.plot || !data) {
			return;
		}

		var points = [];
		for(var i=0; i<data.length; i++){
			points.push([i+1, data[i]]);
		}
		
		function showTooltip(valueX, valueY, x, y) {
			$('<div id="tooltip" class="chart-tooltip">' + $filter('translate')(nConstants.months[valueX-1]) + ': ' + valueY + '<\/div>').css({
				position: 'absolute',
				display: 'none',
				top: y - 40,
				left: x - 30,
				'min-width': 30,
				'text-align': 'center',
				border: '0px solid #ccc',
				padding: '2px 2px',
				'background-color': '#fff',
			}).appendTo("body").fadeIn(200);
		}

		if ($('#site_statistics').size() != 0) {

			$('#site_statistics_loading').hide();
			$('#site_statistics_content').show();

			$.plot($("#site_statistics"), [{
				data: points,
				label: "Number of Invoices"
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




	GWT_Server.business.getStats(NovabillConf.businessId, {
		onSuccess : function(stats){
			$scope.$apply(function(){
				$scope.stats = stats;
				drawInvoicesPerMonthChart(stats.invoiceCountsPerMonth.list);
			});
		},

		onFailure : function(error){}
	});


}]);

