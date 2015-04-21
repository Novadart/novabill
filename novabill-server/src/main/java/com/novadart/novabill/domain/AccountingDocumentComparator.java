package com.novadart.novabill.domain;

import com.google.common.base.Strings;

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

		String doc1Suffix = Strings.nullToEmpty(abInv1.getDocumentIDSuffix());
		String doc2Suffix = Strings.nullToEmpty(abInv2.getDocumentIDSuffix());
		int cmpDocIdCls= doc1Suffix.compareTo(doc2Suffix);
		if(cmpDocIdCls != 0) return cmpDocIdCls;

		int cmpDocId = abInv1.getDocumentID().compareTo(abInv2.getDocumentID());
		if(cmpDocId > 0) return -1;
		if(cmpDocId < 0) return 1;

		return 0;
	}

}
