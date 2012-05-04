package com.novadart.novabill.domain;

import java.util.Comparator;

public class AccountingDocumentComparator implements Comparator<AccountingDocument> {
	
	@Override
	public int compare(AccountingDocument abInv1, AccountingDocument abInv2) {
		if( !abInv1.getClass().equals(abInv2.getClass()) ){
			return 0;
		}
		
		int cmpYear = abInv1.getInvoiceYear().compareTo(abInv2.getInvoiceYear());
		if(cmpYear > 0) return -1;
		if(cmpYear < 0) return 1;
		
		int cmpInvId;
		if(abInv1 instanceof Invoice){
			cmpInvId = abInv1.getDocumentID().compareTo(abInv2.getDocumentID());
		} else {
			cmpInvId = abInv1.getId().compareTo(abInv2.getId());
		}
		if(cmpInvId > 0) return -1;
		if(cmpInvId < 0) return 1;
		return 0;
	}

}
