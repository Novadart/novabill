package com.novadart.novabill.frontend.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.novadart.novabill.frontend.client.presenter.Presenter;

public interface View<P extends Presenter> extends IsWidget {

	public void setPresenter(P presenter);
	public void clean();
	
}
