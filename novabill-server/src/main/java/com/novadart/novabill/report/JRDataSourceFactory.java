package com.novadart.novabill.report;

import com.novadart.novabill.domain.AccountingDocument;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.shared.client.data.FilteringDateType;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.util.Date;
import java.util.List;

public class JRDataSourceFactory {
	
	public static <T extends AccountingDocument> JRBeanCollectionDataSource createDataSource(T doc, Long businessID){
		return new AccountingDocumentJRDataSource<T>(doc, doc.getAccountingDocumentItems(), businessID).getDataSource();
	}
	
	public static JRBeanCollectionDataSource createDataSource(List<Invoice> invoices, Date startDate, Date endDate, FilteringDateType filteringDateType){
		return new PaymentsProspectJRDataSource(invoices, startDate, endDate, filteringDateType).getDataSource();
	}
	
}
