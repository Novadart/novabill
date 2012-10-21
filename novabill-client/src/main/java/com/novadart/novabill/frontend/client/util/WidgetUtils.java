package com.novadart.novabill.frontend.client.util;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;

public class WidgetUtils {
	
	
	public static void setElementHeightToFillSpace(Element target, Element container, int offset, Element... surroundingElements){
		int contHeight = container.getOffsetHeight();
		int surroundingHeightSum = 0;
		for (Element elm : surroundingElements) {
			surroundingHeightSum += elm.getOffsetHeight();
		}
		target.getStyle().setHeight(contHeight - surroundingHeightSum - offset, Unit.PX);
	}

	public static void setElementHeightToFillSpace(Element target, Element container, Element... surroundingElements){
		setElementHeightToFillSpace(target, container, 0, surroundingElements);
	}
	
}
