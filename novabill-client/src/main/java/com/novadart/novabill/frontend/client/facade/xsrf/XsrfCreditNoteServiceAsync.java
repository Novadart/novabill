package com.novadart.novabill.frontend.client.facade.xsrf;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.facade.CreditNoteService;
import com.novadart.novabill.shared.client.facade.CreditNoteServiceAsync;

public class XsrfCreditNoteServiceAsync extends XsrfProtectedService<CreditNoteServiceAsync> implements CreditNoteServiceAsync {

	public XsrfCreditNoteServiceAsync() {
		super((CreditNoteServiceAsync) GWT.create(CreditNoteService.class));
	}

	@Override
	public void getNextInvoiceDocumentID(final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<CreditNoteServiceAsync>(callback) {

			@Override
			protected void performCall(CreditNoteServiceAsync service) {
				service.getNextInvoiceDocumentID(callback);
			}
		});
	}

	@Override
	public void get(final long id, final AsyncCallback<CreditNoteDTO> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<CreditNoteServiceAsync>(callback) {

			@Override
			protected void performCall(CreditNoteServiceAsync service) {
				service.get(id, callback);

			}
		});
	}

	@Override
	public void getAllInRange(final int start, final int length,
			final AsyncCallback<PageDTO<CreditNoteDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<CreditNoteServiceAsync>(callback) {

			@Override
			protected void performCall(CreditNoteServiceAsync service) {
				service.getAllInRange(start, length, callback);
			}
		});
	}

	@Override
	public void getAllForClient(final long id,
			final AsyncCallback<List<CreditNoteDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<CreditNoteServiceAsync>(callback) {

			@Override
			protected void performCall(CreditNoteServiceAsync service) {
				service.getAllForClient(id, callback);

			}
		});
	}

	@Override
	public void add(final CreditNoteDTO creditNoteDTO, final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<CreditNoteServiceAsync>(callback) {

			@Override
			protected void performCall(CreditNoteServiceAsync service) {
				service.add(creditNoteDTO, callback);
			}
		});
	}

	@Override
	public void getAllForClientInRange(final long id, final int start, final int length,
			final AsyncCallback<PageDTO<CreditNoteDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<CreditNoteServiceAsync>(callback) {
			
			@Override
			protected void performCall(CreditNoteServiceAsync service) {
				service.getAllForClientInRange(id, start, length, callback);
			}
		});
	}
	
	
	@Override
	public void remove(final Long id, final AsyncCallback<Void> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<CreditNoteServiceAsync>(callback) {
			
			@Override
			protected void performCall(CreditNoteServiceAsync service) {
				service.remove(id, callback);
			}
		});
	}
	
	
	@Override
	public void update(final CreditNoteDTO creditNoteDTO, final AsyncCallback<Void> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<CreditNoteServiceAsync>(callback) {
			
			@Override
			protected void performCall(CreditNoteServiceAsync service) {
				service.update(creditNoteDTO, callback);
			}
		});
	}
	
	
}
