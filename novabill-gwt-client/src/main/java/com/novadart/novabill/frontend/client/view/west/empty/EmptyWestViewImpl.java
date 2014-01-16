package com.novadart.novabill.frontend.client.view.west.empty;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class EmptyWestViewImpl extends Composite implements EmptyWestView  {

	private static WestViewImplUiBinder uiBinder = GWT
			.create(WestViewImplUiBinder.class);

	interface WestViewImplUiBinder extends UiBinder<Widget, EmptyWestViewImpl> {
	}


	public EmptyWestViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		setStyleName("EmptyWestView");
	}

	@Override
	public void setPresenter(Presenter presenter) {
	}

	@Override
	public void reset() {
	}

}
