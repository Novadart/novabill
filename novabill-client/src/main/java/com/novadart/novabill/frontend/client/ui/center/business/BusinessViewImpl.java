package com.novadart.novabill.frontend.client.ui.center.business;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.novadart.gwtshared.client.textbox.UpdateLabel;
import com.novadart.novabill.frontend.client.ui.center.BusinessView;

public class BusinessViewImpl extends Composite implements BusinessView {

	private static BusinessViewImplUiBinder uiBinder = GWT
			.create(BusinessViewImplUiBinder.class);

	interface BusinessViewImplUiBinder extends
			UiBinder<Widget, BusinessViewImpl> {
	}

	private Presenter presenter;
	
	@UiField FormPanel formPanel;
	@UiField(provided=true) UpdateLabel companyName;
	@UiField(provided=true) UpdateLabel address;
	
	public BusinessViewImpl() {
		companyName = new UpdateLabel("Novadart Snc", new UpdateLabel.Handler() {
			
			@Override
			public void onNewValue(String value) {
				Window.alert("New value: "+value);
			}
		});
		address = new UpdateLabel("via Stradone, 51", null);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		formPanel.setAction(GWT.getHostPageBaseURL()+"/private/businesses/logo");
		formPanel.setMethod(FormPanel.METHOD_POST);
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		
		formPanel.addSubmitHandler(new FormPanel.SubmitHandler() {
			
			@Override
			public void onSubmit(SubmitEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		
		formPanel.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				Window.alert("logo uploaded!");
				
			}
		});
	}

	@Override
	public void setClean() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
//		this.presenter = presenter;
	}
	
	@UiHandler("saveData")
	void onSaveDataClicked(ClickEvent e){
		formPanel.submit();
		
	}

}
