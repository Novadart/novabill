package com.novadart.novabill.frontend.client.ui.widget.list.impl;

import com.novadart.novabill.frontend.client.datawatcher.DataWatcher;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.EstimationPlace;
import com.novadart.novabill.frontend.client.ui.View.Presenter;
import com.novadart.novabill.frontend.client.ui.widget.list.QuickViewList;
import com.novadart.novabill.frontend.client.ui.widget.notification.Notification;
import com.novadart.novabill.frontend.client.util.PDFUtils;
import com.novadart.novabill.shared.client.dto.EstimationDTO;

public class EstimationList extends QuickViewList<EstimationDTO> {

	private Presenter presenter;
	
	public EstimationList() {
		super(new EstimationCell());
		
		EstimationCell ic = (EstimationCell)getCellRenderer();
		ic.setHandler(new EstimationCell.Handler(){

			@Override
			public void onOpenEstimationClicked(EstimationDTO estimation) {
				if(presenter != null){
					EstimationPlace ep = new EstimationPlace();
					ep.setEstimationId(estimation.getId());
					presenter.goTo(ep);
				}
			}
			
			@Override
			public void onPdfClicked(EstimationDTO estimation) {
				if(estimation.getId() == null){
					return;
				}
				PDFUtils.generateEstimationPdf(estimation.getId());
			}

			@Override
			public void onDeleteClicked(EstimationDTO estimation) {
				if(Notification.showYesNoRequest(I18N.INSTANCE.confirmEstimationDeletion())){
					ServerFacade.estimation.remove(estimation.getId(), new WrappedAsyncCallback<Void>() {
						
						@Override
						public void onSuccess(Void result) {
							DataWatcher.getInstance().fireEstimationEvent();
						}
						
						@Override
						public void onException(Throwable caught) {
							Notification.showYesNoRequest(I18N.INSTANCE.errorServerCommunication());		
						}
					});
				}
				
			}
			
		});
		addStyleName("EstimationList");
	}
	
	
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
}
