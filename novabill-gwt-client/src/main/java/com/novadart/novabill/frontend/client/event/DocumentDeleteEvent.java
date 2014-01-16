package com.novadart.novabill.frontend.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;

public class DocumentDeleteEvent extends GwtEvent<DocumentDeleteHandler> {

	public static final Type<DocumentDeleteHandler> TYPE = new Type<DocumentDeleteHandler>();
	private final AccountingDocumentDTO document;

	public DocumentDeleteEvent(AccountingDocumentDTO document) {
		this.document = document;
	}
	
	@Override
	public Type<DocumentDeleteHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DocumentDeleteHandler handler) {
		handler.onDocumentDelete(this);
	}
	
	public AccountingDocumentDTO getDocument() {
		return document;
	}

}
