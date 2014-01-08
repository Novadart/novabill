package com.novadart.novabill.frontend.client.view.center;

import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.novadart.novabill.shared.client.dto.CommodityDTO;

public class CommoditySearchPanel extends PopupPanel implements Focusable {
	
	interface Style extends CssResource{
		String line();
		String panel();
		String advancedSearch();
		String bottomPanel();
		String topPanel();
	}

	private static ItemSearchPanelUiBinder uiBinder = GWT
			.create(ItemSearchPanelUiBinder.class);

	interface ItemSearchPanelUiBinder extends UiBinder<Widget, CommoditySearchPanel> {
	}

	@UiField(provided=true) CellList<CommodityDTO> commodities;
	@UiField Style style;
	
	private final ListDataProvider<CommodityDTO> provider = new ListDataProvider<CommodityDTO>();
	private final ItemInsertionForm itemInsertionForm;
	private String clientId;
	
	public CommoditySearchPanel(ItemInsertionForm form) {
		itemInsertionForm = form;
		commodities = new CellList<CommodityDTO>(new AbstractCell<CommodityDTO>() {

			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context,
					CommodityDTO value, SafeHtmlBuilder sb) {
				sb.appendHtmlConstant("<div class='"+style.line()+"'>");
				sb.appendHtmlConstant("<span>[<b>");
				sb.appendEscaped(value.getSku());
				sb.appendHtmlConstant("</b>]</span>&nbsp;");
				sb.appendEscaped(value.getDescription());
				sb.appendHtmlConstant("</div>");
			}
		});
		
		setWidget(uiBinder.createAndBindUi(this));
		provider.addDataDisplay(commodities);
	}
	
	public void setCommodities(List<CommodityDTO> commodities){
		provider.setList(commodities);
	}
	
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void positionOnTop(final UIObject u){
		setPopupPositionAndShow(new PositionCallback() {
			
			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				int uTop = u.getAbsoluteTop();
				int uLeft = u.getAbsoluteLeft();
				
				setPopupPosition(uLeft, uTop-offsetHeight);
			}
		});
	}
	
	public void setSelectionModel(SingleSelectionModel<CommodityDTO> model){
		commodities.setSelectionModel(model);
	}
	
	
	@UiHandler("advancedSearch")
	void onLoadCommodityClicked(ClickEvent e){
		hide();
		itemInsertionForm.openSelectCommodityDialog(itemInsertionForm, clientId);
	}

	@Override
	public int getTabIndex() {
		return commodities.getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
		commodities.setAccessKey(key);
	}

	@Override
	public void setFocus(boolean focused) {
		commodities.setFocus(focused);
	}

	@Override
	public void setTabIndex(int index) {
		commodities.setTabIndex(index);
	}
}
