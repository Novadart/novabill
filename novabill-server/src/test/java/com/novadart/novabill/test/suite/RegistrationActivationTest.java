package com.novadart.novabill.test.suite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.support.SessionStatus;

import com.dumbster.smtp.SimpleSmtpServer;
import com.novadart.novabill.domain.EmailPasswordHolder;
import com.novadart.novabill.service.TokenGenerator;
import com.novadart.novabill.service.validator.RegistrationValidator;
import com.novadart.novabill.web.mvc.RegistrationController;
import com.novadart.novabill.web.mvc.Urls;
import com.novadart.novabill.web.mvc.command.Registration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:mvc-test-config.xml")
@Transactional
@DirtiesContext
@ActiveProfiles("dev")
public class RegistrationActivationTest extends AuthenticatedTest{
	

	@Autowired
	private RegistrationValidator validator;
	
	private RegistrationController initRegisterController(String token, String activationUrlPattern, int activationPeriod) throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
		RegistrationController controller = new RegistrationController();
		TokenGenerator tokenGenerator = mock(TokenGenerator.class);
		when(tokenGenerator.generateToken()).thenReturn(token);
		MessageSource messageSource = mock(MessageSource.class);
		when(messageSource.getMessage("activation.notification", null, null)).thenReturn("Activation email");
		TestUtils.setPrivateField(RegistrationController.class, controller, "validator", validator);
		return controller;
	}
	
	
	private Registration initRegistration(String token, String email, String password, String confirmPassword, boolean agreementAccepted) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
		Registration registration = new Registration();
		registration.setEmail(email);
		TestUtils.setPrivateField(EmailPasswordHolder.class, registration, "password", password); //avoid password hashing
		TestUtils.setPrivateField(EmailPasswordHolder.class, registration, "confirmPassword", confirmPassword); //avoid password hashing
		registration.setAgreementAccepted(agreementAccepted);
		return registration;
	}
	
	@Test
	public void defaultRegistrationActivationFlow() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException, CloneNotSupportedException{
		String token = "1", email = "foo@bar.com", password = "Novadart28&";
		RegistrationController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, email, password, password, true);
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null, mock(Model.class), mock(HttpServletRequest.class));
		smtpServer.stop();
		assertEquals(2, smtpServer.getReceivedEmailSize()); //notification email and confirmation email
		assertTrue(registerView.startsWith("forward:"+Urls.PUBLIC_LOGIN_CHECK));
	}
	
	@Test
	public void registerPasswordMismatch() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException {
		String token = "1", email = "foo@bar.com", password = "password1", confirmPassword = "password2";
		RegistrationController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, email, password, confirmPassword, true);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null, mock(Model.class), mock(HttpServletRequest.class));
		assertEquals("frontend.register", registerView);
	}
	
	@Test
	public void registerInvalidEmail() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException{
		String token = "1", email = "foo.bar.com", password = "password";
		RegistrationController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, email, password, password, true);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null, mock(Model.class), mock(HttpServletRequest.class));
		assertEquals("frontend.register", registerView);
	}
	
	@Test
	public void registerExistingEmail() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException{
		//dropRegistrations();
		String token = "1", password = "password";
		RegistrationController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, userPasswordMap.keySet().iterator().next(), password, password, true);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null, mock(Model.class), mock(HttpServletRequest.class));
		assertEquals("frontend.register", registerView);
	}
	
	@Test
	public void registerNullEmail() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException{
		String token = "1", password = "password";
		RegistrationController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, null, password, password, true);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null, mock(Model.class), mock(HttpServletRequest.class));
		assertEquals("frontend.register", registerView);
	}
	
	@Test
	public void registerNullPassword() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException{
		String token = "1", email = "foo@bar.com";
		RegistrationController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, email, null, null, true);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null, mock(Model.class), mock(HttpServletRequest.class));
		assertEquals("frontend.register", registerView);
	}
	
	@Test
	public void registerPasswordTooShort() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException{
		String token = "1", email = "foo@bar.com";
		RegistrationController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, email, "abcd", null, true);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null, mock(Model.class), mock(HttpServletRequest.class));
		assertEquals("frontend.register", registerView);
	}
	
	@Test
	public void registerPasswordTooLong() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException{
		String token = "1", email = "foo@bar.com";
		RegistrationController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, email, StringUtils.leftPad("1", 20), null, true);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null, mock(Model.class), mock(HttpServletRequest.class));
		assertEquals("frontend.register", registerView);
	}
	
	@Test
	public void registerAgreementNotAccepted() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException{
		String token = "1", email = "foo@bar.com", password = "Novadart28&";
		RegistrationController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, email, password, password, false);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null, mock(Model.class), mock(HttpServletRequest.class));
		assertEquals("frontend.register", registerView);
	}
	
	
}
