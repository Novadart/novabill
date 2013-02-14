package com.novadart.novabill.frontend.client.view.west.business;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class BusinessWestViewImpl extends Composite implements BusinessWestView {

	private static BusinessWestViewImplUiBinder uiBinder = GWT
			.create(BusinessWestViewImplUiBinder.class);

	interface BusinessWestViewImplUiBinder extends
			UiBinder<Widget, BusinessWestViewImpl> {
	}

	private Presenter presenter;
	
	public BusinessWestViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;	
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
