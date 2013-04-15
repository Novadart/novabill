package com.novadart.novabill.report;

import java.util.ArrayList;
import java.util.Collection;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.novadart.novabill.domain.AccountingDocument;

public class AccountingDocumentJRDataSource<T extends AccountingDocument> {
	
	private T document;
	
	public T getDocument(){
		return this.document;
	}
	
	public AccountingDocumentJRDataSource(T document){
		this.document = document;
	}
	
	public JRBeanCollectionDataSource getDataSource(){
		Collection<AccountingDocumentJRDataSource<T>> coll = new ArrayList<AccountingDocumentJRDataSource<T>>();
		coll.add(this);
		return new JRBeanCollectionDataSource(coll);
	}

}
