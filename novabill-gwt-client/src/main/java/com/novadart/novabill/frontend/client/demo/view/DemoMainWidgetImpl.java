package com.novadart.novabill.frontend.client.demo.view;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.demo.i18n.DemoMessages;
import com.novadart.novabill.frontend.client.demo.widget.dialog.WelcomeToDemo;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.view.MainWidgetImpl;
import com.novadart.novabill.frontend.client.widget.notification.Notification;

public class DemoMainWidgetImpl extends MainWidgetImpl {
	
	public DemoMainWidgetImpl() {
		changePasswordAnchor.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				event.preventDefault();
				Notification.showMessage(DemoMessages.INSTANCE.functionNotAvailable());
			}
		});
		
		Button register = new Button(DemoMessages.INSTANCE.registerAccount());
		register.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.assign(ClientFactory.INSTANCE.getRegisterAccountUrl());
			}
		});
		register.setStyleName(GlobalBundle.INSTANCE.globalCss().action2Button());
		
		com.google.gwt.dom.client.Style style = register.getElement().getStyle();
		style.setMarginBottom(0, Unit.PX);
		style.setMarginLeft(0, Unit.PX);
		style.setMarginRight(0, Unit.PX);
		style.setMarginTop(2, Unit.PX);
		style.setFloat(Float.RIGHT);
		style.setColor("#FFF");
		style.setProperty("textShadow", "none");
		getMenuBar().add(register);
	}
	
	@Override
	protected void onFeedbackClicked(ClickEvent e) {
		Notification.showMessage(DemoMessages.INSTANCE.functionNotAvailable());
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		
		WelcomeToDemo welcomeToDemo = new WelcomeToDemo();
		welcomeToDemo.showCentered();
	}
}
