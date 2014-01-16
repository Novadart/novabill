package com.novadart.novabill.report;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.novadart.novabill.domain.AccountingDocument;
import com.novadart.novabill.domain.AccountingDocumentItem;
import com.novadart.novabill.domain.Logo;

public class AccountingDocumentJRDataSource<T extends AccountingDocument> {
	
	private T document;
	
	private InputStream logoInStream;
	
	private List<AccountingDocumentItem> items;
	
	public T getDocument(){
		return this.document;
	}
	
	public InputStream getLogoInStream() {
		return logoInStream;
	}

	public List<AccountingDocumentItem> getItems() {
		return items;
	}

	public AccountingDocumentJRDataSource(T document, List<AccountingDocumentItem> items, Long businessID){
		this(document, items, Logo.getLogoByBusinessID(businessID));
	}
	
	public AccountingDocumentJRDataSource(T document, List<AccountingDocumentItem> items, Logo logo){
		this.document = document;
		this.items = items;
		if(logo != null && logo.getData() != null)
			logoInStream = new ByteArrayInputStream(logo.getData());
	}
	
	public JRBeanCollectionDataSource getDataSource(){
		Collection<AccountingDocumentJRDataSource<T>> coll = new ArrayList<AccountingDocumentJRDataSource<T>>();
		coll.add(this);
		return new JRBeanCollectionDataSource(coll);
	}

}
