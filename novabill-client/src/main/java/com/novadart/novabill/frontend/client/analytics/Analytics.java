package com.novadart.novabill.frontend.client.analytics;

import com.novadart.novabill.frontend.client.Configuration;

public class Analytics {
	
	public static void trackEvent(String category, String action, String label) {
		if(!Configuration.isDevMode()){
			_trackEvent(category, action, label);
		}
	}

	public static void trackEvent(String category, String action, String label, int intArg) {
		if(!Configuration.isDevMode()){
			_trackEvent(category, action, label, intArg);
		}
	}

	public static void trackPlaceview(String url) {
		if(!Configuration.isDevMode()){
			_trackPlaceview(url);
		}
	}


	private static native void _trackEvent(String category, String action, String label) /*-{
		$wnd._gaq.push(['_trackEvent', category, action, label]);
	}-*/;
	
	private static native void _trackEvent(String category, String action, String label, int intArg) /*-{
		$wnd._gaq.push(['_trackEvent', category, action, label, intArg]);
	}-*/;
	
	private static native void _trackPlaceview(String url) /*-{
		$wnd._gaq.push(['_trackPageview',  '/private#'+url]);
	}-*/;
	
}
