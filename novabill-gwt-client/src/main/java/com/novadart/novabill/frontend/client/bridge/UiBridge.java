package com.novadart.novabill.frontend.client.bridge;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.activity.center.CreditNoteActivity;
import com.novadart.novabill.frontend.client.activity.center.EstimationActivity;
import com.novadart.novabill.frontend.client.activity.center.InvoiceActivity;
import com.novadart.novabill.frontend.client.activity.center.PaymentActivity;
import com.novadart.novabill.frontend.client.activity.center.TransportDocumentActivity;
import com.novadart.novabill.frontend.client.bridge.server.autobean.AutoBeanMaker;
import com.novadart.novabill.frontend.client.bridge.server.autobean.StringList;
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
import com.novadart.novabill.frontend.client.place.invoice.FromTransportDocumentListInvoicePlace;
import com.novadart.novabill.frontend.client.place.invoice.ModifyInvoicePlace;
import com.novadart.novabill.frontend.client.place.invoice.NewInvoicePlace;
import com.novadart.novabill.frontend.client.place.transportdocument.FromEstimationTransportDocumentPlace;
import com.novadart.novabill.frontend.client.place.transportdocument.ModifyTransportDocumentPlace;
import com.novadart.novabill.frontend.client.place.transportdocument.NewTransportDocumentPlace;
import com.novadart.novabill.frontend.client.widget.dialog.client.ClientDialog;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.DocumentIDClassDTO;

public class UiBridge implements ApiBridge {
	
	@Override
	public void inject(AsyncCallback<Void> callback){
		Configuration.injectCss();
		injectNative();
		callback.onSuccess(null);
	}
	
	public native void injectNative()/*-{
		$wnd.GWT_UI = {
			// payments
			showPaymentsPage : @com.novadart.novabill.frontend.client.bridge.UiBridge::showPaymentsPage(Ljava/lang/String;),
			
			// invoices
			showNewInvoicePage : @com.novadart.novabill.frontend.client.bridge.UiBridge::showNewInvoicePage(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			showModifyInvoicePage : @com.novadart.novabill.frontend.client.bridge.UiBridge::showModifyInvoicePage(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			showCloneInvoicePage : @com.novadart.novabill.frontend.client.bridge.UiBridge::showCloneInvoicePage(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			showFromEstimationInvoicePage : @com.novadart.novabill.frontend.client.bridge.UiBridge::showFromEstimationInvoicePage(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			showFromTransportDocumentListInvoicePage : @com.novadart.novabill.frontend.client.bridge.UiBridge::showFromTransportDocumentListInvoicePage(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;),
			
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
			showFromEstimationTransportDocumentPage : @com.novadart.novabill.frontend.client.bridge.UiBridge::showFromEstimationTransportDocumentPage(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)
		};
	
	}-*/;
	
	
	/*
	 * PAYMENTS
	 */
	public static void showPaymentsPage(String wrapperId){
		AcceptsOneWidget panel = new HTMLWrapper(wrapperId);

		PaymentActivity pa = new PaymentActivity(ClientFactory.INSTANCE);
		pa.start(panel, null);
	}
	

