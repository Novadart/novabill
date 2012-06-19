package com.novadart.novabill.frontend.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class InvoicePlace extends Place {

	@Prefix(value=HistoryPrefix.INVOICE)
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
	private InvoiceDTO invoiceToClone;
	private EstimationDTO estimationSource;
	
	public InvoicePlace() {
		setInvoiceId(0L);
	}

	public void setDataForNewInvoice(ClientDTO client, Long invoiceProgressiveId) {
		this.client = client;
		this.invoiceProgressiveId = invoiceProgressiveId;
	}
	
	public void setDataForNewInvoice(ClientDTO client, Long invoiceProgressiveId, InvoiceDTO invoiceToClone) {
		this.client = client;
		this.invoiceProgressiveId = invoiceProgressiveId;
		this.invoiceToClone = invoiceToClone;
	}
	
	public void setDataForNewInvoice(Long invoiceProgressiveId, EstimationDTO estimationSource) {
		this.client = estimationSource.getClient();
		this.invoiceProgressiveId = invoiceProgressiveId;
		this.estimationSource = estimationSource;
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

	public InvoiceDTO getInvoiceToClone() {
		return invoiceToClone;
	}

	public void setInvoiceToClone(InvoiceDTO invoiceToClone) {
		this.invoiceToClone = invoiceToClone;
	}

	public EstimationDTO getEstimationSource() {
		return estimationSource;
	}

	public void setEstimationSource(EstimationDTO estimationSource) {
		this.estimationSource = estimationSource;
	}

}
