'use strict';

angular.module('novabill.constants', [])

.provider('nConstants', function() {

	// these variables are set in the environment with values coming from the server.
	// setting default values useful for testing
	var businessId = 0;
	var defaultPriceListName = '::default';
	var basePath = '/novabill-server/';
	var version = '0';
	
	//updating the values in case they are set in the environment
	if(typeof NovabillConf != 'undefined') {
		businessId = NovabillConf.businessId;
		defaultPriceListName = NovabillConf.defaultPriceListName;
		basePath = NovabillConf.basePath;
		version = NovabillConf.version;
	}

	// this set of properties is available even at module configuration time
	// these properties define the most basic configuration of the UI
	this.conf = {
			businessId : businessId,
			defaultPriceListName : defaultPriceListName,
			version : version,

			privateAreaBaseUrl : basePath + 'private/',
			
			dashboardUrl : basePath + 'private/',
			clientsBaseUrl : basePath + 'private/clients/',
			invoicesBaseUrl : basePath + 'private/invoices/',
			estimationsBaseUrl : basePath + 'private/estimations/',
			creditNotesBaseUrl : basePath + 'private/credit-notes/',
			transportDocumentsBaseUrl : basePath + 'private/transport-documents/',
			commoditiesBaseUrl : basePath + 'private/commodities/',
			paymentsBaseUrl : basePath + 'private/payments/',
			priceListsBaseUrl : basePath + 'private/price-lists/',

			partialsBaseUrl : basePath + 'novabill/partials/'
	};
	
	var baseConf = this.conf;
	
	this.url = {
			htmlFragmentUrl : function(path){ return baseConf.partialsBaseUrl + (path.charAt(0)=='/' ? path.substring(1) : path) + '?v=' + baseConf.version; }
	};
	
	// this is the full set of properties, available only after the module is loaded
	this.$get = function(){

		var baseConf = this.conf;
		
		return {

			conf : baseConf,

			exception : {
				VALIDATION : 'com.novadart.novabill.shared.client.exception.ValidationException'
			},

			validation : {
				NOT_UNIQUE : 'NOT_UNIQUE'
			},

			priceType : {
				FIXED : 'FIXED',
				DISCOUNT_PERCENT : 'DISCOUNT_PERCENT',
				DISCOUNT_FIXED : 'DISCOUNT_FIXED',
				OVERCHARGE_PERCENT : 'OVERCHARGE_PERCENT',
				OVERCHARGE_FIXED : 'OVERCHARGE_FIXED'
			},

			logRecord : {

				entityType : {
					CLIENT : 'CLIENT', 
					COMMODITY : 'COMMODITY', 
					INVOICE : 'INVOICE', 
					ESTIMATION : 'ESTIMATION', 
					CREDIT_NOTE : 'CREDIT_NOTE', 
					TRANSPORT_DOCUMENT : 'TRANSPORT_DOCUMENT',
					PAYMENT_TYPE : 'PAYMENT_TYPE',
					PRICE_LIST : 'PRICE_LIST'
				},

				operationType : {
					CREATE : 'CREATE', 
					UPDATE : 'UPDATE', 
					DELETE : 'DELETE', 
					SET_PAYED : 'SET_PAYED'
				}
			},

			months : ['JAN', 'FEB', 'MAR', 'APR', 'MAY', 'JUN', 'JUL', 'AUG', 'SEP', 'OCT', 'NOV', 'DEC'],

			url : {

				clientList : function(){ return baseConf.clientsBaseUrl + '#/'; },
				clientDetails : function(clientId){ return baseConf.clientsBaseUrl + '#/details/'+clientId; },

				creditNoteList : function(){ return baseConf.creditNotesBaseUrl + '#/'; },
				creditNoteNew : function(clientId){ return baseConf.creditNotesBaseUrl + '#/new/' + clientId; },
				creditNoteFromInvoice : function(invoiceId){ return baseConf.creditNotesBaseUrl + '#/from-invoice/' + invoiceId; },
				creditNoteDetails : function(creditNoteId){ return baseConf.creditNotesBaseUrl + '#/details/' + creditNoteId; },

				commodityList : function(){ return baseConf.commoditiesBaseUrl + '#/'; },
				commodityDetails : function(commodityId){ return baseConf.commoditiesBaseUrl + '#/details/' + commodityId; },

				estimationList : function(){ return baseConf.estimationsBaseUrl + '#/'; },
				estimationNew : function(clientId){ return baseConf.estimationsBaseUrl + '#/new/' + clientId; },
				estimationClone : function(clientId,estimationId){ return baseConf.estimationsBaseUrl + '#/new/' + clientId + '/clone/' + estimationId; },
				estimationDetails : function(estimationId){ return baseConf.estimationsBaseUrl + '#/details/' + estimationId; },

				invoiceList : function(){ return baseConf.invoicesBaseUrl + '#/'; },
				invoiceNew : function(clientId){ return baseConf.invoicesBaseUrl + '#/new/' + clientId; },
				invoiceClone : function(clientId,invoiceId){ return baseConf.invoicesBaseUrl + '#/new/' + clientId + '/clone/' + invoiceId; },
				invoiceDetails : function(invoiceId){ return baseConf.invoicesBaseUrl + '#/details/' + invoiceId; },
				invoiceFromEstimation : function(estimationId){ return baseConf.invoicesBaseUrl + '#/from-estimation/' + estimationId; },
				invoiceFromTransportDocument : function(transportDocumentId){ return baseConf.invoicesBaseUrl + '#/from-transport-document/' + transportDocumentId; },
				invoiceFromTransportDocumentList : function(transportDocumentList){ return baseConf.invoicesBaseUrl + '#/from-transport-document-list/' + transportDocumentList; },

				htmlFragmentUrl : this.url.htmlFragmentUrl,
				
				paymentList : function(){ return baseConf.paymentsBaseUrl + '#/'; },

				priceListList : function(){ return baseConf.priceListsBaseUrl + '#/'; },
				priceListDetails : function(priceListId){ return baseConf.priceListsBaseUrl + '#/details/' + priceListId; },

				transportDocumentList : function(){ return baseConf.transportDocumentsBaseUrl + '#/'; },
				transportDocumentNew : function(clientId){ return baseConf.transportDocumentsBaseUrl + '#/new/' + clientId; },
				transportDocumentDetails : function(transportDocumentId){ return baseConf.transportDocumentsBaseUrl + '#/details/' + transportDocumentId; }

			},

			events : {

				INVOICE_ADDED : 'INVOICE_ADDED',
				INVOICE_REMOVED : 'INVOICE_REMOVED',
				
				CREDIT_NOTE_ADDED : 'CREDIT_NOTE_ADDED',
				CREDIT_NOTE_REMOVED : 'CREDIT_NOTE_REMOVED',
				
				TRANSPORT_DOCUMENT_ADDED : 'TRANSPORT_DOCUMENT_ADDED',
				TRANSPORT_DOCUMENT_REMOVED : 'TRANSPORT_DOCUMENT_REMOVED',
				
				ESTIMATION_ADDED : 'ESTIMATION_ADDED',
				ESTIMATION_REMOVED : 'ESTIMATION_REMOVED',

				SHOW_TRANSPORT_DOCUMENTS_DIALOG : 'SHOW_TRANSPORT_DOCUMENTS_DIALOG',
				SHOW_REMOVAL_DIALOG : 'SHOW_REMOVAL_DIALOG',
				SHOW_EDIT_COMMODITY_DIALOG : 'SHOW_EDIT_COMMODITY_DIALOG'
			}

		};

	};

});