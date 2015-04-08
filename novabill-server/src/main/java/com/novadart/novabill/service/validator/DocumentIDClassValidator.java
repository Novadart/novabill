package com.novadart.novabill.service.validator;

import com.novadart.novabill.domain.DocumentIDClass;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.validation.ErrorCode;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentIDClassValidator {

    @Autowired
    private SimpleValidator validator;

    public void validate(DocumentIDClass docIDClass, boolean checkIfUniqueSuffix) throws ValidationException{
        boolean isValid = true;
        List<ErrorObject> errors = new ArrayList<>();
        try{
            validator.validate(docIDClass); //JSR-303 validation
        }catch (ValidationException e) {
            errors.addAll(e.getErrors());
            isValid = false;
        }
        if(checkIfUniqueSuffix){
            if(docIDClass.getSuffix() != null && docIDClass.suffixExists()){
                isValid = false;
                errors.add(new ErrorObject(Field.name, ErrorCode.NOT_UNIQUE));
            }
        }
        if(!isValid)
            throw new ValidationException(errors, docIDClass.getClass().getName());
    }

}
