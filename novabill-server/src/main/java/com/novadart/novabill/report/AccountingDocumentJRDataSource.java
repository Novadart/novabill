package com.novadart.novabill.report;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.novadart.novabill.domain.AccountingDocument;
import com.novadart.novabill.domain.Logo;

public class AccountingDocumentJRDataSource<T extends AccountingDocument> {
	
	private T document;
	
	private InputStream logoInStream;
	
	public T getDocument(){
		return this.document;
	}
	
	public InputStream getLogoInStream() {
		return logoInStream;
	}

	public AccountingDocumentJRDataSource(T document, Long businessID){
		this(document, Logo.getLogoByBusinessID(businessID));
	}
	
	public AccountingDocumentJRDataSource(T document, Logo logo){
		this.document = document;
		if(logo != null && logo.getData() != null)
			logoInStream = new ByteArrayInputStream(logo.getData());
	}
	
	public JRBeanCollectionDataSource getDataSource(){
		Collection<AccountingDocumentJRDataSource<T>> coll = new ArrayList<AccountingDocumentJRDataSource<T>>();
		coll.add(this);
		return new JRBeanCollectionDataSource(coll);
	}

}
