package com.novadart.novabill.test.suite;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import com.novadart.novabill.aspect.logging.ExceptionTraceAspect;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Notification;
import com.novadart.novabill.domain.Transaction;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.paypal.PayPalIPNHandlerService;
import com.novadart.novabill.paypal.PaymentPlanDescriptor;
import com.novadart.novabill.paypal.PaymentPlansLoader;
import com.novadart.novabill.service.PrincipalDetailsService;
import com.novadart.novabill.service.TokenGenerator;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.periodic.DisablerService;
import com.novadart.novabill.service.periodic.PremiumDisablerService;
import com.novadart.novabill.service.periodic.TrialAccountDisablerService;
import com.novadart.novabill.service.web.BusinessService;
import com.novadart.novabill.service.web.InvoiceService;
import com.novadart.novabill.service.web.PremiumEnablerService;
import com.novadart.novabill.shared.client.data.LayoutType;
import com.novadart.novabill.shared.client.dto.NotificationType;
import com.novadart.novabill.shared.client.exception.*;
import com.novadart.novabill.web.mvc.UpgradeAccountController;
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

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:mvc-test-config.xml")
@Transactional
@ActiveProfiles("dev")
@DirtiesContext
public class AccountUpgradeTest extends AuthenticatedTest {
	
	@Autowired
	private PremiumDisablerService accountStatusService;
	
	@Autowired
	private PaymentPlansLoader paymentPlans;

	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private PremiumEnablerService premiumEnablerService;

	@Autowired
	private TrialAccountDisablerService trialAccountDisablerService;
	
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

	public void accountExpirationInNDaysNotificationTest(int days, DisablerService service, RoleType role){
		Business business = Business.findBusiness(authenticatedPrincipal.getBusiness().getId());
		business.getSettings().setNonFreeAccountExpirationTime(getNDaysFromNowInMillis(days));
		Principal principal = business.getPrincipals().iterator().next();
		principal.getGrantedRoles().clear();
		principal.getGrantedRoles().add(role);
		business.flush();

		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		service.runTasks();
		smtpServer.stop();
		assertEquals(1, smtpServer.getReceivedEmailSize());

		SmtpMessage email = (SmtpMessage)smtpServer.getReceivedEmail().next();
		assertEquals(business.getPrincipals().iterator().next().getUsername(), email.getHeaderValue("To"));

	}
	
	@Test
	public void accountExpirationIn7DaysNotificationTest(){
		accountExpirationInNDaysNotificationTest(7, accountStatusService, RoleType.ROLE_BUSINESS_PREMIUM);
		Notification notification = Notification.findAllNotifications().iterator().next();
		assertEquals(NotificationType.PREMIUM_DOWNGRADE_7_DAYS, notification.getType());
	}
	
	@Test
	public void accountExpirationIn15DaysNotificationTest(){
		accountExpirationInNDaysNotificationTest(15, accountStatusService, RoleType.ROLE_BUSINESS_PREMIUM);
		Notification notification = Notification.findAllNotifications().iterator().next();
		assertEquals(NotificationType.PREMIUM_DOWNGRADE_15_DAYS, notification.getType());
	}
	
	@Test
	public void accountExpirationIn30DaysNotificationTest(){
		accountExpirationInNDaysNotificationTest(30, accountStatusService, RoleType.ROLE_BUSINESS_PREMIUM);
		Notification notification = Notification.findAllNotifications().iterator().next();
		assertEquals(NotificationType.PREMIUM_DOWNGRADE_30_DAYS, notification.getType());
	}

	@Test
	public void trialExpirationIn7DaysNotificationTest(){
		accountExpirationInNDaysNotificationTest(7, trialAccountDisablerService, RoleType.ROLE_BUSINESS_TRIAL);
		Notification notification = Notification.findAllNotifications().iterator().next();
		assertEquals(NotificationType.TRIAL_7_DAYS_LEFT, notification.getType());
	}

	@Test
	public void trialExpirationIn15DaysNotificationTest(){
		accountExpirationInNDaysNotificationTest(15, trialAccountDisablerService
				, RoleType.ROLE_BUSINESS_TRIAL);
		Notification notification = Notification.findAllNotifications().iterator().next();
		assertEquals(NotificationType.TRIAL_15_DAYS_LEFT, notification.getType());
	}



	@Test
	public void disableExpiredAccountsTest(){
		Business business = Business.findBusiness(authenticatedPrincipal.getBusiness().getId());
		business.getSettings().setDefaultLayoutType(LayoutType.TIDY);
		business.getSettings().setNonFreeAccountExpirationTime(System.currentTimeMillis() - 100); //set in past
		business.flush();
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		accountStatusService.runTasks();
		smtpServer.stop();
		assertEquals(1, smtpServer.getReceivedEmailSize());

		SmtpMessage email = (SmtpMessage)smtpServer.getReceivedEmail().next();
		assertEquals(business.getPrincipals().iterator().next().getUsername(), email.getHeaderValue("To"));

		assertTrue(authenticatedPrincipal.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_EXPIRED));
		assertTrue(!authenticatedPrincipal.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_PREMIUM));
		assertTrue(!authenticatedPrincipal.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_TRIAL));
		
		business = Business.findBusiness(business.getId());
		assertEquals(1, business.getNotifications().size());
		assertEquals(NotificationType.PREMIUM_DOWNGRADE, business.getNotifications().iterator().next().getType());
	}

	@Test
	public void disableExpiredTrialAccountsTest(){
		Business business = Business.findBusiness(authenticatedPrincipal.getBusiness().getId());
		business.getSettings().setNonFreeAccountExpirationTime(System.currentTimeMillis() - 100); //set in past
		Principal principal = business.getPrincipals().iterator().next();
		principal.getGrantedRoles().clear();
		principal.getGrantedRoles().add(RoleType.ROLE_BUSINESS_TRIAL);
		business.flush();

		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		trialAccountDisablerService.runTasks();
		smtpServer.stop();
		assertEquals(1, smtpServer.getReceivedEmailSize());

		SmtpMessage email = (SmtpMessage)smtpServer.getReceivedEmail().next();
		assertEquals(business.getPrincipals().iterator().next().getUsername(), email.getHeaderValue("To"));

		assertTrue(authenticatedPrincipal.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_EXPIRED));
		assertTrue(!authenticatedPrincipal.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_PREMIUM));
		assertTrue(!authenticatedPrincipal.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_TRIAL));

		business = Business.findBusiness(business.getId());
		assertEquals(1, business.getNotifications().size());
		assertEquals(NotificationType.TRIAL_FINISHED, business.getNotifications().iterator().next().getType());
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
		assertTrue(Business.findBusiness(businessID).getPrincipals().iterator().next().getGrantedRoles().contains(RoleType.ROLE_BUSINESS_TRIAL));
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
	public void enablePremiumFor12MonthsPremiumUserTest() throws PremiumUpgradeException, FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException {
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
	public void notifyAndInvoiceFreeUserTest() throws PremiumUpgradeException, InterruptedException, DataAccessException, NoSuchObjectException, NotAuthenticatedException, FreeUserAccessForbiddenException {
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
	
	@Test(expected = Exception.class)
	public void notifyIfPremiumUpgradeFail() throws PremiumUpgradeException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException {
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
		try {
			mockIPNService.handle("", parametersMap, new Transaction());
		} catch (PremiumUpgradeException e) {
			assertEquals(2, smtpServer.getReceivedEmailSize());
			throw e;
		}finally {
			smtpServer.stop();
		}
		fail();
	}
	
	
}
