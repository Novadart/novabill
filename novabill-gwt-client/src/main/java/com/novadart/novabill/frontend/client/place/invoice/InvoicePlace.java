package com.novadart.novabill.frontend.client.place.invoice;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.novadart.novabill.frontend.client.place.HistoryPrefix;
import com.novadart.novabill.frontend.client.place.HistoryUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class InvoicePlace extends Place {
	
	public static String ARG_ID = "id";
	public static String ARG_CLONE = "clone";
	public static String ARG_CLIENT = "client";
	public static String ARG_ESTIMATION = "estimation";
	public static String ARG_TRANSPORT_DOCUMENT = "transport_doc";

	@Prefix(value=HistoryPrefix.INVOICE)
	public static class Tokenizer implements PlaceTokenizer<InvoicePlace> {

		@Override
		public InvoicePlace getPlace(String token) {
			Map<String, String> args = HistoryUtils.parseArguments(token);
			
			//NOTE order is important!
			if(args.containsKey(ARG_ID)){
				
				Long invoiceId = null;
				try{
					invoiceId = Long.parseLong(args.get(ARG_ID));
				} catch(NumberFormatException e){
					return null;
				}
				
				ModifyInvoicePlace mip = new ModifyInvoicePlace();
				mip.setInvoiceId(invoiceId);
				return mip;
				
			}  else if(args.containsKey(ARG_CLONE)) {
			
				Long invoiceId = null;
				Long clientId = null;
				try{
					invoiceId = Long.parseLong(args.get(ARG_CLONE));
					clientId = Long.parseLong(args.get(ARG_CLIENT));
				} catch(NumberFormatException e){
					return null;
				}
				
				CloneInvoicePlace cip = new CloneInvoicePlace();
				cip.setInvoiceId(invoiceId);
				cip.setClientId(clientId);
				return cip;
				
				
			} else if(args.containsKey(ARG_CLIENT)) {
				
				Long clientId = null;
				try{
					clientId = Long.parseLong(args.get(ARG_CLIENT));
				} catch(NumberFormatException e){
					return null;
				}
				
				NewInvoicePlace nip = new NewInvoicePlace();
				nip.setClientId(clientId);
				return nip;
				
				
			} else if(args.containsKey(ARG_ESTIMATION)) {
				
				Long estimationId = null;
				try{
					estimationId = Long.parseLong(args.get(ARG_ESTIMATION));
				} catch(NumberFormatException e){
					return null;
				}
				
				FromEstimationInvoicePlace feip = new FromEstimationInvoicePlace();
				feip.setEstimationId(estimationId);
				return feip;
				
			} 
//			else if(args.containsKey(ARG_TRANSPORT_DOCUMENT)) {
//				
//				Long tdocId = null;
//				try{
//					tdocId = Long.parseLong(args.get(ARG_TRANSPORT_DOCUMENT));
//				} catch(NumberFormatException e){
//					return null;
//				}
//				
//				FromTransportDocumentInvoicePlace ftdip = new FromTransportDocumentInvoicePlace();
//				ftdip.setTransportDocumentId(tdocId);
//				return ftdip;
//			} 
//			
			return null;
			
		}

		@Override
		public String getToken(InvoicePlace place) {
			if (place instanceof ModifyInvoicePlace) {
				ModifyInvoicePlace p = (ModifyInvoicePlace) place;
				return HistoryUtils.serialize(ARG_ID, p.getInvoiceId());
			}
			
			if (place instanceof CloneInvoicePlace) {
				CloneInvoicePlace p = (CloneInvoicePlace) place;
				Map<String, String> args = new HashMap<String, String>();
				args.put(ARG_CLONE, String.valueOf(p.getInvoiceId()));
				args.put(ARG_CLIENT, String.valueOf(p.getClientId()));
				return HistoryUtils.serialize(args);
			}
			
			if (place instanceof FromEstimationInvoicePlace) {
				FromEstimationInvoicePlace p = (FromEstimationInvoicePlace) place;
				return HistoryUtils.serialize(ARG_ESTIMATION, p.getEstimationId());
			}
			
//			if (place instanceof FromTransportDocumentInvoicePlace) {
//				FromTransportDocumentInvoicePlace p = (FromTransportDocumentInvoicePlace) place;
//				return HistoryUtils.serialize(ARG_TRANSPORT_DOCUMENT, p.getTransportDocumentId());
//			}
			
			if (place instanceof NewInvoicePlace) {
				NewInvoicePlace p = (NewInvoicePlace) place;
				return HistoryUtils.serialize(ARG_CLIENT, p.getClientId());
			}
			
			return null;
		}
		
	}

}
