package com.novadart.novabill.frontend.client.ui.widget.dialog;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;


public class Dialog extends DialogBox {

	public Dialog() {
		setModal(true);
		setGlassEnabled(true);
		addStyleName("Dialog");
	}

	@Override
	public void show() {
		Window.scrollTo(0, 0);
		super.show();
	}

	public void showCentered(){

		setPopupPositionAndShow(new PositionCallback() {

			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				int windowHeight = Window.getClientHeight();
				int windowWidth = Window.getClientWidth();

				int x = (windowWidth - offsetWidth) / 2;
				int y = (windowHeight - offsetHeight) / 2;

				Dialog.this.setPopupPosition(x, y);
			}
		});
	}

}