package com.novadart.novabill.frontend.client.facade.xsrf;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.facade.TransportDocumentService;
import com.novadart.novabill.shared.client.facade.TransportDocumentServiceAsync;

public class XsrfTransportDocumentServiceAsync extends XsrfProtectedService<TransportDocumentServiceAsync> implements TransportDocumentServiceAsync {

	public XsrfTransportDocumentServiceAsync() {
		super((TransportDocumentServiceAsync) GWT.create(TransportDocumentService.class));
	}

	@Override
	public void get(final long id, final AsyncCallback<TransportDocumentDTO> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<TransportDocumentServiceAsync>(callback) {

			@Override
			protected void performCall(TransportDocumentServiceAsync service) {
				service.get(id, callback);
			}
		});
	}

	@Override
	public void getAllForClient(final long id,
			final AsyncCallback<List<TransportDocumentDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<TransportDocumentServiceAsync>(callback) {

			@Override
			protected void performCall(TransportDocumentServiceAsync service) {
				service.getAllForClient(id, callback);
			}
		});
	}

	@Override
	public void add(final TransportDocumentDTO transportDocDTO,
			final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<TransportDocumentServiceAsync>(callback) {

			@Override
			protected void performCall(TransportDocumentServiceAsync service) {
				service.add(transportDocDTO, callback);
			}
		});

	}

	@Override
	public void remove(final Long id, final AsyncCallback<Void> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<TransportDocumentServiceAsync>(callback) {

			@Override
			protected void performCall(TransportDocumentServiceAsync service) {
				service.remove(id, callback);
			}
		});

	}

	@Override
	public void update(final TransportDocumentDTO transportDocDTO,
			final AsyncCallback<Void> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<TransportDocumentServiceAsync>(callback) {

			@Override
			protected void performCall(TransportDocumentServiceAsync service) {
				service.update(transportDocDTO, callback);
			}
		});

	}

	@Override
	public void getNextTransportDocId(final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<TransportDocumentServiceAsync>(callback) {

			@Override
			protected void performCall(TransportDocumentServiceAsync service) {
				service.getNextTransportDocId(callback);
			}
		});

	}

	@Override
	public void getAllForClientInRange(final long id, final int start, final int length,
			final AsyncCallback<PageDTO<TransportDocumentDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<TransportDocumentServiceAsync>(callback) {

			@Override
			protected void performCall(TransportDocumentServiceAsync service) {
				service.getAllForClientInRange(id, start, length, callback);
			}
		});

	}

	@Override
	public void getAllInRange(final int start, final int length,
			final AsyncCallback<PageDTO<TransportDocumentDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<TransportDocumentServiceAsync>(callback) {

			@Override
			protected void performCall(TransportDocumentServiceAsync service) {
				service.getAllInRange(start, length, callback);
			}
		});

	}

}
