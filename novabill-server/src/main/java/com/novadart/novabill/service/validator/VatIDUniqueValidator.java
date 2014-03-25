package com.novadart.novabill.service.validator;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Configurable;

import com.novadart.novabill.annotation.VatIDUnique;
import com.novadart.novabill.domain.Taxable;

@Configurable
public class VatIDUniqueValidator implements ConstraintValidator<VatIDUnique, Taxable> {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void initialize(VatIDUnique constraintAnnotation) {}

	@Override
	public boolean isValid(Taxable taxable, ConstraintValidatorContext context) {
		Taxable persistedTaxable = null;
		try{
			entityManager.setFlushMode(FlushModeType.COMMIT);
			persistedTaxable = taxable.findByVatID(taxable.getVatID());
		}finally{
			entityManager.setFlushMode(FlushModeType.AUTO);
		}
		return persistedTaxable == null? true: persistedTaxable.getId().equals(taxable.getId());
	}

}
