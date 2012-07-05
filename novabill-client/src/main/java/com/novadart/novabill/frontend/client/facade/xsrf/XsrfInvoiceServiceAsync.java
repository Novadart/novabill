package com.novadart.novabill.frontend.client.facade.xsrf;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.HasRpcToken;
import com.google.gwt.user.client.rpc.XsrfToken;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.facade.InvoiceService;
import com.novadart.novabill.shared.client.facade.InvoiceServiceAsync;

public class XsrfInvoiceServiceAsync extends XsrfProtectedService implements InvoiceServiceAsync {

	private static final InvoiceServiceAsync invoice = 
			(InvoiceServiceAsync)GWT.create(InvoiceService.class);


	@Override
	protected void setXsrfToken(final XsrfToken token) {
		((HasRpcToken) invoice).setRpcToken(token);
	}

	@Override
	public void get(final long id, final AsyncCallback<InvoiceDTO> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate() {

			@Override
			public void performCall() {
				invoice.get(id, callback);
			}

			@Override
			public void manageException(final Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}

	@Override
	public void getAllInRange(final int start, final int length,
			final AsyncCallback<List<InvoiceDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate() {

			@Override
			public void performCall() {
				invoice.getAllInRange(start, length, callback);
			}

			@Override
			public void manageException(final Throwable caught) {
				callback.onFailure(caught);
			}
		});

	}

	@Override
	public void update(final InvoiceDTO invoiceDTO, final AsyncCallback<Void> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate() {

			@Override
			public void performCall() {
				invoice.update(invoiceDTO, callback);
			}

			@Override
			public void manageException(final Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}

	@Override
	public void getAllForClient(final long id,
			final AsyncCallback<List<InvoiceDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate() {

			@Override
			public void performCall() {
				invoice.getAllForClient(id, callback);
			}

			@Override
			public void manageException(final Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}

	@Override
	public void add(final InvoiceDTO invoiceDTO, final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate() {

			@Override
			public void performCall() {
				invoice.add(invoiceDTO, callback);
			}

			@Override
			public void manageException(final Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}

	@Override
	public void getNextInvoiceDocumentID(final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate() {

			@Override
			public void performCall() {
				invoice.getNextInvoiceDocumentID(callback);
			}

			@Override
			public void manageException(final Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}

	@Override
	public void remove(final Long id, final AsyncCallback<Void> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate() {

			@Override
			public void performCall() {
				invoice.remove(id, callback);
			}

			@Override
			public void manageException(final Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}

	@Override
	public void getAllForClientInRange(final long id, final int start, final int length,
			final AsyncCallback<List<InvoiceDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate() {

			@Override
			public void performCall() {
				invoice.getAllForClientInRange(id, start, length, callback);
			}

			@Override
			public void manageException(final Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}
	
	@Override
	public void createFromEstimation(final InvoiceDTO invoiceDTO, final Long estimationID,
			final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate() {

			@Override
			public void performCall() {
				invoice.createFromEstimation(invoiceDTO, estimationID, callback);
			}

			@Override
			public void manageException(final Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}

	@Override
	public void setPayed(final Long id, final Boolean value, final AsyncCallback<Void> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate() {

			@Override
			public void performCall() {
				invoice.setPayed(id, value, callback);
			}

			@Override
			public void manageException(final Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}
	
	@Override
	public void generatePDFToken(final AsyncCallback<String> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate() {

			@Override
			public void performCall() {
				invoice.generatePDFToken(callback);
			}

			@Override
			public void manageException(final Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}

}
