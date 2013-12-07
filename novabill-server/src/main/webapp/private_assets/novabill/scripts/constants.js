'use strict';

angular.module('novabill.constants', [])

.factory('nConstants', function() {
	return {

		exception : {
			VALIDATION : 'com.novadart.novabill.shared.client.exception.ValidationException'
		},

		validation : {
			NOT_UNIQUE : 'NOT_UNIQUE'
		},

		priceType : {
			DERIVED : 'DERIVED', 
			FIXED : 'FIXED'
		},

		logRecord : {
			
			entityType : {
				CLIENT : 'CLIENT', 
				COMMODITY : 'COMMODITY', 
				INVOICE : 'INVOICE', 
				ESTIMATION : 'ESTIMATION', 
				CREDIT_NOTE : 'CREDIT_NOTE', 
				TRANSPORT_DOCUMENT : 'TRANSPORT_DOCUMENT',
				PAYMENT_TYPE : 'PAYMENT_TYPE'
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
			
			clientList : function(){ return NovabillConf.clientsBaseUrl + '#/'; },
			clientDetails : function(clientId){ return NovabillConf.clientsBaseUrl + '#/details/'+clientId; },
			
			creditNoteList : function(){ return NovabillConf.creditNotesBaseUrl + '#/'; },
			creditNoteFromInvoice : function(invoiceId){ return NovabillConf.creditNotesBaseUrl + '#/from-invoice/' + invoiceId; },
			creditNoteDetails : function(creditNoteId){ return NovabillConf.creditNotesBaseUrl + '#/details/' + creditNoteId; },
			
			commodityList : function(){ return NovabillConf.commoditiesBaseUrl + '#/'; },
			commodityDetails : function(commodityId){ return NovabillConf.commoditiesBaseUrl + '#/details/' + commodityId; },
			
			estimationList : function(){ return NovabillConf.estimationsBaseUrl + '#/'; },
			estimationClone : function(clientId,estimationId){ return NovabillConf.estimationsBaseUrl + '#/new/' + clientId + '/clone/' + estimationId; },
			estimationDetails : function(estimationId){ return NovabillConf.estimationsBaseUrl + '#/details/' + estimationId; },
			
			invoiceList : function(){ return NovabillConf.invoicesBaseUrl + '#/'; },
			invoiceClone : function(clientId,invoiceId){ return NovabillConf.invoicesBaseUrl + '#/new/' + clientId + '/clone/' + invoiceId; },
			invoiceDetails : function(invoiceId){ return NovabillConf.invoicesBaseUrl + '#/details/' + invoiceId; },
			invoiceFromEstimation : function(estimationId){ return NovabillConf.invoicesBaseUrl + '#/from-estimation/' + estimationId; },
			invoiceFromTransportDocument : function(transportDocumentId){ return NovabillConf.invoicesBaseUrl + '#/from-transport-document/' + transportDocumentId; },
			
			paymentList : function(){ return NovabillConf.paymentsBaseUrl + '#/'; },
			
			trasportDocumentList : function(){ return NovabillConf.transportDocumentsBaseUrl + '#/'; },
			trasportDocumentDetails : function(transportDocumentId){ return NovabillConf.transportDocumentsBaseUrl + '#/details/' + transportDocumentId; },
			
		}
		
	};
});