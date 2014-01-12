'use strict';

angular.module("novabill.translations", ['pascalprecht.translate'])

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
		
		//other strings
		CANCEL : 'Annulla',
		CLIENTS : 'Clienti',
		CLONE : 'Crea simile',
		COMMODITY : 'Articolo',
		COMMODITIES : 'Articoli',
		COMMODITY_PRICE_PERCENTAGE : 'Variazione',
		CONTINUE : 'Continua',
		CREATE_CREDIT_NOTE : 'Crea Nota di Credito',
		CREATE_INVOICE : 'Crea Fattura',
		CREDIT_NOTES : 'Note di Credito',
		DEFAULT_PRICE : 'Prezzo Base',
		DEFAULT_PRICE_PLACEHOLDER : 'esempio: 15',
		DEFAULT_PRICE_LIST : 'LISTINO BASE',
		DELETE : 'Elimina',
		DESCRIPTION : 'Descrizione',
		DESCRIPTION_PLACEHOLDER : 'esempio: Armadio a cinque ante',
		DETAILS : 'Dettagli',
		EDIT : 'Modifica',
		ESTIMATIONS : 'Offerte',
		FILTER : 'Filtra',
		INVOICES : 'Fatture',
		INVOICES_PER_MONTH : 'Fatture al mese',
		LR_CLIENT_CREATE : 'Congratulazioni! Hai aggiunto <strong><a href="{{link}}">{{clientName}}</a></strong> alla lista dei tuoi clienti',
		LR_CLIENT_DELETE : 'Hai rimosso <strong>{{clientName}}</strong> dalla lista dei tuoi clienti',
		LR_CLIENT_UPDATE : 'Hai aggiornato i dati di <strong><a href="{{link}}">{{clientName}}</a></strong>',
		LR_COMMODITY_CREATE : 'Hai aggiunto <strong><a href="{{link}}">{{commodityName}}</a></strong> alla lista dei tuoi articoli',
		LR_COMMODITY_DELETE : 'Hai cancellato l\'Articolo <strong>{{commodityName}}</strong>',
		LR_COMMODITY_UPDATE : 'Hai aggiornato i dati dell\'Articolo <strong><a href="{{link}}">{{commodityName}}</a></strong>',
		LR_CREDIT_NOTE_CREATE : 'Hai creato la <strong><a href="{{link}}">Nota di Credito #{{documentID}}</a></strong>, cliente {{clientName}}',
		LR_CREDIT_NOTE_DELETE : 'Hai cancellato la <strong>Nota di Credito #{{documentID}}</strong>, cliente {{clientName}}',
		LR_CREDIT_NOTE_UPDATE : 'Hai aggiornato la <strong><a href="{{link}}">Nota di Credito #{{documentID}}</a></strong>, cliente {{clientName}}',
		LR_ESTIMATION_CREATE : 'Hai creato l\'<strong><a href="{{link}}">Offerta #{{documentID}}</a></strong>, cliente {{clientName}}',
		LR_ESTIMATION_DELETE : 'Hai cancellato l\'<strong>Offerta #{{documentID}}</strong>, cliente {{clientName}}',
		LR_ESTIMATION_UPDATE : 'Hai aggiornato l\'<strong><a href="{{link}}">Offerta #{{documentID}}</a></strong>, cliente {{clientName}}',
		LR_INVOICE_CREATE : 'Hai creato la <strong><a href="{{link}}">Fattura #{{documentID}}</a></strong>, cliente {{clientName}}',
		LR_INVOICE_DELETE : 'Hai cancellato la <strong>Fattura #{{documentID}}</strong>, cliente {{clientName}}',
		LR_INVOICE_UPDATE : 'Hai aggiornato la <strong><a href="{{link}}">Fattura #{{documentID}}</a></strong>, cliente {{clientName}}',
		LR_PAYMENT_TYPE_CREATE : 'Hai creato il tipo di Pagamento <strong><a href="{{link}}">{{paymentName}}</a></strong>',
		LR_PAYMENT_TYPE_DELETE : 'Hai cancellato il tipo di Pagamento <strong>{{paymentName}}</strong>',
		LR_PAYMENT_TYPE_UPDATE : 'Hai aggiornato il tipo di Pagamento <strong><a href="{{link}}">{{paymentName}}</a></strong>',
		LR_TRANSPORT_DOCUMENT_CREATE : 'Hai creato il <strong><a href="{{link}}">Documento di Trasporto #{{documentID}}</a></strong>, cliente {{clientName}}',
		LR_TRANSPORT_DOCUMENT_DELETE : 'Hai cancellato il <strong>Documento di Trasporto #{{documentID}}</strong>, cliente {{clientName}}',
		LR_TRANSPORT_DOCUMENT_UPDATE : 'Hai aggiornato il <strong><a href="{{link}}">Documento di Trasporto #{{documentID}}</a></strong>, cliente {{clientName}}',
		LR_PRICE_LIST_CREATE : 'Hai creato il Listino <strong><a href="{{link}}">{{priceListName}}</a></strong>',
		LR_PRICE_LIST_DELETE : 'Hai cancellato il Listino <strong>{{priceListName}}</strong>',
		LR_PRICE_LIST_UPDATE : 'Hai aggiornato il Listino <strong><a href="{{link}}">{{priceListName}}</a></strong>',
		MODIFY_CREDIT_NOTE: 'Modifica Nota di Credito',
		MODIFY_ESTIMATION: 'Modifica Offerta',
		MODIFY_INVOICE: 'Modifica Fattura',
		MODIFY_TRANSPORT_DOCUMENT: 'Modifica Documento di Trasporto',
		NAME : 'Nome',
		NAME_PLACEHOLDER : 'esempio: Listino Clienti Abituali',
		NEW_CLIENT: 'Nuovo Cliente',
		NEW_COMMODITY: 'Nuovo Articolo',
		NEW_CREDIT_NOTE : 'Nuova Nota di Credito',
		NEW_ESTIMATION : 'Nuova Offerta',
		NEW_INVOICE : 'Nuova Fattura',
		NEW_PRICE_LIST: 'Nuovo Listino',
		NEW_TRANSPORT_DOCUMENT: 'Nuovo Documento di Trasporto',
		NO_DATA : 'Nessun dato',
		NUMBER_OF_INVOICES : 'Numero di fatture',
		OPTIONS : 'Opzioni',
		PRICE : 'Prezzo',
		PRICE_DERIVED : 'Variazione Percentuale',
		PRICE_FIXED : 'Prezzo fisso',
		PRICE_LIST : 'Listino',
		PRICE_LISTS : 'Listini',
		PRICE_LIST_NAME : 'Nome listino',
		PRODUCT : 'Prodotto',
		RECENT_ACTIVITIES : 'Attività recenti',
		REMOVAL_QUESTION : 'Eliminare definitivamente {{data}}?',
		SELECT_CLIENT : 'Seleziona un cliente',
		SERVICE : 'Servizio',
		SKU : 'Codice',
		SKU_PLACEHOLDER : 'esempio: T-123',
		STATISTICS_AND_MORE : 'statistiche e novità',
		TAX : 'IVA',
		TAX_PLACEHOLDER : 'esempio: 22',
		TODAY : 'Oggi',
		TOTAL_BEFORE_TAXES : 'Totale IVA escl.',
		TRANSPORT_DOCUMENTS : 'Documenti di Trasporto',
		TYPE : 'Tipo',
		UNIT_OF_MEASURE : 'Unità di Misura',
		UNIT_OF_MEASURE_PLACEHOLDER : 'esempio: pezzo',
		VALIDATION_NAME_TAKEN : 'Nome già utilizzato per un altro listino',
		VALIDATION_INVALID_VALUE : 'Valore non valido',
		VALIDATION_REQUIRED : 'Obbligatorio',
		VALIDATION_SKU_TAKEN : 'Codice già utilizzato per un altro articolo',
		YEAR : 'Anno',
		YESTERDAY : 'Ieri',
		
	  });

	$translateProvider.preferredLanguage('it_IT');
}]);