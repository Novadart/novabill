package com.novadart.novabill.service.periodic;


import com.novadart.novabill.domain.Email;
import com.novadart.novabill.domain.EmailStatus;
import org.springframework.mail.MailSendException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class PeriodicMailSender implements PeriodicService {
	
	public static final short MAX_NUMBER_OF_RETRIES = 10;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	@Scheduled(fixedRate = 2 * MILLIS_IN_HOUR)
	public void runTasks(){
		sendPendingEmails();
	}
	
	@Async
	private void sendPendingEmails(){
		String query = "select email from Email email where email.status = :status";
		List<Email> emails = entityManager.createQuery(query, Email.class).setParameter("status", EmailStatus.PENDING).getResultList();
		for(Email email: emails)
			sendEmail(email);
	}
	
	private void sendEmail(Email email){
		try {
			email.send();
			email.remove();
		} catch (MessagingException | MailSendException e) {
			email.setTries(email.getTries() + 1);
			if(email.getTries() >= MAX_NUMBER_OF_RETRIES)
				email.setStatus(EmailStatus.FAILED);
			email.merge();
		}
	}

}
