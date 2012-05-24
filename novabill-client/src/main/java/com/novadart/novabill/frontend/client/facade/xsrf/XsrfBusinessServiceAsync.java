package com.novadart.novabill.frontend.client.facade.xsrf;

import java.math.BigDecimal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.HasRpcToken;
import com.google.gwt.user.client.rpc.XsrfToken;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.facade.BusinessService;
import com.novadart.novabill.shared.client.facade.BusinessServiceAsync;

public class XsrfBusinessServiceAsync extends XsrfProtectedService implements
BusinessServiceAsync {

	private static final BusinessServiceAsync business = 
			(BusinessServiceAsync)GWT.create(BusinessService.class);

	@Override
	public void update(final BusinessDTO businessDTO, final AsyncCallback<Void> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate() {

			@Override
			public void performCall() {
				business.update(businessDTO, callback);
			}

			@Override
			public void manageException(final Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}

	@Override
	public void countClients(final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate() {

			@Override
			public void performCall() {
				business.countClients(callback);
			}

			@Override
			public void manageException(final Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}

	@Override
	public void countInvoices(final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate() {

			@Override
			public void performCall() {
				business.countInvoices(callback);
			}

			@Override
			public void manageException(final Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}

	@Override
	public void countInvoicesForYear(final int year, final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate() {

			@Override
			public void performCall() {
				business.countInvoicesForYear(year, callback);
			}

			@Override
			public void manageException(final Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}

	@Override
	public void getTotalAfterTaxesForYear(final int year,
			final AsyncCallback<BigDecimal> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate() {

			@Override
			public void performCall() {
				business.getTotalAfterTaxesForYear(year, callback);
			}

			@Override
			public void manageException(final Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}

	@Override
	public void getStats(final AsyncCallback<BusinessStatsDTO> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate() {

			@Override
			public void performCall() {
				business.getStats(callback);
			}

			@Override
			public void manageException(final Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}

	@Override
	protected void setXsrfToken(final XsrfToken token) {
		((HasRpcToken) business).setRpcToken(token);
	}

}
