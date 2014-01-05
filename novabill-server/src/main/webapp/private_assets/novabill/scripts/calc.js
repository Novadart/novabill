'use strict';

angular.module('novabill.calc', [])

/*
 * This service 
 */
.factory('nCalc', [function() {
	
	return {

		/**
		 * Calculates a price given the default price list value and the percentage variation/discount
		 * 
		 * @arg priceValue the value price, expressed (float value)
		 * @arg percentageValue percentage variation (float value)
		 */
		calculatePrice : function(priceValue, percentageValue){
			var value = new BigNumber(priceValue);
			var percentage = new BigNumber(percentageValue).plus(100).dividedBy(100);
			return value.times(percentage).toFixed(2);
		},
		
	};
}]);
