package com.novadart.novabill.frontend.client.facade.xsrf;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.facade.BatchDataFetcherService;
import com.novadart.novabill.shared.client.facade.BatchDataFetcherServiceAsync;
import com.novadart.novabill.shared.client.tuple.Pair;
import com.novadart.novabill.shared.client.tuple.Triple;

public class XsrfBatchDataFetcherServiceAsync extends XsrfProtectedService<BatchDataFetcherServiceAsync> implements BatchDataFetcherServiceAsync {

	public XsrfBatchDataFetcherServiceAsync() {
		super((BatchDataFetcherServiceAsync)GWT.create(BatchDataFetcherService.class));
	}

	@Override
	public void fetchNewInvoiceForClientOpData(final Long clientID,
			final AsyncCallback<Pair<Long, ClientDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BatchDataFetcherServiceAsync>(callback) {

			@Override
			protected void performCall(BatchDataFetcherServiceAsync service) {
				service.fetchNewInvoiceForClientOpData(clientID, callback);
			}
		});
	}

	@Override
	public void fetchNewInvoiceFromEstimationOpData(final Long estimationID,
			final AsyncCallback<Pair<Long, EstimationDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BatchDataFetcherServiceAsync>(callback) {

			@Override
			protected void performCall(BatchDataFetcherServiceAsync service) {
				service.fetchNewInvoiceFromEstimationOpData(estimationID, callback);
			}
		});
	}

	@Override
	public void fetchNewInvoiceFromTransportDocumentOpData(
			final Long transportDocumentID,
			final AsyncCallback<Pair<Long, TransportDocumentDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BatchDataFetcherServiceAsync>(callback) {

			@Override
			protected void performCall(BatchDataFetcherServiceAsync service) {
				service.fetchNewInvoiceFromTransportDocumentOpData(transportDocumentID, callback);
			}
		});
	}

	@Override
	public void fetchCloneInvoiceOpData(final Long invoiceID, final Long clientID,
			final AsyncCallback<Triple<Long, ClientDTO, InvoiceDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BatchDataFetcherServiceAsync>(callback) {

			@Override
			protected void performCall(BatchDataFetcherServiceAsync service) {
				service.fetchCloneInvoiceOpData(invoiceID, clientID, callback);
			}
		});
	}

	@Override
	public void fetchNewEstimationForClientOpData(final Long clientID,
			final AsyncCallback<Pair<Long, ClientDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BatchDataFetcherServiceAsync>(callback) {

			@Override
			protected void performCall(BatchDataFetcherServiceAsync service) {
				service.fetchNewEstimationForClientOpData(clientID, callback);
			}
		});
	}

	@Override
	public void fetchCloneEstimationOpData(final Long invoiceID, final Long clientID,
			final AsyncCallback<Triple<Long, ClientDTO, EstimationDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BatchDataFetcherServiceAsync>(callback) {

			@Override
			protected void performCall(BatchDataFetcherServiceAsync service) {
				service.fetchCloneEstimationOpData(invoiceID, clientID, callback);
			}
		});
	}

	@Override
	public void fetchNewTransportDocumentForClientOpData(final Long clientID,
			final AsyncCallback<Pair<Long, ClientDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BatchDataFetcherServiceAsync>(callback) {

			@Override
			protected void performCall(BatchDataFetcherServiceAsync service) {
				service.fetchNewTransportDocumentForClientOpData(clientID, callback);
			}
		});
	}

	@Override
	public void fetchCloneTransportDocumentOpData(
			final Long transportDocID,
			final Long clientID,
			final AsyncCallback<Triple<Long, ClientDTO, TransportDocumentDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BatchDataFetcherServiceAsync>(callback) {

			@Override
			protected void performCall(BatchDataFetcherServiceAsync service) {
				service.fetchCloneTransportDocumentOpData(transportDocID, clientID, callback);
			}
		});
	}

	@Override
	public void fetchNewCreditNoteForClientOpData(final Long clientID,
			final AsyncCallback<Pair<Long, ClientDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BatchDataFetcherServiceAsync>(callback) {

			@Override
			protected void performCall(BatchDataFetcherServiceAsync service) {
				service.fetchNewCreditNoteForClientOpData(clientID, callback);
			}
		});
	}

	@Override
	public void fetchNewCreditNoteFromInvoiceOpData(final Long invoiceID,
			final AsyncCallback<Pair<Long, InvoiceDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BatchDataFetcherServiceAsync>(callback) {

			@Override
			protected void performCall(BatchDataFetcherServiceAsync service) {
				service.fetchNewCreditNoteFromInvoiceOpData(invoiceID, callback);
			}
		});
	}


}
