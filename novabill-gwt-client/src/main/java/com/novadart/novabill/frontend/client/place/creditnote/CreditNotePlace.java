package com.novadart.novabill.frontend.client.place.creditnote;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.novadart.novabill.frontend.client.place.HistoryPrefix;
import com.novadart.novabill.frontend.client.place.HistoryUtils;

import java.util.Map;

public abstract class CreditNotePlace extends Place {
	
	public static String ARG_ID = "id";
	public static String ARG_CLIENT = "client";
	public static String ARG_INVOICE = "invoice";

	@Prefix(value=HistoryPrefix.CREDIT_NOTE)
	public static class Tokenizer implements PlaceTokenizer<CreditNotePlace> {

		@Override
		public CreditNotePlace getPlace(String token) {
			Map<String, String> args = HistoryUtils.parseArguments(token);

			//NOTE order is important!
			if(args.containsKey(ARG_ID)){

				Long creditNoteId = null;
				try{
					creditNoteId = Long.parseLong(args.get(ARG_ID));
				} catch(NumberFormatException e){
					return null;
				}

				ModifyCreditNotePlace p = new ModifyCreditNotePlace();
				p.setCreditNoteId(creditNoteId);
				return p;

			}  else if(args.containsKey(ARG_CLIENT)) {
				
				Long clientId = null;
				try{
					clientId = Long.parseLong(args.get(ARG_CLIENT));
				} catch(NumberFormatException e){
					return null;
				}
				
				NewCreditNotePlace p = new NewCreditNotePlace();
				p.setClientId(clientId);
				return p;
				
				
			} else if(args.containsKey(ARG_INVOICE)) {
				
				Long invoiceId = null;
				try{
					invoiceId = Long.parseLong(args.get(ARG_INVOICE));
				} catch(NumberFormatException e){
					return null;
				}
				
				FromInvoiceCreditNotePlace p = new FromInvoiceCreditNotePlace();
				p.setInvoiceId(invoiceId);
				return p;
				
			}
			
			return null;

		}

		@Override
		public String getToken(CreditNotePlace place) {
			
			if (place instanceof ModifyCreditNotePlace) {
				ModifyCreditNotePlace p = (ModifyCreditNotePlace) place;
				return HistoryUtils.serialize(ARG_ID, p.getCreditNoteId());
			}
			
			if (place instanceof FromInvoiceCreditNotePlace) {
				FromInvoiceCreditNotePlace p = (FromInvoiceCreditNotePlace) place;
				return HistoryUtils.serialize(ARG_INVOICE, p.getInvoiceId());
			}
			
			if (place instanceof NewCreditNotePlace) {
				NewCreditNotePlace p = (NewCreditNotePlace) place;
				return HistoryUtils.serialize(ARG_CLIENT, p.getClientId());
			}
			
			return null;
		}

	}

}
