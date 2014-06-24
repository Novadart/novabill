package com.novadart.novabill.test.suite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import com.novadart.novabill.aspect.logging.ExceptionTraceAspect;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Email;
import com.novadart.novabill.domain.EmailStatus;
import com.novadart.novabill.domain.Transaction;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.paypal.PayPalIPNHandlerService;
import com.novadart.novabill.paypal.PaymentPlanDescriptor;
import com.novadart.novabill.paypal.PaymentPlansLoader;
import com.novadart.novabill.service.PrincipalDetailsService;
import com.novadart.novabill.service.TokenGenerator;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.periodic.PeriodicMailSender;
import com.novadart.novabill.service.periodic.PremiumDisablerService;
import com.novadart.novabill.service.web.BusinessService;
import com.novadart.novabill.service.web.InvoiceService;
import com.novadart.novabill.service.web.PremiumEnablerService;
import com.novadart.novabill.shared.client.dto.NotificationType;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.PremiumUpgradeException;
import com.novadart.novabill.web.mvc.UpgradeAccountController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:mvc-test-config.xml")
@Transactional
@ActiveProfiles("dev")
public class AccountUpgradeTest extends AuthenticatedTest {
	
	@Autowired
	private PremiumDisablerService accountStatusService;
	
	@Autowired
	private PaymentPlansLoader paymentPlans;

	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private PeriodicMailSender mailSenderService;
	
	@Autowired
	private PremiumEnablerService premiumEnablerService;
	
	@Autowired
	private PaymentPlansLoader paymentPlansLoader;
	
	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private PrincipalDetailsService principalDetailsService;
	
	@Autowired
	private BusinessService businessService;
	
	private long getNDaysFromNowInMillis(int days){
		long DAY_IN_MILLIS = 86_400_000l;
		Long now = System.currentTimeMillis();
		return now + days * DAY_IN_MILLIS;
	}
	
	@Test
	public void wiringTest(){
		assertTrue(premiumEnablerService != null);
	}
	
	
	@Override
	@Before
	public void authenticate() {
		authenticatedPrincipal = Principal.findByUsername("giordano.battilana@novadart.com");
		authenticatePrincipal(authenticatedPrincipal);
	}
	
	public void accountExpirationInNDaysNotificationTest(int days){
		Business business = Business.findBusiness(authenticatedPrincipal.getBusiness().getId());
		business.getSettings().setNonFreeAccountExpirationTime(getNDaysFromNowInMillis(days));
		business.flush();
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		accountStatusService.runTasks();
		smtpServer.stop();
		assertEquals(1, smtpServer.getReceivedEmailSize());
		
		SmtpMessage email = (SmtpMessage)smtpServer.getReceivedEmail().next();
		assertEquals(business.getPrincipals().iterator().next().getUsername(), email.getHeaderValue("To"));
	}
	
	@Test
	public void accountExpirationIn7DaysNotificationTest(){
		accountExpirationInNDaysNotificationTest(7);
	}
	
	@Test
	public void accountExpirationIn15DaysNotificationTest(){
		accountExpirationInNDaysNotificationTest(15);
	}
	
	@Test
	public void accountExpirationIn30DaysNotificationTest(){
		accountExpirationInNDaysNotificationTest(30);
	}

	@Test
	public void accountExpirationIn7DaysNotificationFailureTest(){
		Business business = Business.findBusiness(authenticatedPrincipal.getBusiness().getId());
		business.getSettings().setNonFreeAccountExpirationTime(getNDaysFromNowInMillis(7));
		business.flush();
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2526); //wrong port, email fails
		accountStatusService.runTasks();
		assertEquals(0, smtpServer.getReceivedEmailSize());
		assertEquals(1, Email.countEmails());
		Email email = Email.findAllEmails().get(0);
		assertEquals(EmailStatus.PENDING, email.getStatus());
		assertTrue(email.getTries() > 0);
		
		for(int i = 0; i < PeriodicMailSender.MAX_NUMBER_OF_RETRIES; ++i)
			mailSenderService.runTasks();
		
		smtpServer.stop();
		Email.entityManager().flush();

