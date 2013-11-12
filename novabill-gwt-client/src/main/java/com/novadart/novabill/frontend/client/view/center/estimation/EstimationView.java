package com.novadart.novabill.frontend.client.view.center.estimation;

import com.novadart.gwtshared.client.validation.widget.ValidatedDateBox;
import com.novadart.novabill.frontend.client.view.DocumentView;
import com.novadart.novabill.frontend.client.widget.ValidatedTextArea;

public interface EstimationView extends DocumentView<EstimationView.Presenter> {
	
	public static interface Presenter extends DocumentView.Presenter {
		
	}
	
	public ValidatedDateBox getValidTill();

	public ValidatedTextArea getPaymentNote();

	public ValidatedTextArea getLimitations();

}
