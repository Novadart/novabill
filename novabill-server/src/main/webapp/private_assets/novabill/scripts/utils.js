'use strict';

angular.module('novabill.utils', ['novabill.translations'])

/*
 * FACTORIES
 */
.factory('nSorting', function() {
	return {

		/**
		 * Compare two clients
		 * @return -1 if minor, 0 if equal, 1 if major
		 */
		clientsComparator : function(c1, c2) {
			var s1 = c1.name.toLowerCase();
			var s2 = c2.name.toLowerCase();
			return s1<s2 ? -1 : (s1>s2 ? 1 : 0);
		}

	};
})

.factory('nRegExp', function() {
	return {

		float : /^\-?\d+((\.|\,)\d+)?$/,

		reserved_word : /^\::.*$/

	};
})


/*
 * FILTERS
 */

.filter('nFriendlyDate',['$filter', function($filter){
	
	return function(dateToFormat, format){
		var target = new Date( parseInt(dateToFormat) );
		target.setHours(0, 0, 0, 0);
		
		var today = new Date();
	    today.setHours(0, 0, 0, 0);
	    
	    if(target.getTime() === today.getTime()){
	    
	    	return $filter('translate')('TODAY');
	    
	    } else {

	    	var yesterday = new Date();
		    yesterday.setHours(0, 0, 0, 0);
		    yesterday.setDate(yesterday.getDate() - 1);
		    
		    if(target.getTime() === yesterday.getTime()){
			    
		    	return $filter('translate')('YESTERDAY');
		    
		    } else {
		    	
		    	return $filter('date')(dateToFormat, format);
		    }
	    	
	    }
	};
}])


.filter('nFilterDefault', ['$filter', function($filter) {
	return function(input) {
		return input === NovabillConf.defaultPriceListName ? $filter('translate')('DEFAULT_PRICE_LIST') : input;
	};
}]);
