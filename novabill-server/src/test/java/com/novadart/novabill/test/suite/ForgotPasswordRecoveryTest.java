package com.novadart.novabill.test.suite;

import com.dumbster.smtp.SimpleSmtpServer;
import com.novadart.novabill.domain.EmailPasswordHolder;
import com.novadart.novabill.domain.ForgotPassword;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.TokenGenerator;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.ForgotPasswordValidator;
import com.novadart.novabill.web.mvc.ForgotPasswordController;
import com.novadart.novabill.web.mvc.PasswordRecoveryController;
import com.novadart.novabill.web.mvc.Urls;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.support.SessionStatus;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:mvc-test-config.xml")
@Transactional
@DirtiesContext
@ActiveProfiles("dev")
public class ForgotPasswordRecoveryTest extends AuthenticatedTest{

	@Resource(name = "userPasswordMap")
	protected HashMap<String, String> userPasswordMap;
	
	@Autowired
	private ForgotPasswordValidator validator;
	
	@Autowired
	private UtilsService utilsService;

	private ForgotPasswordController initForgotPasswordController(String token, String passwordRecoveryUrlPattern, int passwordRecoveryPeriod) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, NoSuchAlgorithmException{
		ForgotPasswordController controller = new ForgotPasswordController();
		TokenGenerator tokenGenerator = mock(TokenGenerator.class);
		when(tokenGenerator.generateToken()).thenReturn(token);
		MessageSource messageSource = mock(MessageSource.class);
		when(messageSource.getMessage("password.recovery.notification", null, null)).thenReturn("Password recovery notification");
		TestUtils.setPrivateField(ForgotPasswordController.class, controller, "tokenGenerator", tokenGenerator);
		TestUtils.setPrivateField(ForgotPasswordController.class, controller, "messageSource", messageSource);
		TestUtils.setPrivateField(ForgotPasswordController.class, controller, "validator", validator);
		TestUtils.setPrivateField(ForgotPasswordController.class, controller, "passwordRecoveryUrlPattern", passwordRecoveryUrlPattern);
		TestUtils.setPrivateField(ForgotPasswordController.class, controller, "passwordRecoveryPeriod", passwordRecoveryPeriod);
		return controller;
	}
	
	private ForgotPassword initForgotPassword(String token, String email){
		ForgotPassword forgotPassword = new ForgotPassword();
		forgotPassword.setEmail(email);
		forgotPassword.setActivationToken(token);
		return forgotPassword;
	}
	
