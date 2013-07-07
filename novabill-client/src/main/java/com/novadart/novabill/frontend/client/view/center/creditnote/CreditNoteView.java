package com.novadart.novabill.frontend.client.view.center.creditnote;

import com.google.gwt.user.client.ui.Label;
import com.novadart.novabill.frontend.client.view.DocumentView;

public interface CreditNoteView extends DocumentView<CreditNoteView.Presenter> {
	
	public static interface Presenter extends DocumentView.Presenter{

	}

	Label getTitleLabel();

}
