'use strict';

angular.module('novabill.directives.forms', 
		['novabill.directives.dialogs', 'novabill.directives.validation', 'novabill.constants', 'novabill.calc', 'novabill.utils', 'novabill.ajax', 
		 'novabill.translations', 'angularFileUpload', 'ui.bootstrap'])

		
		
/*
 * Item Form
 */
.directive('nItemForm', ['nConstants', function factory(nConstants){

	return {
		templateUrl: nConstants.url.htmlFragmentUrl('/directives/n-item-form.html'),
		scope: {
			clientId : '@',
			explicitDiscount : '@',
			manageWeight : '@'
		},
		controller : ['$scope', 'nAjax', '$element', 'nSelectCommodityDialog', '$window', 
		              'nSorting', 'nRegExp', 'nCalc', '$filter', 'nConstants',
		              function($scope, nAjax, $element, nSelectCommodityDialog, $window, 
		            		  nSorting, nRegExp, nCalc, $filter, nConstants){
			var BatchDataFetcherUtils = nAjax.BatchDataFetcherUtils();
			$scope.pricelist = null;
			$scope.commodities = null;
			$scope.selectedCommodity = null;
			$scope.item = {
					tax : '22'
			};
			$scope.explicitDiscountCheck = $scope.explicitDiscount !== 'false';
			
			
			BatchDataFetcherUtils.fetchSelectCommodityForDocItemOpData({clientID : $scope.clientId}, function(result){
				$scope.pricelist = result.first;
				$scope.commodities = $scope.pricelist.commodities.sort( nSorting.descriptionComparator );
			});
			
			$scope.isReservedWord = function(str){
				return nRegExp.reserved_word.test(str);
			};
			
			$scope.applyCommodity = function(comm, pricelistName){
				$scope.selectedCommodity = comm;
				
				// assemble the item
				$scope.item = {
						sku : comm.sku,
						description : comm.description,
						unitOfMeasure : comm.unitOfMeasure,
						tax : $filter('number')(comm.tax),
						weight : $filter('number')(comm.weight)
				};
				
				// depending on the price type calculate price and discount
				var priceType = comm.prices[ pricelistName ].priceType;
				if(priceType == 'DISCOUNT_PERCENT' && $scope.explicitDiscount !== 'false' && $scope.explicitDiscountCheck){
					$scope.item.price = $filter('number')( comm.prices[ nConstants.conf.defaultPriceListName ].priceValue );
					$scope.item.discount = $filter('number')( comm.prices[ pricelistName ].priceValue );
				} else {
					$scope.item.price = $filter('number')( nCalc.calculatePriceForCommodity(comm, pricelistName).toString() );
					$scope.item.discount = '0';
				}
				
				
				$element.find('#quantity').focus();
			};
			
			$scope.onCommoditySelected = function($item, $model, $label){
				$scope.applyCommodity($item, $scope.pricelist.name);
			};
			
			$scope.openSelectCommodityDialog = function(){
				var instance = nSelectCommodityDialog.open( $scope.clientId );
				instance.result.then(
					function(result){
						$scope.applyCommodity(result.commodity, result.priceListName);
					});
			};
			
			$scope.reset = function(){
				$scope.form.$setPristine();
				$scope.textOnlyForm.$setPristine();
				$scope.textOnly = false;
				$scope.selectedCommodity = null;
				$scope.item = {
						tax : '22'
				};
			};
			
			$scope.addTextOnlyItem = function(){
				$window.Angular_ItemFormInit_callback(true, $scope.item);
				$scope.reset();
			};
			
			$scope.addItem = function(){
				$window.Angular_ItemFormInit_callback(false, $scope.item);
				$scope.reset();
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
			
			$scope.thumbUrl = nConstants.conf.thumbUrl;

			$scope.refreshLogoUrl = function(){
				$scope.thumbUrl = nConstants.conf.thumbUrl + '?date=' + new Date().getTime();
			};
			
			$scope.setLoader = function(){
				$scope.thumbUrl = nConstants.conf.privateAreaBaseUrl + '../private_assets/img/ajax-loading.gif';
			};

			$scope.removeLogo = function(){
				$scope.setLoader();
				BusinessLogo.remove( $scope.refreshLogoUrl );
			};
			
			$scope.uploadLogo = function($files){
				$scope.errorMessage = null;
				
				$scope.setLoader();
				
				BusinessLogo.upload($files[0], function(result, status, headers, config) {
					
					if(status === 413){
						$scope.errorMessage = $filter('translate')('LOGO_UPLOAD_ERROR_ILLEGAL_SIZE');
						$scope.refreshLogoUrl();
						$scope.showUploadForm = false;
						return;
					}
					
					var res = result.indexOf('<pre>') != -1 ? result.charAt(5) : result;
					switch (parseInt(res)) {
					case 0:
						break;

					default:
					case 1:
					case 4:
						$scope.errorMessage = $filter('translate')('LOGO_UPLOAD_ERROR_ILLEGAL_REQUEST');
						break;
						
					case 2:
						$scope.errorMessage = $filter('translate')('LOGO_UPLOAD_ERROR_ILLEGAL_PAYLOAD');
						break;
						
					case 3:
						$scope.errorMessage = $filter('translate')('LOGO_UPLOAD_ERROR_ILLEGAL_SIZE');
						break;
					}
					$scope.refreshLogoUrl();
					$scope.showUploadForm = false;
					
				}, function(error){
					$scope.errorMessage = $filter('translate')('LOGO_UPLOAD_ERROR_ILLEGAL_REQUEST');
					$scope.refreshLogoUrl();
					$scope.showUploadForm = false;
				});

			};

		}],

		restrict: 'E',
		replace: true
	};

}]);
