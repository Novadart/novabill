package com.novadart.novabill.frontend.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.novadart.novabill.frontend.client.view.View;
import com.novadart.novabill.frontend.client.view.center.business.BusinessView;
import com.novadart.novabill.frontend.client.view.center.business.BusinessViewImpl;
import com.novadart.novabill.frontend.client.view.center.creditnote.CreditNoteView;
import com.novadart.novabill.frontend.client.view.center.creditnote.CreditNoteViewImpl;
import com.novadart.novabill.frontend.client.view.center.estimation.EstimationView;
import com.novadart.novabill.frontend.client.view.center.estimation.EstimationViewImpl;
import com.novadart.novabill.frontend.client.view.center.invoice.InvoiceView;
import com.novadart.novabill.frontend.client.view.center.invoice.InvoiceViewImpl;
import com.novadart.novabill.frontend.client.view.center.payment.PaymentView;
import com.novadart.novabill.frontend.client.view.center.payment.PaymentViewImpl;
import com.novadart.novabill.frontend.client.view.center.transportdocument.TransportDocumentView;
import com.novadart.novabill.frontend.client.view.center.transportdocument.TransportDocumentViewImpl;

public class ClientFactoryImpl implements ClientFactory {
	private static final EventBus eventBus = 
			new SimpleEventBus();
	private static final PlaceController placeController = 
			new PlaceController(eventBus);
	
	private static final Map<Class<?>, View<?>> views =
			new HashMap<Class<?>, View<?>>();

	private static final String URL_LOGO = GWT.getModuleBaseURL()+"../private/businesses/logo";
	private static final String URL_THUMB = GWT.getModuleBaseURL()+"../private/businesses/logo/thumbnail";
	private static final String UPDATE_LOGO = GWT.getModuleBaseURL()+"../private/businesses/logo?token=";
	private static final String DELETE_LOGO = GWT.getModuleBaseURL()+"../private/businesses/logo?token=";
	private static final String CHANGE_PASSWORD_URL = GWT.getModuleBaseURL()+"../private/change-password";
	private static final String XSRF_URL = GWT.getModuleBaseURL() + "../private/gwt/xsrf";
	private static final String POST_FEEDBACK_URL = GWT.getModuleBaseURL()+"../private/feedback";
	private static final String LOGOUT_URL = GWT.getModuleBaseURL()+"../resources/logout";
	private static final String DELETE_ACCOUNT_URL = GWT.getModuleBaseURL()+"../private/delete";
	private static final String EXPORT_REQUEST = GWT.getModuleBaseURL()
			+ "../private/export?clients={c}&invoices={i}&estimations={e}&creditnotes={cn}&transportdocs={t}&token={token}";
	private static final String PDF_REQUEST = 
			GWT.getModuleBaseURL()+"../private/pdf/{document}/{id}?token={token}";
	private static final String PROSPECT_PDF_REQUEST = 
			GWT.getModuleBaseURL()+"../private/pdf/paymentspros/{startDate}/{endDate}?token={token}";
			
	private static String logoUrl = URL_THUMB + "?v=" + new Date().getTime();
	
	@Override
	public String getLogoUrl(){
		return logoUrl;
	}
	
	@Override
	public void refeshLogoUrl(){
		logoUrl = URL_THUMB + "?v=" + new Date().getTime();
	}
	
	@Override
	public String getUrlLogo() {
		return URL_LOGO;
	}

	@Override
	public String getUrlThumb() {
		return URL_THUMB;
	}

	@Override
	public String getUpdateLogo() {
		return UPDATE_LOGO;
	}

	@Override
	public String getDeleteLogo() {
		return DELETE_LOGO;
	}

	@Override
	public String getChangePasswordUrl() {
		return CHANGE_PASSWORD_URL;
	}

	@Override
	public String getXsrfUrl() {
		return XSRF_URL;
	}

	@Override
	public String getPostFeedbackUrl() {
		return POST_FEEDBACK_URL;
	}
	
	@Override
	public String getLogoutUrl() {
		return LOGOUT_URL;
	}
	
	@Override
	public String getDeleteAccountUrl() {
		return DELETE_ACCOUNT_URL;
	}

	@Override
	public String getExportRequest() {
		return EXPORT_REQUEST;
	}
	
	@Override
	public String getPdfRequest() {
		return PDF_REQUEST;
	}
	
	@Override
	public String getPdfProspectRequest() {
		return PROSPECT_PDF_REQUEST;
	}
	
	@Override
	public PlaceController getPlaceController()	{
		return placeController;
	}

	@Override
	public EventBus getEventBus() {
		return eventBus;
	}
	
	@Override
	public String getRegisterAccountUrl() {
		return "";
	}
	
	@SuppressWarnings("unchecked")
	private <T extends View<?>> T getView(Class<?> cl, View<?> view){
		views.put(cl, view);
		view.reset();
		return (T)view;
	}
	
