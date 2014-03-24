package com.novadart.novabill.test.suite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.support.SessionStatus;

import com.dumbster.smtp.SimpleSmtpServer;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.SharingPermit;
import com.novadart.novabill.domain.SharingToken;
import com.novadart.novabill.domain.dto.factory.BusinessDTOFactory;
import com.novadart.novabill.domain.dto.factory.SharingPermitDTOFactory;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.PeriodicPurgerService;
import com.novadart.novabill.service.SharingService;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.web.SharingPermitService;
import com.novadart.novabill.shared.client.dto.SharingPermitDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.web.mvc.SharingController;
import com.novadart.novabill.web.mvc.ajax.SharingPermitController;
import com.novadart.novabill.web.mvc.command.SharingRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ActiveProfiles("dev")
public class SharingTest extends ServiceTest {

	@Autowired
	private SharingPermitService sharingPermitService;
	
	@Autowired
	private SharingService sharingService;
	
	@Value("${sharing.expiration}")
	private Integer invoiceSharingExpiration;
	
	private SharingPermitController initSharingPermitController() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		SharingPermitController controller = new SharingPermitController();
		MessageSource messageSource = mock(MessageSource.class);
		when(messageSource.getMessage("sharing.permit.notification", null, null)).thenReturn("Sharing invoices");
		TestUtils.setPrivateField(SharingPermitController.class, controller, "messageSource", messageSource);
		TestUtils.setPrivateField(SharingPermitController.class, controller, "sharingPermitService", sharingPermitService);
		TestUtils.setPrivateField(SharingPermitController.class, controller, "sharingRequestUrl", "bogus.pattern.com");
		TestUtils.setPrivateField(SharingPermitController.class, controller, "utilsService", utilsService);
		return controller;
	}
	
	@Autowired
	private PeriodicPurgerService periodicPurgerService;
	
	@Autowired
	private UtilsService utilsService;
	
	@Test
	public void grantPermitWithEmailTest() throws ValidationException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		SharingPermitDTO sharingPermitDTO = SharingPermitDTOFactory.toDTO(TestUtils.createSharingPermit());
		Principal authenticatedPrincipal = utilsService.getAuthenticatedPrincipalDetails();
		sharingPermitDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		
		SharingPermitController sharingPermitController = initSharingPermitController();
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		sharingPermitController.add(businessID, true, sharingPermitDTO, null);
		smtpServer.stop();
		
		Business business = Business.findBusinessByVatIDIfSharingPermit(authenticatedPrincipal.getBusiness().getVatID(), sharingPermitDTO.getEmail());
		
		assertTrue(smtpServer.getReceivedEmailSize() == 1);
		assertEquals(2, Business.findBusiness(businessID).getSharingPermits().size());
		assertTrue(business != null);
		assertEquals(businessID, business.getId());
	}
	
	@Test
	public void grantPermitWithoutEmailTest() throws ValidationException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		SharingPermitDTO sharingPermitDTO = SharingPermitDTOFactory.toDTO(TestUtils.createSharingPermit());
		Principal authenticatedPrincipal = utilsService.getAuthenticatedPrincipalDetails();
		sharingPermitDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		
		SharingPermitController sharingPermitController = initSharingPermitController();
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		sharingPermitController.add(businessID, false, sharingPermitDTO, null);
		smtpServer.stop();
		
		Business business = Business.findBusinessByVatIDIfSharingPermit(authenticatedPrincipal.getBusiness().getVatID(), sharingPermitDTO.getEmail());
		
		assertTrue(smtpServer.getReceivedEmailSize() == 0);
		assertEquals(2, Business.findBusiness(businessID).getSharingPermits().size());
		assertTrue(business != null);
		assertEquals(businessID, business.getId());
	}
	
	@Test
	public void sendEmailTest() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, DataAccessException{
		SharingPermitController sharingPermitController = initSharingPermitController();
		SharingPermit permit = authenticatedPrincipal.getBusiness().getSharingPermits().iterator().next();
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		sharingPermitController.sendEmail(permit.getId(), null);
		smtpServer.stop();
		
		assertTrue(smtpServer.getReceivedEmailSize() == 1);
	}
	
	private SharingController initSharingController() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		SharingController controller = new SharingController();
		MessageSource messageSource = mock(MessageSource.class);
		when(messageSource.getMessage("temp.sharing.notification", null, null)).thenReturn("Sharing invoices");
		TestUtils.setPrivateField(SharingController.class, controller, "messageSource", messageSource);
		TestUtils.setPrivateField(SharingController.class, controller, "validator", mock(Validator.class));
		TestUtils.setPrivateField(SharingController.class, controller, "sharingService", sharingService);
		return controller;
	}
	
	@Test
	public void tempSharingTest() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		Business business = Business.findBusiness(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId());
		SharingPermit sharingPermit = TestUtils.createSharingPermit();
		business.getSharingPermits().add(sharingPermit);
		sharingPermit.setBusiness(business);
		business.flush();
		SharingController sharingController = initSharingController();
		SharingRequest request = new SharingRequest();
		request.setEmail(sharingPermit.getEmail());
		request.setVatID(business.getVatID());
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		sharingController.processRequestSubmit(request, new BeanPropertyBindingResult(request, "sharingRequest"), mock(SessionStatus.class), null);
		smtpServer.stop();
		
		assertTrue(smtpServer.getReceivedEmailSize() == 1);
		assertEquals(1, SharingToken.findAllSharingTokens().size());
	}
	
	@Test
	public void tempSharingForNovabillUserTest() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		SharingController sharingController = initSharingController();
		SharingRequest request = new SharingRequest();
		request.setEmail(authenticatedPrincipal.getUsername());
		request.setVatID(authenticatedPrincipal.getBusiness().getVatID());
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		sharingController.processRequestSubmit(request, new BeanPropertyBindingResult(request, "sharingRequest"), mock(SessionStatus.class), null);
		smtpServer.stop();
		
		assertTrue(smtpServer.getReceivedEmailSize() == 1);
		assertEquals(1, SharingToken.findAllSharingTokens().size());
	}
	
	@Test
	public void tempSharingForNovabillInvalidVatIDUserTest() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		SharingController sharingController = initSharingController();
		SharingRequest request = new SharingRequest();
		request.setEmail(authenticatedPrincipal.getUsername());
		request.setVatID("Invalid vatid");
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		sharingController.processRequestSubmit(request, new BeanPropertyBindingResult(request, "sharingRequest"), mock(SessionStatus.class), null);
		smtpServer.stop();
		
		assertTrue(smtpServer.getReceivedEmailSize() == 0);
		assertEquals(0, SharingToken.findAllSharingTokens().size());
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
	@Test
	public void purgeExpiredSharingTokensTest(){
		Long businessID = 1l;
		String token = "token";
		new SharingToken("foo@bar", System.currentTimeMillis() - (invoiceSharingExpiration + 1) * 3_600_000l, businessID, token).persist();
		SharingPermit.entityManager().flush();
		periodicPurgerService.runPurgeTasks();
		SharingPermit.entityManager().flush();
		assertEquals(0, SharingToken.findAllSharingTokens().size());
	}
	
}
