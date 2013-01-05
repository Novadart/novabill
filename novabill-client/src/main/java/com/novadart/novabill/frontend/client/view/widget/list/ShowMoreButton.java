package com.novadart.novabill.frontend.client.view.widget.list;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RowCountChangeEvent;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.resources.ImageResources;

public class ShowMoreButton extends Composite {
	private static final int CANCEL_LOADER_TIMEOUT = 10000;

	private final SimplePanel container = new SimplePanel();
	private AbstractHasData<?> display;
	private final Button button = new Button(I18N.INSTANCE.showMore());
	private final Image loader = new Image(ImageResources.INSTANCE.loader().getSafeUri().asString());
	private final Timer cancelLoaderTimer = new Timer() {
		@Override
		public void run() {
			container.setWidget(button);
		}
	};

	private final int rangeIncrement;

	public ShowMoreButton(int rangeIncrement) {
		this.rangeIncrement = rangeIncrement;
		container.setWidget(button);
		initWidget(container);
		setStyleName("ShowMoreButton");

		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onButtonClicked();
			}
		});

		button.setStyleName(getStylePrimaryName()+"-button");
		loader.setStyleName(getStylePrimaryName()+"-loader");

	}

	public void setDisplay(AbstractHasData<?> display) {
		this.display = display;

		display.addRowCountChangeHandler(new RowCountChangeEvent.Handler() {

			@Override
			public void onRowCountChange(RowCountChangeEvent event) {
				cancelLoaderTimer.cancel();
				container.setWidget(button);
			}
		});

	}

	private void onButtonClicked() {
		container.setWidget(loader);
		Range currentRange = display.getVisibleRange();
		Range nextRange = new Range(0, currentRange.getLength() + rangeIncrement);
		display.setVisibleRange(nextRange);

		cancelLoaderTimer.schedule(CANCEL_LOADER_TIMEOUT);
	}

	public void addStyleNameToButton(String styleName){
		button.addStyleName(styleName);
	}

	public void addStyleNameToLoader(String styleName){
		loader.addStyleName(styleName);
	}
}
