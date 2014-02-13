package com.novadart.novabill.domain;

import java.util.Comparator;

public class AccountingDocumentComparator implements Comparator<AccountingDocument> {
	
	@Override
	public int compare(AccountingDocument abInv1, AccountingDocument abInv2) {
		if( !abInv1.getClass().equals(abInv2.getClass()) ){
			return 0;
		}
		
		int cmpYear = abInv1.getAccountingDocumentYear().compareTo(abInv2.getAccountingDocumentYear());
		if(cmpYear > 0) return -1;
		if(cmpYear < 0) return 1;
		
		int cmpDocId = abInv1.getDocumentID().compareTo(abInv2.getDocumentID());
		if(cmpDocId > 0) return -1;
		if(cmpDocId < 0) return 1;
		return 0;
	}

}
