package com.novadart.novabill.service.periodic;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.service.mail.EmailBuilder;
import com.novadart.novabill.service.mail.MailHandlingType;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public abstract class DisablerService implements PeriodicService {

    @PersistenceContext
    private EntityManager entityManager;

    protected abstract Logger getLogger();

    protected abstract void createPendingExpirationNotification(Business business, int days);

    @Async
    @Transactional(readOnly = true)
    protected void notifySoonToExpireAccounts(String subject, String template, RoleType role,
            int days){
        String query = "select principal from Principal principal inner join principal.grantedRoles gr where " +
                ":lbound <= principal.business.settings.nonFreeAccountExpirationTime and principal.business.settings.nonFreeAccountExpirationTime <= :rbound and gr = :role";
        Long now = System.currentTimeMillis();
        Long lbound = now + (days - 1)  *  MILLIS_IN_DAY - MILLIS_IN_HOUR, rbound = now + days * MILLIS_IN_DAY;
        List<Principal> soonToExpirePrincipals = entityManager.createQuery(query, Principal.class)
                .setParameter("lbound", lbound)
                .setParameter("rbound", rbound)
                .setParameter("role", role).getResultList();
        for(Principal principal: soonToExpirePrincipals){
            createPendingExpirationNotification(principal.getBusiness(), days);
            new EmailBuilder().to(principal.getUsername())
                    .subject(subject)
                    .template(template)
                    .templateVar("expired", false)
                    .templateVar("days", days)
                    .handlingType(MailHandlingType.EXTERNAL_UNACKNOWLEDGED)
                    .build().send();
            getLogger().info("Send {}-days expiration notification to principal {}", days, principal.getUsername());
        }
    }

    protected abstract void createExpirationNotification(Business business);

    @Async
    @Transactional(readOnly = false)
    protected void disableExpiredAccounts(String subject, String template, RoleType role){
        String query = "select principal from Principal principal inner join principal.grantedRoles gr where principal.business.settings.nonFreeAccountExpirationTime <= :now and gr = :role";
        List<Principal> expiredPrincipals = entityManager.createQuery(query, Principal.class)
                .setParameter("now", System.currentTimeMillis())
                .setParameter("role", role).getResultList();
        for(Principal principal: expiredPrincipals){
            disableExpiredAccount(principal);
            new EmailBuilder().to(principal.getUsername())
                    .subject(subject)
                    .template(template)
                    .templateVar("expired", true)
                    .handlingType(MailHandlingType.EXTERNAL_UNACKNOWLEDGED)
                    .build().send();
            createExpirationNotification(principal.getBusiness());
            getLogger().info("Disabled expired account of principal {} with VATID {}", principal.getUsername(), principal.getBusiness().getVatID());
        }
    }

    private void disableExpiredAccount(Principal principal){
        principal.getGrantedRoles().clear();
        principal.getGrantedRoles().add(RoleType.ROLE_BUSINESS_EXPIRED);
        principal.getBusiness().getSettings().setNonFreeAccountExpirationTime(null);
    }

}
