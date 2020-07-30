package com.novadart.novabill.test.suite;

import com.novadart.novabill.domain.AccountingDocument;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.dto.transformer.BusinessDTOTransformer;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.validator.Groups.HeavyBusiness;
import com.novadart.novabill.service.validator.SimpleValidator;
import com.novadart.novabill.service.web.BusinessService;
import com.novadart.novabill.shared.client.data.LayoutType;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.exception.*;
import com.novadart.novabill.shared.client.facade.BusinessGwtService;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:gwt-business-test-config.xml")
@Transactional
@DirtiesContext
@ActiveProfiles("dev")
public class BusinessServiceTest extends ServiceTest {
	
	@Autowired
	private BusinessGwtService businessGwtService;
	
	@Autowired
	private BusinessService businessService;
	
	@Resource(name = "totalsAfterTax")
	protected HashMap<String, String> totalsAfterTax;
	
	@Autowired
	private SimpleValidator validator;
	
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
	public void getStatsAuthorizedTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		BusinessStatsDTO stats = businessGwtService.getStats(authenticatedPrincipal.getBusiness().getId());
		assertEquals(new Long(authenticatedPrincipal.getBusiness().getClients().size()), new Long(stats.getClientsCount()));
		assertEquals(new Long(businessGwtService.countInvoicesForYear(authenticatedPrincipal.getId(), Calendar.getInstance().get(Calendar.YEAR))), new Long(stats.getInvoicesCountForYear()));
		assertEquals(businessGwtService.getTotalsForYear(authenticatedPrincipal.getId(), Calendar.getInstance().get(Calendar.YEAR)).getSecond(), stats.getTotalAfterTaxesForYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getStatsUnauthorizedIDTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		businessGwtService.getStats(getUnathorizedBusinessID());
	}
	
