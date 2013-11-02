package com.novadart.novabill.frontend.client.bridge;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.activity.center.InvoiceActivity;
import com.novadart.novabill.frontend.client.bridge.ui.HTMLWrapper;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.place.invoice.ModifyInvoicePlace;
import com.novadart.novabill.frontend.client.place.invoice.NewInvoicePlace;
import com.novadart.novabill.frontend.client.view.bootstrap.BootstrapDialog;
import com.novadart.novabill.frontend.client.widget.dialog.client.ClientDialog;
import com.novadart.novabill.frontend.client.widget.dialog.selectclient.SelectClientDialog;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class UiBridge implements ApiBridge {
	
	@Override
	public void inject(AsyncCallback<Void> callback){
		Configuration.injectCss();
		injectNative();
		callback.onSuccess(null);
	}
	
	public native void injectNative()/*-{
		$wnd.GWT_UI = {
			// bootstrap
			bootstrapDialog : @com.novadart.novabill.frontend.client.bridge.UiBridge::showBootstrapDialog(),
			
			// clients			
			clientDialog : @com.novadart.novabill.frontend.client.bridge.UiBridge::showNewClientDialog(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			modifyClientDialog : @com.novadart.novabill.frontend.client.bridge.UiBridge::showModifyClientDialog(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			selectClientDialog : @com.novadart.novabill.frontend.client.bridge.UiBridge::showSelectClientDialog(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			
			// invoices
			showNewInvoicePage : @com.novadart.novabill.frontend.client.bridge.UiBridge::showNewInvoicePage(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			showModifyInvoicePage : @com.novadart.novabill.frontend.client.bridge.UiBridge::showModifyInvoicePage(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			
			//pdf
			generateInvoicePdf : @com.novadart.novabill.frontend.client.util.PDFUtils::generateInvoicePdf(Ljava/lang/String;),
			generateEstimationPdf : @com.novadart.novabill.frontend.client.util.PDFUtils::generateEstimationPdf(Ljava/lang/String;),
			generateCreditNotePdf : @com.novadart.novabill.frontend.client.util.PDFUtils::generateCreditNotePdf(Ljava/lang/String;),
			generateTransportDocumentPdf : @com.novadart.novabill.frontend.client.util.PDFUtils::generateTransportDocumentPdf(Ljava/lang/String;),
			
		};
	
	}-*/;
	
	public static void showBootstrapDialog(){
		BootstrapDialog dialog = new BootstrapDialog();
		dialog.showCentered();
	}
	
	
	/*
	 * CLIENTS
	 */
	public static void showNewClientDialog(String businessId, JavaScriptObject callback){
		ClientDialog clientDialog = new ClientDialog(Long.parseLong(businessId), callback);
		clientDialog.showCentered();
	}
	
	public static void showModifyClientDialog(final String businessId, String clientId, final JavaScriptObject callback){
		ServerFacade.INSTANCE.getClientService().get(Long.parseLong(clientId), new ManagedAsyncCallback<ClientDTO>() {
			@Override
			public void onSuccess(ClientDTO result) {
				ClientDialog clientDialog = new ClientDialog(Long.parseLong(businessId), callback);
				clientDialog.setClient(result);
				clientDialog.center();
			}
		});
	}
	
	public static void showSelectClientDialog(final String businessId, final JavaScriptObject callback) {
		SelectClientDialog dialog = new SelectClientDialog(new SelectClientDialog.Handler() {
			@Override
			public void onClientSelected(ClientDTO client) {
				BridgeUtils.invokeJSCallback(client.getId().toString(), callback);
			}
		});
		dialog.center();
	}
	
	
	/*
	 * INVOICES
	 */
	public static void showNewInvoicePage(String wrapperId, String clientId, JavaScriptObject callback) {
		AcceptsOneWidget panel = new HTMLWrapper(wrapperId);
		
		NewInvoicePlace nip = new NewInvoicePlace();
		nip.setClientId(Long.parseLong(clientId));

		InvoiceActivity is = new InvoiceActivity(nip, ClientFactory.INSTANCE, callback);
		is.start(panel, null);
	}
	
	
	public static void showModifyInvoicePage(String wrapperId, String invoiceId, JavaScriptObject callback) {
		AcceptsOneWidget panel = new HTMLWrapper(wrapperId);
		
		ModifyInvoicePlace mip = new ModifyInvoicePlace();
		mip.setInvoiceId(Long.parseLong(invoiceId));

		InvoiceActivity is = new InvoiceActivity(mip, ClientFactory.INSTANCE, callback);
		is.start(panel, null);
	}

}
