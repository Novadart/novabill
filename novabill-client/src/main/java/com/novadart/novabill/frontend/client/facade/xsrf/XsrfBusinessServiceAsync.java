package com.novadart.novabill.frontend.client.facade.xsrf;

import java.math.BigDecimal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.HasRpcToken;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.facade.BusinessService;
import com.novadart.novabill.shared.client.facade.BusinessServiceAsync;

public class XsrfBusinessServiceAsync extends XsrfProtectedService implements
BusinessServiceAsync {

	private static final BusinessServiceAsync business = 
			(BusinessServiceAsync)GWT.create(BusinessService.class);

	
	public XsrfBusinessServiceAsync() {
		super((HasRpcToken) business);
	}
	
	@Override
	public void update(final BusinessDTO businessDTO, final AsyncCallback<Void> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate(callback) {

			@Override
			protected void performCall() {
				business.update(businessDTO, callback);
			}

		});
	}

	@Override
	public void countClients(final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate(callback) {

			@Override
			protected void performCall() {
				business.countClients(callback);
			}

		});
	}

	@Override
	public void countInvoices(final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate(callback) {

			@Override
			protected void performCall() {
				business.countInvoices(callback);
			}
			
		});
	}

	@Override
	public void countInvoicesForYear(final int year, final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate(callback) {

			@Override
			protected void performCall() {
				business.countInvoicesForYear(year, callback);
			}

		});
	}

	@Override
	public void getTotalAfterTaxesForYear(final int year,
			final AsyncCallback<BigDecimal> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate(callback) {

			@Override
			protected void performCall() {
				business.getTotalAfterTaxesForYear(year, callback);
			}

		});
	}

	@Override
	public void getStats(final AsyncCallback<BusinessStatsDTO> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate(callback) {

			@Override
			protected void performCall() {
				business.getStats(callback);
			}

		});
	}
	
	@Override
	public void generateExportToken(AsyncCallback<String> callback) {
		business.generateExportToken(callback);
	}
	
	@Override
	public void generatePDFToken(AsyncCallback<String> callback) {
		business.generatePDFToken(callback);
	}

}
