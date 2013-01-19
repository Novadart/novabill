package com.novadart.novabill.frontend.client.widget.tip;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.i18n.I18N;

public class TipFactory {
	
	public static void show(Tips t, HasOneWidget w){
		if ((Configuration.getNotesBitMask() & (1L << t.ordinal())) != Tip.TIP_DISABLED) {
			Tip tip = new Tip(t, getMessage(t));
			w.setWidget(tip);
		}
	}
	
	
	private static SafeHtml getMessage(Tips t){
		SafeHtmlBuilder shb = new SafeHtmlBuilder();
		switch (t) {
		case center_home_welcome:
			shb.appendHtmlConstant("<p>"+I18N.INSTANCE.welcomeMessage1()+"</p>");
			shb.appendHtmlConstant("<p>"+I18N.INSTANCE.welcomeMessage2()+"</p>");
			shb.appendHtmlConstant("<p>"+I18N.INSTANCE.welcomeMessage3()+"</p>");
			break;
			
		case center_home_yourdocs:
			shb.appendEscaped(I18N.INSTANCE.welcomeMessageDocuments());
			break;

		case west_home_no_clients:
			shb.appendEscaped(I18N.INSTANCE.welcomeMessageClients());
			break;
		}
		return shb.toSafeHtml();
	}

}
