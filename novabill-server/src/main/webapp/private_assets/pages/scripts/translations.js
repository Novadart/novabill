'use strict';

angular.module("novabill.translations", ['pascalprecht.translate'])

.config(['$translateProvider', function($translateProvider){

	$translateProvider.translations('it_IT', {
		
		/*
		 * CLIENTS PAGE
		 */
		NEW_CLIENT: 'Nuovo Cliente',
		
		
		/*
		 * INVOICES PAGE
		 */
		NEW_INVOICE : 'Nuova Fattura',
		MODIFY_INVOICE: 'Modifica Fattura',

		
		/*
		 * ESTIMATIONS PAGE
		 */
		NEW_ESTIMATION : 'Nuovo Preventivo',
		MODIFY_ESTIMATION: 'Modifica Preventivo',
		
		
		/*
		 * CREDIT NOTES PAGE
		 */
		NEW_CREDIT_NOTE : 'Nuova Nota di Credito',
		MODIFY_CREDIT_NOTE: 'Modifica Nota di Credito',
		
		
		/*
		 * TRANSPORT DOCUMENTS PAGE
		 */
		NEW_TRANSPORT_DOCUMENT: 'Nuovo Documento di Trasporto',
		MODIFY_TRANSPORT_DOCUMENT: 'Modifica Documento di Trasporto',
		
	  });

	$translateProvider.preferredLanguage('it_IT');
}]);