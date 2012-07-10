package com.novadart.novabill.frontend.client.facade.xsrf;

import java.math.BigDecimal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.facade.BusinessService;
import com.novadart.novabill.shared.client.facade.BusinessServiceAsync;

public class XsrfBusinessServiceAsync extends XsrfProtectedService<BusinessServiceAsync> implements
BusinessServiceAsync {

	
	public XsrfBusinessServiceAsync() {
		super((BusinessServiceAsync)GWT.create(BusinessService.class));
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
	public void countClients(final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BusinessServiceAsync>(callback) {

			@Override
			protected void performCall(BusinessServiceAsync service) {
				service.countClients(callback);
			}

		});
	}

	@Override
	public void countInvoices(final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BusinessServiceAsync>(callback) {

			@Override
			protected void performCall(BusinessServiceAsync service) {
				service.countInvoices(callback);
			}
			
		});
	}

	@Override
	public void countInvoicesForYear(final int year, final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BusinessServiceAsync>(callback) {

			@Override
			protected void performCall(BusinessServiceAsync service) {
				service.countInvoicesForYear(year, callback);
			}

		});
	}

	@Override
	public void getTotalAfterTaxesForYear(final int year,
			final AsyncCallback<BigDecimal> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BusinessServiceAsync>(callback) {

			@Override
			protected void performCall(BusinessServiceAsync service) {
				service.getTotalAfterTaxesForYear(year, callback);
			}

		});
	}

	@Override
	public void getStats(final AsyncCallback<BusinessStatsDTO> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<BusinessServiceAsync>(callback) {

			@Override
			protected void performCall(BusinessServiceAsync service) {
				service.getStats(callback);
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

}
