'use strict';

angular.module('novabill.settings.controllers', ['novabill.directives', 'novabill.directives.dialogs', 'novabill.directives.forms',
    'novabill.translations', 'novabill.ajax'])


/**
 * SETTINGS PAGE CONTROLLER
 */
    .controller('SettingsCtrl', ['$scope', 'nConstants', 'nAjax',
        'nDownload', '$window', '$location', 'nEmailPreviewDialog',
        function($scope, nConstants, nAjax,
                 nDownload, $window, $location, nEmailPreviewDialog){

            var Business = nAjax.Business();

            $scope.changePasswordUrl = nConstants.conf.changePasswordBaseUrl;
            $scope.deleteAccountUrl = nConstants.conf.deleteAccountUrl;
            $scope.premiumUrl = nConstants.conf.premiumUrl;
            $scope.principalEmail = nConstants.conf.principalEmail;
            $scope.principalCreationDate = nConstants.conf.principalCreationDate;
            $scope.premium = nConstants.conf.premium;
            $scope.checks = {
                pensionContributionEnabled : false,
                witholdTaxEnabled : false
            };

            $scope.onTabChange = function(token){
                $location.search('tab', token);
            };

            $scope.activeTab = {
                business : false,
                profile : false,
                options : false
            };

            if($location.search().tab) {
                $scope.activeTab[$location.search().tab] = true;
            } else {
                $scope.activeTab.business = true;
            }

            Business.get(function(business){
                $scope.business = business;
                $scope.priceDisplayInDocsMonolithic = !business.settings.priceDisplayInDocsMonolithic;
                $scope.checks.pensionContributionEnabled = business.settings.pensionContributionPercent!= null;
                $scope.checks.witholdTaxEnabled = business.settings.witholdTaxPercentFirstLevel!=null && business.settings.witholdTaxPercentSecondLevel!=null;
            });

            $scope.businessUpdateCallback = function(){
                $window.location.reload();
            };

            $scope.invoiceSettingsFormIsValid = function(){
                if($scope.checks.witholdTaxEnabled){
                    if($scope.business.settings.witholdTaxPercentFirstLevel == null ||
                        $scope.business.settings.witholdTaxPercentSecondLevel == null){
                        return false;
                    }
                }

                if($scope.checks.pensionContributionEnabled){
                    if($scope.business.settings.pensionContributionPercent == null){
                        return false;
                    }
                }

                return true;
            };

            $scope.update = function(){

                if(!$scope.checks.witholdTaxEnabled){
                    $scope.business.settings.witholdTaxPercentFirstLevel = null;
                    $scope.business.settings.witholdTaxPercentSecondLevel = null;
                }

                if(!$scope.checks.pensionContributionEnabled){
                    $scope.business.settings.pensionContributionPercent = null;
                }

                $scope.business.$update(function(){
                    Business.setDefaultLayout({defaultLayoutType : $scope.business.settings.defaultLayoutType}, function(){
                        $window.location.reload();
                    });
                });
            };

            $scope.exportZip = nDownload.downloadExportZip;

            $scope.viewPreview = function(){
                nEmailPreviewDialog.open($scope.business);
            };

            $scope.$watch('priceDisplayInDocsMonolithic', function(newValue) {
                if($scope.business){
                    $scope.business.settings.priceDisplayInDocsMonolithic = !newValue;
                }
            });

        }]);


