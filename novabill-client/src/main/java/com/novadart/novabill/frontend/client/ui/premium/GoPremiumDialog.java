package com.novadart.novabill.frontend.client.ui.premium;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.novabill.frontend.client.resources.ImageResources;
import com.novadart.novabill.frontend.client.ui.widget.dialog.Dialog;

public class GoPremiumDialog extends Dialog {

	private static GoPremiumDialogUiBinder uiBinder = GWT
			.create(GoPremiumDialogUiBinder.class);

	interface GoPremiumDialogUiBinder extends
			UiBinder<Widget, GoPremiumDialog> {
	}

	
	private static GoPremiumDialog instance = null;
	
	public static GoPremiumDialog getInstance() {
		if(instance == null){
			instance = new GoPremiumDialog();
		}
		return instance;
	}

	private GoPremiumDialog() {
		setHeightDivisionValue(4);
		setWidget(uiBinder.createAndBindUi(this));
		addStyleName("GoPremiumDialog");
		
	}

	@UiHandler(value={"closeButton", "noThanks"})
	void onCloseButtonClicked(ClickEvent e){
		hide();
	}
	
	@UiFactory
	ImageResources getImageResources(){
		return ImageResources.INSTANCE;
	}
}