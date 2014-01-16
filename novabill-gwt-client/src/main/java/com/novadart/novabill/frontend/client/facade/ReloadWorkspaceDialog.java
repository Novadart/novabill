package com.novadart.novabill.frontend.client.facade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.gwtshared.client.dialog.Dialog;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.resources.GlobalCss;

public class ReloadWorkspaceDialog extends Dialog {

	private static ReloadWorkspaceDialogUiBinder uiBinder = GWT
			.create(ReloadWorkspaceDialogUiBinder.class);

	interface ReloadWorkspaceDialogUiBinder extends UiBinder<Widget, ReloadWorkspaceDialog> {
	}

	@UiField Button reloadPage;
	
	public ReloadWorkspaceDialog() {
		super(GlobalBundle.INSTANCE.dialog());
		setWidget(uiBinder.createAndBindUi(this));
		addStyleName("AuthDialog");
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		Timer timer = new Timer() {
			private int countDown = 30;
			
			@Override
			public void run() {
				if(countDown > 0){
					reloadPage.setText(I18N.INSTANCE.reloadPage()+"... "+countDown--);
				} else {
					Window.Location.reload();
				}
			}
		};
		timer.scheduleRepeating(1000);
		
	}

	@UiHandler("reloadPage")
	void onSubmitClicked(ClickEvent e){
		Window.Location.reload();
	}
	
	@UiFactory
	GlobalCss getGlobalCss(){
		return GlobalBundle.INSTANCE.globalCss();
	}
	
	@UiFactory 
	I18N getI18N(){
		return I18N.INSTANCE;
	}
	
}
