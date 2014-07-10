package com.novadart.novabill.test.suite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.support.SessionStatus;

import com.dumbster.smtp.SimpleSmtpServer;
import com.novadart.novabill.domain.EmailPasswordHolder;
import com.novadart.novabill.domain.Registration;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.TokenGenerator;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.RegistrationValidator;
import com.novadart.novabill.web.mvc.ActivateAccountController;
import com.novadart.novabill.web.mvc.RegistrationController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:mvc-test-config.xml")
@Transactional
@DirtiesContext
@ActiveProfiles("dev")
public class RegistrationActivationTest extends AuthenticatedTest{
	

	@Autowired
	private RegistrationValidator validator;
	
	@Autowired
	private UtilsService utilsService;
	
	private RegistrationController initRegisterController(String token, String activationUrlPattern, int activationPeriod) throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
		RegistrationController controller = new RegistrationController();
		TokenGenerator tokenGenerator = mock(TokenGenerator.class);
		when(tokenGenerator.generateToken()).thenReturn(token);
		MessageSource messageSource = mock(MessageSource.class);
		when(messageSource.getMessage("activation.notification", null, null)).thenReturn("Activation email");
		TestUtils.setPrivateField(RegistrationController.class, controller, "tokenGenerator", tokenGenerator);
		TestUtils.setPrivateField(RegistrationController.class, controller, "messageSource", messageSource);
		TestUtils.setPrivateField(RegistrationController.class, controller, "validator", validator);
		TestUtils.setPrivateField(RegistrationController.class, controller, "activationUrlPattern", activationUrlPattern);
		TestUtils.setPrivateField(RegistrationController.class, controller, "activationPeriod", activationPeriod);
		return controller;
	}
	
	private void dropRegistrations(){
		for(Registration r: Registration.findAllRegistrations())
			r.remove();
		Registration.entityManager().flush();
	}
	
	private Registration initRegistration(String token, String email, String password, String confirmPassword, boolean agreementAccepted) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
		Registration registration = new Registration();
		registration.setEmail(email);
		TestUtils.setPrivateField(EmailPasswordHolder.class, registration, "password", password); //avoid password hashing
		TestUtils.setPrivateField(EmailPasswordHolder.class, registration, "confirmPassword", confirmPassword); //avoid password hashing
		registration.setAgreementAccepted(agreementAccepted);
		registration.setActivationToken(token);
		return registration;
	}
	
	private ActivateAccountController initActivateAccountController() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		ActivateAccountController activateAccountController = new ActivateAccountController();
		TestUtils.setPrivateField(ActivateAccountController.class, activateAccountController, "utilsService", utilsService);
		return activateAccountController;
	}
	
	@Test
	public void defaultRegistrationActivationFlow() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException, CloneNotSupportedException{
		dropRegistrations();
		String token = "1", email = "foo@bar.com", password = "Novadart28&";
		RegistrationController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, email, password, password, true);
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null, mock(Model.class));
		smtpServer.stop();
		assertEquals(2, smtpServer.getReceivedEmailSize()); //besides registration email, notification email is sent too
		
		ActivateAccountController activationController = initActivateAccountController();
		Model model = new ExtendedModelMap();
		String activateView = activationController.setupForm(email, token, model);
		String forwardToSpringSecurityCheck = activationController.processSubmit(email, password, (Registration)model.asMap().get("registration"),
				mock(Model.class), mock(SessionStatus.class), Locale.ITALIAN);
		assertEquals("redirect:/registration-complete", registerView);
		assertEquals("frontend.activate", activateView);
		assertEquals("forward:/resources/login_check", forwardToSpringSecurityCheck);
	}
	
	@Test
	public void registerPasswordMismatch() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException {
		dropRegistrations();
		String token = "1", email = "foo@bar.com", password = "password1", confirmPassword = "password2";
		RegistrationController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, email, password, confirmPassword, true);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null, mock(Model.class));
		assertEquals("frontend.register", registerView);
	}
	
	@Test
	public void registerInvalidEmail() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException{
		dropRegistrations();
		String token = "1", email = "foo.bar.com", password = "password";
		RegistrationController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, email, password, password, true);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null, mock(Model.class));
		assertEquals("frontend.register", registerView);
	}
	
	@Test
	public void registerExistingEmail() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException{
		dropRegistrations();
		String token = "1", password = "password";
		RegistrationController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, userPasswordMap.keySet().iterator().next(), password, password, true);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null, mock(Model.class));
		assertEquals("frontend.register", registerView);
	}
	
	@Test
	public void registerNullEmail() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException{
		dropRegistrations();
		String token = "1", password = "password";
		RegistrationController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, null, password, password, true);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null, mock(Model.class));
		assertEquals("frontend.register", registerView);
	}
	
	@Test
	public void registerNullPassword() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException{
		dropRegistrations();
		String token = "1", email = "foo@bar.com";
		RegistrationController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, email, null, null, true);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null, mock(Model.class));
		assertEquals("frontend.register", registerView);
	}
	
	@Test
	public void registerPasswordTooShort() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException{
		dropRegistrations();
		String token = "1", email = "foo@bar.com";
		RegistrationController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, email, "abcd", null, true);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null, mock(Model.class));
		assertEquals("frontend.register", registerView);
	}
	
	@Test
	public void registerPasswordTooLong() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException{
		dropRegistrations();
		String token = "1", email = "foo@bar.com";
		RegistrationController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, email, StringUtils.leftPad("1", 20), null, true);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null, mock(Model.class));
		assertEquals("frontend.register", registerView);
	}
	
	@Test
	public void registerAgreementNotAccepted() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException{
		dropRegistrations();
		String token = "1", email = "foo@bar.com", password = "Novadart28&";
		RegistrationController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, email, password, password, false);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null, mock(Model.class));
		assertEquals("frontend.register", registerView);
	}
	
	@Test
	public void registerRequestExpired() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException, CloneNotSupportedException{
		dropRegistrations();
		String token = "1", email = "foo@bar.com", password = "Novadart28&";
		RegistrationController registerController = initRegisterController(token, "%s%s", 0); //expires immediately
		Registration registration = initRegistration(token, email, password, password, true);
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null, mock(Model.class));
		smtpServer.stop();
		assertTrue(smtpServer.getReceivedEmailSize() == 2); //notification email is sent too
		
		ActivateAccountController activationController = new ActivateAccountController();
		String activateView = activationController.setupForm(email, token, mock(Model.class));
		assertEquals("redirect:/registration-complete", registerView);
		assertEquals("frontend.invalidActivationRequest", activateView);
	}
	
	@Test
	public void replayRegistrationActivationFlow() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException, CloneNotSupportedException{
		dropRegistrations();
		String token = "1", email = "foo@bar.com", password = "Novadart28&";
		RegistrationController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, email, password, password, true);
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null, mock(Model.class));
		smtpServer.stop();
		assertEquals(2, smtpServer.getReceivedEmailSize());//notification email is sent too
		
		ActivateAccountController activationController = initActivateAccountController();
		Model model = new ExtendedModelMap();
		String activateView1 = activationController.setupForm(email, token, model);
		String forwardToSpringSecurityCheck1 = activationController.processSubmit(email, password, (Registration)model.asMap().get("registration"),
				mock(Model.class), mock(SessionStatus.class), Locale.ITALIAN);
		model = new ExtendedModelMap();
		String activateView2 = activationController.setupForm(email, token, model);
		assertEquals("redirect:/registration-complete", registerView);
		assertEquals("frontend.activate", activateView1);
		assertEquals("forward:/resources/login_check", forwardToSpringSecurityCheck1);
		assertEquals("frontend.invalidActivationRequest", activateView2);
	}
	
	@Test(expected = JpaSystemException.class)
	public void twoRegistrationsWithSameEmail() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException, CloneNotSupportedException{
		dropRegistrations();
		String token1 = "1", token2 = "2", email = "foo@bar.com", password = "Novadart28&";

		//First registration
		RegistrationController registerController = initRegisterController(token1, "%s%s", 24);
		Registration registration = initRegistration(token1, email, password, password, true);
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		String registerView1 = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null, mock(Model.class));
		smtpServer.stop();
		assertTrue(smtpServer.getReceivedEmailSize() == 2); //notification email is sent too
		
		//Second registration
		registerController = initRegisterController(token2, "%s%s", 24);
		registration = initRegistration(token2, email, password, password, true);
		
		smtpServer = SimpleSmtpServer.start(2525);
		String registerView2 = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null, mock(Model.class));
		smtpServer.stop();
		assertTrue(smtpServer.getReceivedEmailSize() == 2); //notification email is sent too
		
		//First activation initiation
		ActivateAccountController activationController1 = initActivateAccountController();
		Model model1 = new ExtendedModelMap();
		String activateView1 = activationController1.setupForm(email, token1, model1);

		//Second activation initiation
		ActivateAccountController activationController2 = initActivateAccountController();
		Model model2 = new ExtendedModelMap();
		String activateView2 = activationController2.setupForm(email, token2, model2);
		String forwardToSpringSecurityCheck1 = activationController1.processSubmit(email, password, (Registration)model1.asMap().get("registration"),
				mock(Model.class), mock(SessionStatus.class), Locale.ITALIAN);
		
		Principal.entityManager().flush();
		
		assertEquals("redirect:/registration-complete", registerView1);
		assertEquals("frontend.activate", activateView1);
		assertEquals("forward:/resources/login_check", forwardToSpringSecurityCheck1);
		
		assertEquals("redirect:/registration-complete", registerView2);
		assertEquals("frontend.activate", activateView2);
		
		activationController2.processSubmit(email, password, (Registration)model2.asMap().get("registration"),
				mock(Model.class), mock(SessionStatus.class), Locale.ITALIAN);
		
		Principal.entityManager().flush();
		
	}
	
	@Test
	public void registrationActivationWrongPassword() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException, CloneNotSupportedException{
		dropRegistrations();
		String token = "1", email = "foo@bar.com", password = "Novadart28&";
		RegistrationController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, email, password, password, true);
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null, mock(Model.class));
		smtpServer.stop();
		assertTrue(smtpServer.getReceivedEmailSize() == 2);//besides registration email, notification email is sent too
		
		ActivateAccountController activationController = initActivateAccountController();
		Model model = new ExtendedModelMap();
		String activateView = activationController.setupForm(email, token, model);
		String backToActivate = activationController.processSubmit(email, password + "1", (Registration)model.asMap().get("registration"),
				mock(Model.class), mock(SessionStatus.class), Locale.ITALIAN);
		assertEquals("redirect:/registration-complete", registerView);
		assertEquals("frontend.activate", activateView);
		assertEquals("frontend.activate", backToActivate);
	}
	
}
