'use strict';

angular.module('novabill.gwtbridge', [])

    .run(['$window', 'nEditClientDialog', 'nAjax', function($window, nEditClientDialog, nAjax){

            $window.Angular_openClientDialog = function(clientId, isClientIncomplete, callback){
                var Client = nAjax.Client();
                Client.get({id:clientId}, function (client) {
                    var instance = nEditClientDialog.open(client, isClientIncomplete);
                    instance.result.then(
                        function(modClient){
                            modClient.$update(function(){
                                callback(angular.toJson(modClient));
                            });
                        }
                    );
                });
            };

        }]);
