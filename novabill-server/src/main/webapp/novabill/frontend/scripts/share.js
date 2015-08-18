'use strict';

angular.module('novabill-frontend.share',
    [
        'novabill-frontend.ajax',
        'novabill-frontend.directives',
        'novabill-frontend.translations',
        'novabill-frontend.constants',
        'infinite-scroll',
        'ui.bootstrap',
        'googlechart'
    ])


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

            this.months = ['JAN', 'FEB', 'MAR', 'APR', 'MAY', 'JUN', 'JUL', 'AUG', 'SEP', 'OCT', 'NOV', 'DEC'];

            /**
             * Compare two clients
             * @return -1 if minor, 0 if equal, 1 if major
             */
            this.clientsComparator = function(c1, c2) {
                var s1 = c1.name.toLowerCase();
                var s2 = c2.name.toLowerCase();
                return s1<s2 ? -1 : (s1>s2 ? 1 : 0);
            };

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
        }])


    .controller('StatsGeneralCtrl', ['$scope', 'nCommons', 'nAjaxFrontend', '$window', '$filter', 'nQueryParams',
        function($scope, nCommons, nAjaxFrontend, $window, $filter, nQueryParams){


            var Stats = nAjaxFrontend.Stats();

            $scope.year = new Date().getFullYear();
            $scope.years = [$scope.year, $scope.year-1, $scope.year-2, $scope.year-3, $scope.year-4];


            $scope.loadStats = function(year){

                Stats.getGeneralBIStats(nQueryParams.businessID, $scope.year, nQueryParams.token, function(stats){

                    $scope.prevYear = $scope.year-1;

                    // calculate totals per months
                    var rows = [];
                    var prevYearStr = $scope.prevYear.toString();
                    for(var i=0; i<12; i++){
                        rows.push({
                            c : [
                                {v: $filter('translate')(nCommons.months[i])},
                                {v: stats.totalsPerMonths[year][i]},
                                {v: stats.totalsPerMonths[prevYearStr][i]}
                            ]
                        });
                    }

                    $scope.totalsPerMonthsChart = {
                        type: "ColumnChart",
                        displayed: true,
                        data: {
                            cols: [
                                { id: 'month', label: $filter('translate')('MONTH'), type: 'string'},
                                { id: 'invoicing-cur', label: $scope.year, type: 'number'},
                                { id: 'invoicing-past', label: prevYearStr, type: 'number'}
                            ],
                            rows: rows
                        },
                        options: {
                            title: $filter('translate')('INVOICING_PER_MONTH'),
                            displayExactValues: true,
                            vAxis: {
                                title: $filter('translate')('TOTAL_INVOICING_BEFORE_TAXES'),
                                viewWindow:{
                                    min: 0
                                }
                            },
                            hAxis: {
                                title: $filter('translate')('MONTH')
                            }
                        }
                    };

                    // calculate totals
                    var totalBeforeTaxes = stats.totals.totalBeforeTax;
                    var totalAfterTaxes = stats.totals.totalAfterTax;
                    var totalTaxes = ( new BigNumber(totalAfterTaxes).minus(new BigNumber(totalBeforeTaxes)) ).toString();


                    $scope.totals = {
                        totalBeforeTaxes : totalBeforeTaxes,
                        totalTaxes : totalTaxes,
                        totalAfterTaxes : totalAfterTaxes
                    };

                    // calculate clients stats
                    $scope.clientsChart = {
                        type: "PieChart",
                        displayed: true,
                        data: {
                            cols: [
                                { id: 'set', label: 'Clients Set', type: 'string'},
                                { id: 'value', label: 'Value', type: 'number'}
                            ],
                            rows: [
                                { c: [{v: $filter('translate')('STATS_CLIENTS_RETURNING', {year : $scope.year})},
                                    {v: stats.clientsVsReturningClients.numberOfReturningClients}] },
                                { c: [{v: $filter('translate')('STATS_CLIENTS_NOT_RETURNING', {year : $scope.year})},
                                    {v: stats.clientsVsReturningClients.numberOfClients - stats.clientsVsReturningClients.numberOfReturningClients}] }
                            ]
                        },
                        options : {

                        }
                    };

                    $scope.clients = {
                        totalCount : stats.clientsVsReturningClients.numberOfClients
                    };

                    // calculate commodities stats
                    var servicesCount = 0;
                    var commoditiesCount = stats.commodityRankingByRevenue.length;
                    for(var j=0; j<commoditiesCount; j++){
                        if(stats.commodityRankingByRevenue[j].service){
                            servicesCount++;
                        }
                    }

                    $scope.commoditiesChart = {
                        type: "PieChart",
                        displayed: true,
                        data: {
                            cols: [
                                { id: 'set', label: 'Commodities Set', type: 'string'},
                                { id: 'value', label: 'Value', type: 'number'}
                            ],
                            rows: [
                                { c: [{v: $filter('translate')('SERVICES')},
                                    {v: servicesCount}] },
                                { c: [{v: $filter('translate')('PRODUCTS')},
                                    {v: commoditiesCount - servicesCount}] }
                            ]
                        },
                        options : {

                        }
                    };
                    $scope.commodities = {
                        totalCount : commoditiesCount
                    };

                    // rankings
                    $scope.ranks = {
                        clients : stats.clientRankingByRevenue,
                        commodities : stats.commodityRankingByRevenue
                    };

                });
            };

            //$scope.openClientStats = function(clientID){
            //    $window.location.assign( nConstants.url.statsClients(clientID, $scope.year) );
            //};
            //
            //$scope.openCommodityStats = function(commodityID){
            //    $window.location.assign( nConstants.url.statsCommodities(commodityID, $scope.year) );
            //};



            $scope.$watch('year', function(newValue, oldValue){
                $scope.loadStats( newValue );
            });

        }])


    .controller('StatsClientsCtrl', ['$scope', 'nConstants', 'nAjax', '$location', '$routeParams', 'nSorting', '$filter', '$window',
        function($scope, nConstants, nAjax, $location, $routeParams, nSorting, $filter, $window){

            var year = parseInt( $routeParams.year );
            var prevYear = year-1;
            $scope.year = year.toString();
            $scope.commodities = [];

            var clientID = $routeParams.clientID;

            var Stats = nAjax.Stats();
            var Business = nAjax.Business();

            $scope.loadYear = function(year){
                $window.location.assign(nConstants.url.statsClients(clientID, year));
            };

            $scope.loadStats = function(clientID, year){
                Stats.getClientBIStats({clientID : clientID, year : year}, function(stats){

                    // calculate totals per months
                    var rows = [];
                    var prevYearStr = prevYear.toString();
                    for(var i=0; i<12; i++){
                        rows.push({
                            c : [
                                {v: $filter('translate')(nConstants.months[i])},
                                {v: stats.totalsPerMonths[year][i]},
                                {v: stats.totalsPerMonths[prevYearStr][i]}
                            ]
                        });
                    }

                    $scope.totalsPerMonthsChart = {
                        type: "ColumnChart",
                        displayed: true,
                        data: {
                            cols: [
                                { id: 'month', label: $filter('translate')('MONTH'), type: 'string'},
                                { id: 'invoicing-cur', label: $scope.year, type: 'number'},
                                { id: 'invoicing-past', label: prevYearStr, type: 'number'}
                            ],
                            rows: rows
                        },
                        options: {
                            title: $filter('translate')('INVOICING_PER_MONTH'),
                            displayExactValues: true,
                            vAxis: {
                                title: $filter('translate')('TOTAL_INVOICING_BEFORE_TAXES'),
                                viewWindow:{
                                    min: 0
                                }
                            },
                            hAxis: {
                                title: $filter('translate')('MONTH')
                            }
                        }
                    };


                    // calculate totals
                    $scope.clientDetails = {
                        timestamp : stats.creationTime,
                        totalBeforeTaxes : stats.totalBeforeTaxes,
                        totalBeforeTaxesCurrentYear : stats.totalBeforeTaxesCurrentYear
                    };

                    // commodities ranking
                    $scope.commodities = stats.commodityStatsForCurrentYear;

                });
            };

            // if clientID is provided we can load the stats already, otherwise we'll load them once we got the clients list

            Business.getClients(function(clients){
                $scope.clients = clients.sort( nSorting.clientsComparator );

                if(clientID !== '0'){

                    $scope.selectedClient = parseInt( clientID );

                } else {
                    if($scope.clients.length > 0){

                        // if user didn't select any client, pick the first one in the row
                        $scope.selectedClient = $scope.clients[0].id;

                    } else {

                        // in this case there are no clients and we can't load anything
                        //TODO display some message
                        return;
                    }
                }

                $scope.loadStats( $scope.selectedClient, $scope.year );

            });

            $scope.openCommodityStats = function(commodityID){
                $window.location.assign( nConstants.url.statsCommodities(commodityID, $scope.year) );
            };

            $scope.clientChanged = function(clientID){
                $location.path('/' + $scope.selectedClient + '/' + $scope.year);
            };

        }]);