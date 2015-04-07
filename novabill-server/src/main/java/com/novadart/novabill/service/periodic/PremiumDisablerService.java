package com.novadart.novabill.service.periodic;

import com.novadart.novabill.annotation.MailMixin;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Notification;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.shared.client.data.LayoutType;
import com.novadart.novabill.shared.client.dto.NotificationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@MailMixin
public class PremiumDisablerService implements PeriodicService {
	
	private static final Logger logger = LoggerFactory.getLogger(PremiumDisablerService.class);
	
	@PersistenceContext
	private EntityManager entityManager; 
	
	@Override
	@Scheduled(cron = "0 0 3 * * ?") //run once a day at 3 AM
	public void runTasks(){
		disableExpiredAccounts();
		notifySoonToExpireAccounts(30);
		notifySoonToExpireAccounts(15);
		notifySoonToExpireAccounts(7);
	}
	
	private void createPendingPremiumDowngradeNotification(Business business, int days) {
		Notification notification = new Notification();
		switch (days) {
		case 7:
			notification.setType(NotificationType.PREMIUM_DOWNGRADE_7_DAYS);
			break;
		case 15:
			notification.setType(NotificationType.PREMIUM_DOWNGRADE_15_DAYS);
			break;
		case 30:
			notification.setType(NotificationType.PREMIUM_DOWNGRADE_30_DAYS);
			break;
		default:
			throw new IllegalArgumentException("Wrong number of days");
		}
		notification.setBusiness(business);
		business.getNotifications().add(notification);
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
			createPendingPremiumDowngradeNotification(principal.getBusiness(), days);
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("expired", false);
			model.put("days", days);
			sendMessage(principal.getUsername(), "Il tuo piano Premium sta per scadere", model, "mail-templates/premium-expiration-notification.vm");
			logger.info("Send {}-days expiration notification to principal {}", days, principal.getUsername());
		}
	}
	
	private void createPremiumDowngradeNotification(Business business){
		Notification notification = new Notification();
		notification.setType(NotificationType.PREMIUM_DOWNGRADE);
		notification.setBusiness(business);
		business.getNotifications().add(notification);
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
			createPremiumDowngradeNotification(principal.getBusiness());
			logger.info("Disabled expired account of principal {} with VATID {}", principal.getUsername(), principal.getBusiness().getVatID());
		}
	}
	
	private void disableExpiredAccount(Principal principal){
		principal.getGrantedRoles().remove(RoleType.ROLE_BUSINESS_PREMIUM);
		principal.getGrantedRoles().add(RoleType.ROLE_BUSINESS_FREE);
		principal.getBusiness().getSettings().setNonFreeAccountExpirationTime(null);
		principal.getBusiness().getSettings().setDefaultLayoutType(LayoutType.DENSE);
	}

}
