package com.novadart.novabill.service.periodic;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PeriodicPurgerService implements PeriodicService {
	
	@Value("${registration.expiration}")
	private Long registrationExpiration;
	
	@Value("${forgotpassword.expiration}")
	private Long forgotPasswordExpiration;
	
	@Value("${sharing.expiration}")
	private Long sharingExpiration;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	@Scheduled(cron = "* 0 0 3 * *")// once a day at 3 am
	public void runTasks(){
		purgeExpiredRegistration();
		purgeExpiredForgotPasswordRequest();
		purgeExpiredSharingTokens();
	}
	
	@Async
	@Transactional
	private void purgeExpiredRegistration(){
		String query = "delete from Registration r where r.creationTime < :threshold";
		entityManager.createQuery(query)
			.setParameter("threshold", System.currentTimeMillis() -  registrationExpiration * MILLIS_IN_HOUR)
			.executeUpdate();
	}
	
	@Async
	@Transactional
	private void purgeExpiredForgotPasswordRequest(){
		String query = "delete from ForgotPassword fpr where fpr.creationTime < :threshold";
		entityManager.createQuery(query)
			.setParameter("threshold", System.currentTimeMillis() -  forgotPasswordExpiration * MILLIS_IN_HOUR)
			.executeUpdate();
	}
	
	@Async
	@Transactional
	private void purgeExpiredSharingTokens(){
		String query = "delete from SharingToken st where st.createdOn < :threshold";
		entityManager.createQuery(query)
			.setParameter("threshold", System.currentTimeMillis() -  sharingExpiration * MILLIS_IN_HOUR)
			.executeUpdate();
	}
	
}