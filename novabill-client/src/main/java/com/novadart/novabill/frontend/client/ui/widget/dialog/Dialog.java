package com.novadart.novabill.frontend.client.ui.widget.dialog;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;


public class Dialog extends DialogBox {
	private static final int HEIGHT_DIVISION_VALUE = 2;
	private static final int WIDTH_DIVISION_VALUE = 2;

	private int heightDivisionValue = HEIGHT_DIVISION_VALUE;
	private int widthDivisionValue = WIDTH_DIVISION_VALUE;
	
	public Dialog() {
		setModal(true);
		setGlassEnabled(true);
		addStyleName("Dialog");
	}
	
	public void setHeightDivisionValue(int heightDivisionValue) {
		this.heightDivisionValue = heightDivisionValue>0 ? heightDivisionValue : HEIGHT_DIVISION_VALUE;
	}
	
	public void setWidthDivisionValue(int widthDivisionValue) {
		this.widthDivisionValue = widthDivisionValue>0 ? widthDivisionValue : WIDTH_DIVISION_VALUE;
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

				int x = (windowWidth - offsetWidth) / widthDivisionValue;
				int y = (windowHeight - offsetHeight) / heightDivisionValue;

				Dialog.this.setPopupPosition(x, y);
			}
		});
	}

}