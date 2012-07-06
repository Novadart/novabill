package com.novadart.novabill.frontend.client.util;

import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.RootPanel;

public class FileDownloadUtils {
	
	private static Frame hiddenFrame = new Frame();
	
	static {
		hiddenFrame.setVisible(false);
		RootPanel.get().add(hiddenFrame);
	}
	
	public static void downloadUrl(String url){
		hiddenFrame.setUrl(url);
	}
	
}
