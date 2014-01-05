'use strict';

angular.module('novabill.calc', [])

/*
 * This service 
 */
.factory('nCalc', [function() {
	
	return {

		calculatePrice : function(priceValue, percentageValue){
			var value = new BigNumber(priceValue);
			var percentage = new BigNumber(percentageValue).plus(100).dividedBy(100);
			return value.times(percentage).toFixed(2);
		},
		
	};
}]);
