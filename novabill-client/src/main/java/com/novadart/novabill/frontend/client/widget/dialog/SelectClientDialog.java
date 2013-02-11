package com.novadart.novabill.frontend.client.widget.dialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.gwtshared.client.dialog.Dialog;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.view.west.standard.ClientCell;
import com.novadart.novabill.frontend.client.widget.search.ClientSearch;
import com.novadart.novabill.frontend.client.widget.tip.TipFactory;
import com.novadart.novabill.frontend.client.widget.tip.Tips;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class SelectClientDialog extends Dialog {
	
	interface ClientSearchStyle extends ClientSearch.Style {}
	interface ClientCellStyle extends ClientCell.Style {}

	private static SelectClientDialogUiBinder uiBinder = GWT
			.create(SelectClientDialogUiBinder.class);

	interface SelectClientDialogUiBinder extends
	UiBinder<Widget, SelectClientDialog> {
	}

	public interface Handler {
		public void onClientSelected(ClientDTO client);
	}

	private final Handler handler;

	@UiField HorizontalPanel filterContainer;
	@UiField Button ok;
	@UiField SimplePanel tip;
	@UiField ScrollPanel listWrapperScroll;
	
	@UiField ClientSearchStyle cs;
	@UiField ClientCellStyle ccs;
	
	private ClientDTO selectedClient = null;
	private ClientSearch clientSearch;

	public SelectClientDialog(Handler handler) {
		this.handler = handler;
		setHeightDivisionValue(5);
		setWidget(uiBinder.createAndBindUi(this));
		
		CellList<ClientDTO> list = new CellList<ClientDTO>(new ClientCell(ccs));

		final SingleSelectionModel<ClientDTO> selModel = new SingleSelectionModel<ClientDTO>(
				new ProvidesKey<ClientDTO>() {

					@Override
					public Object getKey(ClientDTO item) {
						return item.getId();
					}
				});
		list.setSelectionModel(selModel);
		selModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				selectedClient = selModel.getSelectedObject();
				ok.setEnabled(true);
			}

		});
		
		clientSearch = new ClientSearch(cs, list);
		filterContainer.add(clientSearch.getSearchInput());
		filterContainer.add(clientSearch.getResetButton());
		
		listWrapperScroll.setWidget(clientSearch.getWrappedClientList());

		TipFactory.show(Tips.select_client_dialog, tip);
	}
	
	public void setEventBus(EventBus eventBus){
		clientSearch.setEventBus(eventBus);
	}
	

	@UiHandler("ok")
	void onOkClicked(ClickEvent e){
		handler.onClientSelected(selectedClient);
		hide();
	}

	@UiHandler("cancel")
	void onCancelClicked(ClickEvent e){
		hide();
	}

	@UiFactory
	I18N getI18n(){
		return I18N.INSTANCE;
	}

}
