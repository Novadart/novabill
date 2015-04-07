package com.novadart.novabill.service.export.data;

import java.util.Set;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.CreditNote;
import com.novadart.novabill.domain.Estimation;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.Logo;
import com.novadart.novabill.domain.TransportDocument;

public class ExportDataBundle {

	private Set<Invoice> invoices;
	
	private Set<Estimation> estimations;
	
	private Set<CreditNote> creditNotes;
	
	private Set<TransportDocument> transportDocuments;
	
	private Set<Client> clients;
	
	private Business business;
	
	private Logo logo;

	public ExportDataBundle(){}
	
	public ExportDataBundle(Set<Invoice> invoices, Set<Estimation> estimations,
			Set<CreditNote> creditNotes,
			Set<TransportDocument> transportDocuments, Set<Client> clients,
			Business business,
			Logo logo) {
		this.invoices = invoices;
		this.estimations = estimations;
		this.creditNotes = creditNotes;
		this.transportDocuments = transportDocuments;
		this.clients = clients;
		this.business = business;
		this.logo = logo;
	}



	public Set<Invoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(Set<Invoice> invoices) {
		this.invoices = invoices;
	}

	public Set<Estimation> getEstimations() {
		return estimations;
	}

	public void setEstimations(Set<Estimation> estimations) {
		this.estimations = estimations;
	}

	public Set<CreditNote> getCreditNotes() {
		return creditNotes;
	}

	public void setCreditNotes(Set<CreditNote> creditNotes) {
		this.creditNotes = creditNotes;
	}

	public Set<TransportDocument> getTransportDocuments() {
		return transportDocuments;
	}

	public void setTransportDocuments(Set<TransportDocument> transportDocuments) {
		this.transportDocuments = transportDocuments;
	}

	public Set<Client> getClients() {
		return clients;
	}

	public void setClients(Set<Client> clients) {
		this.clients = clients;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public Logo getLogo() {
		return logo;
	}

	public void setLogo(Logo logo) {
		this.logo = logo;
	}
	
	
	
}
