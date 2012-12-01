package com.novadart.novabill.test.suite;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.dto.factory.BusinessDTOFactory;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.BusinessService;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:gwt-business-test-config.xml")
@Transactional
public class BusinessServiceTest extends GWTServiceTest {
	
	@Autowired
	private BusinessService businessService;
	
	@Resource(name = "totalsAfterTax")
	protected HashMap<String, String> totalsAfterTax;
	
	@SuppressWarnings("serial")
	private static Map<String, Field> validationFieldsMap = new HashMap<String, Field>(){{
		put("name", Field.name); put("address", Field.address); put("postcode", Field.postcode);
		put("city", Field.city); put("province", Field.province); put("country", Field.country);
		put("email", Field.email); put("phone", Field.phone); put("mobile", Field.mobile);
		put("fax", Field.fax); put("web", Field.web); put("vatID", Field.vatID); put("ssn", Field.ssn);
	}};
	
	@Test
	public void businessServiceWiringTest(){
		assertNotNull(businessService);
	}
	
	@Test
	public void getStatsAuthorizedTest() throws NotAuthenticatedException, ConcurrentAccessException{
		BusinessStatsDTO stats = businessService.getStats(authenticatedPrincipal.getBusiness().getId());
		assertEquals(new Long(authenticatedPrincipal.getBusiness().getClients().size()), stats.getClientsCount());
		assertEquals(new Long(businessService.countInvoicesForYear(authenticatedPrincipal.getId(), Calendar.getInstance().get(Calendar.YEAR))), stats.getInvoicesCountForYear());
		assertEquals(businessService.getTotalAfterTaxesForYear(authenticatedPrincipal.getId(), Calendar.getInstance().get(Calendar.YEAR)), stats.getTotalAfterTaxesForYear());
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getStatsUnauthorizedIDTest() throws NotAuthenticatedException, ConcurrentAccessException{
		businessService.getStats(getUnathorizedBusinessID());
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getStatsUnauthorizedNullTest() throws NotAuthenticatedException, ConcurrentAccessException{
		businessService.getStats(null);
	}
	
	@Test
	public void countClientsAuthorizedTest() throws NotAuthenticatedException, ConcurrentAccessException{
		assertEquals(new Long(authenticatedPrincipal.getBusiness().getClients().size()).longValue(), new Long(businessService.countClients(authenticatedPrincipal.getId())).longValue());
	}
	
	@Test(expected = AccessDeniedException.class)
	public void countClientsUnauthorizedTest() throws NotAuthenticatedException, ConcurrentAccessException{
		businessService.countClients(getUnathorizedBusinessID());
	}
	
	@Test(expected = AccessDeniedException.class)
	public void countClientsUnauthorizedNullTest() throws NotAuthenticatedException, ConcurrentAccessException{
		businessService.countClients(null);
	}
	
	@Test
	public void countInvoicesAuthorizedTest() throws NotAuthenticatedException, ConcurrentAccessException{
		assertEquals(new Long(authenticatedPrincipal.getBusiness().getInvoices().size()), new Long(businessService.countInvoices(authenticatedPrincipal.getId())));
	}
	
	@Test(expected = AccessDeniedException.class)
	public void countInvoicesUnauthorizedTest() throws NotAuthenticatedException, ConcurrentAccessException{
		businessService.countInvoices(getUnathorizedBusinessID());
	}
	
	@Test(expected = AccessDeniedException.class)
	public void countInvoicesUnauthorizedNullTest() throws NotAuthenticatedException, ConcurrentAccessException{
		businessService.countInvoices(null);
	}
	
	@Test
	public void countInvoicesForYearAuthorizedTest() throws NotAuthenticatedException, ConcurrentAccessException{
		int year = Calendar.getInstance().get(Calendar.YEAR);
		long expected = authenticatedPrincipal.getBusiness().getInvoicesForYear(year).size();
		long actual = businessService.countInvoicesForYear(authenticatedPrincipal.getId(), year);
		assertEquals(expected, actual);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void countInvoicesForYearUnauthorizedTest() throws NotAuthenticatedException, ConcurrentAccessException{
		int year = Calendar.getInstance().get(Calendar.YEAR);
		businessService.countInvoicesForYear(getUnathorizedBusinessID(), year);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void countInvoicesForYearUnauthorizedNullTest() throws NotAuthenticatedException, ConcurrentAccessException{
		int year = Calendar.getInstance().get(Calendar.YEAR);
		businessService.countInvoicesForYear(null, year);
	}
	
	@Test
	public void countInvoicesForYearAuthorizedBogusYearNullTest() throws NotAuthenticatedException, ConcurrentAccessException{
		long expected = 0l;
		long actual = businessService.countInvoicesForYear(authenticatedPrincipal.getId(), 0);
		assertEquals(actual, expected);
	}
	
	@Test
	public void getTotalAfterTaxesForYearAuthorizedTest() throws NotAuthenticatedException, ConcurrentAccessException{
		BigDecimal expected = new BigDecimal(totalsAfterTax.get(authenticatedPrincipal.getUsername()));
		BigDecimal actual = businessService.getTotalAfterTaxesForYear(authenticatedPrincipal.getId(), Calendar.getInstance().get(Calendar.YEAR));
		assertEquals(expected, actual);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getTotalAfterTaxesForYearUnauthorizedTest() throws NotAuthenticatedException, ConcurrentAccessException{
		businessService.getTotalAfterTaxesForYear(getUnathorizedBusinessID(), Calendar.getInstance().get(Calendar.YEAR));
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getTotalAfterTaxesForYearUnauthorizedNullTest() throws NotAuthenticatedException, ConcurrentAccessException{
		businessService.getTotalAfterTaxesForYear(null, Calendar.getInstance().get(Calendar.YEAR));
	}
	
	@Test
	public void updateAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException, ValidationException{
		authenticatedPrincipal.getBusiness().setName("Kick ass company");
		businessService.update(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		assertEquals("Kick ass company", Business.findBusiness(authenticatedPrincipal.getBusiness().getId()).getName());
	}
	
	@Test
	public void updateAuthorizedValidationFieldMappingTest() throws IllegalAccessException, InvocationTargetException, NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Business business = authenticatedPrincipal.getBusiness();
		for(String key: validationFieldsMap.keySet()){
			BeanUtils.setProperty(business, key, StringUtils.leftPad("1", 1000, '1'));
		}
		try{
			businessService.update(BusinessDTOFactory.toDTO(business));
		}catch(ValidationException e){
			Set<Field> expected = new HashSet<Field>(validationFieldsMap.values());
			Set<Field> actual= new HashSet<Field>();
			for(ErrorObject error: e.getErrors())
				actual.add(error.getField());
			assertEquals(expected, actual);
		}
	}
	
	@Test(expected = ValidationException.class)
	public void updateAuthorizedValidationErrorTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException, ValidationException{
		authenticatedPrincipal.getBusiness().setName(StringUtils.leftPad("1", 1000, '1'));
		businessService.update(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
	}

}
