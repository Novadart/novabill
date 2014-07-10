package com.novadart.novabill.service.validator;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Configurable;

import com.novadart.novabill.annotation.SharingPermitEmailBusinessUnique;
import com.novadart.novabill.domain.SharingPermit;

@Configurable
public class SharingPermitEmailBusinessUniqueValidator implements ConstraintValidator<SharingPermitEmailBusinessUnique, SharingPermit> {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void initialize(SharingPermitEmailBusinessUnique arg0) {}

	@Override
	public boolean isValid(SharingPermit sharingPermit, ConstraintValidatorContext context) {
		SharingPermit persistedSharingPermit = null;
		try{
			entityManager.setFlushMode(FlushModeType.COMMIT);
			persistedSharingPermit = SharingPermit.findByEmailForBusiness(sharingPermit.getEmail(), sharingPermit.getBusiness().getId());
		}finally{
			entityManager.setFlushMode(FlushModeType.AUTO);
		}
		return persistedSharingPermit == null? true: persistedSharingPermit.getId().equals(sharingPermit.getId()); 
	}

}
