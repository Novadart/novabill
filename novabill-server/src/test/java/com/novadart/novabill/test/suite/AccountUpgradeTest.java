package com.novadart.novabill.test.suite;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.service.AccountStatusManagerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:mvc-test-config.xml")
@Transactional
@ActiveProfiles("dev")
public class AccountUpgradeTest extends AuthenticatedTest {
	
	@Autowired
	private AccountStatusManagerService accountStatusService;

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
	
	
}
