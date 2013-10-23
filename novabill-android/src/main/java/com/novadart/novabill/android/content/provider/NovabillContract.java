package com.novadart.novabill.android.content.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class NovabillContract {
	
	public static final String AUTHORITY = "com.novadart.novabill";
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
	
	public static final class Items implements BaseColumns {
		
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.com.novadart.novabill.novabill_items";
		
		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.com.novadart.novabill.novabill_items";
		
	}
	
	public static final class Clients implements BaseColumns {
		
		public static final Uri CONTENT_URI = Uri.withAppendedPath(NovabillContract.CONTENT_URI, "clients");
		
		public static final String NAME = "name";
		public static final String ADDRESS = "address";
		public static final String POSTCODE = "postcode";
		public static final String CITY = "city";
		public static final String PROVINCE = "province";
		public static final String COUNTRY = "country";
		public static final String EMAIL = "email";
		public static final String PHONE = "phone";
		public static final String MOBILE = "mobile";
		public static final String FAX = "fax";
		public static final String WEB = "web";
		public static final String VAT_ID = "vatID";
		public static final String SSN = "ssn";
		public static final String VERSION = "version";
		
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.com.novadart.novabill.novabill_clients";
		
		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.com.novadart.novabill.novabill_clients";
		
		public static final String[] PROJECTION_ALL = {_ID, NAME, ADDRESS, POSTCODE, CITY, PROVINCE, COUNTRY, EMAIL, PHONE, MOBILE, FAX, WEB, VAT_ID, SSN, VERSION};
		
		public static final String SORT_ORDER_DEFAULT = NAME + " ASC";
		
		
	}

}
