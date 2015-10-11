'use strict';

angular.module('novabill.directives.validation',
    ['novabill.utils', 'novabill.translations'])



    /*
     * Vat validation attribute.
     */
    .directive('nVatId', ['nRegExp', function(nRegExp) {
        return {
            require: 'ngModel',
            restrict: 'A',
            scope : {
                nOptional : '@',
                nCountry : '='
            },
            link: function(scope, elm, attrs, ctrl) {

                //validation is enabled only when the country is Italy

                ctrl.$validators.vatId = function(viewValue) {
                    var optional = attrs.nOptional !== undefined && scope.nOptional!=='false';

                    if(viewValue){
                        return (scope.nCountry !== undefined && scope.nCountry !== 'IT') || nRegExp.vatId.test(viewValue);
                    } else {
                        return optional;
                    }

                };

                scope.$watch('nCountry', function() {
                    if(scope.nCountry) {
                        ctrl.$validate();
                    }
                });

            }
        };
    }])



    /*
     * SSN validation attribute.
     */
    .directive('nSsn', ['nRegExp', function(nRegExp) {
        return {
            require: 'ngModel',
            restrict: 'A',
            scope : {
                nOptional : '@',
                nCountry : '='
            },
            link: function(scope, elm, attrs, ctrl) {

                //validation is enabled only when the country is Italy

                ctrl.$validators.ssn = function(viewValue) {
                    var optional = attrs.nOptional !== undefined && scope.nOptional!=='false';

                    if(viewValue){
                        return (scope.nCountry !== undefined && scope.nCountry !== 'IT') || nRegExp.ssn.test(viewValue);
                    } else {
                        return optional;
                    }
                };

                scope.$watch('nCountry', function() {
                    if(scope.nCountry) {
                        ctrl.$validate();
                    }
                });
            }
        };
    }])



    /*
     * SSN or Vat ID validation attribute.
     */
    .directive('nSsnOrVatId', ['nRegExp', function(nRegExp) {
        return {
            require: 'ngModel',
            restrict: 'A',
            scope : {
                nOptional : '@',
                nCountry : '='
            },
            link: function(scope, elm, attrs, ctrl) {

                //validation is enabled only when the country is Italy

                ctrl.$validators.ssnOrVatId = function(viewValue) {
                    var optional = attrs.nOptional !== undefined && scope.nOptional!=='false';

                    if(viewValue){
                        return (scope.nCountry !== undefined && scope.nCountry !== 'IT') || nRegExp.ssn.test(viewValue) || nRegExp.vatId.test(viewValue);
                    } else {
                        return optional;
                    }
                };

                scope.$watch('nCountry', function() {
                    if(scope.nCountry) {
                        ctrl.$validate();
                    }
                });

            }
        };
    }])


    /*
     * docIdClass validation attribute.
     */
    .directive('nDocIdClass', ['nRegExp', function(nRegExp) {
        return {
            require: 'ngModel',
            restrict: 'A',
            link: function(scope, elm, attrs, ctrl) {

                ctrl.$validators.docIdClass = function(viewValue) {
                    return viewValue === '' || nRegExp.docIdClass.test(viewValue);
                };

            }
        };
    }])


    /*
     * Smart Tax attribute.
     * User can insert , or . to separate decimals
     * Value mast be between 0 and 100
     */
    .directive('nSmartTax', ['nRegExp', function(nRegExp) {
        return {
            require: 'ngModel',
            restrict: 'A',
            link: function(scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function(viewValue) {

                    if(viewValue === ''){
                        ctrl.$setValidity('tax', true);
                        return '';
                    }

                    if (nRegExp.positiveTwoDecimalsFloatNumber.test(viewValue)) {
                        var dotVal = viewValue.replace(',', '.');
                        var floatVal = parseFloat(dotVal);
                        if(floatVal >= 0 && floatVal < 100){
                            ctrl.$setValidity('tax', true);
                            return dotVal;
                        } else {
                            ctrl.$setValidity('tax', false);
                            return undefined;
                        }

                    } else {
                        ctrl.$setValidity('tax', false);
                        return undefined;
                    }
                });

                ctrl.$formatters.push(function(modelValue) {
                    return modelValue ? String(modelValue).replace('.', ',') : '';
                });
            }
        };
    }])


    /*
     * Smart Price attribute.
     * User can insert , or . to separate decimals
     */
    .directive('nSmartPrice', ['nRegExp', function(nRegExp) {
        return {
            require: 'ngModel',
            restrict: 'A',
            link: function(scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function(viewValue) {

                    if(viewValue === ''){
                        ctrl.$setValidity('price', true);
                        return '';
                    }

                    if (nRegExp.positiveTwoDecimalsFloatNumber.test(viewValue)) {
                        var dotVal = viewValue.replace(',', '.');
                        var floatVal = parseFloat(dotVal);
                        if(floatVal >= 0){
                            ctrl.$setValidity('price', true);
                            return dotVal;
                        } else {
                            ctrl.$setValidity('price', false);
                            return undefined;
                        }

                    } else {
                        ctrl.$setValidity('price', false);
                        return undefined;
                    }
                });

                ctrl.$formatters.push(function(modelValue) {
                    return modelValue ? new String(modelValue).replace('.', ',') : '';
                });
            }
        };
    }])


    /*
     * Smart Float attribute.
     * User can insert , or . to separate decimals.
     * if "positive-float" attribute is set to true the float must be positive
     */
    .directive('nSmartFloat', ['nRegExp', function(nRegExp) {
        return {
            require: 'ngModel',
            scope : {
                positiveFloat : '='
            },
            restrict: 'A',
            link: function(scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function(viewValue) {
                    var testExp = attrs.positiveFloat !== undefined ? nRegExp.positiveFloatNumber : nRegExp.floatNumber;

                    if(viewValue === ''){
                        ctrl.$setValidity('float', true);
                        return '';
                    }

                    if (testExp.test(viewValue)) {
                        ctrl.$setValidity('float', true);
                        return viewValue.replace(',', '.');
                    } else {
                        ctrl.$setValidity('float', false);
                        return undefined;
                    }
                });

                ctrl.$formatters.push(function(modelValue) {
                    return modelValue ? new String(modelValue).replace('.', ',') : '';
                });
            }
        };
    }])


    /*
     * Check if the text is not a reserved word
     */
    .directive('nNotReserved', ['nRegExp', '$filter', function(nRegExp, $filter) {
        return {
            require: 'ngModel',
            restrict: 'A',
            link: function(scope, elm, attrs, ctrl) {

                ctrl.$validators.notReserved = function(viewValue) {
                    return !(nRegExp.reserved_word.test(viewValue) || $filter('translate')('DEFAULT_PRICE_LIST').toLowerCase() == viewValue.toLowerCase());
                };

            }
        };
    }])



    /*
     * Input for sku values
     */
    .directive('nSkuInput', ['nRegExp', '$filter', function(nRegExp, $filter) {
        return {
            require: 'ngModel',
            replace: true,
            scope : {
                origSku : '='
            },
            controller : ['$scope', function($scope){
                $scope.originalSku = $scope.origSku;
            }],
            link: function(scope, element, attrs, ctrl) {
                ctrl.$parsers.push(function(viewValue) {
                    if(viewValue == ''){
                        if(nRegExp.reserved_word.test(scope.originalSku)){
                            ctrl.$setValidity('skuRequired', true);
                            return scope.originalSku;
                        } else {
                            ctrl.$setValidity('skuRequired', false);
                            return undefined;
                        }
                    } else {
                        if (nRegExp.reserved_word.test(viewValue)) {
                            ctrl.$setValidity('notReserved', false);
                            return undefined;
                        } else {
                            ctrl.$setValidity('notReserved', true);
                            return viewValue;
                        }
                    }
                });

                ctrl.$formatters.push(function(data) {
                    return $filter('nReplaceReservedWord')(data);
                });
            }
        };
    }]);
