package com.novadart.novabill.frontend.client.view.west.configuration;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;

public class ConfigurationWestViewImpl extends Composite implements ConfigurationWestView {

	private static ConfigurationWestViewImplUiBinder uiBinder = GWT
			.create(ConfigurationWestViewImplUiBinder.class);

	interface ConfigurationWestViewImplUiBinder extends
			UiBinder<Widget, ConfigurationWestViewImpl> {
	}
	
	@UiField Hyperlink general;
	@UiField Hyperlink payment;

	private Presenter presenter;
	
	public ConfigurationWestViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		presenter.onLoad();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void reset() {
	}

	@Override
	public Hyperlink getGeneral() {
		return general;
	}
	
	@Override
	public Hyperlink getPayment() {
		return payment;
	}
	
}
