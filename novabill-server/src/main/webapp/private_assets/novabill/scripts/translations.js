'use strict';

angular.module("novabill.translations", ['pascalprecht.translate'])

.config(['$translateProvider', function($translateProvider){

	/*
	 * NOTE: KEEP IT IN ALPHABETICAL ORDER!
	 */
	$translateProvider.translations('it_IT', {
		
		DEFAULT_PRICE_LIST : 'LISTINO BASE',
		
		MODIFY_CREDIT_NOTE: 'Modifica Nota di Credito',
		MODIFY_ESTIMATION: 'Modifica Preventivo',
		MODIFY_INVOICE: 'Modifica Fattura',
		MODIFY_TRANSPORT_DOCUMENT: 'Modifica Documento di Trasporto',
		
		NEW_CLIENT: 'Nuovo Cliente',
		NEW_CREDIT_NOTE : 'Nuova Nota di Credito',
		NEW_ESTIMATION : 'Nuovo Preventivo',
		NEW_INVOICE : 'Nuova Fattura',
		NEW_TRANSPORT_DOCUMENT: 'Nuovo Documento di Trasporto',
		
		TODAY : 'Oggi',
		
		YESTERDAY : 'Ieri',
		
		
	  });

	$translateProvider.preferredLanguage('it_IT');
}]);