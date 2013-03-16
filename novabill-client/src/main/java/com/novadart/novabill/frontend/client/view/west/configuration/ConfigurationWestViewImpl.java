package com.novadart.novabill.frontend.client.view.west.configuration;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.HistoryPrefix;

public class ConfigurationWestViewImpl extends Composite implements ConfigurationWestView {

	private static ConfigurationWestViewImplUiBinder uiBinder = GWT
			.create(ConfigurationWestViewImplUiBinder.class);

	interface ConfigurationWestViewImplUiBinder extends
			UiBinder<Widget, ConfigurationWestViewImpl> {
	}
	
	public static interface Style extends CssResource {
		String selectedMenuItem();
		String menu();
		String menuItem();
	}
	
	@UiField Hyperlink general;
	@UiField Hyperlink payment;
	
	@UiField Style style;
	
	private Presenter presenter;

	public ConfigurationWestViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		general.setTargetHistoryToken(HistoryPrefix.BUSINESS+":");
		payment.setTargetHistoryToken(HistoryPrefix.PAYMENT+":");
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		this.presenter.onLoad();
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void reset() {
		general.removeStyleName(style.selectedMenuItem());
		payment.removeStyleName(style.selectedMenuItem());
	}

	@Override
	public Hyperlink getGeneral() {
		return general;
	}
	
	@Override
	public Hyperlink getPayment() {
		return payment;
	}
	
	@Override
	public Style getStyle() {
		return style;
	}
	
	@UiFactory
	I18N getI18n(){
		return I18N.INSTANCE;
	}

}
