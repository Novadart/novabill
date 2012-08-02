package com.novadart.novabill.service;

import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.novadart.novabill.annotation.MailMixin;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.security.RoleType;


@Service
@MailMixin
public class AccountStatusManagerService {
	
	@PersistenceContext
	private EntityManager entityManager; 
	
	@Scheduled(cron = "* 0 0 * * *") //run once a day at midnight
	public void runTasks(){
		disableExpiredAccounts();
	}
	
	@Async
	@Transactional
	private void disableExpiredAccounts(){
		String query = "select business from Business business, in (business.grantedRoles) gr where business.nonFreeAccountExpirationTime < :now and gr = :role";
		List<Business> expiredBusinesses = entityManager.createQuery(query, Business.class)
				.setParameter("now", System.currentTimeMillis())
				.setParameter("role", RoleType.ROLE_BUSINESS_PREMIUM).getResultList();
		for(Business business: expiredBusinesses){
			disableExpiredAccount(business);
			sendMessage(business.getEmail(), "Premium account expired", new HashMap<String, Object>(), "mail-templates/premium-expiration-notification.vm");
		}
	}
	
	private void disableExpiredAccount(Business business){
		business.getGrantedRoles().remove(RoleType.ROLE_BUSINESS_PREMIUM);
		business.getGrantedRoles().add(RoleType.ROLE_BUSINESS_FREE);
		business.setNonFreeAccountExpirationTime(null);
	}

}