	private void setPasswordsToPrivateFields(ForgotPassword forgotPassword, String password, String confirmPassword) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
		TestUtils.setPrivateField(EmailPasswordHolder.class, forgotPassword, "password", password);
		TestUtils.setPrivateField(EmailPasswordHolder.class, forgotPassword, "confirmPassword", confirmPassword);
	}
	
	private PasswordRecoveryController initPasswordRecoveryController() throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
		PasswordRecoveryController controller = new PasswordRecoveryController();
		TestUtils.setPrivateField(PasswordRecoveryController.class, controller, "validator", validator);
		return controller;
	}
	
	private void dropForgotPasswords(){
		for(ForgotPassword fp: ForgotPassword.findAllForgotPasswords())
			fp.remove();
		ForgotPassword.entityManager().flush();
	}
	
	@Test
	public void defaultForgotPasswordRecoveryFlow() throws SecurityException, IllegalArgumentException, NoSuchAlgorithmException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException, CloneNotSupportedException{
		dropForgotPasswords();
		String token = "1", email = userPasswordMap.keySet().iterator().next(), newPassword = "N3w password$";
		ForgotPasswordController forgotPasswordController = initForgotPasswordController(token, "%s%s", 24);
		ForgotPassword forgotPassword = initForgotPassword(token, email);
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		String forgotPasswordView = forgotPasswordController.processSubmit(forgotPassword, new BeanPropertyBindingResult(forgotPassword, "forgotPassword"),
				mock(SessionStatus.class), null, mock(Model.class));
		smtpServer.stop();
		assertTrue(smtpServer.getReceivedEmailSize() == 1);
		
		
		PasswordRecoveryController recoveryController = initPasswordRecoveryController();
		Model model = new ExtendedModelMap();
		String passwordRecoveryView = recoveryController.setupForm(email, token, model);
		forgotPassword = (ForgotPassword)model.asMap().get("forgotPassword");
		setPasswordsToPrivateFields(forgotPassword, newPassword, newPassword);
		String passwordRecoverySuccessView = recoveryController.processSubmit(forgotPassword, new BeanPropertyBindingResult(forgotPassword, "forgotPassword"),
				mock(SessionStatus.class), mock(Model.class));
		
		assertEquals("redirect:/forgot-password-ok", forgotPasswordView);
		assertEquals("frontend.passwordRecovery", passwordRecoveryView);
		assertEquals("redirect:" + Urls.PUBLIC_PASSWORD_RECOVERY_OK, passwordRecoverySuccessView);
		
		Principal persistedPrincipal = Principal.findByUsername(email);
		assertTrue(utilsService.isPasswordValid(persistedPrincipal.getPassword(), newPassword));
	}
	
	@Test
	public void forgotPasswordNonExistingEmail() throws SecurityException, IllegalArgumentException, NoSuchAlgorithmException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException{
		dropForgotPasswords();
		String token = "1", email = "foo@bar.com";
		ForgotPasswordController forgotPasswordController = initForgotPasswordController(token, "%s%s", 24);
		ForgotPassword forgotPassword = initForgotPassword(token, email);
		String forgotPasswordView = forgotPasswordController.processSubmit(forgotPassword, new BeanPropertyBindingResult(forgotPassword, "forgotPassword"),
				mock(SessionStatus.class), null, mock(Model.class));
		assertEquals("frontend.forgotPassword", forgotPasswordView);
	}
	
	@Test
	public void forgotPasswordEmailNull() throws SecurityException, IllegalArgumentException, NoSuchAlgorithmException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException{
		dropForgotPasswords();
		String token = "1";
		ForgotPasswordController forgotPasswordController = initForgotPasswordController(token, "%s%s", 24);
		ForgotPassword forgotPassword = initForgotPassword(token, null);
		String forgotPasswordView = forgotPasswordController.processSubmit(forgotPassword, new BeanPropertyBindingResult(forgotPassword, "forgotPassword"),
				mock(SessionStatus.class), null, mock(Model.class));
		assertEquals("frontend.forgotPassword", forgotPasswordView);
	}
	
	@Test
	public void forgotPasswordPasswordMismatch() throws SecurityException, IllegalArgumentException, NoSuchAlgorithmException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException, CloneNotSupportedException{
		dropForgotPasswords();
		String token = "1", email = userPasswordMap.keySet().iterator().next(), newPassword = "new password1", confirmNewPassword = "new password2";
		ForgotPasswordController forgotPasswordController = initForgotPasswordController(token, "%s%s", 24);
		ForgotPassword forgotPassword = initForgotPassword(token, email);
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		String forgotPasswordView = forgotPasswordController.processSubmit(forgotPassword, new BeanPropertyBindingResult(forgotPassword, "forgotPassword"),
				mock(SessionStatus.class), null, mock(Model.class));
		smtpServer.stop();
		assertTrue(smtpServer.getReceivedEmailSize() == 1);
		
		PasswordRecoveryController recoveryController = initPasswordRecoveryController();
		Model model = new ExtendedModelMap();
		String passwordRecoveryView = recoveryController.setupForm(email, token, model);
		forgotPassword = (ForgotPassword)model.asMap().get("forgotPassword");
		setPasswordsToPrivateFields(forgotPassword, newPassword, confirmNewPassword);
		String backTopasswordRecoveryView = recoveryController.processSubmit(forgotPassword, new BeanPropertyBindingResult(forgotPassword, "forgotPassword"),
				mock(SessionStatus.class), mock(Model.class));
		
		assertEquals("redirect:/forgot-password-ok", forgotPasswordView);
		assertEquals("frontend.passwordRecovery", passwordRecoveryView);
		assertEquals("frontend.passwordRecovery", backTopasswordRecoveryView);
	}
	
	@Test
	public void forgotPasswordPasswordNull() throws SecurityException, IllegalArgumentException, NoSuchAlgorithmException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException, CloneNotSupportedException{
		dropForgotPasswords();
		String token = "1", email = userPasswordMap.keySet().iterator().next();
		ForgotPasswordController forgotPasswordController = initForgotPasswordController(token, "%s%s", 24);
		ForgotPassword forgotPassword = initForgotPassword(token, email);
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		String forgotPasswordView = forgotPasswordController.processSubmit(forgotPassword, new BeanPropertyBindingResult(forgotPassword, "forgotPassword"),
				mock(SessionStatus.class), null, mock(Model.class));
		smtpServer.stop();
		assertTrue(smtpServer.getReceivedEmailSize() == 1);
		
		PasswordRecoveryController recoveryController = initPasswordRecoveryController();
		Model model = new ExtendedModelMap();
		String passwordRecoveryView = recoveryController.setupForm(email, token, model);
		forgotPassword = (ForgotPassword)model.asMap().get("forgotPassword");
		setPasswordsToPrivateFields(forgotPassword, null, null);
		String backTopasswordRecoveryView = recoveryController.processSubmit(forgotPassword, new BeanPropertyBindingResult(forgotPassword, "forgotPassword"),
				mock(SessionStatus.class), mock(Model.class));
		
		assertEquals("redirect:/forgot-password-ok", forgotPasswordView);
		assertEquals("frontend.passwordRecovery", passwordRecoveryView);
		assertEquals("frontend.passwordRecovery", backTopasswordRecoveryView);
	}
	
	@Test
	public void forgotPasswordPasswordTooLong() throws SecurityException, IllegalArgumentException, NoSuchAlgorithmException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException, CloneNotSupportedException{
		dropForgotPasswords();
		String token = "1", email = userPasswordMap.keySet().iterator().next(), newPassword = "looooooooooooong password";
		ForgotPasswordController forgotPasswordController = initForgotPasswordController(token, "%s%s", 24);
		ForgotPassword forgotPassword = initForgotPassword(token, email);
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		String forgotPasswordView = forgotPasswordController.processSubmit(forgotPassword, new BeanPropertyBindingResult(forgotPassword, "forgotPassword"),
				mock(SessionStatus.class), null, mock(Model.class));
		smtpServer.stop();
		assertTrue(smtpServer.getReceivedEmailSize() == 1);
		
		PasswordRecoveryController recoveryController = initPasswordRecoveryController();
		Model model = new ExtendedModelMap();
		String passwordRecoveryView = recoveryController.setupForm(email, token, model);
		forgotPassword = (ForgotPassword)model.asMap().get("forgotPassword");
		setPasswordsToPrivateFields(forgotPassword, newPassword, newPassword);
		String backTopasswordRecoveryView = recoveryController.processSubmit(forgotPassword, new BeanPropertyBindingResult(forgotPassword, "forgotPassword"),
				mock(SessionStatus.class), mock(Model.class));
		
		assertEquals("redirect:/forgot-password-ok", forgotPasswordView);
		assertEquals("frontend.passwordRecovery", passwordRecoveryView);
		assertEquals("frontend.passwordRecovery", backTopasswordRecoveryView);
	}
	
	
	@Test
	public void forgotPasswordPasswordTooShort() throws SecurityException, IllegalArgumentException, NoSuchAlgorithmException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException, CloneNotSupportedException{
		dropForgotPasswords();
		String token = "1", email = userPasswordMap.keySet().iterator().next(), newPassword = "pass";
		ForgotPasswordController forgotPasswordController = initForgotPasswordController(token, "%s%s", 24);
		ForgotPassword forgotPassword = initForgotPassword(token, email);
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		String forgotPasswordView = forgotPasswordController.processSubmit(forgotPassword, new BeanPropertyBindingResult(forgotPassword, "forgotPassword"),
				mock(SessionStatus.class), null, mock(Model.class));
		smtpServer.stop();
		assertTrue(smtpServer.getReceivedEmailSize() == 1);
		
		PasswordRecoveryController recoveryController = initPasswordRecoveryController();
		Model model = new ExtendedModelMap();
		String passwordRecoveryView = recoveryController.setupForm(email, token, model);
		forgotPassword = (ForgotPassword)model.asMap().get("forgotPassword");
		setPasswordsToPrivateFields(forgotPassword, newPassword, newPassword);
		String backTopasswordRecoveryView = recoveryController.processSubmit(forgotPassword, new BeanPropertyBindingResult(forgotPassword, "forgotPassword"),
				mock(SessionStatus.class), mock(Model.class));
		
		assertEquals("redirect:/forgot-password-ok", forgotPasswordView);
		assertEquals("frontend.passwordRecovery", passwordRecoveryView);
		assertEquals("frontend.passwordRecovery", backTopasswordRecoveryView);
	}
	
	
	@Test
	public void forgotPasswordExpiredRequest() throws SecurityException, IllegalArgumentException, NoSuchAlgorithmException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException, CloneNotSupportedException{
		dropForgotPasswords();
		String token = "1", email = userPasswordMap.keySet().iterator().next();
		ForgotPasswordController forgotPasswordController = initForgotPasswordController(token, "%s%s", 0); //expires immediately
		ForgotPassword forgotPassword = initForgotPassword(token, email);
		
		forgotPassword.setExpirationDate(new Date(System.currentTimeMillis() - 1_000_000));
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		String forgotPasswordView = forgotPasswordController.processSubmit(forgotPassword, new BeanPropertyBindingResult(forgotPassword, "forgotPassword"),
				mock(SessionStatus.class), null, mock(Model.class));
		smtpServer.stop();
		assertTrue(smtpServer.getReceivedEmailSize() == 1);
		
		PasswordRecoveryController recoveryController = initPasswordRecoveryController();
		String passwordRecoveryView = recoveryController.setupForm(email, token, mock(Model.class));
		
		assertEquals("redirect:/forgot-password-ok", forgotPasswordView);
		assertEquals("frontend.passwordRecoveryFailure", passwordRecoveryView);
	}
	
	@Test
	public void twoForgotPasswordRequestsSameEmail() throws NoSuchAlgorithmException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException, CloneNotSupportedException{
		dropForgotPasswords();
		String token1 = "1", token2 = "2", email = userPasswordMap.keySet().iterator().next(), password1 = "Password1$", password2 = "Password2$";

		//First forgot password
		ForgotPasswordController forgotPasswordController = initForgotPasswordController(token1, "%S%S", 24);
		ForgotPassword forgotPassword = initForgotPassword(token1, email);
		
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		String forgotPasswordView1 = forgotPasswordController.processSubmit(forgotPassword, new BeanPropertyBindingResult(forgotPassword, "forgotPassword"),
				mock(SessionStatus.class), null, mock(Model.class));
		smtpServer.stop();
		assertTrue(smtpServer.getReceivedEmailSize() == 1);
		
		//Second forgot password
		forgotPasswordController = initForgotPasswordController(token2, "%s%s", 24);
		forgotPassword = initForgotPassword(token2, email);
		
		smtpServer = SimpleSmtpServer.start(2525);
		String forgotPasswordView2 = forgotPasswordController.processSubmit(forgotPassword, new BeanPropertyBindingResult(forgotPassword, "forgotPassword"),
				mock(SessionStatus.class), null, mock(Model.class));
		smtpServer.stop();
		assertTrue(smtpServer.getReceivedEmailSize() == 1);
		
		//First password recovery
		PasswordRecoveryController passwordRecoveryController1 = initPasswordRecoveryController();
		Model model1 = new ExtendedModelMap();
		String passwordRecoverView1 = passwordRecoveryController1.setupForm(email, token1, model1);

		//Second password recovery
		PasswordRecoveryController passwordRecoveryController2 = initPasswordRecoveryController();
		Model model2 = new ExtendedModelMap();
		String passwordRecoverView2 = passwordRecoveryController2.setupForm(email, token2, model2);
		
		//Submitting first password recovery form
		forgotPassword = (ForgotPassword)model1.asMap().get("forgotPassword");
		setPasswordsToPrivateFields(forgotPassword, password1, password1);
		String passwordRecoverySuccessView1 = passwordRecoveryController1.processSubmit(forgotPassword, new BeanPropertyBindingResult(forgotPassword, "forgotPassword"),
				mock(SessionStatus.class), mock(Model.class));
		
		Principal.entityManager().flush();
		
		Principal persistedPrincipal = Principal.findByUsername(email);
		assertTrue(utilsService.isPasswordValid(persistedPrincipal.getPassword(), password1));
		
		//Submitting first password recovery form
		forgotPassword = (ForgotPassword)model2.asMap().get("forgotPassword");
		setPasswordsToPrivateFields(forgotPassword, password2, password2);
		String passwordRecoverySuccessView2 = passwordRecoveryController2.processSubmit(forgotPassword, new BeanPropertyBindingResult(forgotPassword, "forgotPassword"),
				mock(SessionStatus.class), mock(Model.class));
		
		Principal.entityManager().flush();
		
		persistedPrincipal = Principal.findByUsername(email);
		assertTrue(utilsService.isPasswordValid(persistedPrincipal.getPassword(), password2));
		
		
		assertEquals("redirect:/forgot-password-ok", forgotPasswordView1);
		assertEquals("frontend.passwordRecovery", passwordRecoverView1);
		assertEquals("redirect:" + Urls.PUBLIC_PASSWORD_RECOVERY_OK, passwordRecoverySuccessView1);
		
		assertEquals("redirect:/forgot-password-ok", forgotPasswordView2);
		assertEquals("frontend.passwordRecovery", passwordRecoverView2);
		assertEquals("redirect:" + Urls.PUBLIC_PASSWORD_RECOVERY_OK, passwordRecoverySuccessView2);
		
	}
	
	
	
	
}
