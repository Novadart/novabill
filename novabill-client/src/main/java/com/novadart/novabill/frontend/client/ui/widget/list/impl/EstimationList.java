package com.novadart.novabill.frontend.client.ui.widget.list.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.novadart.novabill.frontend.client.datawatcher.DataWatcher;
import com.novadart.novabill.frontend.client.facade.AuthAwareAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.EstimationPlace;
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
			public void onOpenEstimationClicked(EstimationDTO estimation) {
				if(presenter != null){
					EstimationPlace ep = new EstimationPlace();
					ep.setEstimationId(estimation.getId());
					presenter.goTo(ep);
				}
			}
			
			@Override
			public void onPdfClicked(EstimationDTO estimation) {
				Window.open(GWT.getHostPageBaseURL()+"private/pdf/estimations/"+estimation.getId(), null, null);
			}

			@Override
			public void onDeleteClicked(EstimationDTO estimation) {
				if(Window.confirm(I18N.get.confirmEstimationDeletion())){
					ServerFacade.estimation.remove(estimation.getId(), new AuthAwareAsyncCallback<Void>() {
						
						@Override
						public void onSuccess(Void result) {
							DataWatcher.getInstance().fireEstimationEvent();
						}
						
						@Override
						public void onException(Throwable caught) {
							Window.confirm(I18N.get.errorServerCommunication());		
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
