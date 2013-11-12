package com.novadart.novabill.frontend.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;

public class DocumentUpdateEvent extends GwtEvent<DocumentUpdateHandler> {

	public static final Type<DocumentUpdateHandler> TYPE = new Type<DocumentUpdateHandler>();
	private final AccountingDocumentDTO document;

	public DocumentUpdateEvent(AccountingDocumentDTO document) {
		this.document = document;
	}
	
	@Override
	public Type<DocumentUpdateHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DocumentUpdateHandler handler) {
		handler.onDocumentUpdate(this);
	}
	
	public AccountingDocumentDTO getDocument() {
		return document;
	}

}
