package com.novadart.novabill.frontend.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class InvoicePlace extends Place {

	public static class Tokenizer implements PlaceTokenizer<InvoicePlace> {

		@Override
		public InvoicePlace getPlace(String token) {
			Long invoiceId = -1L;
			try {
				invoiceId = Long.parseLong(token);
			} catch (NumberFormatException e) {
			}
			
			InvoicePlace ip = new InvoicePlace();
			ip.setInvoiceId(invoiceId);
			return ip;
		}

		@Override
		public String getToken(InvoicePlace place) {
			return String.valueOf(place.getInvoiceId());
		}
		
	}

	private Long invoiceId;
	private ClientDTO client;
	private Long invoiceProgressiveId;
	
	public InvoicePlace() {
		setInvoiceId(0L);
	}

	public void setDataForNewInvoice(ClientDTO client, Long invoiceProgressiveId) {
		this.client = client;
		this.invoiceProgressiveId = invoiceProgressiveId;
	}
	
	public ClientDTO getClient() {
		return client;
	}

	public Long getInvoiceProgressiveId() {
		return invoiceProgressiveId;
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}


}
