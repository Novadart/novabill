package com.novadart.novabill.service.periodic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.annotation.MailMixin;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.domain.security.RoleType;


@Service
@MailMixin
public class PremiumDisablerService implements PeriodicService {
	
	@PersistenceContext
	private EntityManager entityManager; 
	
	@Override
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
		String query = "select principal from Principal principal inner join principal.grantedRoles gr where " +
				":lbound <= principal.business.settings.nonFreeAccountExpirationTime and principal.business.settings.nonFreeAccountExpirationTime <= :rbound and gr = :role";
		Long now = System.currentTimeMillis();
		Long lbound = now + (days - 1)  *  MILLIS_IN_DAY - MILLIS_IN_HOUR, rbound = now + days * MILLIS_IN_DAY;
		List<Principal> soonToExpirePrincipals = entityManager.createQuery(query, Principal.class)
				.setParameter("lbound", lbound)
				.setParameter("rbound", rbound)
				.setParameter("role", RoleType.ROLE_BUSINESS_PREMIUM).getResultList();
		for(Principal principal: soonToExpirePrincipals){
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("expired", false);
			model.put("days", days);
			sendMessage(principal.getUsername(), "Il tuo piano Premium sta per scadere", model, "mail-templates/premium-expiration-notification.vm");
		}
	}
	
	@Async
	@Transactional(readOnly = false)
	private void disableExpiredAccounts(){
		String query = "select principal from Principal principal inner join principal.grantedRoles gr where principal.business.settings.nonFreeAccountExpirationTime <= :now and gr = :role";
		List<Principal> expiredPrincipals = entityManager.createQuery(query, Principal.class)
				.setParameter("now", System.currentTimeMillis())
				.setParameter("role", RoleType.ROLE_BUSINESS_PREMIUM).getResultList();
		for(Principal principal: expiredPrincipals){
			disableExpiredAccount(principal);
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("expired", true);
			sendMessage(principal.getUsername(), "Il tuo piano Premium è scaduto", model, "mail-templates/premium-expiration-notification.vm");
		}
	}
	
	private void disableExpiredAccount(Principal principal){
		principal.getGrantedRoles().remove(RoleType.ROLE_BUSINESS_PREMIUM);
		principal.getGrantedRoles().add(RoleType.ROLE_BUSINESS_FREE);
		principal.getBusiness().getSettings().setNonFreeAccountExpirationTime(null);
	}

}
