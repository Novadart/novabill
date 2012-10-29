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
import com.novadart.novabill.shared.client.validation.ErrorCode;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;

@Service
public class ConstraintViolationToApplicationErrorMapper {
	
	private static class PropertyMappingPair{
		
		private String propertyName;
		
		private Object[] arguments;
		
		public PropertyMappingPair(String propertyName, Object[] arguments){
			this.propertyName = propertyName;
			this.arguments = arguments;
		}

		public String getPropertyName() {
			return propertyName;
		}

		public Object[] getArguments() {
			return arguments;
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
		return new PropertyMappingPair(StringUtils.join(propertyChain, "_"), indexes.size() == 0? null: indexes.toArray());
	}
	
	public <T> List<ErrorObject> convert(Set<ConstraintViolation<T>> violations){
		List<ErrorObject> errors = new ArrayList<ErrorObject>();
		for(ConstraintViolation<T> violation: violations){
			PropertyMappingPair pair = getProperty(violation.getPropertyPath());
			if(violation.getConstraintDescriptor().getAnnotation().annotationType().equals(NotBlank.class))
				errors.add(new ErrorObject(Field.valueOf(pair.getPropertyName()), ErrorCode.BLANK_OR_NULL, pair.getArguments()));
			else if(violation.getConstraintDescriptor().getAnnotation().annotationType().equals(NotNull.class))
				errors.add(new ErrorObject(Field.valueOf(pair.getPropertyName()), ErrorCode.NULL, pair.getArguments()));
			else if(violation.getConstraintDescriptor().getAnnotation().annotationType().equals(Email.class))
				errors.add(new ErrorObject(Field.valueOf(pair.getPropertyName()), ErrorCode.MALFORMED_EMAIL, pair.getArguments()));
			else if(violation.getConstraintDescriptor().getAnnotation().annotationType().equals(Pattern.class))
				errors.add(new ErrorObject(Field.valueOf(pair.getPropertyName()), ErrorCode.MALFORMED_REGEX_PATTERN, pair.getArguments()));
			else if(violation.getConstraintDescriptor().getAnnotation().annotationType().equals(Size.class))
				errors.add(new ErrorObject(Field.valueOf(pair.getPropertyName()), ErrorCode.LENGTH, pair.getArguments()));
			else
				throw new RuntimeException("No such constraint violation exception!");
		}
		return errors;
	}

}
