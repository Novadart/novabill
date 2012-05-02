package com.novadart.novabill.domain;

import java.util.Comparator;

public class AbstractInvoiceComparator implements Comparator<AbstractInvoice> {
	
	@Override
	public int compare(AbstractInvoice abInv1, AbstractInvoice abInv2) {
		if( !abInv1.getClass().equals(abInv2.getClass()) ){
			return 0;
		}
		
		int cmpYear = abInv1.getInvoiceYear().compareTo(abInv2.getInvoiceYear());
		if(cmpYear > 0) return -1;
		if(cmpYear < 0) return 1;
		
		int cmpInvId;
		if(abInv1 instanceof Invoice){
			cmpInvId = abInv1.getInvoiceID().compareTo(abInv2.getInvoiceID());
		} else {
			cmpInvId = abInv1.getId().compareTo(abInv2.getId());
		}
		if(cmpInvId > 0) return -1;
		if(cmpInvId < 0) return 1;
		return 0;
	}

}
