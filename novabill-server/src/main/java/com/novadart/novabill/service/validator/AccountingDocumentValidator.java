package com.novadart.novabill.service.validator;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import com.novadart.novabill.domain.AccountingDocument;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.validation.ErrorCode;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;

@Service
@Primary
public class AccountingDocumentValidator{
	
	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private SimpleValidator simpleValidator;
	
	private List<Long> computeDocumentIDGaps(List<Long> docIDs, int max){
		int size = docIDs.size(); 
		if(size == 0)
			return new ArrayList<Long>();
		BitSet docIDsBSet = new BitSet(docIDs.get(size - 1).intValue() - 1);
		for(Long docID: docIDs)
			docIDsBSet.set(docID.intValue() - 1);
		docIDsBSet.flip(0, docIDs.get(0).intValue() - 1);//flip all bits till the first document
		BitSet mask = new BitSet(docIDsBSet.length());
		mask.flip(0, docIDsBSet.length());
		docIDsBSet.xor(mask);
		List<Long> gaps = new ArrayList<Long>(Math.min(docIDsBSet.cardinality(), max));
		for(int i=docIDsBSet.nextSetBit(0), c = 0; i >= 0 && c < max; i = docIDsBSet.nextSetBit(i+1), c++)
			gaps.add(new Long(i + 1));
		return gaps;
	}
	
	private <T extends AccountingDocument> boolean validateDocumentID(Class<T> cls, T doc, List<Long> gapsAccumulator){
		Business authenticatedBusiness = utilsService.getAuthenticatedPrincipalDetails().getBusiness();
		for(T persistedDoc : authenticatedBusiness.getDocsByIdInYear(cls, doc.getDocumentID(), doc.getAccountingDocumentYear())){
			if(!persistedDoc.getId().equals(doc.getId())){//same documentID, but different id: error!
				List<Long> docIDs = authenticatedBusiness.getCurrentYearDocumentsIDs(cls);
				List<Long> gaps = computeDocumentIDGaps(docIDs, 10);
				if(gaps.size() > 0){
					for(Long gap: gaps)
						gapsAccumulator.add(gap);
				}else
					gapsAccumulator.add(authenticatedBusiness.getNextAccountingDocDocumentID(cls));
				return false;
			}
		}
		return true;
	}
	
	public <T extends AccountingDocument> void validate(Class<T> cls, T doc) throws ValidationException{
		boolean isValid = true;
		List<ErrorObject> errors = new ArrayList<ErrorObject>();
		try{
			simpleValidator.validate(doc); //JSR-303 validation
		}catch (ValidationException e) {
			errors.addAll(e.getErrors());
			isValid = false;
		}
		//validate documentID
		List<Long> gapsAccumulator = new ArrayList<Long>();
		isValid &= validateDocumentID(cls, doc, gapsAccumulator);
		if(gapsAccumulator.size() > 0)
			errors.add(new ErrorObject(Field.documentID, ErrorCode.INVALID_DOCUMENT_ID, gapsAccumulator));
		if(!isValid)
			throw new ValidationException(errors);
	}

}
