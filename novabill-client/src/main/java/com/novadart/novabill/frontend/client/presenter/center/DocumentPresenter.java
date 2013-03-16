package com.novadart.novabill.frontend.client.presenter.center;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.presenter.AbstractPresenter;
import com.novadart.novabill.frontend.client.view.DocumentView;
import com.novadart.novabill.frontend.client.view.MainWidget;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.exception.ValidationException;

public abstract class DocumentPresenter<V extends DocumentView<?>> extends AbstractPresenter<V> implements DocumentView.Presenter {
	
	private static final DateTimeFormat YEAR_FORMAT = DateTimeFormat.getFormat("yyyy");
	
	private ClientDTO client;
	
	public DocumentPresenter(PlaceController placeController, EventBus eventBus, V view) {
		super(placeController, eventBus, view);
	}
	
	@Override
	public void go(AcceptsOneWidget panel) {
		MainWidget.getInstance().setLargeView();
		super.go(panel);
	}
	
	protected void handleServerValidationException(ValidationException ex){
		//		
	}
	
	protected ClientDTO getClient() {
		return client;
	}
	
	protected void setClient(ClientDTO client) {
		this.client = client;
	}
	
	public static DateTimeFormat getYearFormat() {
		return YEAR_FORMAT;
	}
}
