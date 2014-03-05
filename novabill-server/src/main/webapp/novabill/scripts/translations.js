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
		BUSINESS_OPTION_FOOTER_TEXT : 'Testo da includere in piè di pagina in fattura',
		BUSINESS_OPTION_INCOGNITO_ENABLED : 'Non stampare l\'intestazione nelle Offerte',
		BUSINESS_OPTION_PRICE_IN_DOCS_MONOLITHIC : 'Esplicitare lo sconto percentuale quando si aggiunge un articolo da listino',
		BUSINESS_TAB_BUSINESS : 'Dati Aziendali',
		BUSINESS_TAB_LAYOUT : 'Impostazioni di Stampa',
		BUSINESS_TAB_OPTIONS : 'Impostazioni Varie',
		CANCEL : 'Annulla',
		CLEAR : 'Ripulisci',
		CLIENT_DELETION_ALERT : 'Non è possibile cancellare questo cliente, in quanto esistono ancora dei documenti ad esso associati',
		CLIENTS : 'Clienti',
		CLONE : 'Crea simile',
		COMMODITY : 'Articolo',
		COMMODITIES : 'Articoli',
		COMMODITY_PRICE_DISCOUNT : 'Sconto',
		COMMODITY_PRICE_OVERCHARGE : 'Ricarico',
		CONTINUE : 'Continua',
		CREATE_CREDIT_NOTE : 'Crea Nota di Credito',
		CREATE_INVOICE : 'Crea Fattura',
		CREATE_INVOICE_FROM_MULTIPLE_TRANSPORT_DOCUMENTS : 'Seleziona i Documenti di Trasporto da includere in Fattura',
		CREATE_TRANSPORT_DOCUMENT : 'Crea DDT',
		CREDIT_NOTE : 'Nota di Credito',
		CREDIT_NOTES : 'Note di Credito',
		DEFAULT_PRICE : 'Prezzo Base',
		DEFAULT_PRICE_PLACEHOLDER : 'esempio: 15',
		DEFAULT_PRICE_LIST : 'LISTINO BASE',
		DELETE : 'Elimina',
		DESCRIPTION : 'Descrizione',
		DESCRIPTION_PLACEHOLDER : 'esempio: Armadio a cinque ante',
		DETAILS : 'Dettagli',
		EDIT : 'Modifica',
		ESTIMATION : 'Offerta',
		ESTIMATIONS : 'Offerte',
		FILTER : 'Filtra',
		INSERT_CLIENT_NAME : 'Ragione sociale del nuovo cliente',
		INVOICE_REF_TITLE : 'Questo Documento di Trasporto è stato incluso in una fattura.\nClicca per vederla.',
		INVOICE : 'Fattura',
		INVOICES : 'Fatture',
		INVOICES_PER_MONTH : 'Fatture al mese',
		LAYOUT_TYPE : 'Formato di Stampa',
		LAYOUT_TYPE_DENSE : 'COMPATTO - adatto a documenti che contengono molti articoli',
		LAYOUT_TYPE_TIDY : 'ELEGANTE - maggiore spazio tra i vari elementi del documento',
		LR_CLIENT_CREATE : 'Congratulazioni! Hai aggiunto <strong><a href="{{link}}">{{clientName}}</a></strong> alla lista dei tuoi clienti',
		LR_CLIENT_DELETE : 'Hai rimosso <strong>{{clientName}}</strong> dalla lista dei tuoi clienti',
		LR_CLIENT_UPDATE : 'Hai aggiornato i dati di <strong><a href="{{link}}">{{clientName}}</a></strong>',
		LR_COMMODITY_CREATE : 'Hai aggiunto <strong><a href="{{link}}">{{commodityName}}</a></strong> alla lista dei tuoi articoli',
		LR_COMMODITY_DELETE : 'Hai cancellato l\'Articolo <strong>{{commodityName}}</strong>',
		LR_COMMODITY_UPDATE : 'Hai aggiornato i dati dell\'Articolo <strong><a href="{{link}}">{{commodityName}}</a></strong>',
		LR_CREDIT_NOTE_CREATE : 'Hai creato la <strong><a href="{{link}}">Nota di Credito #{{documentID}}</a></strong> per il cliente {{clientName}}',
		LR_CREDIT_NOTE_DELETE : 'Hai cancellato la <strong>Nota di Credito #{{documentID}}</strong> per il cliente {{clientName}}',
		LR_CREDIT_NOTE_UPDATE : 'Hai aggiornato la <strong><a href="{{link}}">Nota di Credito #{{documentID}}</a></strong> per il cliente {{clientName}}',
		LR_ESTIMATION_CREATE : 'Hai creato l\'<strong><a href="{{link}}">Offerta #{{documentID}}</a></strong> per il cliente {{clientName}}',
		LR_ESTIMATION_DELETE : 'Hai cancellato l\'<strong>Offerta #{{documentID}}</strong> per il cliente {{clientName}}',
		LR_ESTIMATION_UPDATE : 'Hai aggiornato l\'<strong><a href="{{link}}">Offerta #{{documentID}}</a></strong> per il cliente {{clientName}}',
		LR_INVOICE_CREATE : 'Hai creato la <strong><a href="{{link}}">Fattura #{{documentID}}</a></strong> per il cliente {{clientName}}',
		LR_INVOICE_DELETE : 'Hai cancellato la <strong>Fattura #{{documentID}}</strong> per il cliente {{clientName}}',
		LR_INVOICE_UPDATE : 'Hai aggiornato la <strong><a href="{{link}}">Fattura #{{documentID}}</a></strong> per il cliente {{clientName}}',
		LR_INVOICE_SET_PAYED_FALSE : 'Hai impostato come <strong>non incassata</strong> la <strong><a href="{{link}}">Fattura #{{documentID}}</a></strong> per il cliente {{clientName}}',
		LR_INVOICE_SET_PAYED_TRUE : 'Hai impostato come <strong>incassata</strong> la <strong><a href="{{link}}">Fattura #{{documentID}}</a></strong> per il cliente {{clientName}}',
		LR_PAYMENT_TYPE_CREATE : 'Hai creato il tipo di Pagamento <strong><a href="{{link}}">{{paymentName}}</a></strong>',
		LR_PAYMENT_TYPE_DELETE : 'Hai cancellato il tipo di Pagamento <strong>{{paymentName}}</strong>',
		LR_PAYMENT_TYPE_UPDATE : 'Hai aggiornato il tipo di Pagamento <strong><a href="{{link}}">{{paymentName}}</a></strong>',
		LR_TRANSPORT_DOCUMENT_CREATE : 'Hai creato il <strong><a href="{{link}}">Documento di Trasporto #{{documentID}}</a></strong> per il cliente {{clientName}}',
		LR_TRANSPORT_DOCUMENT_DELETE : 'Hai cancellato il <strong>Documento di Trasporto #{{documentID}}</strong> per il cliente {{clientName}}',
		LR_TRANSPORT_DOCUMENT_UPDATE : 'Hai aggiornato il <strong><a href="{{link}}">Documento di Trasporto #{{documentID}}</a></strong> per il cliente {{clientName}}',
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
		NOT_PAYED : 'Non Incassata',
		NOTIFICATION : 'Notifica',
		NUMBER_OF_INVOICES : 'Numero di fatture',
		OPEN : 'Apri',
		OPTIONS : 'Opzioni',
		PAYED : 'Incassata', 
		PAYMENT_EXPIRATION : 'Scadenza Pagamento',
		PAYMENT_EXPIRED : 'Termini di pagamento scaduti',
		PAYMENT_TYPES : 'Tipologie di Pagamento',
		PAYMENTS_MANAGEMENT : 'Pagamenti',
		PAYMENTS_STATUS : 'Situazione Pagamenti',
		PAYMENTS_STATUS_END_DATE : 'A',
		PAYMENTS_STATUS_PRINT : 'Stampa Prospetto',
		PAYMENTS_STATUS_START_DATE : 'Da',
		PRICE : 'Prezzo',
		PRICE_DISCOUNT_PERCENT : 'Sconto %',
		PRICE_DISCOUNT_FIXED : 'Sconto €',
		PRICE_OVERCHARGE_PERCENT : 'Ricarico %',
		PRICE_OVERCHARGE_FIXED : 'Ricarico €',
		PRICE_FIXED : 'Prezzo fisso',
		PRICE_LIST : 'Listino',
		PRICE_LISTS : 'Listini',
		PRICE_LIST_NAME : 'Nome listino',
		PRODUCT : 'Prodotto',
		RECENT_ACTIVITIES : 'Attività recenti',
		REMOVAL_QUESTION : 'Eliminare definitivamente {{data}}?',
		SAVE_CHANGES : 'Salva Modifiche',
		SELECT_CLIENT : 'Seleziona un Cliente',
		SELECT_COMMODITY : 'Seleziona un Articolo',
		SELECT_ALL : 'Seleziona Tutti',
		SELECTED : 'Selezionati',
		SERVICE : 'Servizio',
		SET_PRICE : 'Imposta Prezzo',
		SETTINGS : 'Impostazioni',
		SKU : 'Codice',
		SKU_PLACEHOLDER : 'esempio: T-123',
		SIMILAR_CLIENTS : 'Clienti simili',
		SSN : 'C.F.',
		STATISTICS_AND_MORE : 'statistiche e novità',
		TAX : 'IVA',
		TAX_PLACEHOLDER : 'esempio: 22',
		TODAY : 'Oggi',
		TOGGLE_PAYED : 'Incassata / Non Incassata',
		TOTAL : 'Totale',
		TOTAL_BEFORE_TAXES : 'Fatturato',
		TRANSPORT_DOCUMENT_AREADY_IN_INVOICE_ALERT : 'Documenti già inclusi in fatture precedenti',
		TRANSPORT_DOCUMENT : 'Documento di Trasporto',
		TRANSPORT_DOCUMENTS : 'Documenti di Trasporto',
		TYPE : 'Tipo',
		UNIT_OF_MEASURE : 'Unità di Misura',
		UNIT_OF_MEASURE_PLACEHOLDER : 'esempio: pezzo',
		UNSELECT_ALL : 'Annulla Selezione',
		VALIDATION_NAME_TAKEN : 'Nome già utilizzato per un altro listino',
		VALIDATION_INVALID_VALUE : 'Valore non valido',
		VALIDATION_REQUIRED : 'Obbligatorio',
		VALIDATION_SKU_TAKEN : 'Codice già utilizzato per un altro articolo',
		VATID : 'P.IVA',
		YEAR : 'Anno',
		YESTERDAY : 'Ieri',
		WEIGHT : 'Peso (kg)',
		WEIGHT_PLACEHOLDER : 'esempio: 2'
		
	  });

	$translateProvider.preferredLanguage('it_IT');
}]);