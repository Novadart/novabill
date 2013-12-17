package com.novadart.novabill.test.suite;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.domain.AccountingDocument;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.dto.factory.BusinessDTOFactory;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.web.BusinessService;
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
	private BusinessGwtService businessGwtService;
	
	@Autowired
	private BusinessService businessService;
	
	@Resource(name = "totalsAfterTax")
	protected HashMap<String, String> totalsAfterTax;
	
	@Override
	@Before
	public void authenticate() {
		authenticatedPrincipal = Principal.findByUsername("giordano.battilana@novadart.com");
		authenticatePrincipal(authenticatedPrincipal);
	}
	
	@SuppressWarnings("serial")
	private static Map<String, Field> validationFieldsMap = new HashMap<String, Field>(){{
		put("name", Field.name); put("address", Field.address); put("postcode", Field.postcode);
		put("city", Field.city); put("province", Field.province); put("country", Field.country);
		put("email", Field.email); put("phone", Field.phone); put("mobile", Field.mobile);
		put("fax", Field.fax); put("web", Field.web); put("vatID", Field.vatID); put("ssn", Field.ssn);
	}};
	
	@Test
	public void businessServiceWiringTest(){
		assertNotNull(businessGwtService);
		assertNotNull(businessService);
	}
	
	@Test
	public void getStatsAuthorizedTest() throws NotAuthenticatedException, DataAccessException{
		BusinessStatsDTO stats = businessGwtService.getStats(authenticatedPrincipal.getBusiness().getId());
		assertEquals(new Long(authenticatedPrincipal.getBusiness().getClients().size()), new Long(stats.getClientsCount()));
		assertEquals(new Long(businessGwtService.countInvoicesForYear(authenticatedPrincipal.getId(), Calendar.getInstance().get(Calendar.YEAR))), new Long(stats.getInvoicesCountForYear()));
		assertEquals(businessGwtService.getTotalsForYear(authenticatedPrincipal.getId(), Calendar.getInstance().get(Calendar.YEAR)).getSecond(), stats.getTotalAfterTaxesForYear());
		assertEquals(stats.getInvoiceCountsPerMonth(), Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, authenticatedPrincipal.getBusiness().getId() == 1? 24: 1, 0, 0));
	}
	
	@Test(expected = DataAccessException.class)
	public void getStatsUnauthorizedIDTest() throws NotAuthenticatedException, DataAccessException{
		businessGwtService.getStats(getUnathorizedBusinessID());
	}
	
	@Test(expected = DataAccessException.class)
	public void getStatsUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException{
		businessGwtService.getStats(null);
	}
	
	@Test
	public void countClientsAuthorizedTest() throws NotAuthenticatedException, DataAccessException{
		assertEquals(new Long(authenticatedPrincipal.getBusiness().getClients().size()).longValue(), new Long(businessGwtService.countClients(authenticatedPrincipal.getId())).longValue());
	}
	
	@Test(expected = DataAccessException.class)
	public void countClientsUnauthorizedTest() throws NotAuthenticatedException, DataAccessException{
		businessGwtService.countClients(getUnathorizedBusinessID());
	}
	
	@Test(expected = DataAccessException.class)
	public void countClientsUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException{
		businessGwtService.countClients(null);
	}
	
	@Test
	public void countInvoicesForYearAuthorizedTest() throws NotAuthenticatedException, DataAccessException{
		int year = Calendar.getInstance().get(Calendar.YEAR);
		long expected = authenticatedPrincipal.getBusiness().getInvoicesForYear(year).size();
		long actual = businessGwtService.countInvoicesForYear(authenticatedPrincipal.getId(), year);
		assertEquals(expected, actual);
	}
	
	@Test(expected = DataAccessException.class)
	public void countInvoicesForYearUnauthorizedTest() throws NotAuthenticatedException, DataAccessException{
		int year = Calendar.getInstance().get(Calendar.YEAR);
		businessGwtService.countInvoicesForYear(getUnathorizedBusinessID(), year);
	}
	
	@Test(expected = DataAccessException.class)
	public void countInvoicesForYearUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException{
		int year = Calendar.getInstance().get(Calendar.YEAR);
		businessGwtService.countInvoicesForYear(null, year);
	}
	
	@Test
	public void countInvoicesForYearAuthorizedBogusYearNullTest() throws NotAuthenticatedException, DataAccessException{
		long expected = 0l;
		long actual = businessGwtService.countInvoicesForYear(authenticatedPrincipal.getId(), 0);
		assertEquals(actual, expected);
	}
	
	@Test
	public void getTotalAfterTaxesForYearAuthorizedTest() throws NotAuthenticatedException, DataAccessException{
		BigDecimal expected = new BigDecimal(totalsAfterTax.get(authenticatedPrincipal.getUsername()));
		BigDecimal actual = businessGwtService.getTotalsForYear(authenticatedPrincipal.getId(), Calendar.getInstance().get(Calendar.YEAR)).getSecond();
		assertEquals(expected, actual);
	}
	
	@Test(expected = DataAccessException.class)
	public void getTotalAfterTaxesForYearUnauthorizedTest() throws NotAuthenticatedException, DataAccessException{
		businessGwtService.getTotalsForYear(getUnathorizedBusinessID(), Calendar.getInstance().get(Calendar.YEAR));
	}
	
	@Test(expected = DataAccessException.class)
	public void getTotalAfterTaxesForYearUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException{
		businessGwtService.getTotalsForYear(null, Calendar.getInstance().get(Calendar.YEAR));
	}
	
	@Test
	public void updateAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException{
		authenticatedPrincipal.getBusiness().setName("Kick ass company");
		businessGwtService.update(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		assertEquals("Kick ass company", Business.findBusiness(authenticatedPrincipal.getBusiness().getId()).getName());
	}
	
	@Test
	public void updateAuthorizedValidationFieldMappingTest() throws IllegalAccessException, InvocationTargetException, NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Business business = authenticatedPrincipal.getBusiness();
		for(String key: validationFieldsMap.keySet()){
			BeanUtils.setProperty(business, key, StringUtils.leftPad("1", 1000, '1'));
		}
		try{
			businessGwtService.update(BusinessDTOFactory.toDTO(business));
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
		businessGwtService.update(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
	}
	
	@Test
	public void getInvoicesAuthorizedTest() throws NotAuthenticatedException, DataAccessException{
		businessGwtService.getInvoices(authenticatedPrincipal.getBusiness().getId(), getYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getInvoicesUnauthorizedIDTest() throws NotAuthenticatedException, DataAccessException{
		businessGwtService.getInvoices(getUnathorizedBusinessID(), getYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getInvoicesUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException{
		businessGwtService.getInvoices(null, getYear());
	}
	
	@Test
	public void getCreditNotesAuthorizedTest() throws NotAuthenticatedException, DataAccessException{
		businessGwtService.getCreditNotes(authenticatedPrincipal.getBusiness().getId(), getYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getCreditNotesUnauthorizedIDTest() throws NotAuthenticatedException, DataAccessException{
		businessGwtService.getCreditNotes(getUnathorizedBusinessID(), getYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getCreditNotesUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException{
		businessGwtService.getCreditNotes(null, getYear());
	}
	
	@Test
	public void getEstimationsAuthorizedTest() throws NotAuthenticatedException, DataAccessException{
		businessGwtService.getEstimations(authenticatedPrincipal.getBusiness().getId(), getYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getEstimationsUnauthorizedIDTest() throws NotAuthenticatedException, DataAccessException{
		businessGwtService.getEstimations(getUnathorizedBusinessID(), getYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getEstimationsUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException{
		businessGwtService.getEstimations(null, getYear());
	}
	
	@Test
	public void getTransportDocumentsAuthorizedTest() throws NotAuthenticatedException, DataAccessException{
		businessGwtService.getTransportDocuments(authenticatedPrincipal.getBusiness().getId(), getYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getTransportDocumentsUnauthorizedIDTest() throws NotAuthenticatedException, DataAccessException{
		businessGwtService.getTransportDocuments(getUnathorizedBusinessID(), getYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getTransportDocumentsUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException{
		businessGwtService.getTransportDocuments(null, getYear());
	}
	
	@Test
	public void getClientsAuthorizedTest() throws NotAuthenticatedException, DataAccessException{
		businessGwtService.getClients(authenticatedPrincipal.getBusiness().getId());
	}
	
	@Test(expected = DataAccessException.class)
	public void getClientsUnauthorizedIDTest() throws NotAuthenticatedException, DataAccessException{
		businessGwtService.getClients(getUnathorizedBusinessID());
	}
	
	@Test(expected = DataAccessException.class)
	public void getClientsUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException{
		businessGwtService.getClients(null);
	}
	
	@Test
	public void getAuthorizedTest() throws NotAuthenticatedException, DataAccessException{
		BusinessDTO businessDTO = businessGwtService.get(authenticatedPrincipal.getBusiness().getId());
		Business business = authenticatedPrincipal.getBusiness();
		assertEquals(business.getName(), businessDTO.getName());
		assertEquals(business.getAddress(), businessDTO.getAddress());
		assertEquals(business.getSsn(), businessDTO.getSsn());
		assertEquals(business.getVatID(), businessDTO.getVatID());
	}
	
	@Test(expected = DataAccessException.class)
	public void getUnauthorizedIDTest() throws NotAuthenticatedException, DataAccessException{
		businessGwtService.get(getUnathorizedBusinessID());
	}
	
	@Test(expected = DataAccessException.class)
	public void getUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException{
		businessGwtService.get(null);
	}
	
	@Test
	public void addAuthorizedTest() throws NotAuthenticatedException, AuthorizationException, ValidationException, DataAccessException, 
									com.novadart.novabill.shared.client.exception.CloneNotSupportedException{
		authenticatedPrincipal.setBusiness(null);
		Business business = TestUtils.createBusiness();
		business.setId(businessGwtService.add(BusinessDTOFactory.toDTO(business)));
		Business.entityManager().flush();
		Business actualBusiness = Principal.findPrincipal(authenticatedPrincipal.getId()).getBusiness();
		assertTrue(EqualsBuilder.reflectionEquals(business, actualBusiness, "version", "paymentTypes",
				"nonFreeAccountExpirationTime", "items", "accounts" , "invoices", "estimations", "creditNotes",
				"transportDocuments", "clients", "principals", "priceLists"));
	}
	
	@Test(expected = DataAccessException.class)
	public void addNullTest() throws NotAuthenticatedException, AuthorizationException, ValidationException, DataAccessException, 
								com.novadart.novabill.shared.client.exception.CloneNotSupportedException{
		businessGwtService.add(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void addNotNullIDTest() throws NotAuthenticatedException, AuthorizationException, ValidationException, DataAccessException, 
										com.novadart.novabill.shared.client.exception.CloneNotSupportedException{
		authenticatedPrincipal.setBusiness(null);
		Business business = TestUtils.createBusiness();
		business.setId(1l);
		businessGwtService.add(BusinessDTOFactory.toDTO(business));
	}
	
	private <T extends AccountingDocument> Set<Integer> extractYears(Set<T> docs){
		Set<Integer> years = new HashSet<>();
		for(T doc: docs)
			years.add(doc.getAccountingDocumentYear());
		return years;
	}
	
	@Test
	public void getYearsTest() throws NotAuthenticatedException, DataAccessException{
		Business business = Business.findBusiness(authenticatedPrincipal.getBusiness().getId());
		assertTrue(new HashSet<>(businessService.getInvoceYears(business.getId())) .equals(extractYears(business.getInvoices())));
		assertTrue(new HashSet<>(businessService.getCreditNoteYears(business.getId())) .equals(extractYears(business.getCreditNotes())));
		assertTrue(new HashSet<>(businessService.getEstimationYears(business.getId())) .equals(extractYears(business.getEstimations())));
		assertTrue(new HashSet<>(businessService.getTransportDocumentYears(business.getId())) .equals(extractYears(business.getTransportDocuments())));
	}

}
