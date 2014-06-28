'use strict';

angular.module('novabill.directives.forms', 
		['novabill.directives.dialogs', 'novabill.constants', 'novabill.calc', 'novabill.utils', 'novabill.ajax', 
		 'novabill.translations', 'angularFileUpload', 'ui.bootstrap'])

		
		
/*
 * Item Form
 */
.directive('nItemForm', ['nConstants', function factory(nConstants){

	return {
		templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-item-form.html'),
		scope: {
			clientId : '@',
			explicitDiscount : '@'
		},
		controller : ['$scope', 'nAjax', '$element', 'nSelectCommodityDialog', '$window', 
		              'nSorting', 'nRegExp', 'nCalc', '$filter', 'nConstants',
		              function($scope, nAjax, $element, nSelectCommodityDialog, $window, 
		            		  nSorting, nRegExp, nCalc, $filter, nConstants){
			var BatchDataFetcherUtils = nAjax.BatchDataFetcherUtils();
			$scope.pricelist = null;
			$scope.commodities = null;
			$scope.selectedCommodity = null;
			$scope.item = null;
			$scope.explicitDiscountCheck = $scope.explicitDiscount !== 'false';
			
			
			BatchDataFetcherUtils.fetchSelectCommodityForDocItemOpData({clientID : $scope.clientId}, function(result){
				$scope.pricelist = result.first;
				$scope.commodities = $scope.pricelist.commodities.sort( nSorting.descriptionComparator );
			});
			
			
			$scope.applyCommodity = function(comm){
				$scope.selectedCommodity = comm;
				
				$scope.item = {
						sku : nRegExp.reserved_word.test(comm.sku) ? null : comm.sku,
						description : comm.description,
						unitOfMeasure : comm.unitOfMeasure,
						tax : $filter('number')(comm.tax),
						weight : $filter('number')(comm.weight)
				};
				
				var priceType = comm.prices[ $scope.pricelist.name ].priceType;
				
				if(priceType == 'DISCOUNT_PERCENT' && $scope.explicitDiscount !== 'false' && $scope.explicitDiscountCheck){
					
					$scope.item.price = $filter('number')( comm.prices[ nConstants.conf.defaultPriceListName ].priceValue );
					$scope.item.discount = $filter('number')( comm.prices[ $scope.pricelist.name ].priceValue );
					
				} else {
					
					$scope.item.price = $filter('number')( nCalc.calculatePriceForCommodity(comm, $scope.pricelist.name).toString() );
					
				}
				
				
				$element.find('#unitOfMeasure').focus();
			};
			
			$scope.onCommoditySelected = function($item, $model, $label){
				$scope.applyCommodity($item);
			};
			
			$scope.openSelectCommodityDialog = function(){
				var instance = nSelectCommodityDialog.open( $scope.clientId );
				instance.result.then(
					function(result){
							
					});
			};
			
			$scope.addItem = function(){
				$window.Angular_ItemFormInit_callback($scope.textOnly, $scope.item);
			};
		}],
		restrict: 'E',
		replace: true
	};

}])

/*
 * Business Form
 */
.directive('nBusinessForm', ['nConstants', function factory(nConstants){

	return {
		templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-business-form.html'),
		scope: { 
			business : '=',
			callback : '&'
		},
		controller : ['$scope', 
		              function($scope){
			
			$scope.saveOrUpdate = function(){
				
				if($scope.business.id){
					$scope.business.$update(function(){
						$scope.callback();
					});
				} else {
					$scope.business.settings = {};
					$scope.business.$save(function(){
						$scope.callback();
					});
				}
				
			};
			
		}],
		restrict: 'E',
		replace: true
	};

}])



/*
 * Business Logo
 */
.directive('nBusinessLogo', ['nConstants', function factory(nConstants){

	return {
		templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-business-logo.html'),

		controller : ['$scope', 'nConstants', 'nAjax', '$filter',
		              function($scope, nConstants, nAjax, $filter){
			var BusinessLogo = nAjax.BusinessLogo();
			
			$scope.logoUrl = nConstants.conf.logoUrl;

			$scope.refreshLogoUrl = function(){
				$scope.logoUrl = nConstants.conf.logoUrl + '?date=' + new Date().getTime();
			};

			$scope.removeLogo = function(){
				BusinessLogo.remove( $scope.refreshLogoUrl );
			};
			
			$scope.uploadLogo = function($files){
				$scope.errorMessage = null;
				
				BusinessLogo.upload($files[0], function(result, status, headers, config) {
					
					var res = result.indexOf('<pre>') != -1 ? result.charAt(5) : result;
					switch (parseInt(res)) {
					case 0:
						$scope.showUploadForm = false;
						$scope.refreshLogoUrl();
						break;

					default:
					case 1:
					case 4:
						$scope.errorMessage = $filter('translate')('LOGO_UPLOAD_ERROR_ILLEGAL_REQUEST');
						$scope.refreshLogoUrl();
						break;
						
					case 2:
						$scope.errorMessage = $filter('translate')('LOGO_UPLOAD_ERROR_ILLEGAL_PAYLOAD');
						break;
						
					case 3:
						$scope.errorMessage = $filter('translate')('LOGO_UPLOAD_ERROR_ILLEGAL_SIZE');
						break;
					}
					
				});

			};

		}],

		restrict: 'E',
		replace: true
	};

}]);
