package com.novadart.novabill.frontend.client.util;

import com.google.gwt.user.client.Window;

public class NativePopupUtils {

	private static final String POPUP_PARAMS = 
			"width={w},height={h},top={t},left={l},status=no,menubar=no,scrollbars=no,location=no,resizable=no";

	public static void showCentered(String url, int width, int height){
		int left = (Window.getClientWidth() - width) / 2;
		int top = (Window.getClientHeight() - height) / 2;
		show(url, width, height, top, left);
	}
	
	public static void show(String url, int width, int height, int top, int left){
		String params = POPUP_PARAMS.replace("{w}", width+"")
				.replace("{h}", height+"").replace("{t}", top+"").replace("{l}", left+"");
		
		Window.open(url, "_blank", params);
	}
	
}
