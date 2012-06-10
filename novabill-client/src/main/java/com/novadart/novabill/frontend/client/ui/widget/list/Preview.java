package com.novadart.novabill.frontend.client.ui.widget.list;

import com.google.gwt.user.client.ui.IsWidget;
import com.novadart.novabill.frontend.client.ui.View.Presenter;

public interface Preview<T> extends IsWidget {

	public void updatePreview(T value);
	public void setPresenter(Presenter presenter);
}