	@SuppressWarnings("unchecked")
	private <T extends View<?>> T getView(Class<?> cl){
		View<?> view = views.get(cl);
		view.reset();
		return (T)view;
	}
	
//	@Override
//	public void getHomeView(AsyncCallback<HomeView> callback) {
//		if(views.containsKey(HomeView.class)){
//			callback.onSuccess((HomeView) getView(HomeView.class));
//		} else {
//			callback.onSuccess((HomeView) getView(HomeView.class, new HomeViewImpl()));
//		}
//	}

	@Override
	public void getInvoiceView(final boolean readonly, final AsyncCallback<InvoiceView> callback) {
		if(views.containsKey(InvoiceView.class)){
			callback.onSuccess((InvoiceView) getView(InvoiceView.class));
		} else {
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callback.onSuccess((InvoiceView) getView(InvoiceView.class, new InvoiceViewImpl(readonly)));
				}
				
				@Override
				public void onFailure(Throwable reason) {
					Window.Location.reload();
				}
			});
		}
	}
	
	@Override
	public void getEstimationView(final AsyncCallback<EstimationView> callback) {
		if(views.containsKey(EstimationView.class)){
			callback.onSuccess((EstimationView) getView(EstimationView.class));
		} else {
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callback.onSuccess((EstimationView) getView(EstimationView.class, new EstimationViewImpl()));
				}
				
				@Override
				public void onFailure(Throwable reason) {
					Window.Location.reload();
				}
			});
		}
	}

	@Override
	public void getBusinessView(final AsyncCallback<BusinessView> callback) {
		if(views.containsKey(BusinessView.class)){
			callback.onSuccess((BusinessView) getView(BusinessView.class));
		} else {
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callback.onSuccess((BusinessView) getView(BusinessView.class, new BusinessViewImpl()));
				}
				
				@Override
				public void onFailure(Throwable reason) {
					Window.Location.reload();
				}
			});
		}
	}
//
//	@Override
//	public void getClientView(final AsyncCallback<ClientView> callback) {
//		if(views.containsKey(ClientView.class)){
//			callback.onSuccess((ClientView) getView(ClientView.class));
//		} else {
//			GWT.runAsync(new RunAsyncCallback() {
//				
//				@Override
//				public void onSuccess() {
//					callback.onSuccess((ClientView) getView(ClientView.class, new ClientViewImpl()));
//				}
//				
//				@Override
//				public void onFailure(Throwable reason) {
//					Window.Location.reload();
//				}
//			});
//		}
//	}
	
	@Override
	public void getCreditNoteView(final AsyncCallback<CreditNoteView> callback) {
		if(views.containsKey(CreditNoteView.class)){
			callback.onSuccess((CreditNoteView) getView(CreditNoteView.class));
		} else {
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callback.onSuccess((CreditNoteView) getView(CreditNoteView.class, new CreditNoteViewImpl()));
				}
				
				@Override
				public void onFailure(Throwable reason) {
					Window.Location.reload();
				}
			});
		}
	}
	
	@Override
	public void getTransportDocumentView(final AsyncCallback<TransportDocumentView> callback) {
		if(views.containsKey(TransportDocumentView.class)){
			callback.onSuccess((TransportDocumentView) getView(TransportDocumentView.class));
		} else {
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callback.onSuccess((TransportDocumentView) getView(TransportDocumentView.class, new TransportDocumentViewImpl()));
				}
				
				@Override
				public void onFailure(Throwable reason) {
					Window.Location.reload();
				}
			});
		}
	}
	
	@Override
	public void getPaymentView(final AsyncCallback<PaymentView> callback) {
		if(views.containsKey(PaymentView.class)){
			callback.onSuccess((PaymentView) getView(PaymentView.class));
		} else {
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callback.onSuccess((PaymentView) getView(PaymentView.class, new PaymentViewImpl()));
				}
				
				@Override
				public void onFailure(Throwable reason) {
					Window.Location.reload();
				}
			});
		}
	}
//
//	@Override
//	public void getStandardWestView(final AsyncCallback<StandardWestView> callback) {
//		if(views.containsKey(StandardWestView.class)){
//			callback.onSuccess((StandardWestView) getView(StandardWestView.class));
//		} else {
//			callback.onSuccess((StandardWestView) getView(StandardWestView.class, new StandardWestViewImpl()));
//		}
//	}
//	
//	@Override
//	public void getEmptyWestView(AsyncCallback<EmptyWestView> callback) {
//		if(views.containsKey(EmptyWestView.class)){
//			callback.onSuccess((EmptyWestView) getView(EmptyWestView.class));
//		} else {
//			callback.onSuccess((EmptyWestView) getView(EmptyWestView.class, new EmptyWestViewImpl()));
//		}
//	}
//	
//	@Override
//	public void getConfigurationWestView(AsyncCallback<ConfigurationWestView> callback) {
//		if(views.containsKey(ConfigurationWestView.class)){
//			callback.onSuccess((ConfigurationWestView) getView(ConfigurationWestView.class));
//		} else {
//			callback.onSuccess((ConfigurationWestView) getView(ConfigurationWestView.class, new ConfigurationWestViewImpl()));
//		}
//	}
	
}
