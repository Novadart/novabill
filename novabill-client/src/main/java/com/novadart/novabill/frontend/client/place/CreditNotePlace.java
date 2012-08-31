package com.novadart.novabill.frontend.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class CreditNotePlace extends Place {

	@Prefix(value=HistoryPrefix.CREDIT_NOTE)
	public static class Tokenizer implements PlaceTokenizer<CreditNotePlace> {

		@Override
		public CreditNotePlace getPlace(String token) {
			Long creditNoteId = -1L;
			try {
				creditNoteId = Long.parseLong(token);
			} catch (NumberFormatException e) {
			}
			
			CreditNotePlace ip = new CreditNotePlace();
			ip.setCreditNoteId(creditNoteId);
			return ip;
		}

		@Override
		public String getToken(CreditNotePlace place) {
			return String.valueOf(place.getCreditNoteId());
		}
		
	}

	private Long creditNoteId;
	private ClientDTO client;
	private Long creditNoteProgressiveId;
	private InvoiceDTO sourceInvoice;
	
	public CreditNotePlace() {
		setCreditNoteId(0L);
	}

	public void setDataForNewCreditNote(ClientDTO client, Long creditoNoteProgressiveId) {
		this.client = client;
		this.creditNoteProgressiveId = creditoNoteProgressiveId;
	}
	
	public void setDataForNewCreditNote(Long creditoNoteProgressiveId, InvoiceDTO invoice) {
		this.client = invoice.getClient();
		this.creditNoteProgressiveId = creditoNoteProgressiveId;
		this.sourceInvoice = invoice;
	}
	
	public ClientDTO getClient() {
		return client;
	}

	public Long getCreditNoteId() {
		return creditNoteId;
	}

	public void setCreditNoteId(Long creditNoteId) {
		this.creditNoteId = creditNoteId;
	}

	public Long getCreditNoteProgressiveId() {
		return creditNoteProgressiveId;
	}

	public InvoiceDTO getSourceInvoice() {
		return sourceInvoice;
	}

	public void setSourceInvoice(InvoiceDTO sourceInvoice) {
		this.sourceInvoice = sourceInvoice;
	}

	public void setClient(ClientDTO client) {
		this.client = client;
	}

}
