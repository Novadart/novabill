package com.novadart.novabill.report;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.novadart.novabill.domain.AccountingDocument;

public class JRDataSourceFactory {
	
	public static <T extends AccountingDocument> JRBeanCollectionDataSource createDataSource(T doc, Long businessID){
		return new AccountingDocumentJRDataSource<T>(doc, doc.getAccountingDocumentItems(), businessID).getDataSource();
	}

}
