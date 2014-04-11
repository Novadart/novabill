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
		ADDRESS : 'Indirizzo',
		ADDRESS_NAME_PLACEHOLDER : 'esempio: Uffici dell\'Amministrazione',
		ADDRESS_PLACEHOLDER : 'esempio: via Grande Strada, 54',
		ADDRESSES_MANAGEMENT : 'Gestione Indirizzi',
		BUSINESS_OPTION_FOOTER_TEXT : 'Testo da includere in piè di pagina',
		BUSINESS_OPTION_INCOGNITO_ENABLED : 'Non stampare l\'intestazione nelle Offerte',
		BUSINESS_OPTION_PRICE_IN_DOCS_MONOLITHIC : 'Esplicitare lo sconto percentuale quando si aggiunge un articolo da listino',
		BUSINESS_TAB_BUSINESS : 'Dati Aziendali',
		BUSINESS_TAB_LAYOUT : 'Impostazioni di Stampa',
		BUSINESS_TAB_OPTIONS : 'Impostazioni Varie',
		BUSINESS_TAB_SHARE : 'Condivisione Documenti',
		CANCEL : 'Annulla',
		CITY : 'Città',
		CITY_PLACEHOLDER : 'esempio: Cittadella',
		CLEAR : 'Ripulisci',
		CLIENT_DELETION_ALERT : 'Non è possibile cancellare questo cliente, in quanto esistono ancora dei documenti ad esso associati',
		CLIENTS : 'Clienti',
		CLONE : 'Crea simile',
		COMMODITY : 'Articolo',
		COMMODITIES : 'Articoli',
		COMMODITY_PRICE_DISCOUNT : 'Sconto',
		COMMODITY_PRICE_OVERCHARGE : 'Ricarico',
		COMPANY_NAME : 'Ragione Sociale',
		COMPANY_NAME_PLACEHOLDER : 'Ragione Sociale',
		CONTINUE : 'Continua',
		COUNTRY : 'Nazione',
		CREATE_CREDIT_NOTE : 'Crea Nota di Credito',
		CREATE_INVOICE : 'Crea Fattura',
		CREATE_INVOICE_FROM_MULTIPLE_TRANSPORT_DOCUMENTS : 'Seleziona i Documenti di Trasporto da includere in Fattura',
		CREATE_TRANSPORT_DOCUMENT : 'Crea DDT',
		CREATED_ON : 'Creato il',
		CREDIT_NOTE : 'Nota di Credito',
		CREDIT_NOTES : 'Note di Credito',
		DEFAULT_PRICE : 'Prezzo Base',
		DEFAULT_PRICE_PLACEHOLDER : 'esempio: 15',
		DEFAULT_PRICE_LIST : 'LISTINO BASE',
		DELETE : 'Elimina',
		DESCRIPTION : 'Descrizione',
		DESCRIPTION_PLACEHOLDER : 'esempio: Armadio a cinque ante',
		DETAILS : 'Dettagli',
		DOCUMENTS : 'Documenti',
		EDIT : 'Modifica',
		EMAIL : 'E-Mail',
		EMAIL_PLACEHOLDER : 'esempio: mario.rossi@example.com',
		ESTIMATION : 'Offerta',
		ESTIMATIONS : 'Offerte',
		EXPORT_CLIENTS : 'Esporta i Clienti',
		EXPORT_CREDIT_NOTES : 'Esporta le Note di Credito',
		EXPORT_ESTIMATIONS : 'Esporta le Offerte',
		EXPORT_INVOICES : 'Esporta le Fatture',
		EXPORT_TRANSPORT_DOCUMENTS : 'Esporta i Documenti di Trasporto',
		FAX : 'Fax',
		FAX_PLACEHOLDER : 'esempio: 987 32322323',
		FILTER : 'Filtra',
		INFO : 'Informazioni',
		INSERT_CLIENT_NAME : 'Ragione sociale del nuovo cliente',
		INVOICE_REF_TITLE : 'Questo Documento di Trasporto è stato incluso in una fattura.\nClicca per vederla.',
		INVOICE : 'Fattura',
		INVOICES : 'Fatture',
		INVOICES_PER_MONTH : 'Fatture al mese',
		LAYOUT_TYPE : 'Formato di Stampa',
		LAYOUT_TYPE_DENSE : 'COMPATTO - adatto a documenti che contengono molti articoli',
		LAYOUT_TYPE_TIDY : 'SPAZIOSO - maggiore spazio tra i vari elementi del documento',
		LEGAL_ADDRESS : 'Indirizzo Legale',
		LOGO_UPLOAD_ERROR_ILLEGAL_PAYLOAD : 'Il file che stai tentando di caricare non è valido.',
		LOGO_UPLOAD_ERROR_ILLEGAL_REQUEST : 'La procedura di caricamento del logo non è andata a buon fine. Per favore ritenta e/o prova con un altro file. Grazie.',
		LOGO_UPLOAD_ERROR_ILLEGAL_SIZE : 'Per favore carica un\'immagine di al massimo 1MB',
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
		LR_SHARING_PERMIT_CREATE : 'Hai concesso a <strong>{{sharingPermitDesc}}</strong> la possibilità di accedere ai tuoi dati',
		LR_SHARING_PERMIT_DELETE : 'Hai rimosso per <strong>{{sharingPermitDesc}}</strong> la possibilità di accedere ai tuoi dati',
		LR_TRANSPORT_DOCUMENT_CREATE : 'Hai creato il <strong><a href="{{link}}">Documento di Trasporto #{{documentID}}</a></strong> per il cliente {{clientName}}',
		LR_TRANSPORT_DOCUMENT_DELETE : 'Hai cancellato il <strong>Documento di Trasporto #{{documentID}}</strong> per il cliente {{clientName}}',
		LR_TRANSPORT_DOCUMENT_UPDATE : 'Hai aggiornato il <strong><a href="{{link}}">Documento di Trasporto #{{documentID}}</a></strong> per il cliente {{clientName}}',
		LR_PRICE_LIST_CREATE : 'Hai creato il Listino <strong><a href="{{link}}">{{priceListName}}</a></strong>',
		LR_PRICE_LIST_DELETE : 'Hai cancellato il Listino <strong>{{priceListName}}</strong>',
		LR_PRICE_LIST_UPDATE : 'Hai aggiornato il Listino <strong><a href="{{link}}">{{priceListName}}</a></strong>',
		MOBILE : 'Cellulare',
		MOBILE_PLACEHOLDER : 'esempio: 123 445 55 12',
		MODIFY_CREDIT_NOTE: 'Modifica Nota di Credito',
		MODIFY_ESTIMATION: 'Modifica Offerta',
		MODIFY_INVOICE: 'Modifica Fattura',
		MODIFY_LOGO: 'Modifica Logo',
		MODIFY_TRANSPORT_DOCUMENT: 'Modifica Documento di Trasporto',
		NAME : 'Nome',
		NAME_PLACEHOLDER : 'esempio: Listino Clienti Abituali',
		NEW_ADDRESS: 'Nuovo Indirizzo',
		NEW_CLIENT: 'Nuovo Cliente',
		NEW_COMMODITY: 'Nuovo Articolo',
		NEW_CREDIT_NOTE : 'Nuova Nota di Credito',
		NEW_ESTIMATION : 'Nuova Offerta',
		NEW_INVOICE : 'Nuova Fattura',
		NEW_PRICE_LIST: 'Nuovo Listino',
		NEW_SHARE : 'Nuova Condivisione',
		NEW_TRANSPORT_DOCUMENT: 'Nuovo Documento di Trasporto',
		NO : 'No',
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
		PAYMENTS_STATUS : 'Fatture Non Incassate',
		PAYMENTS_STATUS_END_DATE : 'A',
		PAYMENTS_STATUS_ORDERING_DATE_TYPE : 'Ordina per',
		PAYMENTS_STATUS_FILTERING_CREATION_DATE : 'Data di Creazione',
		PAYMENTS_STATUS_FILTERING_PAYMENT_DUEDATE : 'Data di Scadenza',
		PAYMENTS_STATUS_PRINT : 'Stampa',
		PAYMENTS_STATUS_START_DATE : 'Da',
		POSTCODE : 'C.A.P.',
		POSTCODE_PLACEHOLDER : 'esempio: 35010',
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
		PROVINCE : 'Provincia',
		PROVINCE_PLACEHOLDER : 'esempio: Milano',
		RECENT_ACTIVITIES : 'Attività recenti',
		REMOVAL_QUESTION : 'Eliminare definitivamente {{data}}?',
		REMOVE_LOGO : 'Rimuovi Logo',
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
		SHARING_PERMIT : 'Condivisione',
		SHARING_PERMIT_DESCRIPTION_PLACEHOLDER : 'esempio: Commercialista',
		SHARING_PERMIT_EMAIL_SENT : 'Email inviata correttamente',
		SHARING_PERMIT_INFO1 : 'Le condivisioni permettono di mettere a disposizione di altri collaboratori (come i commercialisti) parte dei propri dati, come le fatture.',
		SHARING_PERMIT_INFO2 : 'Per vedere i dati resi accessibili tramite le condivisioni premi il pulsante sottostante.',
		SHARING_PERMIT_REMOVAL_QUESTION : 'Se rimuovi questa condivisione, il possessore dell\'email {{email}} non potrà più accedere ai dati. Continuare?',
		SHARING_PERMIT_SEND_EMAIL : 'Inviare un\'email informativa all\'indirizzo email indicato?',
		SHARING_PERMIT_SEND_EMAIL_CONFIRM : 'Inviare un\'email informativa a {{email}}',
		SHARING_PERMIT_SEND_EMAIL_OPTION : 'Invia email',
		SIMILAR_CLIENTS : 'Clienti simili',
		SSN : 'C.F.',
		SSN_EXTENDED : 'Codice Fiscale',
		SSN_PLACEHOLDER : 'esempio: AAABBB11A99H443P o 07643520567',
		STATISTICS_AND_MORE : 'statistiche e novità',
		TAX : 'IVA',
		TAX_PLACEHOLDER : 'esempio: 22',
		TELEPHONE : 'Telefono',
		TELEPHONE_PLACEHOLDER : 'esempio: 123/45678910',
		TITLE_COMPANY_DATA : 'I dati della tua azienda',
		TITLE_EXPORT_DATA : 'Esporta i tuoi dati',
		TITLE_SET_LOGO : 'Imposta il tuo logo',
		TODAY : 'Oggi',
		TOGGLE_PAYED : 'Incassata',
		TOTAL : 'Totale',
		TOTAL_BEFORE_TAXES : 'Fatturato',
		TRANSPORT_DOCUMENT_AREADY_IN_INVOICE_ALERT : 'Documenti già inclusi in fatture precedenti',
		TRANSPORT_DOCUMENT : 'Documento di Trasporto',
		TRANSPORT_DOCUMENTS : 'Documenti di Trasporto',
		TRY_SHARE : 'Prova Condivisione',
		TYPE : 'Tipo',
		UNIT_OF_MEASURE : 'Unità di Misura',
		UNIT_OF_MEASURE_PLACEHOLDER : 'esempio: pezzo',
		UNSELECT_ALL : 'Annulla Selezione',
		VALIDATION_EMAIL_TAKEN : 'Email già utilizzata in un\'altra condivisione',
		VALIDATION_NAME_TAKEN : 'Nome già utilizzato per un altro listino',
		VALIDATION_INVALID_VALUE : 'Valore non valido',
		VALIDATION_REQUIRED : 'Obbligatorio',
		VALIDATION_SKU_TAKEN : 'Codice già utilizzato per un altro articolo',
		VATID : 'P.IVA',
		VATID_PLACEHOLDER : 'esempio: 07643520567 o IT07643520567',
		YEAR : 'Anno',
		YES : 'Sì',
		YESTERDAY : 'Ieri',
		WEBSITE : 'Sito Web',
		WEBSITE_PLACEHOLDER : 'esempio: www.example.com',
		WEIGHT : 'Peso (kg)',
		WEIGHT_PLACEHOLDER : 'esempio: 2'
		
	  });

	$translateProvider.preferredLanguage('it_IT');
}]);