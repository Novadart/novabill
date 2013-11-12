package com.novadart.novabill.frontend.client.view.center;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.user.client.ui.Composite;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;

public abstract class AccountDocument extends Composite {
	
	public interface Bundle extends ClientBundle{
		public static final Bundle INSTANCE = GWT.create(Bundle.class);

		@Source("AccountDocument.css")
		AccountDocumentCss accountDocument();
		
	} 
	
	protected static final AccountDocumentCss CSS = Bundle.INSTANCE.accountDocument();

	@Override
	protected void onLoad() {
		super.onLoad();
		CSS.ensureInjected();
//		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
//			
//			@Override
//			public void execute() {
//				WidgetUtils.setElementHeightToFillSpace(getBody(), getElement(), getNonBodyElements());
//			}
//		});
	}
	
//	protected abstract Element[] getNonBodyElements();
//	
//	protected abstract Element getBody();
	
	protected abstract ValidatedTextBox getNumber();
	
//	protected abstract ScrollPanel getDocScroll();
	
}
