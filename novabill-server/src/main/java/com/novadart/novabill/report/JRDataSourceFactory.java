package com.novadart.novabill.report;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.google.common.base.Splitter;
import com.novadart.novabill.domain.AccountingDocument;
import com.novadart.novabill.domain.AccountingDocumentItem;
import com.novadart.novabill.domain.Logo;
import com.novadart.novabill.shared.client.data.LayoutType;

public class JRDataSourceFactory {
	
	public static int INVOICE_DENSE_TABLE_ROWS_NUM = 28;
	
	public static int CREDNOTE_DENSE_TABLE_ROWS_NUM = 30;
	
	public static int ESTIMATION_DENSE_TABLE_ROWS_NUM = 28;
	
	public static int TRANSPORTDOC_DENSE_TABLE_ROWS_NUM = 23;
	
	public static int DENSE_DESC_WIDTH_IN_CHARS = 82;
	
	private static int getRowCount(DocumentType docType){
		switch (docType) {
		case INVOICE:
			return INVOICE_DENSE_TABLE_ROWS_NUM;
		case CREDIT_NOTE:
			return CREDNOTE_DENSE_TABLE_ROWS_NUM;
		case ESTIMATION:
			return ESTIMATION_DENSE_TABLE_ROWS_NUM;
		case TRANSPORT_DOCUMENT:
			return TRANSPORTDOC_DENSE_TABLE_ROWS_NUM;
		}
		throw new RuntimeException("Unknown layout type");
	}
	
	
	public static <T extends AccountingDocument> JRBeanCollectionDataSource createDataSourceds(T doc, LayoutType layoutType, DocumentType docType, Long businessID){
		if(LayoutType.TIDY.equals(layoutType))
			return new AccountingDocumentJRDataSource<T>(doc, doc.getAccountingDocumentItems(), businessID).getDataSource();
		if(LayoutType.DENSE.equals(layoutType)){
			List<AccountingDocumentItem> items = new ArrayList<>(doc.getAccountingDocumentItems());
			int rowCnt = 0;
			for(AccountingDocumentItem item: items){
				Iterator<String> iter = Splitter.onPattern("\r?\n").split(item.getDescription()).iterator();
				int lnCnt = 0;
				while(iter.hasNext()){
					String tk = iter.next();
					lnCnt += tk.length() / DENSE_DESC_WIDTH_IN_CHARS + (tk.length() % DENSE_DESC_WIDTH_IN_CHARS > 0? 1: 0);
				}
				rowCnt += Math.max(1, lnCnt / 2);
			}
			int denseTableRowsNum = getRowCount(docType);
			int r = rowCnt % denseTableRowsNum;
			Logo logo = Logo.getLogoByBusinessID(businessID);
			int rowsPerPage = denseTableRowsNum - r - (logo != null? 4: 0);
			for(int i = 0; i < rowsPerPage; ++i)
				items.add(new AccountingDocumentItem());
			return new AccountingDocumentJRDataSource<AccountingDocument>(doc, items, logo).getDataSource();
		}
		throw new RuntimeException("Unknown layout type");
		 
	}

}