	/*
	 * INVOICES
	 */
	public static void showNewInvoicePage(String wrapperId, String clientId, final JavaScriptObject callback) {
		final AcceptsOneWidget panel = new HTMLWrapper(wrapperId);
		
		final NewInvoicePlace nip = new NewInvoicePlace();
		nip.setClientId(Long.parseLong(clientId));

		ServerFacade.INSTANCE.getDocumentIdClassGwtService().getAll(Configuration.getBusinessId(), new ManagedAsyncCallback<List<DocumentIDClassDTO>>() {
			@Override
			public void onSuccess(List<DocumentIDClassDTO> documentIDClassDTOs) {
				nip.setDocumentIDClassDTOs(documentIDClassDTOs);
				InvoiceActivity is = new InvoiceActivity(nip, ClientFactory.INSTANCE, callback);
				is.start(panel, null);
			}
		});
	}
	
	
	public static void showCloneInvoicePage(String wrapperId, String clientId, String invoiceId, final JavaScriptObject callback) {
		final AcceptsOneWidget panel = new HTMLWrapper(wrapperId);
		
		final CloneInvoicePlace cip = new CloneInvoicePlace();
		cip.setInvoiceId(Long.parseLong(invoiceId));
		cip.setClientId(Long.parseLong(clientId));

		ServerFacade.INSTANCE.getDocumentIdClassGwtService().getAll(Configuration.getBusinessId(), new ManagedAsyncCallback<List<DocumentIDClassDTO>>() {
			@Override
			public void onSuccess(List<DocumentIDClassDTO> documentIDClassDTOs) {
				cip.setDocumentIDClassDTOs(documentIDClassDTOs);
				InvoiceActivity is = new InvoiceActivity(cip, ClientFactory.INSTANCE, callback);
				is.start(panel, null);
			}
		});

	}
	
	
	public static void showModifyInvoicePage(String wrapperId, String invoiceId, final JavaScriptObject callback) {
		final AcceptsOneWidget panel = new HTMLWrapper(wrapperId);
		
		final ModifyInvoicePlace mip = new ModifyInvoicePlace();
		mip.setInvoiceId(Long.parseLong(invoiceId));

		ServerFacade.INSTANCE.getDocumentIdClassGwtService().getAll(Configuration.getBusinessId(), new ManagedAsyncCallback<List<DocumentIDClassDTO>>() {
			@Override
			public void onSuccess(List<DocumentIDClassDTO> documentIDClassDTOs) {
				mip.setDocumentIDClassDTOs(documentIDClassDTOs);
				InvoiceActivity is = new InvoiceActivity(mip, ClientFactory.INSTANCE, callback);
				is.start(panel, null);
			}
		});
	}
	
	
	public static void showFromEstimationInvoicePage(String wrapperId, String estimationId, final JavaScriptObject callback) {
		final AcceptsOneWidget panel = new HTMLWrapper(wrapperId);
		
		final FromEstimationInvoicePlace fei = new FromEstimationInvoicePlace();
		fei.setEstimationId(Long.parseLong(estimationId));

		ServerFacade.INSTANCE.getDocumentIdClassGwtService().getAll(Configuration.getBusinessId(), new ManagedAsyncCallback<List<DocumentIDClassDTO>>() {
			@Override
			public void onSuccess(List<DocumentIDClassDTO> documentIDClassDTOs) {
				fei.setDocumentIDClassDTOs(documentIDClassDTOs);
				InvoiceActivity is = new InvoiceActivity(fei, ClientFactory.INSTANCE, callback);
				is.start(panel, null);
			}
		});
	}
	
	public static void showFromTransportDocumentListInvoicePage(String wrapperId, String transportDocumentList, final JavaScriptObject callback) {
		final AcceptsOneWidget panel = new HTMLWrapper(wrapperId);
		
		StringList bean = AutoBeanCodex.decode(AutoBeanMaker.INSTANCE, StringList.class, transportDocumentList).as();
		
		List<Long> longs = new ArrayList<Long>(bean.getList().size());
		for (String str : bean.getList()) {
			longs.add( Long.parseLong(str) );
		}
		
		final FromTransportDocumentListInvoicePlace ftdi = new FromTransportDocumentListInvoicePlace();
		ftdi.setTransportDocumentList(longs);

		ServerFacade.INSTANCE.getDocumentIdClassGwtService().getAll(Configuration.getBusinessId(), new ManagedAsyncCallback<List<DocumentIDClassDTO>>() {
			@Override
			public void onSuccess(List<DocumentIDClassDTO> documentIDClassDTOs) {
				ftdi.setDocumentIDClassDTOs(documentIDClassDTOs);
				InvoiceActivity is = new InvoiceActivity(ftdi, ClientFactory.INSTANCE, callback);
				is.start(panel, null);
			}
		});

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
	
	public static void showFromEstimationTransportDocumentPage(String wrapperId, String estimationId, JavaScriptObject callback) {
		AcceptsOneWidget panel = new HTMLWrapper(wrapperId);
		
		FromEstimationTransportDocumentPlace fei = new FromEstimationTransportDocumentPlace();
		fei.setEstimationId(Long.parseLong(estimationId));

		TransportDocumentActivity is = new TransportDocumentActivity(fei, ClientFactory.INSTANCE, callback);
		is.start(panel, null);
	}
	
}