		assertEquals(EmailStatus.FAILED, email.getStatus());
		assertTrue(email.getTries() == PeriodicMailSender.MAX_NUMBER_OF_RETRIES);
		
	}
	
	
	@Test
	public void accountExpirationIn7DaysNotificationSuccessOnLaterTryTest(){
		Business business = Business.findBusiness(authenticatedPrincipal.getBusiness().getId());
		business.getSettings().setNonFreeAccountExpirationTime(getNDaysFromNowInMillis(7));
		business.flush();
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2526); //wrong port, email fails
		accountStatusService.runTasks();
		assertEquals(0, smtpServer.getReceivedEmailSize());
		assertEquals(1, Email.countEmails());
		Email email = Email.findAllEmails().get(0);
		assertEquals(EmailStatus.PENDING, email.getStatus());
		assertTrue(email.getTries() > 0);
		
		smtpServer.stop();
		smtpServer = SimpleSmtpServer.start(2525);
		mailSenderService.runTasks();
		smtpServer.stop();
		
		Email.entityManager().flush();
		assertEquals(0, Email.countEmails());
		assertEquals(1, smtpServer.getReceivedEmailSize());
		
	}
	
	
	@Test
	public void disableExpiredAccountsTest(){
		Business business = Business.findBusiness(authenticatedPrincipal.getBusiness().getId());
		business.getSettings().setNonFreeAccountExpirationTime(System.currentTimeMillis() - 100); //set in past
		business.flush();
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		accountStatusService.runTasks();
		smtpServer.stop();
		assertEquals(1, smtpServer.getReceivedEmailSize());
		
		SmtpMessage email = (SmtpMessage)smtpServer.getReceivedEmail().next();
		assertEquals(business.getPrincipals().iterator().next().getUsername(), email.getHeaderValue("To"));
		
		assertTrue(authenticatedPrincipal.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_FREE));
		assertTrue(!authenticatedPrincipal.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_PREMIUM));
		
		business = Business.findBusiness(business.getId());
		assertEquals(1, business.getNotifications().size());
		assertEquals(NotificationType.PREMIUM_DOWNGRADE, business.getNotifications().iterator().next().getType());
	}
	
	
	@Test
	public void paymentPlansTest(){
		for(PaymentPlanDescriptor paymentPlan: paymentPlans.getPayPalPaymentPlanDescriptors()){
			assertTrue(EqualsBuilder.reflectionEquals(paymentPlan, paymentPlans.getPayPalPaymentPlanDescriptor(paymentPlan.getItemName())));
		}
	}
	
	private UpgradeAccountController initUpgradeAccountController(String token) throws NoSuchAlgorithmException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		UpgradeAccountController controller = new UpgradeAccountController();
		TokenGenerator tokenGenerator = mock(TokenGenerator.class);
		when(tokenGenerator.generateToken()).thenReturn(token);
		TestUtils.setPrivateField(UpgradeAccountController.class, controller, "utilsService", utilsService);
		TestUtils.setPrivateField(UpgradeAccountController.class, controller, "paymentPlans", paymentPlans);
		return controller;
	}
	
	private HttpServletRequest mockRequest(){
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getScheme()).thenReturn("http");
		when(request.getServerName()).thenReturn("localhost");
		when(request.getServerPort()).thenReturn(8080);
		when(request.getContextPath()).thenReturn("/");
		return request;
	}
	
	@Test
	public void initiateUpgradeAccountTest() throws NoSuchAlgorithmException, MalformedURLException, UnsupportedEncodingException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		UpgradeAccountController controller = initUpgradeAccountController("1");
		String view = controller.display(mock(Model.class), mockRequest());
		assertEquals("private.premium", view);
	}
	
	@Test
	public void enablePremiumFor12MonthsFreeUserTest() throws PremiumUpgradeException{
		Long businessID = getUnathorizedBusinessID();
		assertTrue(Business.findBusiness(businessID).getPrincipals().iterator().next().getGrantedRoles().contains(RoleType.ROLE_BUSINESS_FREE));
		premiumEnablerService.enablePremiumForNMonths(Business.findBusiness(businessID), 12);
		assertTrue(Business.findBusiness(businessID).getPrincipals().iterator().next().getGrantedRoles().contains(RoleType.ROLE_BUSINESS_PREMIUM));
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.MONTH, 12);
		assertTrue(calendar.getTimeInMillis() - Business.findBusiness(businessID).getSettings().getNonFreeAccountExpirationTime() < 3600000);
		Business biz = Business.findBusiness(getUnathorizedBusinessID());
		assertEquals(1, biz.getNotifications().size());
		assertEquals(NotificationType.PREMIUM_UPGRADE, biz.getNotifications().iterator().next().getType());
	}
	
	@Test
	public void enablePremiumFor12MonthsPremiumUserTest() throws PremiumUpgradeException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		assertTrue(Business.findBusiness(businessID).getPrincipals().iterator().next().getGrantedRoles().contains(RoleType.ROLE_BUSINESS_PREMIUM));
		Long base = getNDaysFromNowInMillis(30); //set in future
		Business.findBusiness(businessID).getSettings().setNonFreeAccountExpirationTime(base);
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		premiumEnablerService.enablePremiumForNMonths(Business.findBusiness(businessID), 12);
		smtpServer.stop();
		assertEquals(1, smtpServer.getReceivedEmailSize());
		assertTrue(Business.findBusiness(businessID).getPrincipals().iterator().next().getGrantedRoles().contains(RoleType.ROLE_BUSINESS_PREMIUM));
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(base);
		calendar.add(Calendar.MONTH, 12);
		assertTrue(calendar.getTimeInMillis() - Business.findBusiness(businessID).getSettings().getNonFreeAccountExpirationTime() == 0);
		Business biz = Business.findBusiness(businessID);
		assertEquals(1, biz.getNotifications().size());
		assertEquals(NotificationType.PREMIUM_EXTENSION, biz.getNotifications().iterator().next().getType());
		assertEquals(1, businessService.getNotifications(businessID).size());
		businessService.markNotificationAsSeen(businessID, biz.getNotifications().iterator().next().getId());
		assertEquals(0, businessService.getNotifications(businessID).size());
	}
	
	@Test
	public void notifyAndInvoiceFreeUserTest() throws PremiumUpgradeException, InterruptedException, DataAccessException, NoSuchObjectException, NotAuthenticatedException{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		Long businessID = getUnathorizedBusinessID();
		Business business = Business.findBusiness(businessID);
		Business novadartBusiness = Business.findBusiness(authenticatedPrincipal.getBusiness().getId());
		novadartBusiness.getInvoices().size();
		novadartBusiness.getClients().size();
		
		String emailAddr = business.getPrincipals().iterator().next().getUsername();
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		premiumEnablerService.notifyAndInvoiceBusiness(business, paymentPlansLoader.getPayPalPaymentPlanDescriptors()[0].getItemName(), emailAddr);
		smtpServer.stop();
		assertTrue(novadartBusiness.findClientByVatIDOrSsn(business.getVatID()) != null);
		assertTrue(invoiceService.getAllForClient(novadartBusiness.findClientByVatIDOrSsn(business.getVatID()).getId(), year).size() == 1);
		assertEquals(1, smtpServer.getReceivedEmailSize());
		SmtpMessage email = (SmtpMessage)smtpServer.getReceivedEmail().next();
		assertEquals(emailAddr, email.getHeaderValue("To"));
		assertEquals("Conferma attivazione Novabill Premium", email.getHeaderValue("Subject"));
	}
	
	public static class MockPaypelIPNHandlerService extends PayPalIPNHandlerService{

		@Override
		protected boolean check(String transactionType, Map<String, String> parametersMap) {
			return true;
		}
		
	}
	
	@Test(expected = PremiumUpgradeException.class)
	@DirtiesContext
	public void notifyIfPremiumUpgradeFail() throws PremiumUpgradeException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException{
		ExceptionTraceAspect.aspectOf().setIgnoreExceptions(new String[]{"java.lang.RuntimeException"});
		Long businessID = getUnathorizedBusinessID();
		Business business = Business.findBusiness(businessID);
		PremiumEnablerService mockedService = mock(PremiumEnablerService.class);
		doThrow(new PremiumUpgradeException(new Exception())).when(mockedService).notifyAndInvoiceBusiness(business,
				paymentPlans.getPayPalPaymentPlanDescriptors()[0].getItemName(), "risto.gligorov@novadart.com");
		MockPaypelIPNHandlerService mockIPNService = new MockPaypelIPNHandlerService();
		TestUtils.setPrivateField(PayPalIPNHandlerService.class, mockIPNService, "premiumEnablerService", mockedService);
		TestUtils.setPrivateField(PayPalIPNHandlerService.class, mockIPNService, "principalDetailsService", principalDetailsService);
		TestUtils.setPrivateField(PayPalIPNHandlerService.class, mockIPNService, "paymentPlans", paymentPlans);
		Map<String, String> parametersMap = new HashMap<>();
		parametersMap.put("custom", "risto.gligorov@novadart.com");
		parametersMap.put("item_name", "Piano Novabill Premium - 1 Anno");
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		try{
			mockIPNService.handle("", parametersMap, new Transaction());
		}finally {
			smtpServer.stop();
			assertEquals(2, smtpServer.getReceivedEmailSize());
		}
		
	}
	
	
}
