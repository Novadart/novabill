package com.novadart.novabill.frontend.client.ui.center.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.novadart.novabill.frontend.client.datawatcher.DataWatchEvent.DATA;
import com.novadart.novabill.frontend.client.datawatcher.DataWatchEventHandler;
import com.novadart.novabill.frontend.client.datawatcher.DataWatcher;
import com.novadart.novabill.frontend.client.facade.AuthAwareAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.EstimationPlace;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.place.InvoicePlace;
import com.novadart.novabill.frontend.client.ui.center.ClientView;
import com.novadart.novabill.frontend.client.ui.center.client.dialog.ClientDialog;
import com.novadart.novabill.frontend.client.ui.widget.list.impl.EstimationList;
import com.novadart.novabill.frontend.client.ui.widget.list.impl.InvoiceList;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;

public class ClientViewImpl extends Composite implements ClientView {

	private static ClientViewImplUiBinder uiBinder = GWT
			.create(ClientViewImplUiBinder.class);

	interface ClientViewImplUiBinder extends UiBinder<Widget, ClientViewImpl> {
	}

	private Presenter presenter;
	private ClientDTO client;
	private final ListDataProvider<InvoiceDTO> invoiceDataProvider = new ListDataProvider<InvoiceDTO>();
	private final ListDataProvider<EstimationDTO> estimationDataProvider = new ListDataProvider<EstimationDTO>();
	
	@UiField InvoiceList invoiceList;
	@UiField EstimationList estimationList;
	@UiField Label clientName;
	@UiField HTML clientDetails;
	@UiField TabLayoutPanel tabPanel;

	
	public ClientViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		setStyleName("ClientView");
		invoiceDataProvider.addDataDisplay(invoiceList.getList());
		estimationDataProvider.addDataDisplay(estimationList.getList());
		DataWatcher.getInstance().addDataEventHandler(new DataWatchEventHandler() {
			
			@Override
			public void onDataUpdated(DATA data) {
				if(DATA.INVOICE.equals(data)){
					loadInvoices();
				}
				
			}
		});
		DataWatcher.getInstance().addDataEventHandler(new DataWatchEventHandler() {
			
			@Override
			public void onDataUpdated(DATA data) {
				if(DATA.ESTIMATION.equals(data)){
					loadEstimations();
				}
				
			}
		});
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		invoiceList.setPresenter(presenter);
		estimationList.setPresenter(presenter);
	}

	@Override
	public void setClean() {
		clientName.setText("");
		clientDetails.setHTML("");
		invoiceDataProvider.getList().clear();
		invoiceDataProvider.refresh();
		estimationDataProvider.getList().clear();
		estimationDataProvider.refresh();
		tabPanel.selectTab(0);
	}
	
	@UiHandler("newInvoice")
	void onNewInvoiceClicked(ClickEvent e){
		ServerFacade.invoice.getNextInvoiceDocumentID(new AuthAwareAsyncCallback<Long>() {
			
			@Override
			public void onSuccess(Long result) {
				if(result == null){
					return;
				}
				InvoicePlace ip = new InvoicePlace();
				ip.setDataForNewInvoice(client, result);
				presenter.goTo(ip);
			}
			
			@Override
			public void onException(Throwable caught) {
				
			}
		});
	}
	
	@UiHandler("newEstimation")
	void onNewEstimationClicked(ClickEvent e){
		EstimationPlace ep = new EstimationPlace();
		ep.setDataForNewInvoice(client);
		presenter.goTo(ep);
	}
	
	
	@UiHandler("modifyClient")
	void onModifyClientClicked(ClickEvent e){
		ClientDialog cd = ClientDialog.getInstance();
		cd.setClient(client);
		cd.showCentered();
	}

	
	@UiHandler("cancelClient")
	void onCancelClientClicked(ClickEvent e){
		if(Window.confirm(I18N.get.confirmClientDeletion())){
			ServerFacade.client.remove(client.getId(), new AuthAwareAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					DataWatcher.getInstance().fireClientEvent();
					DataWatcher.getInstance().fireStatsEvent();
					presenter.goTo(new HomePlace());
				}

				@Override
				public void onException(Throwable caught) {
					if(caught instanceof DataIntegrityException){
						Window.alert(I18N.get.errorClientCancelation());
					} else {
						Window.alert(I18N.get.errorServerCommunication());
					}
				}
			});
		}
	}

	@Override
	public void setClient(ClientDTO client) {
		if(client == null){
			presenter.goTo(new HomePlace());
		}
		this.client = client;
		clientName.setText(client.getName());
		updateClientDetails(client);
		loadInvoices();
		loadEstimations();
	}
	
	private void updateClientDetails(ClientDTO client){
		SafeHtmlBuilder sb = new SafeHtmlBuilder();

		sb.appendHtmlConstant("<div class='address'>");
		sb.appendEscaped(client.getAddress());
		sb.appendHtmlConstant("</div>");

		sb.appendHtmlConstant("<div class='address-2'>");
		sb.appendEscaped(client.getPostcode() + " " 
				+ client.getCity() + " (" + client.getProvince() +") " + client.getCountry());
		sb.appendHtmlConstant("</div>");


		boolean hasPhone = client.getPhone()!=null && !client.getPhone().isEmpty();
		boolean hasFax = client.getFax()!=null && !client.getFax().isEmpty();
		sb.appendHtmlConstant("<div class='address-3'>");
		sb.appendEscaped( ( (hasPhone?"Tel. "+client.getPhone():"") 
				+ (hasFax?" Fax "+client.getFax():"").trim() 
				+ " " + I18N.get.vatID()+" "+client.getVatID() ).trim() );
		sb.appendHtmlConstant("</div>");

		clientDetails.setHTML(sb.toSafeHtml());
	}
	
	
	
	private void loadInvoices(){
		ServerFacade.invoice.getAllForClient(client.getId(), new AuthAwareAsyncCallback<List<InvoiceDTO>>() {

			@Override
			public void onException(Throwable caught) {
				Window.alert(I18N.get.errorServerCommunication());
			}

			@Override
			public void onSuccess(List<InvoiceDTO> result) {
				if(result == null){
					return;
				}
				invoiceDataProvider.setList(result);
				invoiceDataProvider.refresh();
			}
		});
	}
	
	private void loadEstimations(){
		ServerFacade.estimation.getAllForClient(client.getId(), new AuthAwareAsyncCallback<List<EstimationDTO>>() {

			@Override
			public void onException(Throwable caught) {
				Window.alert(I18N.get.errorServerCommunication());
			}

			@Override
			public void onSuccess(List<EstimationDTO> result) {
				if(result == null){
					return;
				}
				estimationDataProvider.setList(result);
				estimationDataProvider.refresh();
			}
		});
	}

}