	@Test(expected = DataAccessException.class)
	public void getStatsUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		businessGwtService.getStats(null);
	}
	
	@Test
	public void countClientsAuthorizedTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		assertEquals(new Long(authenticatedPrincipal.getBusiness().getClients().size()).longValue(), new Long(businessGwtService.countClients(authenticatedPrincipal.getId())).longValue());
	}
	
	@Test(expected = DataAccessException.class)
	public void countClientsUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		businessGwtService.countClients(getUnathorizedBusinessID());
	}
	
	@Test(expected = DataAccessException.class)
	public void countClientsUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		businessGwtService.countClients(null);
	}
	
	@Test
	public void countInvoicesForYearAuthorizedTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		long expected = authenticatedPrincipal.getBusiness().getInvoicesForYear(year).size();
		long actual = businessGwtService.countInvoicesForYear(authenticatedPrincipal.getId(), year);
		assertEquals(expected, actual);
	}
	
	@Test(expected = DataAccessException.class)
	public void countInvoicesForYearUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		businessGwtService.countInvoicesForYear(getUnathorizedBusinessID(), year);
	}
	
	@Test(expected = DataAccessException.class)
	public void countInvoicesForYearUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		businessGwtService.countInvoicesForYear(null, year);
	}
	
	@Test
	public void countInvoicesForYearAuthorizedBogusYearNullTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		long expected = 0l;
		long actual = businessGwtService.countInvoicesForYear(authenticatedPrincipal.getId(), 0);
		assertEquals(actual, expected);
	}
	
	@Test
	public void getTotalAfterTaxesForYearAuthorizedTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		BigDecimal expected = new BigDecimal(totalsAfterTax.get(authenticatedPrincipal.getUsername()));
		BigDecimal actual = businessGwtService.getTotalsForYear(authenticatedPrincipal.getId(), Calendar.getInstance().get(Calendar.YEAR)).getSecond();
		assertEquals(expected, actual);
	}
	
	@Test(expected = DataAccessException.class)
	public void getTotalAfterTaxesForYearUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		businessGwtService.getTotalsForYear(getUnathorizedBusinessID(), Calendar.getInstance().get(Calendar.YEAR));
	}
	
	@Test(expected = DataAccessException.class)
	public void getTotalAfterTaxesForYearUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		businessGwtService.getTotalsForYear(null, Calendar.getInstance().get(Calendar.YEAR));
	}
	
	@Test
	public void updateAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, FreeUserAccessForbiddenException {
		authenticatedPrincipal.getBusiness().setName("Kick ass company");
		businessGwtService.update(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		assertEquals("Kick ass company", Business.findBusiness(authenticatedPrincipal.getBusiness().getId()).getName());
	}
	
	@Test
	public void updateAuthorizedValidationFieldMappingTest() throws IllegalAccessException, InvocationTargetException, NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Business business = authenticatedPrincipal.getBusiness();
		for(String key: validationFieldsMap.keySet()){
			BeanUtils.setProperty(business, key, StringUtils.leftPad("1", 1000, '1'));
		}
		try{
			businessGwtService.update(BusinessDTOTransformer.toDTO(business));
		}catch(ValidationException e){
			Set<Field> expected = new HashSet<Field>(validationFieldsMap.values());
			Set<Field> actual= new HashSet<Field>();
			for(ErrorObject error: e.getErrors())
				actual.add(error.getField());
			assertEquals(expected, actual);
		}
	}
	
	@Test
	public void updateAuthorizedTaxNullValidationErrorTest() throws NotAuthenticatedException, NoSuchObjectException, ValidationException, DataAccessException, FreeUserAccessForbiddenException {
		Business business = authenticatedPrincipal.getBusiness();
		business.setVatID("");
		business.setSsn("");
		boolean validationError = false;
		try {
			businessGwtService.update(BusinessDTOTransformer.toDTO(business));
		} catch (ValidationException e) {
			validationError = true;
		}
		assertTrue(validationError);
	}
	
	@Test(expected = ValidationException.class)
	public void updateAuthorizedValidationErrorTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, FreeUserAccessForbiddenException {
		authenticatedPrincipal.getBusiness().setName(StringUtils.leftPad("1", 1000, '1'));
		businessGwtService.update(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
	}
	
	@Test
	public void getInvoicesAuthorizedTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		businessGwtService.getInvoices(authenticatedPrincipal.getBusiness().getId(), getYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getInvoicesUnauthorizedIDTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		businessGwtService.getInvoices(getUnathorizedBusinessID(), getYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getInvoicesUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		businessGwtService.getInvoices(null, getYear());
	}
	
	@Test
	public void getCreditNotesAuthorizedTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		businessGwtService.getCreditNotes(authenticatedPrincipal.getBusiness().getId(), getYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getCreditNotesUnauthorizedIDTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		businessGwtService.getCreditNotes(getUnathorizedBusinessID(), getYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getCreditNotesUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		businessGwtService.getCreditNotes(null, getYear());
	}
	
	@Test
	public void getEstimationsAuthorizedTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		businessGwtService.getEstimations(authenticatedPrincipal.getBusiness().getId(), getYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getEstimationsUnauthorizedIDTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		businessGwtService.getEstimations(getUnathorizedBusinessID(), getYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getEstimationsUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		businessGwtService.getEstimations(null, getYear());
	}
	
	@Test
	public void getTransportDocumentsAuthorizedTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		businessGwtService.getTransportDocuments(authenticatedPrincipal.getBusiness().getId(), getYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getTransportDocumentsUnauthorizedIDTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		businessGwtService.getTransportDocuments(getUnathorizedBusinessID(), getYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getTransportDocumentsUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		businessGwtService.getTransportDocuments(null, getYear());
	}
	
	@Test
	public void getClientsAuthorizedTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		businessGwtService.getClients(authenticatedPrincipal.getBusiness().getId());
	}
	
	@Test(expected = DataAccessException.class)
	public void getClientsUnauthorizedIDTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		businessGwtService.getClients(getUnathorizedBusinessID());
	}
	
	@Test(expected = DataAccessException.class)
	public void getClientsUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		businessGwtService.getClients(null);
	}
	
	@Test
	public void getAuthorizedTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		BusinessDTO businessDTO = businessGwtService.get(authenticatedPrincipal.getBusiness().getId());
		Business business = authenticatedPrincipal.getBusiness();
		assertEquals(business.getName(), businessDTO.getName());
		assertEquals(business.getAddress(), businessDTO.getAddress());
		assertEquals(business.getSsn(), businessDTO.getSsn());
		assertEquals(business.getVatID(), businessDTO.getVatID());
	}
	
	@Test(expected = DataAccessException.class)
	public void getUnauthorizedIDTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		businessGwtService.get(getUnathorizedBusinessID());
	}
	
	@Test(expected = DataAccessException.class)
	public void getUnauthorizedNullTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		businessGwtService.get(null);
	}
	
	@Test
	public void addAuthorizedTest() throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException, 
									com.novadart.novabill.shared.client.exception.CloneNotSupportedException{
		authenticatedPrincipal.setBusiness(null);
		Business business = TestUtils.createBusiness();
		business.setId(businessGwtService.add(BusinessDTOTransformer.toDTO(business)));
		Business.entityManager().flush();
		Business actualBusiness = Principal.findPrincipal(authenticatedPrincipal.getId()).getBusiness();
		assertTrue(EqualsBuilder.reflectionEquals(business, actualBusiness, "version", "paymentTypes",
				"nonFreeAccountExpirationTime", "items", "accounts" , "invoices", "estimations", "creditNotes",
				"transportDocuments", "clients", "principals", "priceLists", "settings"));
		assertEquals(LayoutType.DENSE, actualBusiness.getSettings().getDefaultLayoutType());
	}
	
	@Test
	public void addAuthorizedDefaultLayoutTest() throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException, 
									com.novadart.novabill.shared.client.exception.CloneNotSupportedException{
		authenticatedPrincipal.setBusiness(null);
		Business business = TestUtils.createBusiness();
		BusinessDTO businessDTO = BusinessDTOTransformer.toDTO(business);
		businessDTO.getSettings().setDefaultLayoutType(null);
		business.setId(businessGwtService.add(businessDTO));
		Business.entityManager().flush();
		Business actualBusiness = Principal.findPrincipal(authenticatedPrincipal.getId()).getBusiness();
		assertEquals(LayoutType.DENSE, actualBusiness.getSettings().getDefaultLayoutType());
	} 
	
	@Test(expected = DataAccessException.class)
	public void addNullTest() throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException, 
								com.novadart.novabill.shared.client.exception.CloneNotSupportedException{
		businessGwtService.add(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void addNotNullIDTest() throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException, 
										com.novadart.novabill.shared.client.exception.CloneNotSupportedException{
		authenticatedPrincipal.setBusiness(null);
		Business business = TestUtils.createBusiness();
		business.setId(1l);
		businessGwtService.add(BusinessDTOTransformer.toDTO(business));
	}
	
	private <T extends AccountingDocument> Set<Integer> extractYears(Set<T> docs){
		Set<Integer> years = new HashSet<>();
		for(T doc: docs)
			years.add(doc.getAccountingDocumentYear());
		return years;
	}
	
	@Test
	public void getYearsTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		Business business = Business.findBusiness(authenticatedPrincipal.getBusiness().getId());
		assertTrue(new HashSet<>(businessService.getInvoceYears(business.getId())) .equals(extractYears(business.getInvoices())));
		assertTrue(new HashSet<>(businessService.getCreditNoteYears(business.getId())) .equals(extractYears(business.getCreditNotes())));
		assertTrue(new HashSet<>(businessService.getEstimationYears(business.getId())) .equals(extractYears(business.getEstimations())));
		assertTrue(new HashSet<>(businessService.getTransportDocumentYears(business.getId())) .equals(extractYears(business.getTransportDocuments())));
	}
	
	@Test(expected = ValidationException.class)
	public void nullTaxableFieldsAddTest() throws ValidationException{
		Business business = TestUtils.createBusiness();
		business.setVatID(null);
		business.setSsn(null);
		validator.validate(business, HeavyBusiness.class);
	}
	
	@Test(expected = ValidationException.class)
	public void blankTaxableFieldsAddTest() throws ValidationException{
		Business business = TestUtils.createBusiness();
		business.setVatID("");
		business.setSsn("    ");
		validator.validate(business, HeavyBusiness.class);
	}

	@Test
	public void setDefaultLayoutTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		businessService.setDefaultLayout(businessID, LayoutType.TIDY);
		Business.entityManager().flush();
		assertEquals(LayoutType.TIDY, Business.findBusiness(businessID).getSettings().getDefaultLayoutType());
	}
	
	@Test(expected = AccessDeniedException.class)
	public void setDefaultLayoutUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException{
		Long businessID = getUnathorizedBusinessID();
		businessService.setDefaultLayout(businessID, LayoutType.TIDY);
	}

	@Test
	public void witholdTaxUpdateTest() throws NotAuthenticatedException, FreeUserAccessForbiddenException, NoSuchObjectException, DataAccessException, ValidationException {
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		Business business = Business.findBusiness(businessID);
		BigDecimal preUpdateWitholdTaxFirstLevel = business.getSettings().getWitholdTaxPercentFirstLevel();
		BigDecimal preUpdateWitholdTaxSecondLevel = business.getSettings().getWitholdTaxPercentSecondLevel();
		business.getSettings().setWitholdTaxPercentSecondLevel(new BigDecimal("20.0"));
		businessGwtService.update(BusinessDTOTransformer.toDTO(business));
		assertEquals(null, preUpdateWitholdTaxFirstLevel);
		assertEquals(null, preUpdateWitholdTaxSecondLevel);
		assertEquals(new BigDecimal("20.0"), Business.findBusiness(businessID).getSettings().getWitholdTaxPercentSecondLevel());
	}

	@Test
	public void pensionContributionUpdateTest() throws NotAuthenticatedException, FreeUserAccessForbiddenException, NoSuchObjectException, DataAccessException, ValidationException {
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		Business business = Business.findBusiness(businessID);
		BigDecimal preUpdatePensionContribution = business.getSettings().getWitholdTaxPercentSecondLevel();
		business.getSettings().setPensionContributionPercent(new BigDecimal("4.0"));
		businessGwtService.update(BusinessDTOTransformer.toDTO(business));
		assertEquals(null, preUpdatePensionContribution);
		assertEquals(new BigDecimal("4.0"), Business.findBusiness(businessID).getSettings().getPensionContributionPercent());
	}
	
}
