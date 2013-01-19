package com.novadart.novabill.frontend.client.facade.xsrf;

import java.math.BigDecimal;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.facade.BusinessService;
import com.novadart.novabill.shared.client.facade.BusinessServiceAsync;

public class XsrfBusinessServiceAsync extends XsrfProtectedService<BusinessServiceAsync> implements
BusinessServiceAsync {

	
	public XsrfBusinessServiceAsync() {
		super((BusinessServiceAsync)GWT.create(BusinessService.class));
	}
	
	
	@Override
	public void get(final Long businessID, final AsyncCallback<BusinessDTO> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BusinessServiceAsync>(callback) {

			@Override
			protected void performCall(BusinessServiceAsync service) {
				service.get(businessID, callback);
			}

		});
	}
	
	@Override
	public void updateNotesBitMask(final Long notesBitMask, final AsyncCallback<Void> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BusinessServiceAsync>(callback) {

			@Override
			protected void performCall(BusinessServiceAsync service) {
				service.updateNotesBitMask(notesBitMask, callback);
			}

		});
	}
	
	@Override
	public void update(final BusinessDTO businessDTO, final AsyncCallback<Void> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BusinessServiceAsync>(callback) {

			@Override
			protected void performCall(BusinessServiceAsync service) {
				service.update(businessDTO, callback);
			}

		});
	}

	@Override
	public void generateExportToken(AsyncCallback<String> callback) {
		getService().generateExportToken(callback);
	}
	
	@Override
	public void generatePDFToken(AsyncCallback<String> callback) {
		getService().generatePDFToken(callback);
	}

	@Override
	public void countClients(final Long businessID, final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BusinessServiceAsync>(callback) {

			@Override
			protected void performCall(BusinessServiceAsync service) {
				service.countClients(businessID, callback);
			}

		});
	}
	
	@Override
	public void countInvoices(final Long businessID, final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BusinessServiceAsync>(callback) {

			@Override
			protected void performCall(BusinessServiceAsync service) {
				service.countInvoices(businessID, callback);
			}

		});
	}
	
	@Override
	public void countInvoicesForYear(final Long BusinessID, final Integer year,
			final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BusinessServiceAsync>(callback) {

			@Override
			protected void performCall(BusinessServiceAsync service) {
				service.countInvoicesForYear(BusinessID, year, callback);
			}

		});
	}
	
	@Override
	public void getStats(final Long businessID,
			final AsyncCallback<BusinessStatsDTO> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BusinessServiceAsync>(callback) {

			@Override
			protected void performCall(BusinessServiceAsync service) {
				service.getStats(businessID, callback);
			}

		});
	}
	
	@Override
	public void getTotalAfterTaxesForYear(final Long businessID, final Integer year,
			final AsyncCallback<BigDecimal> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BusinessServiceAsync>(callback) {

			@Override
			protected void performCall(BusinessServiceAsync service) {
				service.getTotalAfterTaxesForYear(businessID, year, callback);
			}

		});
	}

	@Override
	public void getCreditNotes(final Long businessID,
			final AsyncCallback<List<CreditNoteDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BusinessServiceAsync>(callback) {

			@Override
			protected void performCall(BusinessServiceAsync service) {
				service.getCreditNotes(businessID, callback);
			}

		});
		
	}

	@Override
	public void getEstimations(final Long businessID,
			final AsyncCallback<List<EstimationDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BusinessServiceAsync>(callback) {

			@Override
			protected void performCall(BusinessServiceAsync service) {
				service.getEstimations(businessID, callback);
			}

		});
	}

	@Override
	public void getInvoices(final Long businessID,
			final AsyncCallback<List<InvoiceDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BusinessServiceAsync>(callback) {

			@Override
			protected void performCall(BusinessServiceAsync service) {
				service.getInvoices(businessID, callback);
			}

		});
	}

	@Override
	public void getTransportDocuments(final Long businessID,
			final AsyncCallback<List<TransportDocumentDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BusinessServiceAsync>(callback) {

			@Override
			protected void performCall(BusinessServiceAsync service) {
				service.getTransportDocuments(businessID, callback);
			}

		});
	}

	@Override
	public void getClients(final Long businessID,
			final AsyncCallback<List<ClientDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BusinessServiceAsync>(callback) {

			@Override
			protected void performCall(BusinessServiceAsync service) {
				service.getClients(businessID, callback);
			}

		});
	}
	
}
