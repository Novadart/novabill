package com.novadart.novabill.test.suite;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.support.SessionStatus;
import com.novadart.novabill.domain.EmailPasswordHolder;
import com.novadart.novabill.domain.Registration;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.TokenGenerator;
import com.novadart.novabill.service.validator.RegistrationValidator;
import com.novadart.novabill.web.mvc.ActivateAccountController;
import com.novadart.novabill.web.mvc.RegisterController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:mvc-test-config.xml")
@Transactional
public class RegistrationActivationTest {
	
	@Resource(name = "userPasswordMap")
	protected HashMap<String, String> userPasswordMap;

	@Autowired
	private RegistrationValidator validator;
	
	private RegisterController initRegisterController(String token, String activationUrlPattern, int activationPeriod) throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
		RegisterController controller = new RegisterController();
		TokenGenerator tokenGenerator = mock(TokenGenerator.class);
		when(tokenGenerator.generateToken()).thenReturn(token);
		MessageSource messageSource = mock(MessageSource.class);
		when(messageSource.getMessage("activation.notification", null, null)).thenReturn("Activation email");
		TestUtils.setPrivateField(RegisterController.class, controller, "tokenGenerator", tokenGenerator);
		TestUtils.setPrivateField(RegisterController.class, controller, "messageSource", messageSource);
		TestUtils.setPrivateField(RegisterController.class, controller, "validator", validator);
		TestUtils.setPrivateField(RegisterController.class, controller, "activationUrlPattern", activationUrlPattern);
		TestUtils.setPrivateField(RegisterController.class, controller, "activationPeriod", activationPeriod);
		return controller;
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
	
