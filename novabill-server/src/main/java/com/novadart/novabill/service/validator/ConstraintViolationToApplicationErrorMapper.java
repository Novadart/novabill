package com.novadart.novabill.service.validator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Path.Node;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.stereotype.Service;

import com.novadart.novabill.annotation.PaymentDeltaNotNull;
import com.novadart.novabill.annotation.TaxFieldsNotNull;
import com.novadart.novabill.shared.client.validation.ErrorCode;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;

/*
 * ConstraintViolationToApplicationErrorMapper service class maps validation errors
 * to error codes ({@link com.novadart.novabill.shared.client.validation.ErrorCode})
 * that are understood by the GWT client. Care must be taken to ensure that property
 * paths of offending fields are known to the ConstraintViolationToApplicationErrorMapper
 * (specified in {@link com.novadart.novabill.shared.client.validation.Field}). The
 * translation from property path to {@link com.novadart.novabill.shared.client.validation.ErrorCode}
 * is straight forward: each doth '.' along the path is replaced with '_'. For example,
 * 'contact.name' field from TransportDocument is mapped to 'contact_name'. In case of
 * validation error in accounting document item field additional information, namely the index,
 * is provided.  For example if the offending field is 'accountingDocumentItems[0].description'
 * this path is mapped to 'accountingDocumentItems_description' and the index 0 is passed
 * as additional parameter.
 */
@Service
public class ConstraintViolationToApplicationErrorMapper {
	
	private static class PropertyMappingPair{
		
		private String propertyName;
		
		private Integer[] indexes;
		
		public PropertyMappingPair(String propertyName, Integer[] indexes){
			this.propertyName = propertyName;
			this.indexes = indexes;
		}

		public String getPropertyName() {
			return propertyName;
		}

		public Integer[] getIndexes() {
			return indexes;
		}
		
		
	}
	
	private PropertyMappingPair getProperty(Path path){
		Iterator<Node> iter = path.iterator();
		List<Integer> indexes = new ArrayList<Integer>();
		List<String> propertyChain = new ArrayList<String>();
		while(iter.hasNext()){
			Node node = iter.next();
			if(node.isInIterable())
				indexes.add(node.getIndex());
			propertyChain.add(node.getName());
		}
		return new PropertyMappingPair(StringUtils.join(propertyChain, "_"), indexes.size() == 0? null: indexes.toArray(new Integer[indexes.size()]));
	}
	
	public <T> List<ErrorObject> convert(Set<ConstraintViolation<T>> violations){
		List<ErrorObject> errors = new ArrayList<ErrorObject>();
		for(ConstraintViolation<T> violation: violations){
			PropertyMappingPair pair = getProperty(violation.getPropertyPath());
			if(violation.getConstraintDescriptor().getAnnotation().annotationType().equals(NotBlank.class))
				errors.add(new ErrorObject(Field.valueOf(pair.getPropertyName()), ErrorCode.BLANK_OR_NULL, pair.getIndexes()));
			else if(violation.getConstraintDescriptor().getAnnotation().annotationType().equals(NotNull.class))
				errors.add(new ErrorObject(Field.valueOf(pair.getPropertyName()), ErrorCode.NULL, pair.getIndexes()));
			else if(violation.getConstraintDescriptor().getAnnotation().annotationType().equals(Email.class))
				errors.add(new ErrorObject(Field.valueOf(pair.getPropertyName()), ErrorCode.MALFORMED_EMAIL, pair.getIndexes()));
			else if(violation.getConstraintDescriptor().getAnnotation().annotationType().equals(Pattern.class))
				errors.add(new ErrorObject(Field.valueOf(pair.getPropertyName()), ErrorCode.MALFORMED_REGEX_PATTERN, pair.getIndexes()));
			else if(violation.getConstraintDescriptor().getAnnotation().annotationType().equals(Size.class))
				errors.add(new ErrorObject(Field.valueOf(pair.getPropertyName()), ErrorCode.LENGTH, pair.getIndexes()));
			else if(violation.getConstraintDescriptor().getAnnotation().annotationType().equals(TaxFieldsNotNull.class))
				errors.add(new ErrorObject(Field.vatID, ErrorCode.BLANK_OR_NULL));
			else if(violation.getConstraintDescriptor().getAnnotation().annotationType().equals(PaymentDeltaNotNull.class))
				errors.add(new ErrorObject(Field.paymentTypeCls, ErrorCode.NULL));
			else
				throw new RuntimeException("No such constraint violation exception!");
		}
		return errors;
	}

}
