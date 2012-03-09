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
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class InvoiceList extends QuickViewList<InvoiceDTO> {

	private Presenter presenter;
	
	public InvoiceList() {
		super(new InvoiceCell());
		
		InvoiceCell ic = (InvoiceCell)getCellRenderer();
		ic.setHandler(new InvoiceCell.Handler(){

			@Override
			public void onOpenInvoiceClicked(InvoiceDTO invoice) {
				if(presenter != null){
					InvoicePlace ip = new InvoicePlace();
					ip.setInvoiceId(invoice.getId());
					presenter.goTo(ip);
				}
			}
			
			@Override
			public void onPdfClicked(InvoiceDTO invoice) {
				Window.open(GWT.getHostPageBaseURL()+"private/invoices/"+invoice.getId(), null, null);
			}

			@Override
			public void onDeleteClicked(InvoiceDTO invoice) {
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
