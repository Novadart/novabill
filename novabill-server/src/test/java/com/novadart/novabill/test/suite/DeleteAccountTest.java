package com.novadart.novabill.test.suite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpSession;

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
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.support.SessionStatus;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.web.mvc.BusinessLogoController;
import com.novadart.novabill.web.mvc.DeleteAccountController;
import com.novadart.novabill.web.mvc.command.DeleteAccount;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:mvc-test-config.xml")
@Transactional
@DirtiesContext
@ActiveProfiles("dev")
public class DeleteAccountTest extends AuthenticatedTest {
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private UtilsService utilsService;
	
	@Override
	@Before
	public void authenticate() {
		authenticatedPrincipal = Principal.findByUsername("giordano.battilana@novadart.com");
		authenticatePrincipal(authenticatedPrincipal);
	}
	
	private DeleteAccountController initDeleteAccountController(String username, String password, String providedPassword) throws NoSuchAlgorithmException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		DeleteAccountController deleteAccountController = new DeleteAccountController();
		TestUtils.setPrivateField(DeleteAccountController.class, deleteAccountController, "utilsService", utilsService);
		TestUtils.setPrivateField(DeleteAccountController.class, deleteAccountController, "validator", validator);
		TestUtils.setPrivateField(DeleteAccountController.class, deleteAccountController, "businessLogoController", mock(BusinessLogoController.class));
		return deleteAccountController;
	}
	
	@Test
	public void defaultDeleteAccountFlow() throws NoSuchAlgorithmException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException{
		String username = userPasswordMap.keySet().iterator().next(), password = userPasswordMap.get(username);
		Long businessID = Principal.findByUsername(username).getBusiness().getId();
		DeleteAccountController deleteAccountController = initDeleteAccountController(username, password, password);
		String deleteAccountView = deleteAccountController.setupForm(mock(Model.class), mock(HttpSession.class));
		DeleteAccount deleteAccount = new DeleteAccount();
		deleteAccount.setPassword(password);
		BindingResult result = mock(BindingResult.class);
		when(result.hasErrors()).thenReturn(false);
		String redirectLogoutView = deleteAccountController.processSubmit(deleteAccount, result, mock(SessionStatus.class));
		Principal.entityManager().flush();
		assertEquals("private.deleteAccount", deleteAccountView);
		assertEquals("forward:/resources/logout", redirectLogoutView);
		assertEquals(null, Principal.findByUsername(username));
		assertEquals(null, Business.findBusiness(businessID));
	}
	
	private void invalidPasswordValue(String username, String passValue) throws NoSuchAlgorithmException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		DeleteAccountController deleteAccountController = initDeleteAccountController(username, userPasswordMap.get(username), passValue);
		DeleteAccount deleteAccount = new DeleteAccount();
		deleteAccount.setPassword(passValue);
		BindingResult result = new BeanPropertyBindingResult(deleteAccount, "deleteAccount");
		String view = deleteAccountController.processSubmit(deleteAccount, result, mock(SessionStatus.class));
		assertEquals("private.deleteAccount", view);
		assertTrue(result.hasErrors());
	}
	
	
	@Test
	public void deleteAccountBlankPasswordTest() throws NoSuchAlgorithmException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		invalidPasswordValue(userPasswordMap.keySet().iterator().next(), "");
	}
	
	@Test
	public void deleteAccountWrongPasswordTest() throws NoSuchAlgorithmException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		String username = userPasswordMap.keySet().iterator().next();
		invalidPasswordValue(username, userPasswordMap.get(username) + "1");
	}

}
