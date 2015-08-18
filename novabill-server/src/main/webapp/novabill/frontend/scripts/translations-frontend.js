'use strict';

angular.module("novabill-frontend.translations", ['pascalprecht.translate'])

.config(['$translateProvider', function($translateProvider){

	/*
	 * NOTE: KEEP IT IN ALPHABETICAL ORDER!
	 */
	$translateProvider.translations('it_IT', {

        //Series
        JAN : 'Gennaio',
        FEB : 'Febbraio',
        MAR : 'Marzo',
        APR : 'Aprile',
        MAY : 'Maggio',
        JUN : 'Giugno',
        JUL : 'Luglio',
        AUG : 'Agosto',
        SEP : 'Settembre',
        OCT : 'Ottobre',
        NOV : 'Novembre',
        DEC : 'Dicembre',


		CLEAR : 'Ripulisci',
        CLIENTS : 'Clienti',
        COMMODITIES : 'Articoli',
		CREDIT_NOTES : 'Note di Credito',
        DESCRIPTION : 'Descrizione',
		DOWNLOAD : 'Scarica',
		INVOICES : 'Fatture',
        NAME : 'Nome',
		NO_DATA : 'Nessun dato',
		PAYMENTS_STATUS_END_DATE : 'Fine Intervallo',
		PAYMENTS_STATUS_PRINT : 'Stampa',
		PAYMENTS_STATUS_START_DATE : 'Inizio Intervallo',
        PRODUCT : 'Prodotto',
        SERVICE : 'Servizio',
        STATS_CLIENT_ADDED_AT : 'Data Creazione',
        STATS_CLIENTS : 'Statistiche Clienti',
        STATS_CLIENTS_FOR_COMMODITIES_SORTED_BY_INVOICING : 'Clienti che hanno acquistato l\'articolo, ordinati per fatturato (IVA escl.)',
        STATS_CLIENTS_RETURNING : 'Tornati nel {{year}}',
        STATS_CLIENTS_NOT_RETURNING : 'Non ancora tornati nel {{year}}',
        STATS_CLIENTS_SORTED_BY_INVOICING : 'Clienti ordinati per fatturato (IVA escl.)',
        STATS_COMMODITIES : 'Articoli',
        STATS_COMMODITIES_FOR_CLIENT_SORTED_BY_INVOICING : 'Articoli acquistati dal cliente ordinati per fatturato (IVA escl.)',
        STATS_COMMODITIES_SORTED_BY_INVOICING : 'Articoli ordinati per fatturato (IVA escl.)',
        STATS_GENERAL : 'Statistiche Generali',
        STATS_INVOICING_PREV_YEAR : 'Anno {{year}}: {{amount}}',
        TOTAL : 'Totale',
		TOTAL_INVOICING : 'Fatturato',
		TOTAL_INVOICING_BEFORE_TAXES : 'Fatturato IVA escl.',
		TOTAL_INVOICING_BEFORE_TAXES_OVERALL : 'Fatturato IVA escl. totale',
		TRANSPORT_DOCUMENT_AREADY_IN_INVOICE_ALERT : 'Documenti già inclusi in fatture precedenti',
        VAT : 'IVA'
	  });

	$translateProvider.preferredLanguage('it_IT');
}]);