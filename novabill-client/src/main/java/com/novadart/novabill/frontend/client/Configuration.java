package com.novadart.novabill.frontend.client;

import com.google.gwt.i18n.client.Dictionary;
import com.novadart.novabill.frontend.client.facade.AuthAwareAsyncCallback;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;

public class Configuration {

	private final static Dictionary configuration = Dictionary.getDictionary("business");

	private static BusinessDTO business;
	private static BusinessStatsDTO stats;


	public static final void init(AuthAwareAsyncCallback<Void> callback){
		try {
			
			BusinessDTO business = new BusinessDTO();
			business.setAddress(configuration.get("address"));
			business.setCity(configuration.get("city"));
			business.setCountry(configuration.get("country"));
			business.setEmail(configuration.get("email"));
			business.setFax(configuration.get("fax"));
			business.setId( Long.parseLong(configuration.get("id")) );
			business.setMobile(configuration.get("mobile"));
			business.setName(configuration.get("name"));
			business.setPhone(configuration.get("phone"));
			business.setPostcode(configuration.get("postcode"));
			business.setProvince(configuration.get("province"));
			business.setSsn(configuration.get("ssn"));
			business.setVatID(configuration.get("vatID"));
			business.setWeb(configuration.get("web"));
			
			setBusiness(business);
			
			callback.onSuccess(null);
			
		} catch(Exception e){
			callback.onFailure(e);
		}
	}

	public static BusinessDTO getBusiness() {
		return business;
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

}
