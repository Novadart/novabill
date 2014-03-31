'use strict';

angular.module("novabill-frontend.translations", ['pascalprecht.translate'])

.config(['$translateProvider', function($translateProvider){

	/*
	 * NOTE: KEEP IT IN ALPHABETICAL ORDER!
	 */
	$translateProvider.translations('it_IT', {
		CLEAR : 'Ripulisci',
		NO_DATA : 'Nessun dato',
		PAYMENTS_STATUS_END_DATE : 'A',
		PAYMENTS_STATUS_PRINT : 'Stampa',
		PAYMENTS_STATUS_START_DATE : 'Da',
	  });

	$translateProvider.preferredLanguage('it_IT');
}]);