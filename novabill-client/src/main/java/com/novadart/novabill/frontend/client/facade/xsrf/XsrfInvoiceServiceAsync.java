package com.novadart.novabill.frontend.client.facade.xsrf;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.facade.InvoiceService;
import com.novadart.novabill.shared.client.facade.InvoiceServiceAsync;

public class XsrfInvoiceServiceAsync extends XsrfProtectedService<InvoiceServiceAsync> implements InvoiceServiceAsync {

	public XsrfInvoiceServiceAsync() {
		super((InvoiceServiceAsync) GWT.create(InvoiceService.class));
	}

	@Override
	public void get(final long id, final AsyncCallback<InvoiceDTO> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<InvoiceServiceAsync>(callback) {

			@Override
			protected void performCall(InvoiceServiceAsync service) {
				service.get(id, callback);
			}

		});
	}

	@Override
	public void getAllInRange(final int start, final int length,
			final AsyncCallback<PageDTO<InvoiceDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<InvoiceServiceAsync>(callback) {

			@Override
			protected void performCall(InvoiceServiceAsync service) {
				service.getAllInRange(start, length, callback);
			}

		});
	}

	@Override
	public void update(final InvoiceDTO invoiceDTO, final AsyncCallback<Void> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<InvoiceServiceAsync>(callback) {

			@Override
			protected void performCall(InvoiceServiceAsync service) {
				service.update(invoiceDTO, callback);
			}

		});
	}

	@Override
	public void getAllForClient(final long id,
			final AsyncCallback<List<InvoiceDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<InvoiceServiceAsync>(callback) {

			@Override
			protected void performCall(InvoiceServiceAsync service) {
				service.getAllForClient(id, callback);
			}

		});
	}

	@Override
	public void add(final InvoiceDTO invoiceDTO, final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<InvoiceServiceAsync>(callback) {

			@Override
			protected void performCall(InvoiceServiceAsync service) {
				service.add(invoiceDTO, callback);
			}

		});
	}

	@Override
	public void getNextInvoiceDocumentID(final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<InvoiceServiceAsync>(callback) {

			@Override
			protected void performCall(InvoiceServiceAsync service) {
				service.getNextInvoiceDocumentID(callback);
			}

		});
	}

	@Override
	public void remove(final Long id, final AsyncCallback<Void> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<InvoiceServiceAsync>(callback) {

			@Override
			protected void performCall(InvoiceServiceAsync service) {
				service.remove(id, callback);
			}

		});
	}

	@Override
	public void getAllForClientInRange(final long id, final int start, final int length,
			final AsyncCallback<PageDTO<InvoiceDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<InvoiceServiceAsync>(callback) {

			@Override
			protected void performCall(InvoiceServiceAsync service) {
				service.getAllForClientInRange(id, start, length, callback);
			}

		});
	}

	@Override
	public void setPayed(final Long id, final Boolean value, final AsyncCallback<Void> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<InvoiceServiceAsync>(callback) {

			@Override
			protected void performCall(InvoiceServiceAsync service) {
				service.setPayed(id, value, callback);
			}

		});
	}
	
}
