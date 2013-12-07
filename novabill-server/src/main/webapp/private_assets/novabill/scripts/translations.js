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
		
		//Other strings		
		DEFAULT_PRICE_LIST : 'LISTINO BASE',
		
		LR_CLIENT_CREATE : 'Congratulazioni! Hai aggiunto <strong><a href="{{link}}">{{clientName}}</a></strong> alla lista dei tuoi clienti',
		LR_CLIENT_DELETE : 'Hai rimosso <strong>{{clientName}}</strong> dalla lista dei tuoi clienti',
		LR_CLIENT_UPDATE : 'Hai aggiornato i dati di <strong><a href="{{link}}">{{clientName}}</a></strong>',
		LR_COMMODITY_CREATE : 'Hai aggiunto <strong><a href="{{link}}">{{commodityName}}</a></strong> alla lista dei tuoi articoli',
		LR_COMMODITY_DELETE : 'Hai cancellato l\'Articolo <strong>{{commodityName}}</strong>',
		LR_COMMODITY_UPDATE : 'Hai aggiornato i dati dell\'Articolo <strong><a href="{{link}}">{{commodityName}}</a></strong>',
		LR_CREDIT_NOTE_CREATE : 'Hai creato la <strong><a href="{{link}}">Nota di Credito #{{documentID}}</a></strong>, cliente {{clientName}}',
		LR_CREDIT_NOTE_DELETE : 'Hai cancellato la <strong>Nota di Credito #{{documentID}}</strong>, cliente {{clientName}}',
		LR_CREDIT_NOTE_UPDATE : 'Hai aggiornato la <strong><a href="{{link}}">Nota di Credito #{{documentID}}</a></strong>, cliente {{clientName}}',
		LR_ESTIMATION_CREATE : 'Hai creato il <strong><a href="{{link}}">Preventivo #{{documentID}}</a></strong>, cliente {{clientName}}',
		LR_ESTIMATION_DELETE : 'Hai cancellato il <strong>Preventivo #{{documentID}}</strong>, cliente {{clientName}}',
		LR_ESTIMATION_UPDATE : 'Hai aggiornato il <strong><a href="{{link}}">Preventivo #{{documentID}}</a></strong>, cliente {{clientName}}',
		LR_INVOICE_CREATE : 'Hai creato la <strong><a href="{{link}}">Fattura #{{documentID}}</a></strong>, cliente {{clientName}}',
		LR_INVOICE_DELETE : 'Hai cancellato la <strong>Fattura #{{documentID}}</strong>, cliente {{clientName}}',
		LR_INVOICE_UPDATE : 'Hai aggiornato la <strong><a href="{{link}}">Fattura #{{documentID}}</a></strong>, cliente {{clientName}}',
		LR_PAYMENT_TYPE_CREATE : 'Hai creato il tipo di Pagamento <strong><a href="{{link}}">{{paymentName}}</a></strong>',
		LR_PAYMENT_TYPE_DELETE : 'Hai cancellato il tipo di Pagamento <strong>{{paymentName}}</strong>',
		LR_PAYMENT_TYPE_UPDATE : 'Hai aggiornato il tipo di Pagamento <strong><a href="{{link}}">{{paymentName}}</a></strong>',
		LR_TRANSPORT_DOCUMENT_CREATE : 'Hai creato il <strong><a href="{{link}}">Documento di Trasporto #{{documentID}}</a></strong>, cliente {{clientName}}',
		LR_TRANSPORT_DOCUMENT_DELETE : 'Hai cancellato il <strong>Documento di Trasporto #{{documentID}}</strong>, cliente {{clientName}}',
		LR_TRANSPORT_DOCUMENT_UPDATE : 'Hai aggiornato il <strong><a href="{{link}}">Documento di Trasporto #{{documentID}}</a></strong>, cliente {{clientName}}',
		
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