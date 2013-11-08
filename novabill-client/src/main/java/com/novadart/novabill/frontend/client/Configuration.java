package com.novadart.novabill.frontend.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.shared.client.dto.BusinessDTO;

public class Configuration {

//	private static Dictionary businessJS;
//
	private static BusinessDTO business;
//	private static BusinessStatsDTO stats;
//	private static long notesBitMask;
//	private static boolean devMode;
//
//
//	private static void loadConfiguration(final AsyncCallback<Void> callback){
//		try {
//			injectCss();
//
//			devMode = Boolean.parseBoolean(readDevMode());
//
//			notesBitMask = Long.parseLong(readNotesBitMask());
//
//			Map<String, String> values = new HashMap<String, String>();
//
//			//load all the values
//			if(businessExists() && loadBusinessValues(values)){
//				
//				BusinessDTO business = new BusinessDTO();
//				setBusiness(business);
//
//				business.setId( Long.parseLong(businessJS.get("id")) );
//
//				business.setAddress(values.get("address"));
//				business.setCity(values.get("city"));
//				business.setCountry(values.get("country"));
//				business.setEmail(values.get("email"));
//				business.setFax(values.get("fax"));
//				business.setMobile(values.get("mobile"));
//				business.setName(values.get("name"));
//				business.setPhone(values.get("phone"));
//				business.setPostcode(values.get("postcode"));
//				business.setProvince(values.get("province"));
//				business.setSsn(values.get("ssn"));
//				business.setVatID(values.get("vatID"));
//				business.setWeb(values.get("web"));
//				business.setPremium(Boolean.valueOf(values.get("premium")));
//
//				callback.onSuccess(null);
//
//			} else {
//
//				GWT.runAsync(new RunAsyncCallback() {
//
//					@Override
//					public void onSuccess() {
//						final BootstrapDialog bd = new BootstrapDialog();
//						bd.setHandler(new BootstrapDialog.Handler() {
//
//							@Override
//							public void businessData(final BusinessDTO business) {
//
//								ServerFacade.INSTANCE.getBusinessService().add(business, new ManagedAsyncCallback<Long>() {
//
//									@Override
//									public void onSuccess(Long result) {
//										Window.Location.reload();
//									}
//
//									@Override
//									public void onFailure(Throwable caught) {
//										callback.onFailure(caught);
//									}
//
//								});
//							}
//						});
//
//						bd.showCentered();
//
//					}
//
//					@Override
//					public void onFailure(Throwable reason) {
//						Window.Location.reload();
//					}
//				});
//
//			}
//
//
//
//		} catch(Exception e){
//			callback.onFailure(e);
//		}
//	}
//
//
//	private static native String readNotesBitMask()/*-{
//		return $wnd.notesBitMask;
//	}-*/;
//
//	private static native String readDevMode()/*-{
//		return $wnd.devMode;
//	}-*/;
//
//	public static boolean isDevMode() {
//		return devMode;
//	}
//
//	public static long getNotesBitMask() {
//		return notesBitMask;
//	}
//
//	public static void setNotesBitMask(long notesBitMask) {
//		Configuration.notesBitMask = notesBitMask;
//	}
//
//	public static boolean isPremium() {
//		return business.isPremium();
//	}
//
	public static BusinessDTO getBusiness() {
		return business;
	}

	public static Long getBusinessId() {
		return business.getId();
	}
//
//	public static void setBusiness(BusinessDTO business) {
//		Configuration.business = business;
//	}
//
//	public static BusinessStatsDTO getStats() {
//		return stats;
//	}
//
//	public static void setStats(BusinessStatsDTO stats) {
//		Configuration.stats = stats;
//	}
//
//	private static boolean businessExists(){
//		try {
//			businessJS = Dictionary.getDictionary("business");
//			return true;
//		} catch (MissingResourceException e) {
//			return false;
//		}
//	}
//	
//	private static boolean loadBusinessValues(Map<String, String> vals){
//		boolean valuesLoaded = true;
//
//		for(String key : new String[]{"address","city","country","email","fax","mobile","name",
//				"phone","postcode","province","premium","ssn","vatID","web"}){
//			try {
//				vals.put(key, businessJS.get(key));
//			} catch(MissingResourceException e) {
//				vals.put(key, "");
//				valuesLoaded = false;
//			}
//		}
//		return valuesLoaded;
//	}
	
	
	private static native String readBusinessId()/*-{
		return $wnd.NovabillConf.businessId;
	}-*/;
	
	public static void init(final AsyncCallback<Void> callback){
		ServerFacade.INSTANCE.getBusinessService().get(Long.parseLong(readBusinessId()), new ManagedAsyncCallback<BusinessDTO>() {

			@Override
			public void onSuccess(BusinessDTO result) {
				business = result;
				callback.onSuccess(null);
			}
		});
	}

	public static void injectCss() {
		GlobalBundle.INSTANCE.dialog().ensureInjected();
		GlobalBundle.INSTANCE.globalCss().ensureInjected();
		GlobalBundle.INSTANCE.loaderButton().ensureInjected();
		GlobalBundle.INSTANCE.richTextBoxCss().ensureInjected();
		GlobalBundle.INSTANCE.validatedWidget().ensureInjected();
	}

}
