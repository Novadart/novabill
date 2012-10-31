package com.novadart.novabill.frontend.client.ui.widget.dialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.novadart.gwtshared.client.dialog.Dialog;
import com.novadart.gwtshared.client.textbox.RichTextBox;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.ui.widget.search.ClientSearch;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class SelectClientDialog extends Dialog {

	private static SelectClientDialogUiBinder uiBinder = GWT
			.create(SelectClientDialogUiBinder.class);

	interface SelectClientDialogUiBinder extends
	UiBinder<Widget, SelectClientDialog> {
	}

	public interface Handler {
		public void onClientSelected(ClientDTO client);
	}

	private final Handler handler;

	@UiField(provided=true) SimplePanel listWrapper;
	@UiField(provided=true) RichTextBox filter;
	@UiField(provided=true) Image clearFilter;
	@UiField Button ok;

	private ClientDTO selectedClient = null;

	public SelectClientDialog(Handler handler) {
		this.handler = handler;
		setHeightDivisionValue(5);

		CellList<ClientDTO> list = new CellList<ClientDTO>(new ClientCell());

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

		ClientSearch clientSearch = new ClientSearch(list);
		listWrapper = clientSearch.getWrappedClientList();
		filter = clientSearch.getSearchInput();
		clearFilter = clientSearch.getResetButton();

		setWidget(uiBinder.createAndBindUi(this));
		addStyleName("SelectClientDialog");
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
