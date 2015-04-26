package com.novadart.novabill.domain.dto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.novadart.novabill.domain.AccountingDocument;
import com.novadart.novabill.domain.CreditNote;
import com.novadart.novabill.domain.Estimation;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.TransportDocument;
import com.novadart.novabill.domain.dto.transformer.CreditNoteDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.EstimationDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.InvoiceDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.TransportDocumentDTOTransformer;
import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;

public class DTOUtils {

	
	/**
	 *	Conversion 
	 */
	
	public static <T1 extends AccountingDocumentDTO, T2 extends AccountingDocument> List<T1> toDTOList(List<T2> docs, ToDTOConverter<T1, T2> converter, boolean copyItems){
		List<T1> docDTOs = new ArrayList<T1>();
		for(T2 doc: docs)
			docDTOs.add(converter.toDTO(doc, copyItems));
		return docDTOs;
	}
	
	public interface ToDTOConverter<T1 extends AccountingDocumentDTO, T2 extends AccountingDocument>{
		public T1 toDTO(T2 doc, boolean copyItems);
	}
	
	public static ToDTOConverter<InvoiceDTO, Invoice> invoiceDTOConverter = new ToDTOConverter<InvoiceDTO, Invoice>(){
		@Override
		public InvoiceDTO toDTO(Invoice doc, boolean copyItems){
			return InvoiceDTOTransformer.toDTO(doc, copyItems);
		}
	};
	
	public static ToDTOConverter<CreditNoteDTO, CreditNote> creditNoteDTOConverter = new ToDTOConverter<CreditNoteDTO, CreditNote>(){
		@Override
		public CreditNoteDTO toDTO(CreditNote doc, boolean copyItems){
			return CreditNoteDTOTransformer.toDTO(doc, copyItems);
		}
	};
	
	public static ToDTOConverter<EstimationDTO, Estimation> estimationDTOConverter = new ToDTOConverter<EstimationDTO, Estimation>() {
		@Override
		public EstimationDTO toDTO(Estimation doc, boolean copyItems) {
			return EstimationDTOTransformer.toDTO(doc, copyItems);
		}
	};
	
	public static ToDTOConverter<TransportDocumentDTO, TransportDocument> transportDocDTOConverter = new ToDTOConverter<TransportDocumentDTO, TransportDocument>() {
		@Override
		public TransportDocumentDTO toDTO(TransportDocument doc, boolean copyItems) {
			return TransportDocumentDTOTransformer.toDTO((TransportDocument)doc, copyItems);
		}
	};
	/*
	 * 
	 */
	
	public static <T extends AccountingDocumentDTO> T findDocumentInCollection(Collection<T> collection, Long id) throws NoSuchObjectException{
		Iterator<T> iter = collection.iterator();
		T doc;
		while(iter.hasNext())
			if((doc = iter.next()).getId().equals(id))
				return doc;
		throw new NoSuchObjectException();
	}
	
	public static interface Predicate<T extends AccountingDocumentDTO>{
		public boolean isTrue(T doc);
	}
	
	public static class EqualsYearPredicate<T extends AccountingDocumentDTO> implements Predicate<T>{
		
		private int year;
		
		public EqualsYearPredicate(int year) {
			this.year = year;
		}

		@Override
		public boolean isTrue(T doc) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(doc.getAccountingDocumentDate());
			return year == calendar.get(Calendar.YEAR);
		}
		
	}
	
	public static <T extends AccountingDocumentDTO> Collection<T> filter(Collection<T> collection, Predicate<T> pred){
		ArrayList<T> filtered = new ArrayList<T>(collection.size());
		Iterator<T> iter = collection.iterator();
		T doc;
		while(iter.hasNext())
			if(pred.isTrue(doc = iter.next()))
				filtered.add(doc);
		return filtered;
	}
	
	public static <E> List<E> range(List<E> list, Integer start, Integer length){
		return new ArrayList<E>(list.subList(start, Math.min(start + length, list.size())));
	}
	
	public static boolean compareItems(AccountingDocumentDTO lhs, AccountingDocumentDTO rhs, boolean ignoreID){
		if(lhs.getItems().size() != rhs.getItems().size())
			return false;
		for(int i = 0; i < lhs.getItems().size(); ++i){
			if(!ignoreID && !EqualsBuilder.reflectionEquals(lhs.getItems().get(i), rhs.getItems().get(i), false))
				return false;
			if(ignoreID && !EqualsBuilder.reflectionEquals(lhs.getItems().get(i), rhs.getItems().get(i), "id"))
				return false;
		}
		return true;
	}
	
}
