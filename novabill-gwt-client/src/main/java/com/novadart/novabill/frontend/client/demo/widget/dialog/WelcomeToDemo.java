package com.novadart.novabill.frontend.client.demo.widget.dialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.gwtshared.client.dialog.Dialog;
import com.novadart.novabill.frontend.client.demo.i18n.DemoMessages;
import com.novadart.novabill.frontend.client.demo.resources.DemoImageResources;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.resources.GlobalCss;

public class WelcomeToDemo extends Dialog {

	private static WelcomeToDemoUiBinder uiBinder = GWT
			.create(WelcomeToDemoUiBinder.class);

	interface WelcomeToDemoUiBinder extends UiBinder<Widget, WelcomeToDemo> {
	}
	
	public WelcomeToDemo() {
		super(GlobalBundle.INSTANCE.dialog());
		setHeightDivisionValue(4);
		setWidget(uiBinder.createAndBindUi(this));
	}

	@UiFactory
	DemoMessages getDemoMessages(){
		return DemoMessages.INSTANCE;
	}
	
	@UiFactory
	GlobalCss getGlobalCss(){
		return GlobalBundle.INSTANCE.globalCss();
	}
	
	@UiFactory
	DemoImageResources getDemoImageResources(){
		return DemoImageResources.INSTANCE;
	}
	
	@UiHandler("closeDialog")
	void onCloseDialog(ClickEvent e){
		hide();
	}
}
