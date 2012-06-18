package com.novadart.novabill.service.validator;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.validation.ErrorCode;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;
import com.novadart.novabill.shared.client.validation.InvoiceErrorObject;


@Service
public class InvoiceValidator extends AccountingDocumentValidator {
	
	@Autowired
	private UtilsService utilsService;
	
	private List<Long> computeDocumentIDGaps(List<Long> invoiceIDs, int max){
		int size = invoiceIDs.size(); 
		if(size == 0)
			return new ArrayList<Long>();
		BitSet invoiceIDsBSet = new BitSet(invoiceIDs.get(size - 1).intValue() - 1);
		for(Long invoiceID: invoiceIDs)
			invoiceIDsBSet.set(invoiceID.intValue() - 1);
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
		Business authenticatedBusiness = utilsService.getAuthenticatedPrincipalDetails().getPrincipal();
		List<Invoice> persistedInvoices  = authenticatedBusiness.getInvoiceByIdInYear(invoice.getDocumentID(), invoice.getAccountingDocumentYear()); 
		for(Invoice persistedInvoice : persistedInvoices){
			if(!persistedInvoice.getId().equals(invoice.getId())){//same documentID, but different id: error!
				List<Long> invoiceIDs = authenticatedBusiness.getCurrentYearInvoicesDocumentIDs();
				List<Long> gaps = computeDocumentIDGaps(invoiceIDs, 10);
				if(gaps.size() > 0){
					for(Long gap: gaps)
						gapsAccumulator.add(gap);
				}else
					gapsAccumulator.add(authenticatedBusiness.getNextInvoiceDocumentID());
				return false;
			}
		}
		return true;
	}
	
	public void validate(Invoice invoice) throws ValidationException{
		boolean isValid = true;
		List<ErrorObject> errors = new ArrayList<ErrorObject>();
		try{
			super.validate(invoice); //JSR-303 validation
		}catch (ValidationException e) {
			errors.addAll(e.getErrors());
			isValid = false;
		}
		//validate documentID
		List<Long> gapsAccumulator = new ArrayList<Long>();
		isValid &= validateInvoiceDocumentID(invoice, gapsAccumulator);
		if(gapsAccumulator.size() > 0)
			errors.add(new InvoiceErrorObject(Field.documentID, ErrorCode.INVALID_DOCUMENT_ID, gapsAccumulator));
		if(!isValid)
			throw new ValidationException(errors);
	}

}
