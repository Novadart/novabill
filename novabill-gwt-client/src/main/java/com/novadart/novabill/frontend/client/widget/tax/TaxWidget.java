package com.novadart.novabill.frontend.client.widget.tax;

import java.math.BigDecimal;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.gwtshared.client.validation.ValidationBundle;
import com.novadart.gwtshared.client.validation.widget.ValidatedWidget;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.resources.ImageResources;
import com.novadart.novabill.frontend.client.util.DocumentUtils;

public class TaxWidget extends ValidatedWidget<BigDecimal> implements HasEnabled {

	private static final String FLAG_VALUE = "_other_";
	
	private static final ValidationBundle<BigDecimal> VALIDATOR = new ValidationBundle<BigDecimal>() {

		@Override
		public boolean isValid(BigDecimal value) {
			return value != null;
		}

		@Override
		public String getErrorMessage() {
			return I18N.INSTANCE.numberValidationError();
		}
	};

	private final SimplePanel body = new SimplePanel();
	private final TextBox customTaxBox = new TextBox();
	private final ListBox listBox = buildListBox();
	private final Widget customTax = buildCustomTax();
	

	
	public TaxWidget() {
		super(GlobalBundle.INSTANCE.validatedWidget(), VALIDATOR);
		initWidget(body);
		listBox.setSelectedIndex(0);
		body.setWidget(listBox);
	}
	

	private ListBox buildListBox(){
		final ListBox listBox = new ListBox();
		for (String item : I18N.INSTANCE.vatItems()) {
			listBox.addItem(item+"%", item);
		}
		listBox.addItem(I18N.INSTANCE.other()+"...", FLAG_VALUE);
		listBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				if(listBox.getValue(listBox.getSelectedIndex()).equals(FLAG_VALUE)){
					body.setWidget(customTax);
				}
			}
		});
		return listBox;
	}

	private Widget buildCustomTax(){
		HorizontalPanel container = new HorizontalPanel();
		Image clear = new Image(ImageResources.INSTANCE.clear_left());
		container.add(customTaxBox);
		container.add(clear);
		clear.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				reset();
			}
		});
		return container;
	}

	@Override
	public void setValue(BigDecimal value) {
		body.setWidget(customTax);
		customTaxBox.setValue(NumberFormat.getDecimalFormat().format(value));
	}

	@Override
	public BigDecimal getValue() {
		if(body.getWidget().equals(customTax)){
			try{
				BigDecimal tax = DocumentUtils.parseValue(customTaxBox.getValue());
				return tax;
			} catch (NumberFormatException e) {
				return null;
			}
			
		} else {
			return new BigDecimal(Double.parseDouble(listBox.getValue(listBox.getSelectedIndex())));
		}
	}

	@Override
	protected void updateUI(boolean isValid) {}

	@Override
	protected void resetUI() {
		listBox.setSelectedIndex(0);
		body.setWidget(listBox);
		customTaxBox.setText("");
	}

	@Override
	public boolean isEmpty() {
		if(body.getWidget().equals(customTax)){
			return VALIDATOR.isValid(getValue());
		}
		return false;
	}


	@Override
	public boolean isEnabled() {
		return listBox.isEnabled() && customTaxBox.isEnabled();
	}


	@Override
	public void setEnabled(boolean enabled) {
		listBox.setEnabled(enabled);
		customTaxBox.setEnabled(enabled);
	}


}
