'use strict';

angular.module("novabill-frontend.translations", ['pascalprecht.translate'])

.config(['$translateProvider', function($translateProvider){

	/*
	 * NOTE: KEEP IT IN ALPHABETICAL ORDER!
	 */
	$translateProvider.translations('it_IT', {
		CLEAR : 'Ripulisci',
		CREDIT_NOTES : 'Note di Credito',
		DOWNLOAD : 'Scarica',
		INVOICES : 'Fatture',
		NO_DATA : 'Nessun dato',
		PAYMENTS_STATUS_END_DATE : 'Fine Intervallo',
		PAYMENTS_STATUS_PRINT : 'Stampa',
		PAYMENTS_STATUS_START_DATE : 'Inizio Intervallo',
		STATISTICS : 'Statistiche'
	  });

	$translateProvider.preferredLanguage('it_IT');
}]);