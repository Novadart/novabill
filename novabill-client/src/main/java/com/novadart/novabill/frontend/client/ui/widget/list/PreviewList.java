package com.novadart.novabill.frontend.client.ui.widget.list;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.novabill.frontend.client.ui.View.Presenter;

public abstract class PreviewList<T> extends Composite implements PreviewCell.Handler<T> {

	private final CellList<T> cellList;
	private final Preview<T> preview;

	public PreviewList() {
		HorizontalPanel skeleton = new HorizontalPanel();
		initWidget(skeleton);
		setStyleName("PreviewList");
		
		PreviewCell<T> cell = buildPreviewCell();
		cell.setHandler(this);
		
		cellList = new CellList<T>(cell);
		preview = buildPreview();
		
		cellList.addStyleName(getStyleName()+"-cellList");
		skeleton.add(cellList);
		((Widget) preview).addStyleName(getStyleName()+"-preview");
		skeleton.add(preview);
	}

	protected abstract PreviewCell<T> buildPreviewCell();
	
	protected abstract Preview<T> buildPreview();
	
	public void onClick(T value) {
		preview.updatePreview(value);
	};
	
	public CellList<T> getCellList(){
		return cellList;
	}
	
	public void setPresenter(Presenter presenter) {
		preview.setPresenter(presenter);
	}
	
}
