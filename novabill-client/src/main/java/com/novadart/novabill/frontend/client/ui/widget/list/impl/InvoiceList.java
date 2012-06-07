package com.novadart.novabill.frontend.client.ui.widget.list.impl;

import com.novadart.novabill.frontend.client.datawatcher.DataWatcher;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.InvoicePlace;
import com.novadart.novabill.frontend.client.ui.View.Presenter;
import com.novadart.novabill.frontend.client.ui.widget.list.QuickViewList;
import com.novadart.novabill.frontend.client.ui.widget.notification.Notification;
import com.novadart.novabill.frontend.client.util.PDFUtils;
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
				if(invoice.getId() == null){
					return;
				}
				PDFUtils.generateInvoicePdf(invoice.getId());
			}

			@Override
			public void onDeleteClicked(InvoiceDTO invoice) {
				if(Notification.showYesNoRequest(I18N.INSTANCE.confirmInvoiceDeletion())){
					ServerFacade.invoice.remove(invoice.getId(), new WrappedAsyncCallback<Void>() {
						
						@Override
						public void onSuccess(Void result) {
							DataWatcher.getInstance().fireInvoiceEvent();
							DataWatcher.getInstance().fireStatsEvent();
						}
						
						@Override
						public void onException(Throwable caught) {
							Notification.showYesNoRequest(I18N.INSTANCE.errorServerCommunication());		
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
