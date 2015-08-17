'use strict';

angular.module('novabill-frontend.share', ['novabill-frontend.ajax', 'novabill-frontend.directives', 'novabill-frontend.translations',
    'novabill-frontend.constants', 'infinite-scroll', 'ui.bootstrap'])


    .factory('nQueryParams', ['$window', function($window){
        var searchStr = $window.location.search.substring(1);
        var toks = searchStr.split('&');

        var queryParams = {};
        var tmp = null;
        for(var i in toks){
            tmp = toks[i].split('=');
            queryParams[tmp[0]] = tmp[1];
        }

        return queryParams;
    }])

    .service('nCommons', ['nConstantsFrontend','nQueryParams','$document',
        function(nConstantsFrontend,nQueryParams,$document){

            var _this = this;
            var now = new Date();

            this.HIDDEN_IFRAME = angular.element('<iframe style="display: none;"></iframe>');
            this.DEFAULT_START_DATE = new Date(now.getFullYear(), 0, 1);
            this.DEFAULT_END_DATE = new Date(now.getFullYear(), 11, 31);
            this.PARTITION = 50;
            this.DATE_OPTIONS = {
                'starting-day' : '1',
                'clear-text' : 'Ripulisci'
            };

            angular.element($document[0].body).append(this.HIDDEN_IFRAME);

            function downloadDocuments(docsType, startDate, endDate){
                var sd = startDate != null ? _this.formatDate(startDate) : '';
                var ed = endDate != null ? _this.formatDate(endDate) : '';

                var url = (nConstantsFrontend.conf.baseUrl + 'share/{businessID}/'+docsType+'/download?token={token}&startDate={startDate}&endDate={endDate}')
                    .replace('{businessID}', nQueryParams.businessID)
                    .replace('{token}', nQueryParams.token)
                    .replace('{startDate}', sd)
                    .replace('{endDate}', ed);

                _this.HIDDEN_IFRAME.attr('src', url);
            }

            this.formatDate = function(date){
                return date.getFullYear() + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + '-' + ('0' + date.getDate()).slice(-2);
            };

            this.downloadInvoices = function(startDate, endDate){ downloadDocuments('invoices', startDate, endDate); };

            this.downloadCreditNotes = function(startDate, endDate){ downloadDocuments('creditnotes', startDate, endDate); };

        }])


    .controller('InvoicesCtrl', ['$scope', 'nCommons', '$document', 'nQueryParams', 'nAjaxFrontend',
        function($scope, nCommons, $document, nQueryParams, nAjaxFrontend){

            $scope.dateOptions = nCommons.DATE_OPTIONS;
            var loadedInvoices = [];
            var firstLoad = true;

            $scope.isLoading = function(){
                return $scope.loadingInvoices;
            };

            $scope.loadInvoices = function(startDate, endDate){
                $scope.loadingInvoices = true;
                $scope.invoices = null;

                nAjaxFrontend.getInvoices(nQueryParams.businessID, nQueryParams.token,
                    startDate != null ? nCommons.formatDate(startDate) : '',
                    endDate != null ? nCommons.formatDate(endDate) : '',
                    function(invoices){

                        firstLoad = false;
                        loadedInvoices = invoices;
                        $scope.invoices = loadedInvoices.slice(0, 15);
                        $scope.loadingInvoices = false;

                    });

            };

            $scope.loadMoreInvoices = function(){
                if($scope.invoices){
                    var currentIndex = $scope.invoices.length;
                    $scope.invoices = $scope.invoices.concat(loadedInvoices.slice(currentIndex, currentIndex+nCommons.PARTITION));
                }
            };

            $scope.openStartDate = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.openSD = true;
            };

            $scope.openEndDate = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.openED = true;
            };

            $scope.download = function(){
                nCommons.downloadInvoices($scope.startDate, $scope.endDate);
            };

            $scope.clear = function(){
                $scope.startDate = nCommons.DEFAULT_START_DATE;
                $scope.endDate = nCommons.DEFAULT_END_DATE;
                $scope.invoices = null;
                $scope.loadInvoices(nCommons.DEFAULT_START_DATE, nCommons.DEFAULT_END_DATE);
            };

            $scope.$watch('startDate', function(newValue, oldValue){
                if(!firstLoad){
                    $scope.loadInvoices(newValue, $scope.endDate);
                }
            });

            $scope.$watch('endDate', function(newValue, oldValue){
                if(!firstLoad){
                    $scope.loadInvoices($scope.startDate, newValue);
                }
            });

            $scope.clear();
        }])


    .controller('CreditNotesCtrl', ['$scope', '$document', 'nQueryParams', 'nAjaxFrontend', 'nCommons',
        function($scope, $document, nQueryParams, nAjaxFrontend, nCommons){

            $scope.dateOptions = nCommons.DATE_OPTIONS;

            var loadedCreditNotes = [];
            var firstLoad = true;

            $scope.isLoading = function(){
                return $scope.loadingCreditNotes;
            };

            $scope.loadCreditNotes = function(startDate, endDate){
                $scope.loadingCreditNotes = true;
                $scope.creditNotes = null;

                nAjaxFrontend.getCreditNotes(nQueryParams.businessID, nQueryParams.token,
                    startDate != null ? nCommons.formatDate(startDate) : '',
                    endDate != null ? nCommons.formatDate(endDate) : '',
                    function(notes){

                        firstLoad = false;
                        loadedCreditNotes = notes;
                        $scope.creditNotes = loadedCreditNotes.slice(0, 15);
                        $scope.loadingCreditNotes = false;

                    });
            };

            $scope.loadMoreCreditNotes = function(){
                if($scope.creditNotes){
                    var currentIndex = $scope.creditNotes.length;
                    $scope.creditNotes = $scope.creditNotes.concat(loadedCreditNotes.slice(currentIndex, currentIndex+nCommons.PARTITION));
                }
            };

            $scope.openStartDate = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.openSD = true;
            };

            $scope.openEndDate = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.openED = true;
            };

            $scope.download = function(){
                nCommons.downloadCreditNotes($scope.startDate, $scope.endDate);
            };

            $scope.clear = function(){
                $scope.startDate = nCommons.DEFAULT_START_DATE;
                $scope.endDate = nCommons.DEFAULT_END_DATE;
                $scope.creditNotes = null;
                $scope.loadCreditNotes(nCommons.DEFAULT_START_DATE, nCommons.DEFAULT_END_DATE);
            };

            $scope.$watch('startDate', function(newValue, oldValue){
                if(!firstLoad){
                    $scope.loadCreditNotes(newValue, $scope.endDate);
                }
            });

            $scope.$watch('endDate', function(newValue, oldValue){
                if(!firstLoad){
                    $scope.loadCreditNotes($scope.startDate, newValue);
                }
            });

            $scope.clear();
        }]);