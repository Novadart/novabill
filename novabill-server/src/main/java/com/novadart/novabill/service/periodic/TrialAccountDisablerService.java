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
public class TrialAccountDisablerService extends DisablerService {

    private static final Logger logger = LoggerFactory.getLogger(PremiumDisablerService.class);

    private static final String SUBJECT_TOO_EXPIRE = "Il tuo piano Trial sta per scadere";
    private static final String TEMPLATE = "mail-templates/trial-expiration-notification.vm";
    private static final String SUBJECT_EXPIRED = "Il tuo piano Trial è scaduto";

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    @Scheduled(cron = "0 0 3 * * ?") //run once a day at 3 AM
    public void runTasks(){
        disableExpiredAccounts(SUBJECT_EXPIRED, TEMPLATE, RoleType.ROLE_BUSINESS_TRIAL);
        notifySoonToExpireAccounts(SUBJECT_TOO_EXPIRE, TEMPLATE, RoleType.ROLE_BUSINESS_TRIAL, 15);
        notifySoonToExpireAccounts(SUBJECT_TOO_EXPIRE, TEMPLATE, RoleType.ROLE_BUSINESS_TRIAL, 7);
    }

    @Override
    final protected void createPendingExpirationNotification(Business business, int days) {
        Notification notification = new Notification();
        switch (days) {
            case 7:
                notification.setType(NotificationType.TRIAL_7_DAYS_LEFT);
                break;
            case 15:
                notification.setType(NotificationType.TRIAL_15_DAYS_LEFT);
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
        notification.setType(NotificationType.TRIAL_FINISHED);
        notification.setBusiness(business);
        business.getNotifications().add(notification);
    }

}
