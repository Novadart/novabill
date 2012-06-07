package com.novadart.novabill.service.validator;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.validation.ErrorCode;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;


@Service
public class InvoiceValidator {
	
	@Autowired
	private UtilsService utilsService;
	
	private List<Long> computeDocumentIDGaps(List<Long> invoiceIDs, int max){
		int size = invoiceIDs.size(); 
		if(size == 0)
			return new ArrayList<Long>();
		BitSet invoiceIDsBSet = new BitSet(invoiceIDs.get(size - 1).intValue() - 1);
		for(Long invoiceID: invoiceIDs)
			invoiceIDsBSet.flip(invoiceID.intValue() - 1);
		invoiceIDsBSet.flip(0, invoiceIDs.get(0).intValue() - 1);//flip all bits till the first invoice
		BitSet mask = new BitSet(invoiceIDsBSet.length());
		mask.flip(0, invoiceIDsBSet.length());
		invoiceIDsBSet.xor(mask);
		List<Long> gaps = new ArrayList<Long>(Math.min(invoiceIDsBSet.cardinality(), max));
		for(int i=invoiceIDsBSet.nextSetBit(0), c = 0; i >= 0 && c < max; i = invoiceIDsBSet.nextSetBit(i+1), c++)
			gaps.add(new Long(i + 1));
		return gaps;
	}
	
	private boolean validateInvoiceDocumentID(Invoice invoice, List<Long> gapsAccumulator){
		List<Long> invoiceIDs = utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getCurrentYearInvoicesDocumentIDs();
		if(invoiceIDs.size() == 0)//first invoice
			return true;
		invoiceIDs.add(invoice.getDocumentID());
		List<Long> gaps = computeDocumentIDGaps(invoiceIDs, 10);
		if(gaps.size() > 0){
			for(Long gap: gaps)
				gapsAccumulator.add(gap);
			return false;
		}
		return true;
	}
	
	public void validate(Invoice invoice) throws ValidationException{
		boolean isValid = true;
		List<ErrorObject> errors = new ArrayList<ErrorObject>(); 
		//validate documentID
		List<Long> gapsAccumulator = new ArrayList<Long>();
		isValid &= validateInvoiceDocumentID(invoice, gapsAccumulator);
		if(gapsAccumulator.size() > 0)
			errors.add(new ErrorObject(Field.DOCUMENT_ID, ErrorCode.INVALID_DOCUMENT_ID, gapsAccumulator));
		if(!isValid)
			throw new ValidationException(errors);
	}

}
