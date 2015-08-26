'use strict';

angular.module('novabill.gwtbridge', [])

    .run(['$window', 'nEditClientDialog', 'nAjax', '$location', function($window, nEditClientDialog, nAjax, $location){

            $window.Angular_openClientDialog = function(clientId, isClientIncomplete, callback){
                var Client = nAjax.Client();
                Client.get({id:clientId}, function (client) {
                    var instance = nEditClientDialog.open(client, isClientIncomplete);
                    instance.result.then(
                        function(modClient){
                            modClient.$update(function(){
                                callback(angular.toJson(modClient));
                            });
                        },

                        // this gwt bridge method is used to complete the client info, in case the dialog is closed we go to
                        // the root list
                        function(){
                            $location.path('/');
                        }
                    );
                });
            };

        }]);