	@Test
	public void defaultRegistrationActivationFlow() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException, CloneNotSupportedException{
		String token = "1", email = "foo@bar.com", password = "password";
		RegisterController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, email, password, password, true);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null);
		ActivateAccountController activationController = new ActivateAccountController();
		Model model = new ExtendedModelMap();
		String activateView = activationController.setupForm(email, token, model);
		String forwardToSpringSecurityCheck = activationController.processSubmit(email, password, (Registration)model.asMap().get("registration"),
				mock(Model.class), mock(SessionStatus.class));
		assertEquals("redirect:/registrationCompleted", registerView);
		assertEquals("activate", activateView);
		assertEquals("forward:/resources/j_spring_security_check", forwardToSpringSecurityCheck);
	}
	
	@Test
	public void registerPasswordMismatch() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException {
		String token = "1", email = "foo@bar.com", password = "password1", confirmPassword = "password2";
		RegisterController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, email, password, confirmPassword, true);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null);
		assertEquals("register", registerView);
	}
	
	@Test
	public void registerInvalidEmail() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException{
		String token = "1", email = "foo.bar.com", password = "password";
		RegisterController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, email, password, password, true);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null);
		assertEquals("register", registerView);
	}
	
	@Test
	public void registerExistingEmail() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException{
		String token = "1", password = "password";
		RegisterController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, userPasswordMap.keySet().iterator().next(), password, password, true);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null);
		assertEquals("register", registerView);
	}
	
	@Test
	public void registerNullEmail() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException{
		String token = "1", password = "password";
		RegisterController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, null, password, password, true);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null);
		assertEquals("register", registerView);
	}
	
	@Test
	public void registerNullPassword() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException{
		String token = "1", email = "foo@bar.com";
		RegisterController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, email, null, null, true);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null);
		assertEquals("register", registerView);
	}
	
	@Test
	public void registerPasswordTooShort() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException{
		String token = "1", email = "foo@bar.com";
		RegisterController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, email, "abcd", null, true);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null);
		assertEquals("register", registerView);
	}
	
	@Test
	public void registerPasswordTooLong() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException{
		String token = "1", email = "foo@bar.com";
		RegisterController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, email, StringUtils.leftPad("1", 20), null, true);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null);
		assertEquals("register", registerView);
	}
	
	@Test
	public void registerAgreementNotAccepted() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException{
		String token = "1", email = "foo@bar.com", password = "password";
		RegisterController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, email, password, password, false);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null);
		assertEquals("register", registerView);
	}
	
	@Test
	public void registerRequestExpired() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException, CloneNotSupportedException{
		String token = "1", email = "foo@bar.com", password = "password";
		RegisterController registerController = initRegisterController(token, "%s%s", 0); //expires immediately
		Registration registration = initRegistration(token, email, password, password, true);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null);
		ActivateAccountController activationController = new ActivateAccountController();
		String activateView = activationController.setupForm(email, token, mock(Model.class));
		assertEquals("redirect:/registrationCompleted", registerView);
		assertEquals("invalidActivationRequest", activateView);
	}
	
	@Test
	public void replayRegistrationActivationFlow() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException, CloneNotSupportedException{
		String token = "1", email = "foo@bar.com", password = "password";
		RegisterController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, email, password, password, true);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null);
		ActivateAccountController activationController = new ActivateAccountController();
		Model model = new ExtendedModelMap();
		String activateView1 = activationController.setupForm(email, token, model);
		String forwardToSpringSecurityCheck1 = activationController.processSubmit(email, password, (Registration)model.asMap().get("registration"),
				mock(Model.class), mock(SessionStatus.class));
		model = new ExtendedModelMap();
		String activateView2 = activationController.setupForm(email, token, model);
		assertEquals("redirect:/registrationCompleted", registerView);
		assertEquals("activate", activateView1);
		assertEquals("forward:/resources/j_spring_security_check", forwardToSpringSecurityCheck1);
		assertEquals("invalidActivationRequest", activateView2);
	}
	
	@Test(expected = JpaSystemException.class)
	public void twoRegistrationsWithSameEmail() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException, CloneNotSupportedException{
		String token1 = "1", token2 = "2", email = "foo@bar.com", password = "password";

		//First registration
		RegisterController registerController = initRegisterController(token1, "%s%s", 24);
		Registration registration = initRegistration(token1, email, password, password, true);
		String registerView1 = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null);
		
		//Second registration
		registerController = initRegisterController(token2, "%s%s", 24);
		registration = initRegistration(token2, email, password, password, true);
		String registerView2 = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null);
		
		//First activation initiation
		ActivateAccountController activationController1 = new ActivateAccountController();
		Model model1 = new ExtendedModelMap();
		String activateView1 = activationController1.setupForm(email, token1, model1);

		//Second activation initiation
		ActivateAccountController activationController2 = new ActivateAccountController();
		Model model2 = new ExtendedModelMap();
		String activateView2 = activationController2.setupForm(email, token2, model2);
		
		String forwardToSpringSecurityCheck1 = activationController1.processSubmit(email, password, (Registration)model1.asMap().get("registration"),
				mock(Model.class), mock(SessionStatus.class));
		
		Principal.entityManager().flush();
		
		assertEquals("redirect:/registrationCompleted", registerView1);
		assertEquals("activate", activateView1);
		assertEquals("forward:/resources/j_spring_security_check", forwardToSpringSecurityCheck1);
		
		assertEquals("redirect:/registrationCompleted", registerView2);
		assertEquals("activate", activateView2);
		
		activationController2.processSubmit(email, password, (Registration)model2.asMap().get("registration"),
				mock(Model.class), mock(SessionStatus.class));
		
		Principal.entityManager().flush();
		
	}
	
	@Test
	public void registrationActivationWrongPassword() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException, CloneNotSupportedException{
		String token = "1", email = "foo@bar.com", password = "password";
		RegisterController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, email, password, password, true);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null);
		ActivateAccountController activationController = new ActivateAccountController();
		Model model = new ExtendedModelMap();
		String activateView = activationController.setupForm(email, token, model);
		String backToActivate = activationController.processSubmit(email, password + "1", (Registration)model.asMap().get("registration"),
				mock(Model.class), mock(SessionStatus.class));
		assertEquals("redirect:/registrationCompleted", registerView);
		assertEquals("activate", activateView);
		assertEquals("activate", backToActivate);
	}
	
	@Test
	public void registrationActivationNullPassword() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException, CloneNotSupportedException{
		String token = "1", email = "foo@bar.com", password = "password";
		RegisterController registerController = initRegisterController(token, "%s%s", 24);
		Registration registration = initRegistration(token, email, password, password, true);
		String registerView = registerController.processSubmit(registration, new BeanPropertyBindingResult(registration, "registration"), mock(SessionStatus.class), null);
		ActivateAccountController activationController = new ActivateAccountController();
		Model model = new ExtendedModelMap();
		String activateView = activationController.setupForm(email, token, model);
		String backToActivate = activationController.processSubmit(email, null, (Registration)model.asMap().get("registration"),
				mock(Model.class), mock(SessionStatus.class));
		assertEquals("redirect:/registrationCompleted", registerView);
		assertEquals("activate", activateView);
		assertEquals("activate", backToActivate);
	}

}
