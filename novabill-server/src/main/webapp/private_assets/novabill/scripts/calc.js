'use strict';

angular.module('novabill.calc', ['novabill.constants'])

/*
 * This service 
 */
.factory('nCalc', ['nConstants', function(nConstants) {
	
	return {

		/**
		 * Calculates a price given the default price list value and the percentage variation/discount
		 * 
		 * @arg priceValue the value price, expressed (float value)
		 * @arg percentageValue percentage variation (float value)
		 */
		calculatePriceWithPercentageVariation : function(priceValue, percentageValue){
			var value = new BigNumber(priceValue);
			var percentage = new BigNumber(percentageValue).plus(100).dividedBy(100);
			return value.times(percentage).toFixed(2);
		},
		
		
		calculatePriceForCommodity : function(commodity, priceListName){
			var price = commodity.pricesMap.prices[priceListName];
			
			if(priceListName === nConstants.conf.defaultPriceListName){
				return price.priceValue;
			} else {
				
				if(price === undefined || !price.id){
					//if no price for the given price list, return the default price
					price = commodity.pricesMap.prices[nConstants.conf.defaultPriceListName];
					return price.priceValue;
				}
				
				switch (price.priceType) {
				case nConstants.priceType.DERIVED:
					var defaultPrice = commodity.pricesMap.prices[nConstants.conf.defaultPriceListName];
					return this.calculatePriceWithPercentageVariation(defaultPrice.priceValue, price.priceValue)
					break;

				default:
				case nConstants.priceType.FIXED:
					return price.priceValue;
				}
			}
		},
		
	};
}]);
