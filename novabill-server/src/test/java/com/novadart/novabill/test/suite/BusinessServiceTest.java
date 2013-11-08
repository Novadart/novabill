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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.dto.factory.BusinessDTOFactory;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.BusinessGwtService;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:gwt-business-test-config.xml")
@Transactional
@ActiveProfiles("dev")
public class BusinessServiceTest extends GWTServiceTest {
	
	@Autowired
	private BusinessGwtService businessService;
	
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
	public void getStatsAuthorizedTest() throws NotAuthenticatedException, DataAccessException{
		BusinessStatsDTO stats = businessService.getStats(authenticatedPrincipal.getBusiness().getId());
		assertEquals(new Long(authenticatedPrincipal.getBusiness().getClients().size()), stats.getClientsCount());
		assertEquals(new Long(businessService.countInvoicesForYear(authenticatedPrincipal.getId(), Calendar.getInstance().get(Calendar.YEAR))), stats.getInvoicesCountForYear());
		assertEquals(businessService.getTotalAfterTaxesForYear(authenticatedPrincipal.getId(), Calendar.getInstance().get(Calendar.YEAR)), stats.getTotalAfterTaxesForYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getStatsUnauthorizedIDTest() throws NotAuthenticatedException, DataAccessException{
		businessService.getStats(getUnathorizedBusinessID());
	}
	
	@Test(expected = DataAccessException.class)
	public void getStatsUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException{
		businessService.getStats(null);
	}
	
	@Test
	public void countClientsAuthorizedTest() throws NotAuthenticatedException, DataAccessException{
		assertEquals(new Long(authenticatedPrincipal.getBusiness().getClients().size()).longValue(), new Long(businessService.countClients(authenticatedPrincipal.getId())).longValue());
	}
	
	@Test(expected = DataAccessException.class)
	public void countClientsUnauthorizedTest() throws NotAuthenticatedException, DataAccessException{
		businessService.countClients(getUnathorizedBusinessID());
	}
	
	@Test(expected = DataAccessException.class)
	public void countClientsUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException{
		businessService.countClients(null);
	}
	
	@Test
	public void countInvoicesAuthorizedTest() throws NotAuthenticatedException, DataAccessException{
		assertEquals(new Long(authenticatedPrincipal.getBusiness().getInvoices().size()), new Long(businessService.countInvoices(authenticatedPrincipal.getId())));
	}
	
	@Test(expected = DataAccessException.class)
	public void countInvoicesUnauthorizedTest() throws NotAuthenticatedException, DataAccessException{
		businessService.countInvoices(getUnathorizedBusinessID());
	}
	
	@Test(expected = DataAccessException.class)
	public void countInvoicesUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException{
		businessService.countInvoices(null);
	}
	
	@Test
	public void countInvoicesForYearAuthorizedTest() throws NotAuthenticatedException, DataAccessException{
		int year = Calendar.getInstance().get(Calendar.YEAR);
		long expected = authenticatedPrincipal.getBusiness().getInvoicesForYear(year).size();
		long actual = businessService.countInvoicesForYear(authenticatedPrincipal.getId(), year);
		assertEquals(expected, actual);
	}
	
	@Test(expected = DataAccessException.class)
	public void countInvoicesForYearUnauthorizedTest() throws NotAuthenticatedException, DataAccessException{
		int year = Calendar.getInstance().get(Calendar.YEAR);
		businessService.countInvoicesForYear(getUnathorizedBusinessID(), year);
	}
	
	@Test(expected = DataAccessException.class)
	public void countInvoicesForYearUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException{
		int year = Calendar.getInstance().get(Calendar.YEAR);
		businessService.countInvoicesForYear(null, year);
	}
	
	@Test
	public void countInvoicesForYearAuthorizedBogusYearNullTest() throws NotAuthenticatedException, DataAccessException{
		long expected = 0l;
		long actual = businessService.countInvoicesForYear(authenticatedPrincipal.getId(), 0);
		assertEquals(actual, expected);
	}
	
	@Test
	public void getTotalAfterTaxesForYearAuthorizedTest() throws NotAuthenticatedException, DataAccessException{
		BigDecimal expected = new BigDecimal(totalsAfterTax.get(authenticatedPrincipal.getUsername()));
		BigDecimal actual = businessService.getTotalAfterTaxesForYear(authenticatedPrincipal.getId(), Calendar.getInstance().get(Calendar.YEAR));
		assertEquals(expected, actual);
	}
	
	@Test(expected = DataAccessException.class)
	public void getTotalAfterTaxesForYearUnauthorizedTest() throws NotAuthenticatedException, DataAccessException{
		businessService.getTotalAfterTaxesForYear(getUnathorizedBusinessID(), Calendar.getInstance().get(Calendar.YEAR));
	}
	
	@Test(expected = DataAccessException.class)
	public void getTotalAfterTaxesForYearUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException{
		businessService.getTotalAfterTaxesForYear(null, Calendar.getInstance().get(Calendar.YEAR));
	}
	
	@Test
	public void updateAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException{
		authenticatedPrincipal.getBusiness().setName("Kick ass company");
		businessService.update(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		assertEquals("Kick ass company", Business.findBusiness(authenticatedPrincipal.getBusiness().getId()).getName());
	}
	
	@Test
	public void updateAuthorizedValidationFieldMappingTest() throws IllegalAccessException, InvocationTargetException, NotAuthenticatedException, DataAccessException, NoSuchObjectException{
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
	public void updateAuthorizedValidationErrorTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException{
		authenticatedPrincipal.getBusiness().setName(StringUtils.leftPad("1", 1000, '1'));
		businessService.update(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
	}
	
	@Test
	public void getInvoicesAuthorizedTest() throws NotAuthenticatedException, DataAccessException{
		businessService.getInvoices(authenticatedPrincipal.getBusiness().getId());
	}
	
	@Test(expected = DataAccessException.class)
	public void getInvoicesUnauthorizedIDTest() throws NotAuthenticatedException, DataAccessException{
		businessService.getInvoices(getUnathorizedBusinessID());
	}
	
	@Test(expected = DataAccessException.class)
	public void getInvoicesUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException{
		businessService.getInvoices(null);
	}
	
	@Test
	public void getCreditNotesAuthorizedTest() throws NotAuthenticatedException, DataAccessException{
		businessService.getCreditNotes(authenticatedPrincipal.getBusiness().getId());
	}
	
	@Test(expected = DataAccessException.class)
	public void getCreditNotesUnauthorizedIDTest() throws NotAuthenticatedException, DataAccessException{
		businessService.getCreditNotes(getUnathorizedBusinessID());
	}
	
	@Test(expected = DataAccessException.class)
	public void getCreditNotesUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException{
		businessService.getCreditNotes(null);
	}
	
	@Test
	public void getEstimationsAuthorizedTest() throws NotAuthenticatedException, DataAccessException{
		businessService.getEstimations(authenticatedPrincipal.getBusiness().getId());
	}
	
	@Test(expected = DataAccessException.class)
	public void getEstimationsUnauthorizedIDTest() throws NotAuthenticatedException, DataAccessException{
		businessService.getEstimations(getUnathorizedBusinessID());
	}
	
	@Test(expected = DataAccessException.class)
	public void getEstimationsUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException{
		businessService.getEstimations(null);
	}
	
	@Test
	public void getTransportDocumentsAuthorizedTest() throws NotAuthenticatedException, DataAccessException{
		businessService.getTransportDocuments(authenticatedPrincipal.getBusiness().getId());
	}
	
	@Test(expected = DataAccessException.class)
	public void getTransportDocumentsUnauthorizedIDTest() throws NotAuthenticatedException, DataAccessException{
		businessService.getTransportDocuments(getUnathorizedBusinessID());
	}
	
	@Test(expected = DataAccessException.class)
	public void getTransportDocumentsUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException{
		businessService.getTransportDocuments(null);
	}
	
	@Test
	public void getClientsAuthorizedTest() throws NotAuthenticatedException, DataAccessException{
		businessService.getClients(authenticatedPrincipal.getBusiness().getId());
	}
	
	@Test(expected = DataAccessException.class)
	public void getClientsUnauthorizedIDTest() throws NotAuthenticatedException, DataAccessException{
		businessService.getClients(getUnathorizedBusinessID());
	}
	
	@Test(expected = DataAccessException.class)
	public void getClientsUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException{
		businessService.getClients(null);
	}
	
	@Test
	public void getAuthorizedTest() throws NotAuthenticatedException, DataAccessException{
		BusinessDTO businessDTO = businessService.get(authenticatedPrincipal.getBusiness().getId());
		Business business = authenticatedPrincipal.getBusiness();
		assertEquals(business.getName(), businessDTO.getName());
		assertEquals(business.getAddress(), businessDTO.getAddress());
		assertEquals(business.getSsn(), businessDTO.getSsn());
		assertEquals(business.getVatID(), businessDTO.getVatID());
	}
	
	@Test(expected = DataAccessException.class)
	public void getUnauthorizedIDTest() throws NotAuthenticatedException, DataAccessException{
		businessService.get(getUnathorizedBusinessID());
	}
	
	@Test(expected = DataAccessException.class)
	public void getUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException{
		businessService.get(null);
	}
	
	@Test
	public void addAuthorizedTest() throws NotAuthenticatedException, AuthorizationException, ValidationException, DataAccessException, 
									com.novadart.novabill.shared.client.exception.CloneNotSupportedException{
		authenticatedPrincipal.setBusiness(null);
		Business business = TestUtils.createBusiness();
		business.setId(businessService.add(BusinessDTOFactory.toDTO(business)));
		Business.entityManager().flush();
		Business actualBusiness = Principal.findPrincipal(authenticatedPrincipal.getId()).getBusiness();
		assertTrue(EqualsBuilder.reflectionEquals(business, actualBusiness, "version", "paymentTypes",
				"nonFreeAccountExpirationTime", "items", "accounts" , "invoices", "estimations", "creditNotes",
				"transportDocuments", "clients", "principals"));
	}
	
	@Test(expected = DataAccessException.class)
	public void addNullTest() throws NotAuthenticatedException, AuthorizationException, ValidationException, DataAccessException, 
								com.novadart.novabill.shared.client.exception.CloneNotSupportedException{
		businessService.add(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void addNotNullIDTest() throws NotAuthenticatedException, AuthorizationException, ValidationException, DataAccessException, 
										com.novadart.novabill.shared.client.exception.CloneNotSupportedException{
		authenticatedPrincipal.setBusiness(null);
		Business business = TestUtils.createBusiness();
		business.setId(1l);
		businessService.add(BusinessDTOFactory.toDTO(business));
	}

}
