package com.novadart.novabill.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.annotation.MailMixin;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.security.RoleType;


//@Service
@MailMixin
public class AccountStatusManagerService {
	
	private static final Long DAY_IN_MILLIS = 86400000l;
	
	@PersistenceContext
	private EntityManager entityManager; 
	
	@Scheduled(cron = "* 0 0 3 * *") //run once a day at 3 AM
	public void runTasks(){
		disableExpiredAccounts();
		notifySoonToExpireAccounts(30);
		notifySoonToExpireAccounts(15);
		notifySoonToExpireAccounts(7);
	}
	
	@Async
	@Transactional(readOnly = true)
	private void notifySoonToExpireAccounts(int days){
		String query = "select business from Business business, in (business.grantedRoles) gr where " +
				":lbound < business.settings.nonFreeAccountExpirationTime and business.settings.nonFreeAccountExpirationTime < :rbound and gr = :role";
		Long now = System.currentTimeMillis();
		Long lbound = now + (days - 1)  *  DAY_IN_MILLIS, rbound = now + days * DAY_IN_MILLIS;
		List<Business> soonToExpireBusinesses = entityManager.createQuery(query, Business.class)
				.setParameter("lbound", lbound)
				.setParameter("rbound", rbound)
				.setParameter("role", RoleType.ROLE_BUSINESS_PREMIUM).getResultList();
		for(Business business: soonToExpireBusinesses){
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("expired", false);
			model.put("days", days);
			sendMessage(business.getEmail(), "Premium account soon to expire", model, "mail-templates/premium-expiration-notification.vm");
		}
	}
	
	@Async
	@Transactional(readOnly = false)
	private void disableExpiredAccounts(){
		String query = "select business from Business business, in (business.grantedRoles) gr where business.settings.nonFreeAccountExpirationTime < :now and gr = :role";
		List<Business> expiredBusinesses = entityManager.createQuery(query, Business.class)
				.setParameter("now", System.currentTimeMillis())
				.setParameter("role", RoleType.ROLE_BUSINESS_PREMIUM).getResultList();
		for(Business business: expiredBusinesses){
			disableExpiredAccount(business);
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("expired", true);
			sendMessage(business.getEmail(), "Premium account expired", model, "mail-templates/premium-expiration-notification.vm");
		}
	}
	
	private void disableExpiredAccount(Business business){
//		business.getGrantedRoles().remove(RoleType.ROLE_BUSINESS_PREMIUM);
//		business.getGrantedRoles().add(RoleType.ROLE_BUSINESS_FREE);
//		business.setNonFreeAccountExpirationTime(null);
	}

}
