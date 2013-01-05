package com.novadart.novabill.frontend.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;

public class DocumentAddEvent extends GwtEvent<DocumentAddHandler> {

	public static final Type<DocumentAddHandler> TYPE = new Type<DocumentAddHandler>();
	private final AccountingDocumentDTO document;

	public DocumentAddEvent(AccountingDocumentDTO document) {
		this.document = document;
	}
	
	@Override
	public Type<DocumentAddHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DocumentAddHandler handler) {
		handler.onDocumentAdd(this);
	}
	
	public AccountingDocumentDTO getDocument() {
		return document;
	}

}
