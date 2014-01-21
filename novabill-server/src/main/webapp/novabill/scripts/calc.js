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
		 * @param priceValue the value price, expressed (float value)
		 * @param percentageValue percentage variation (float value). if positive means discount, if negative means overchargecarge
		 * 
		 * @return the price of the commodity rounded to the second decimal
		 */
		calculatePriceWithPercentageVariation : function(priceValue, percentageValue){
			var value = new BigNumber(priceValue);
			var percentage = new BigNumber(percentageValue).times(new BigNumber('-1')).plus(100).dividedBy(100);
			return value.times(percentage).round(2);
		},
		
		/**
		 * Default prices are already rounded to the 2nd decimal
		 */
		calculatePriceForCommodity : function(commodity, priceListName){
			var COMMODITY_PRICES_HACK = commodity.pricesMap ? commodity.pricesMap.prices : commodity.prices;
			var price = COMMODITY_PRICES_HACK[priceListName];
			
			if(priceListName === nConstants.conf.defaultPriceListName){
				return new BigNumber(price.priceValue);
			} else {
				
				if(price === undefined || !price.id){
					//if no price for the given price list, return the default price
					price = COMMODITY_PRICES_HACK[nConstants.conf.defaultPriceListName];
					return new BigNumber(price.priceValue);
				}
				
				switch (price.priceType) {
				case nConstants.priceType.DERIVED:
					var defaultPrice = COMMODITY_PRICES_HACK[nConstants.conf.defaultPriceListName];
					return this.calculatePriceWithPercentageVariation(defaultPrice.priceValue, price.priceValue);

				default:
				case nConstants.priceType.FIXED:
					return new BigNumber(price.priceValue);
				}
			}
		}
		
	};
}]);
