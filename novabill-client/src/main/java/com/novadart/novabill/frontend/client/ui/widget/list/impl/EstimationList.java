package com.novadart.novabill.frontend.client.ui.widget.list.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.novadart.novabill.frontend.client.datawatcher.DataWatcher;
import com.novadart.novabill.frontend.client.facade.AuthAwareAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.InvoicePlace;
import com.novadart.novabill.frontend.client.ui.View.Presenter;
import com.novadart.novabill.frontend.client.ui.widget.list.QuickViewList;
import com.novadart.novabill.shared.client.dto.EstimationDTO;

public class EstimationList extends QuickViewList<EstimationDTO> {

	private Presenter presenter;
	
	public EstimationList() {
		super(new EstimationCell());
		
		EstimationCell ic = (EstimationCell)getCellRenderer();
		ic.setHandler(new EstimationCell.Handler(){

			@Override
			public void onOpenInvoiceClicked(EstimationDTO invoice) {
				if(presenter != null){
					InvoicePlace ip = new InvoicePlace();
					ip.setInvoiceId(invoice.getId());
					presenter.goTo(ip);
				}
			}
			
			@Override
			public void onPdfClicked(EstimationDTO invoice) {
				Window.open(GWT.getHostPageBaseURL()+"private/invoices/"+invoice.getId(), null, null);
			}

			@Override
			public void onDeleteClicked(EstimationDTO invoice) {
				if(Window.confirm(I18N.get.confirmInvoiceDeletion())){
					ServerFacade.invoice.remove(invoice.getId(), new AuthAwareAsyncCallback<Void>() {
						
						@Override
						public void onSuccess(Void result) {
							DataWatcher.getInstance().fireInvoiceEvent();
							DataWatcher.getInstance().fireStatsEvent();
						}
						
						@Override
						public void onException(Throwable caught) {
							Window.confirm(I18N.get.errorServerCommunication());		
						}
					});
				}
				
			}
			
		});
		addStyleName("InvoiceList");
	}
	
	
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
}
