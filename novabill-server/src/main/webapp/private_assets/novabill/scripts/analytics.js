'use strict';

angular.module('novabill.analytics', [])


.factory('nAnalytics', ['$rootScope', '$window', '$location', '$log', function($rootScope, $window, $location, $log) {

	// setup Google Analytics if present, or mock it
	var GA = $window._gaq ? $window._gaq : {
		push : function(event){ 
			$log.debug('Analytics::_trackPageview[ '+ (event.length > 1 ? event[1] : $window.location.href) +' ]');
		},
	};

	//service instance
	var nAnalyticsInstance = {

			// track apge view
			trackPageview : function(url){
				if(url) {
					GA.push(['_trackPageview',  url]);
				} else {
					GA.push(['_trackPageview']);
				}
			},

	};

	// automatically track page views
	$rootScope.$on('$viewContentLoaded', function(){
		nAnalyticsInstance.trackPageview();
	});

	return nAnalyticsInstance;

}]);