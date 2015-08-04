package com.novadart.novabill.service.periodic;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.novadart.novabill.service.PDFStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;


@Service
public class PeriodicPurgerService implements PeriodicService {
	
	private static final Logger logger = LoggerFactory.getLogger(PeriodicPurgerService.class);
	
	@Value("${forgotpassword.expiration}")
	private Long forgotPasswordExpiration;
	
	@Value("${sharing.expiration}")
	private Long sharingExpiration;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private PDFStorageService pdfStorageService;

	@Override
	@Scheduled(cron = "0 0 3 * * ?")// once a day at 3 am
	public void runTasks(){
		purgeExpiredForgotPasswordRequest();
		purgeExpiredSharingTokens();
		purgeOrphanPDFs();
	}
	
	@Async
	@Transactional(readOnly = false)
	private void purgeExpiredForgotPasswordRequest(){
		String query = "delete from ForgotPassword fpr where fpr.creationTime < :threshold";
		int affectedRows = entityManager.createQuery(query)
			.setParameter("threshold", System.currentTimeMillis() -  forgotPasswordExpiration * MILLIS_IN_HOUR)
			.executeUpdate();
		logger.info("{} rows deleted from ForgotPassword", affectedRows);
	}
	
	@Async
	@Transactional(readOnly = false)
	private void purgeExpiredSharingTokens(){
		String query = "delete from SharingToken st where st.createdOn < :threshold";
		int affectedRows = entityManager.createQuery(query)
			.setParameter("threshold", System.currentTimeMillis() -  sharingExpiration * MILLIS_IN_HOUR)
			.executeUpdate();
		logger.info("{} rows deleted from SharingToken", affectedRows);
	}

	@Async
	private void purgeOrphanPDFs(){
		pdfStorageService.purgeOrphanPDFs();
	}

}
