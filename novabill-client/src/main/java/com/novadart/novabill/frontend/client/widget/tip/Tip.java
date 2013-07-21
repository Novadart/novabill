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
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;

class Tip extends Composite {
	
	public static final byte TIP_ENABLED = 1;
	public static final byte TIP_DISABLED = 0;
	
	private static boolean pendingCall = false;
	
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
		if(pendingCall){
			return;
		}
		pendingCall = true;
		ServerFacade.INSTANCE.getBusinessService().updateNotesBitMask(unsetBitForTip(tipCode, Configuration.getNotesBitMask()), new ManagedAsyncCallback<Long>() {

			@Override
			public void onSuccess(Long result) {
				Configuration.setNotesBitMask(result);
				removeFromParent();
				pendingCall = false;
			}
			
			@Override
			public void onFailure(Throwable caught) {
				//let's not display errors to the user in this case, let's simply hide the tip
				removeFromParent();
				pendingCall = false;
			}
		});

	}
	
	private long unsetBitForTip(Tips tip, long bitmap){
		return bitmap & ~(1 << tip.ordinal());
	}
	
}
