package com.novadart.novabill.frontend.client.widget.tip;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

class Tip extends Composite {

	private static TipUiBinder uiBinder = GWT.create(TipUiBinder.class);

	interface TipUiBinder extends UiBinder<Widget, Tip> {
	}
	
	@UiField(provided=true) HTML tip;
	
	private final Tips tipCode;

	Tip(Tips tipCode, SafeHtml message) {
		this.tipCode = tipCode;
		tip = new HTML(message);
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("closeTip")
	void closeTipClicked(ClickEvent e){
//		ServerFacade.business.update(null, new ManagedAsyncCallback<Void>() {
//
//			@Override
//			public void onSuccess(Void result) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			
//		});
		tipCode.ordinal();
		removeFromParent();
	}
	
}