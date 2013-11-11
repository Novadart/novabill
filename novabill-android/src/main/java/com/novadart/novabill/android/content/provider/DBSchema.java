package com.novadart.novabill.android.content.provider;

public final class DBSchema {
	
	
	public static final class UserTbl {
		
		public static final String TABLE_NAME = "user";
		
		public static final String EMAIL = "email";
		
		public static final String _ID = "_id";
		
	}
	
	public static final class SyncMarkTbl {
		
		public static final String TABLE_NAME = "sync_mark";
		
		public static final String MARK = "mark";
		
		public static final String USER_ID = "user_id";
		
		public static final Long INIT_MARK = -1l;
		
	}
	
	public static final class ClientTbl {
		public static final String TABLE_NAME = "client";
		
		public static final String _ID = "_id";
		
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
		
		public static final String SERVER_ID = "server_id";
		
		public static final String USER_ID = "user_id";
	}
	

}
