package com.novadart.novabill.frontend.client;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.ui.bootstrap.BootstrapDialog;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;

public class Configuration {

	private final static Dictionary businessJS = Dictionary.getDictionary("business");

	private static BusinessDTO business;
	private static BusinessStatsDTO stats;

	public static final void init(final WrappedAsyncCallback<Void> callback){
		try {
			Map<String, String> values = new HashMap<String, String>();
			
			BusinessDTO business = new BusinessDTO();
			setBusiness(business);
			
			business.setId( Long.parseLong(businessJS.get("id")) );
			
			if(loadBusinessValues(values)){
				
				business.setAddress(values.get("address"));
				business.setCity(values.get("city"));
				business.setCountry(values.get("country"));
				business.setEmail(values.get("email"));
				business.setFax(values.get("fax"));
				business.setMobile(values.get("mobile"));
				business.setName(values.get("name"));
				business.setPhone(values.get("phone"));
				business.setPostcode(values.get("postcode"));
				business.setProvince(values.get("province"));
				business.setSsn(values.get("ssn"));
				business.setVatID(values.get("vatID"));
				business.setWeb(values.get("web"));
				business.setPremium(Boolean.valueOf(values.get("premium")));
				
				callback.onSuccess(null);
				
			} else {
				
				GWT.runAsync(new RunAsyncCallback() {
					
					@Override
					public void onSuccess() {
						final BootstrapDialog bd = new BootstrapDialog();
						bd.setHandler(new BootstrapDialog.Handler() {
							
							@Override
							public void businessData(final BusinessDTO business) {
								
								ServerFacade.business.update(business, new AsyncCallback<Void>() {
									
									@Override
									public void onSuccess(Void result) {
										bd.hide();
										setBusiness(business);
										callback.onSuccess(null);
									}
									
									@Override
									public void onFailure(Throwable caught) {
										callback.onException(caught);
									}
								});
							}
						});
						
						bd.showCentered();
						
					}
					
					@Override
					public void onFailure(Throwable reason) {
						Window.Location.reload();
					}
				});
				
			}
			
			
			
		} catch(Exception e){
			callback.onException(e);
		}
	}

	public static boolean isPremium() {
		return business.isPremium();
	}
	
	public static BusinessDTO getBusiness() {
		return business;
	}
	
	public static Long getBusinessId() {
		return business.getId();
	}

	public static void setBusiness(BusinessDTO business) {
		Configuration.business = business;
	}

	public static BusinessStatsDTO getStats() {
		return stats;
	}

	public static void setStats(BusinessStatsDTO stats) {
		Configuration.stats = stats;
	}
	
	private static boolean loadBusinessValues(Map<String, String> vals){
		boolean valuesLoaded = true;
		
		for(String key : new String[]{"address","city","country","email","fax","mobile","name",
				"phone","postcode","province","premium","ssn","vatID","web"}){
			try {
				vals.put(key, businessJS.get(key));
			} catch(MissingResourceException e) {
				vals.put(key, "");
				valuesLoaded = false;
			}
		}
		return valuesLoaded;
	}

}
