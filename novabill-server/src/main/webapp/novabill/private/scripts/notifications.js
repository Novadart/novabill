'use strict';

angular.module('novabill.notifications', ['novabill.translations', 'novabill.ajax', 'toaster'])

.controller('NotificationsController', ['$scope', 'nAjax', 'toaster', 
                                        function($scope, nAjax, toaster){

	var Business = nAjax.Business();
	
	$scope.addToast = function(not){
		switch (not.type) {
		case 'PREMIUM_UPGRADE':
			toaster.pop(
					'success', 
					'Piano Premium attivato!', 
					'Ti confermiamo che il piano "Premium" è stato attivato correttamente.<br>Puoi iniziare subito a utilizzare le funzionalità aggiuntive.<br><br>Grazie,<br>Il Team Novabill', 
					0, 
					'trustedHtml', 
					function(){
						Business.markNotificationAsSeen({notificationId : not.id});
						return true;
					}
			);
			break;

		case 'PREMIUM_EXTENSION':
			toaster.pop(
					'success', 
					'Piano Premium prolungato', 
					'Ti confermiamo che il piano "Premium" è stato prolungato correttamente.<br>Puoi controllare lo stato del tuo piano nell\'area "Impostazioni".<br><br>Grazie,<br>Il Team Novabill', 
					0, 
					'trustedHtml', 
					function(){
						Business.markNotificationAsSeen({notificationId : not.id});
						return true;
					}
			);
			break;

		case 'PREMIUM_DOWNGRADE':
			toaster.pop(
					'warning', 
					'Piano Premium scaduto', 
					'Il piano "Premium" che avevi sottoscritto è scaduto ed è stato disattivato.<br>Se vuoi riattivarlo, premi sul link nella barra di intestazione.<br><br>Grazie,<br>Il Team Novabill', 
					0, 
					'trustedHtml', 
					function(){
						Business.markNotificationAsSeen({notificationId : not.id});
						return true;
					}
			);
			break;

		default:
			break;
		}
	};

	Business.getNotifications(function(nots){
		for(var i=0; i<nots.length; i++){
			$scope.addToast( nots[i] );
		}
	});

}]);