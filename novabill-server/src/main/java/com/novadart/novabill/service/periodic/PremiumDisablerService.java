package com.novadart.novabill.service.periodic;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Notification;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.shared.client.dto.NotificationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class PremiumDisablerService extends DisablerService {
	
	private static final Logger logger = LoggerFactory.getLogger(PremiumDisablerService.class);

	private static final String SUBJECT_TOO_EXPIRE = "Il tuo piano Premium sta per scadere";
	private static final String TEMPLATE = "mail-templates/premium-expiration-notification.vm";
	private static final String SUBJECT_EXPIRED = "Il tuo piano Premium è scaduto";
	
	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	@Scheduled(cron = "0 0 3 * * ?") //run once a day at 3 AM
	public void runTasks(){
		disableExpiredAccounts(SUBJECT_EXPIRED, TEMPLATE, RoleType.ROLE_BUSINESS_PREMIUM);
		notifySoonToExpireAccounts(SUBJECT_TOO_EXPIRE, TEMPLATE, RoleType.ROLE_BUSINESS_PREMIUM, 30);
		notifySoonToExpireAccounts(SUBJECT_TOO_EXPIRE, TEMPLATE, RoleType.ROLE_BUSINESS_PREMIUM, 15);
		notifySoonToExpireAccounts(SUBJECT_TOO_EXPIRE, TEMPLATE, RoleType.ROLE_BUSINESS_PREMIUM, 7);
	}

	@Override
	final protected void createPendingExpirationNotification(Business business, int days) {
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

	@Override
	final protected void createExpirationNotification(Business business){
		Notification notification = new Notification();
		notification.setType(NotificationType.PREMIUM_DOWNGRADE);
		notification.setBusiness(business);
		business.getNotifications().add(notification);
	}
	
}
