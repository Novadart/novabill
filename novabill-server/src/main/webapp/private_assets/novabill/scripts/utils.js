'use strict';

angular.module('novabill.utils', ['novabill.translations', 'novabill.constants'])

/*
 * FACTORIES
 */
.factory('nSorting', ['nConstants', function(nConstants) {
	return {

		/**
		 * Compare two clients
		 * @return -1 if minor, 0 if equal, 1 if major
		 */
		clientsComparator : function(c1, c2) {
			var s1 = c1.name.toLowerCase();
			var s2 = c2.name.toLowerCase();
			return s1<s2 ? -1 : (s1>s2 ? 1 : 0);
		},
		
		/**
		 * Compare two price lists
		 * @return -1 if minor, 0 if equal, 1 if major
		 */
		priceListsComparator : function(p1, p2){
			if(p1.name === nConstants.conf.defaultPriceListName){ return -1; }
			if(p2.name === nConstants.conf.defaultPriceListName){ return 1; }
			
			var n1 = p1.name.toLowerCase();
			var n2 = p2.name.toLowerCase();
			return n1<n2 ? -1 : (n1>n2 ? 1 : 0);
		},
		
		/**
		 * 
		 */
		descriptionComparator : function(i1, i2){
			var n1 = i1.description.toLowerCase();
			var n2 = i2.description.toLowerCase();
			return n1<n2 ? -1 : (n1>n2 ? 1 : 0);
		},

	};
}])

.factory('nRegExp', function() {
	return {

		fiscalFloat : /^(\+|\-)?\d+((\.|\,)\d{1,2})?$/,
		
		positiveFiscalFloat : /^\d+((\.|\,)\d{1,2})?$/,

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


.filter('nFilterDefault', ['$filter', 'nConstants', function($filter, nConstants) {
	return function(input) {
		return input === nConstants.conf.defaultPriceListName ? $filter('translate')('DEFAULT_PRICE_LIST') : input;
	};
}]);
