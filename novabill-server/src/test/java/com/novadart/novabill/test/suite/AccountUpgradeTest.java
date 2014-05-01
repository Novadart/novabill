package com.novadart.novabill.test.suite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.UpgradeToken;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.paypal.PayPalPaymentPlanDescriptor;
import com.novadart.novabill.paypal.PaymentPlansLoader;
import com.novadart.novabill.service.AccountStatusManagerService;
import com.novadart.novabill.service.TokenGenerator;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.web.mvc.UpgradeAccountController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:mvc-test-config.xml")
@Transactional
@ActiveProfiles("dev")
public class AccountUpgradeTest extends AuthenticatedTest {
	
	@Autowired
	private AccountStatusManagerService accountStatusService;
	
	@Autowired
	private PaymentPlansLoader paymentPlans;

	@Autowired
	private UtilsService utilsService;
	
	private long getNDaysFromNowInMillis(int days){
		long DAY_IN_MILLIS = 86_400_000l;
		Long now = System.currentTimeMillis();
		return now + days * DAY_IN_MILLIS;
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
	}
	
	
	@Test
	public void paymentPlansTest(){
		for(PayPalPaymentPlanDescriptor paymentPlan: paymentPlans.getPayPalPaymentPlanDescriptors()){
			assertTrue(EqualsBuilder.reflectionEquals(paymentPlan, paymentPlans.getPayPalPaymentPlanDescriptor(paymentPlan.getItemName())));
		}
	}
	
	private UpgradeAccountController initUpgradeAccountController(String token) throws NoSuchAlgorithmException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		UpgradeAccountController controller = new UpgradeAccountController();
		TokenGenerator tokenGenerator = mock(TokenGenerator.class);
		when(tokenGenerator.generateToken()).thenReturn(token);
		TestUtils.setPrivateField(UpgradeAccountController.class, controller, "tokenGenerator", tokenGenerator);
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
		assertEquals("private.upgrade", view);
		assertEquals(1, UpgradeToken.countUpgradeTokens());
		assertEquals(authenticatedPrincipal.getUsername(), UpgradeToken.findAllUpgradeTokens().iterator().next().getEmail());
	}
	
	@Test
	public void handlePaypalReturnTest() throws NoSuchAlgorithmException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, MalformedURLException, UnsupportedEncodingException{
		UpgradeAccountController controller = initUpgradeAccountController("1");
		controller.display(mock(Model.class), mockRequest());
		String view = controller.handlePaypalReturn("1", authenticatedPrincipal.getUsername());
		assertEquals("private.premiumUpgradeSuccess", view);
		assertEquals(0, UpgradeToken.countUpgradeTokens());
	}
	
}
