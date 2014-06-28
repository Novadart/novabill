package com.novadart.novabill.test.suite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.support.SessionStatus;

import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.ChangePasswordValidator;
import com.novadart.novabill.web.mvc.ChangePasswordController;
import com.novadart.novabill.web.mvc.command.ChangePassword;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:mvc-test-config.xml")
@Transactional
@DirtiesContext
@ActiveProfiles("dev")
public class ChangePasswordControllerTest extends AuthenticatedTest{
	
	@Autowired
	private ChangePasswordValidator validator;
	
	@Autowired
	private UtilsService utilsService;
	
	private ChangePassword initChangePassword(String email, String password, String newPassword, String confirmNewPassword) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
		ChangePassword changePassword = new ChangePassword();
		changePassword.setEmail(email);
		TestUtils.setPrivateField(ChangePassword.class, changePassword, "password", password);
		changePassword.setNewPassword(newPassword);
		changePassword.setConfirmNewPassword(confirmNewPassword);
		return changePassword;
	}
	
	private ChangePasswordController initChangePasswordController(String email) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
		ChangePasswordController controller = new ChangePasswordController();
		UtilsService utilsService = mock(UtilsService.class);
		when(utilsService.getAuthenticatedPrincipalDetails()).thenReturn(Principal.findByUsername(email));
		TestUtils.setPrivateField(ChangePasswordController.class, controller, "validator", validator);
		TestUtils.setPrivateField(ChangePasswordController.class, controller, "utilsService", utilsService);
		return controller;
	}
	
	@Test
	public void defaultChangePasswordFlow() throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
		String email = userPasswordMap.keySet().iterator().next();
		String password = userPasswordMap.get(email);
		String newPassword = "Novadart28&", confirmNewPassword = "Novadart28&";
		ChangePassword changePassword = initChangePassword(email, password, newPassword, confirmNewPassword);
		ChangePasswordController controller = initChangePasswordController(email);
		String view = controller.processSubmit(changePassword, new BeanPropertyBindingResult(changePassword, "changePassword"), mock(SessionStatus.class));
		Principal.entityManager().flush();
		assertEquals("redirect:/private/", view);
		Principal persistedPrincipal = Principal.findByUsername(email);
		assertTrue(utilsService.isPasswordValid(persistedPrincipal.getPassword(), newPassword));
	}
	
	@Test
	public void changePasswordInvalidPassword() throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
		String email = userPasswordMap.keySet().iterator().next();
		String password = userPasswordMap.get(email) + "11";
		String newPassword = "password", confirmNewPassword = "password";
		ChangePassword changePassword = initChangePassword(email, password, newPassword, confirmNewPassword);
		ChangePasswordController controller = initChangePasswordController(email);
		String view = controller.processSubmit(changePassword, new BeanPropertyBindingResult(changePassword, "changePassword"), mock(SessionStatus.class));
		assertEquals("private.changePassword", view);
	}
	
	@Test
	public void changePasswordNewPasswordMismatch() throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
		String email = userPasswordMap.keySet().iterator().next();
		String password = userPasswordMap.get(email);
		String newPassword = "password", confirmNewPassword = "password11";
		ChangePassword changePassword = initChangePassword(email, password, newPassword, confirmNewPassword);
		ChangePasswordController controller = initChangePasswordController(email);
		String view = controller.processSubmit(changePassword, new BeanPropertyBindingResult(changePassword, "changePassword"), mock(SessionStatus.class));
		assertEquals("private.changePassword", view);
	}
	
	
	@Test
	public void changePasswordNewPasswordTooShort() throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
		String email = userPasswordMap.keySet().iterator().next();
		String password = userPasswordMap.get(email);
		String newPassword = "pass", confirmNewPassword = "passs";
		ChangePassword changePassword = initChangePassword(email, password, newPassword, confirmNewPassword);
		ChangePasswordController controller = initChangePasswordController(email);
		String view = controller.processSubmit(changePassword, new BeanPropertyBindingResult(changePassword, "changePassword"), mock(SessionStatus.class));
		assertEquals("private.changePassword", view);
	}
	
	@Test
	public void changePasswordNewPasswordTooLong() throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
		String email = userPasswordMap.keySet().iterator().next();
		String password = userPasswordMap.get(email);
		String newPassword = "looooooooooong password", confirmNewPassword = "looooooooooong password";
		ChangePassword changePassword = initChangePassword(email, password, newPassword, confirmNewPassword);
		ChangePasswordController controller = initChangePasswordController(email);
		String view = controller.processSubmit(changePassword, new BeanPropertyBindingResult(changePassword, "changePassword"), mock(SessionStatus.class));
		assertEquals("private.changePassword", view);
	}
	
	@Test
	public void changePasswordNewPasswordSameAsOldTest() throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
		String email = userPasswordMap.keySet().iterator().next(), password = "Novadart28&";
		Principal principal = Principal.findByUsername(email);
		principal.setPassword(password);
		principal.flush();
		String newPassword = password, confirmNewPassword = password;
		ChangePassword changePassword = initChangePassword(email, password, newPassword, confirmNewPassword);
		ChangePasswordController controller = initChangePasswordController(email);
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(changePassword, "changePassword");
		String view = controller.processSubmit(changePassword, result, mock(SessionStatus.class));
		assertTrue(result.hasErrors());
		assertEquals("private.changePassword", view);
		
	}
	
}
