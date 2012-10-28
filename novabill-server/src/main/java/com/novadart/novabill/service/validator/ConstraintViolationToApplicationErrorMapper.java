package com.novadart.novabill.service.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
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
	
	private String getProperty(Path path){
		return StringUtils.join(path.iterator(), "_");
	}
	
	public <T> List<ErrorObject> convert(Set<ConstraintViolation<T>> violations){
		List<ErrorObject> errors = new ArrayList<ErrorObject>();
		for(ConstraintViolation<T> violation: violations){
			String property = getProperty(violation.getPropertyPath());
			if(violation.getConstraintDescriptor().getAnnotation().annotationType().equals(NotBlank.class))
				errors.add(new ErrorObject(Field.valueOf(property), ErrorCode.BLANK_OR_NULL));
			else if(violation.getConstraintDescriptor().getAnnotation().annotationType().equals(NotNull.class))
				errors.add(new ErrorObject(Field.valueOf(property), ErrorCode.NULL));
			else if(violation.getConstraintDescriptor().getAnnotation().annotationType().equals(Email.class))
				errors.add(new ErrorObject(Field.valueOf(property), ErrorCode.MALFORMED_EMAIL));
			else if(violation.getConstraintDescriptor().getAnnotation().annotationType().equals(Pattern.class))
				errors.add(new ErrorObject(Field.valueOf(property), ErrorCode.MALFORMED_REGEX_PATTERN));
			else if(violation.getConstraintDescriptor().getAnnotation().annotationType().equals(Size.class))
				errors.add(new ErrorObject(Field.valueOf(property), ErrorCode.LENGTH));
			else
				throw new RuntimeException("No such constraint violation exception!");
		}
		return errors;
	}

}
