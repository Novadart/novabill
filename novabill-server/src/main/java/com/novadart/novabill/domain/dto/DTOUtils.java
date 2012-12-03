package com.novadart.novabill.domain.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import com.novadart.novabill.domain.AccountingDocument;
import com.novadart.novabill.domain.CreditNote;
import com.novadart.novabill.domain.Estimation;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.TransportDocument;
import com.novadart.novabill.domain.dto.factory.CreditNoteDTOFactory;
import com.novadart.novabill.domain.dto.factory.EstimationDTOFactory;
import com.novadart.novabill.domain.dto.factory.InvoiceDTOFactory;
import com.novadart.novabill.domain.dto.factory.TransportDocumentDTOFactory;
import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public class DTOUtils {

	
	/**
	 *	Conversion 
	 */
	
	public static <T1 extends AccountingDocumentDTO, T2 extends AccountingDocument> List<T1> toDTOList(List<T2> docs, ToDTOConverter<T1, T2> converter){
		List<T1> docDTOs = new ArrayList<T1>();
		for(T2 doc: docs)
			docDTOs.add(converter.toDTO(doc));
		return docDTOs;
	}
	
	public interface ToDTOConverter<T1 extends AccountingDocumentDTO, T2 extends AccountingDocument>{
		public T1 toDTO(T2 doc);
	}
	
	public static ToDTOConverter<InvoiceDTO, Invoice> invoiceDTOConverter = new ToDTOConverter<InvoiceDTO, Invoice>(){
		@Override
		public InvoiceDTO toDTO(Invoice doc){
			return InvoiceDTOFactory.toDTO(doc);
		}
	};
	
	public static ToDTOConverter<CreditNoteDTO, CreditNote> creditNoteDTOConverter = new ToDTOConverter<CreditNoteDTO, CreditNote>(){
		@Override
		public CreditNoteDTO toDTO(CreditNote doc){
			return CreditNoteDTOFactory.toDTO(doc);
		}
	};
	
	public static ToDTOConverter<EstimationDTO, Estimation> estimationDTOConverter = new ToDTOConverter<EstimationDTO, Estimation>() {
		@Override
		public EstimationDTO toDTO(Estimation doc) {
			return EstimationDTOFactory.toDTO(doc);
		}
	};
	
	public static ToDTOConverter<TransportDocumentDTO, TransportDocument> transportDocDTOConverter = new ToDTOConverter<TransportDocumentDTO, TransportDocument>() {
		@Override
		public TransportDocumentDTO toDTO(TransportDocument doc) {
			return TransportDocumentDTOFactory.toDTO((TransportDocument)doc);
		}
	};
	/*
	 * 
	 */
	
	public static <T extends AccountingDocumentDTO> T findDocumentInCollection(Collection<T> collection, Long id){
		Iterator<T> iter = collection.iterator();
		T doc;
		while(iter.hasNext())
			if((doc = iter.next()).getId().equals(id))
				return doc;
		return null;
	}
	
	public static interface Predicate<T extends AccountingDocumentDTO>{
		public boolean isTrue(T doc);
	}
	
	public static <T extends AccountingDocumentDTO> Collection<T> filter(Collection<T> collection, Predicate<T> pred){
		ArrayList<T> filtered = new ArrayList<T>();
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


}
