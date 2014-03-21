package com.novadart.novabill.test.suite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.dumbster.smtp.SimpleSmtpServer;
import com.novadart.novabill.domain.SharingPermit;
import com.novadart.novabill.service.SharingService;
import com.novadart.novabill.service.PeriodicPurgerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:mvc-test-config.xml")
@Transactional
@ActiveProfiles("dev")
public class SharingTest {

	@Autowired
	private SharingService sharingService;
	
	@Value("${sharing.expiration}")
	private Integer invoiceSharingExpiration;
	
	@Autowired
	private PeriodicPurgerService periodicPurgerService;
	
	@Test
	public void shareInvoicesTemporarilyAndNotifyParticipantTest(){
		String email = "foo@bar";
		Long businessID = 1l;
		Locale locale = LocaleContextHolder.getLocale();
		MessageSource messageSource = mock(MessageSource.class);
		when(messageSource.getMessage("sharing.notification", null, locale)).thenReturn("Docs sharing");
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		sharingService.issueSharingPermitTemporarilyAndNotifyParticipant(businessID, email, messageSource, locale);
		smtpServer.stop();
		
		assertTrue(smtpServer.getReceivedEmailSize() == 1);
		assertEquals(1, SharingPermit.findAllSharingPermits().size());
	}
	
//	@Test
//	public void validInvoiceSharingRequestTest(){
//		Long businessID = 1l;
//		String token = "token";
//		new SharingPermit(token, "foo@bar", businessID, System.currentTimeMillis()).persist();
//		SharingPermit.entityManager().flush();
//		assertTrue(sharingService.isValidRequest(businessID, token));
//	}
//	
//	@Test
//	public void invalidInvoiceSharingRequestTest(){
//		Long businessID = 1l;
//		String token = "token";
//		new SharingPermit(token, "foo@bar", businessID, System.currentTimeMillis() - (invoiceSharingExpiration + 1) * 3_600_000l).persist();
//		SharingPermit.entityManager().flush();
//		assertTrue(!sharingService.isValidRequest(businessID, token));
//	}
//	
//	@Test
//	public void purgeExpiredInvoiceSharingPermitsTest(){
//		Long businessID = 1l;
//		String token = "token";
//		new SharingPermit(token, "foo@bar", businessID, System.currentTimeMillis() - (invoiceSharingExpiration + 1) * 3_600_000l).persist();
//		SharingPermit.entityManager().flush();
//		periodicPurgerService.runPurgeTasks();
//		SharingPermit.entityManager().flush();
//		assertEquals(0, SharingPermit.findAllSharingPermits().size());
//	}
	
}
