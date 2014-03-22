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
import com.novadart.novabill.domain.dto.factory.BusinessDTOFactory;
import com.novadart.novabill.domain.dto.factory.SharingPermitDTOFactory;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.PeriodicPurgerService;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.web.SharingPermitService;
import com.novadart.novabill.shared.client.dto.SharingPermitDTO;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.web.mvc.ajax.SharingPermitController;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ActiveProfiles("dev")
public class SharingTest extends ServiceTest {

	@Autowired
	private SharingPermitService sharingPermitService;
	
	@Value("${sharing.expiration}")
	private Integer invoiceSharingExpiration;
	
	private SharingPermitController initSharingPermitController() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		SharingPermitController controller = new SharingPermitController();
		MessageSource messageSource = mock(MessageSource.class);
		when(messageSource.getMessage("sharing.notification", null, null)).thenReturn("Sharing invoices");
		TestUtils.setPrivateField(SharingPermitController.class, controller, "messageSource", messageSource);
		TestUtils.setPrivateField(SharingPermitController.class, controller, "sharingPermitService", sharingPermitService);
		TestUtils.setPrivateField(SharingPermitController.class, controller, "sharingRequestUrl", "bogus.pattern.com");
		return controller;
	}
	
	@Autowired
	private PeriodicPurgerService periodicPurgerService;
	
	@Autowired
	private UtilsService utilsService;
	
	@Test
	public void grantPermitTest() throws ValidationException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		SharingPermitDTO sharingPermitDTO = SharingPermitDTOFactory.toDTO(TestUtils.createSharingPermit());
		Principal authenticatedPrincipal = utilsService.getAuthenticatedPrincipalDetails();
		sharingPermitDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		
		SharingPermitController sharingPermitController = initSharingPermitController();
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		sharingPermitController.add(businessID, sharingPermitDTO, null);
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
