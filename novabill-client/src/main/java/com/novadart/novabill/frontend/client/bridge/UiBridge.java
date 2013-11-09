package com.novadart.novabill.frontend.client.bridge;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.activity.center.CreditNoteActivity;
import com.novadart.novabill.frontend.client.activity.center.EstimationActivity;
import com.novadart.novabill.frontend.client.activity.center.InvoiceActivity;
import com.novadart.novabill.frontend.client.activity.center.TransportDocumentActivity;
import com.novadart.novabill.frontend.client.bridge.ui.HTMLWrapper;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.place.creditnote.FromInvoiceCreditNotePlace;
import com.novadart.novabill.frontend.client.place.creditnote.ModifyCreditNotePlace;
import com.novadart.novabill.frontend.client.place.creditnote.NewCreditNotePlace;
import com.novadart.novabill.frontend.client.place.estimation.CloneEstimationPlace;
import com.novadart.novabill.frontend.client.place.estimation.ModifyEstimationPlace;
import com.novadart.novabill.frontend.client.place.estimation.NewEstimationPlace;
import com.novadart.novabill.frontend.client.place.invoice.CloneInvoicePlace;
import com.novadart.novabill.frontend.client.place.invoice.FromEstimationInvoicePlace;
import com.novadart.novabill.frontend.client.place.invoice.FromTransportDocumentInvoicePlace;
import com.novadart.novabill.frontend.client.place.invoice.ModifyInvoicePlace;
import com.novadart.novabill.frontend.client.place.invoice.NewInvoicePlace;
import com.novadart.novabill.frontend.client.place.transportdocument.ModifyTransportDocumentPlace;
import com.novadart.novabill.frontend.client.place.transportdocument.NewTransportDocumentPlace;
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
			showCloneInvoicePage : @com.novadart.novabill.frontend.client.bridge.UiBridge::showCloneInvoicePage(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			showFromEstimationInvoicePage : @com.novadart.novabill.frontend.client.bridge.UiBridge::showFromEstimationInvoicePage(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			showFromTransportDocumentInvoicePage : @com.novadart.novabill.frontend.client.bridge.UiBridge::showFromTransportDocumentInvoicePage(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			
			// estimations
			showNewEstimationPage : @com.novadart.novabill.frontend.client.bridge.UiBridge::showNewEstimationPage(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			showModifyEstimationPage : @com.novadart.novabill.frontend.client.bridge.UiBridge::showModifyEstimationPage(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			showCloneEstimationPage : @com.novadart.novabill.frontend.client.bridge.UiBridge::showCloneEstimationPage(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			
			// credit notes
			showNewCreditNotePage : @com.novadart.novabill.frontend.client.bridge.UiBridge::showNewCreditNotePage(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			showModifyCreditNotePage : @com.novadart.novabill.frontend.client.bridge.UiBridge::showModifyCreditNotePage(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			showFromInvoiceCreditNotePage : @com.novadart.novabill.frontend.client.bridge.UiBridge::showFromInvoiceCreditNotePage(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			
			// transport documents
			showNewTransportDocumentPage : @com.novadart.novabill.frontend.client.bridge.UiBridge::showNewTransportDocumentPage(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			showModifyTransportDocumentPage : @com.novadart.novabill.frontend.client.bridge.UiBridge::showModifyTransportDocumentPage(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			
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
	
	
	public static void showCloneInvoicePage(String wrapperId, String clientId, String invoiceId, JavaScriptObject callback) {
		AcceptsOneWidget panel = new HTMLWrapper(wrapperId);
		
		CloneInvoicePlace cip = new CloneInvoicePlace();
		cip.setInvoiceId(Long.parseLong(invoiceId));
		cip.setClientId(Long.parseLong(clientId));

		InvoiceActivity is = new InvoiceActivity(cip, ClientFactory.INSTANCE, callback);
		is.start(panel, null);
	}
	
	
	public static void showModifyInvoicePage(String wrapperId, String invoiceId, JavaScriptObject callback) {
		AcceptsOneWidget panel = new HTMLWrapper(wrapperId);
		
		ModifyInvoicePlace mip = new ModifyInvoicePlace();
		mip.setInvoiceId(Long.parseLong(invoiceId));

		InvoiceActivity is = new InvoiceActivity(mip, ClientFactory.INSTANCE, callback);
		is.start(panel, null);
	}
	
	
	public static void showFromEstimationInvoicePage(String wrapperId, String estimationId, JavaScriptObject callback) {
		AcceptsOneWidget panel = new HTMLWrapper(wrapperId);
		
		FromEstimationInvoicePlace fei = new FromEstimationInvoicePlace();
		fei.setEstimationId(Long.parseLong(estimationId));

		InvoiceActivity is = new InvoiceActivity(fei, ClientFactory.INSTANCE, callback);
		is.start(panel, null);
	}
	
	
	public static void showFromTransportDocumentInvoicePage(String wrapperId, String transportDocumentId, JavaScriptObject callback) {
		AcceptsOneWidget panel = new HTMLWrapper(wrapperId);
		
		FromTransportDocumentInvoicePlace ftdi = new FromTransportDocumentInvoicePlace();
		ftdi.setTransportDocumentId(Long.parseLong(transportDocumentId));

		InvoiceActivity is = new InvoiceActivity(ftdi, ClientFactory.INSTANCE, callback);
		is.start(panel, null);
	}
	
	/*
	 * ESTIMATIONS
	 */
	public static void showNewEstimationPage(String wrapperId, String clientId, JavaScriptObject callback) {
		AcceptsOneWidget panel = new HTMLWrapper(wrapperId);
		
		NewEstimationPlace nep = new NewEstimationPlace();
		nep.setClientId(Long.parseLong(clientId));

		EstimationActivity is = new EstimationActivity(nep, ClientFactory.INSTANCE, callback);
		is.start(panel, null);
	}
	
	
	public static void showCloneEstimationPage(String wrapperId, String clientId, String estimationId, JavaScriptObject callback) {
		AcceptsOneWidget panel = new HTMLWrapper(wrapperId);
		
		CloneEstimationPlace cep = new CloneEstimationPlace();
		cep.setEstimationId(Long.parseLong(estimationId));
		cep.setClientId(Long.parseLong(clientId));

		EstimationActivity ea = new EstimationActivity(cep, ClientFactory.INSTANCE, callback);
		ea.start(panel, null);
	}
	
	
	public static void showModifyEstimationPage(String wrapperId, String estimationId, JavaScriptObject callback) {
		AcceptsOneWidget panel = new HTMLWrapper(wrapperId);
		
		ModifyEstimationPlace mep = new ModifyEstimationPlace();
		mep.setEstimationId(Long.parseLong(estimationId));

		EstimationActivity is = new EstimationActivity(mep, ClientFactory.INSTANCE, callback);
		is.start(panel, null);
	}
	
	
	/*
	 * CREDIT NOTES
	 */
	public static void showNewCreditNotePage(String wrapperId, String clientId, JavaScriptObject callback) {
		AcceptsOneWidget panel = new HTMLWrapper(wrapperId);
		
		NewCreditNotePlace nep = new NewCreditNotePlace();
		nep.setClientId(Long.parseLong(clientId));

		CreditNoteActivity is = new CreditNoteActivity(nep, ClientFactory.INSTANCE, callback);
		is.start(panel, null);
	}
	
	
	public static void showFromInvoiceCreditNotePage(String wrapperId, String invoiceId, JavaScriptObject callback) {
		AcceptsOneWidget panel = new HTMLWrapper(wrapperId);
		
		FromInvoiceCreditNotePlace fip = new FromInvoiceCreditNotePlace();
		fip.setInvoiceId(Long.parseLong(invoiceId));

		CreditNoteActivity is = new CreditNoteActivity(fip, ClientFactory.INSTANCE, callback);
		is.start(panel, null);
	}
	
	
	public static void showModifyCreditNotePage(String wrapperId, String creditNoteId, JavaScriptObject callback) {
		AcceptsOneWidget panel = new HTMLWrapper(wrapperId);
		
		ModifyCreditNotePlace mep = new ModifyCreditNotePlace();
		mep.setCreditNoteId(Long.parseLong(creditNoteId));

		CreditNoteActivity is = new CreditNoteActivity(mep, ClientFactory.INSTANCE, callback);
		is.start(panel, null);
	}
	
	
	/*
	 * TRANSPORT DOCUMENTS
	 */
	public static void showNewTransportDocumentPage(String wrapperId, String clientId, JavaScriptObject callback) {
		AcceptsOneWidget panel = new HTMLWrapper(wrapperId);
		
		NewTransportDocumentPlace nep = new NewTransportDocumentPlace();
		nep.setClientId(Long.parseLong(clientId));

		TransportDocumentActivity is = new TransportDocumentActivity(nep, ClientFactory.INSTANCE, callback);
		is.start(panel, null);
	}
	
	
	public static void showModifyTransportDocumentPage(String wrapperId, String transportDocumentId, JavaScriptObject callback) {
		AcceptsOneWidget panel = new HTMLWrapper(wrapperId);
		
		ModifyTransportDocumentPlace mep = new ModifyTransportDocumentPlace();
		mep.setTransportDocumentId(Long.parseLong(transportDocumentId));

		TransportDocumentActivity is = new TransportDocumentActivity(mep, ClientFactory.INSTANCE, callback);
		is.start(panel, null);
	}

}