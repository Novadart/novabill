'use strict';

angular.module('novabill.calc', ['novabill.constants'])

/*
 * This service 
 */
.factory('nCalc', ['nConstants', function(nConstants) {
	
	return {

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
				
				var defaultPrice = new BigNumber( COMMODITY_PRICES_HACK[nConstants.conf.defaultPriceListName].priceValue );
				var priceValue = new BigNumber( price.priceValue );
				var percentValue = null;
				
				switch (price.priceType) {
				case nConstants.priceType.DISCOUNT_PERCENT:
					percentValue = priceValue.times(new BigNumber('-1')).plus(100).dividedBy(100);
					return defaultPrice.times(percentValue).round(2);
					
				case nConstants.priceType.DISCOUNT_FIXED:
					return defaultPrice.minus(priceValue).round(2);

				case nConstants.priceType.OVERCHARGE_PERCENT:
					percentValue = priceValue.plus(100).dividedBy(100);
					return defaultPrice.times(percentValue).round(2);
					
				case nConstants.priceType.OVERCHARGE_FIXED:
					return defaultPrice.plus(priceValue).round(2);
					
				case nConstants.priceType.FIXED:
					return priceValue;
					
				default:
					return null;
				}
			}
		}
		
	};
}]);
