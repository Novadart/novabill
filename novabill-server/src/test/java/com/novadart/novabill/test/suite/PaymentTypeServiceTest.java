package com.novadart.novabill.test.suite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.aspect.logging.DBLoggerAspect;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.LogRecord;
import com.novadart.novabill.domain.PaymentType;
import com.novadart.novabill.domain.dto.factory.BusinessDTOFactory;
import com.novadart.novabill.domain.dto.factory.PaymentTypeDTOFactory;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.shared.client.data.EntityType;
import com.novadart.novabill.shared.client.data.OperationType;
import com.novadart.novabill.shared.client.dto.PaymentDateType;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.PaymentTypeGwtService;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:gwt-paymenttype-test-config.xml")
@Transactional
public class PaymentTypeServiceTest extends ServiceTest {
	
	@Autowired
	private PaymentTypeGwtService paymentTypeService;
	
	@Test
	public void paymentServiceWiringTest(){
		assertNotNull(paymentTypeService);
	}
	
	@Override
	@Before
	public void authenticate() {
		authenticatedPrincipal = Principal.findByUsername("giordano.battilana@novadart.com");
		authenticatePrincipal(authenticatedPrincipal);
	}
	
	@Test
	public void addAuthorizedTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, JsonParseException, JsonMappingException, IOException{
		PaymentTypeDTO paymentTypeDTO = PaymentTypeDTOFactory.toDTO(TestUtils.createPaymentType());
		paymentTypeDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		Long id = paymentTypeService.add(paymentTypeDTO);
		PaymentType.entityManager().flush();
		PaymentTypeDTO persistedDTO = PaymentTypeDTOFactory.toDTO(PaymentType.findPaymentType(id));
		assertTrue(EqualsBuilder.reflectionEquals(paymentTypeDTO, persistedDTO, "id", "business"));
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.PAYMENT_TYPE, rec.getEntityType());
		assertEquals(id, rec.getEntityID());
		assertEquals(OperationType.CREATE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(paymentTypeDTO.getName(), details.get(DBLoggerAspect.PAYMENT_TYPE_NAME));
		
	}
	
	@Test(expected = ValidationException.class)
	public void addAuthorizedInvalidImmediateNullTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, JsonParseException, JsonMappingException, IOException{
		PaymentType paymentType = new PaymentType();
		paymentType.setName("Payment type test name");
		paymentType.setDefaultPaymentNote("Payment type test defualt note");
		paymentType.setPaymentDateGenerator(PaymentDateType.IMMEDIATE);
		PaymentTypeDTO paymentTypeDTO = PaymentTypeDTOFactory.toDTO(paymentType);
		paymentTypeDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		paymentTypeService.add(paymentTypeDTO);
	}
	
	@Test(expected = ValidationException.class)
	public void addAuthorizedInvalidEndOfMonthNullTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, JsonParseException, JsonMappingException, IOException{
		PaymentType paymentType = new PaymentType();
		paymentType.setName("Payment type test name");
		paymentType.setDefaultPaymentNote("Payment type test defualt note");
		paymentType.setPaymentDateGenerator(PaymentDateType.END_OF_MONTH);
		PaymentTypeDTO paymentTypeDTO = PaymentTypeDTOFactory.toDTO(paymentType);
		paymentTypeDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		paymentTypeService.add(paymentTypeDTO);
	}
	
	@Test(expected = DataAccessException.class)
	public void addUnauthorizedTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException{
		PaymentTypeDTO paymentTypeDTO = PaymentTypeDTOFactory.toDTO(TestUtils.createPaymentType());
		paymentTypeDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(getUnathorizedBusinessID())));
		paymentTypeService.add(paymentTypeDTO);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedNullTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException{
		paymentTypeService.add(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedIDNotNullTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException{
		PaymentTypeDTO paymentTypeDTO = PaymentTypeDTOFactory.toDTO(TestUtils.createPaymentType());
		paymentTypeDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		paymentTypeDTO.setId(1l);
		paymentTypeService.add(paymentTypeDTO);
	}
	
	@Test
	public void addAuthorizedValidationFieldMappingTest() throws NotAuthenticatedException, FreeUserAccessForbiddenException, DataAccessException{
		PaymentTypeDTO paymentTypeDTO = new PaymentTypeDTO();
		paymentTypeDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		try{
			paymentTypeService.add(paymentTypeDTO);
		}catch (ValidationException e) {
			Set<Field> actual= new HashSet<Field>();
			for(ErrorObject error: e.getErrors())
				actual.add(error.getField());
			assertTrue(actual.contains(Field.name));
			assertTrue(actual.contains(Field.defaultPaymentNote));
			assertTrue(actual.contains(Field.paymentDateGenerator));
		}
	}
	
	private PaymentTypeDTO addPaymentType() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException{
		PaymentTypeDTO paymentTypeDTO = PaymentTypeDTOFactory.toDTO(TestUtils.createPaymentType());
		paymentTypeDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		paymentTypeDTO.setId(paymentTypeService.add(paymentTypeDTO));
		PaymentType.entityManager().flush();
		return paymentTypeDTO;
	}
	
	private void setDefaultPaymentType(Long clientID, Long paymentTypeID){
		Client client = Client.findClient(clientID);
		PaymentType paymentType = PaymentType.findPaymentType(paymentTypeID);
		client.setDefaultPaymentType(paymentType);
		paymentType.getClients().add(client);
		Client.entityManager().flush();
	}
	
	@Test
	public void removeAuthorizedTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException, JsonParseException, JsonMappingException, IOException{
		Long paymentTypeID = addPaymentType().getId();
		PaymentType paymentType = PaymentType.findPaymentType(paymentTypeID);
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		setDefaultPaymentType(client.getId(), paymentTypeID);
		paymentTypeService.remove(authenticatedPrincipal.getBusiness().getId(), paymentTypeID);
		PaymentType.entityManager().flush();
		assertNull(PaymentType.findPaymentType(paymentTypeID));
		assertNull(Client.findClient(client.getId()).getDefaultPaymentType());
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.PAYMENT_TYPE, rec.getEntityType());
		assertEquals(paymentTypeID, rec.getEntityID());
		assertEquals(OperationType.DELETE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(paymentType.getName(), details.get(DBLoggerAspect.PAYMENT_TYPE_NAME));
	}
	
	@Test(expected = DataAccessException.class)
	public void removeUnAuthorizedTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
		Long id = addPaymentType().getId();
		paymentTypeService.remove(getUnathorizedBusinessID(), id);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeBusinessIDNullTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
		Long id = addPaymentType().getId();
		paymentTypeService.remove(null, id);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeAuthorizedIDNullTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
		paymentTypeService.remove(getUnathorizedBusinessID(), null);
	}
	
	@Test
	public void updateAuthorizedTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException, JsonParseException, JsonMappingException, IOException{
		PaymentTypeDTO paymentTypeDTO = addPaymentType();
		paymentTypeDTO.setName("Test name!!!");
		paymentTypeService.update(paymentTypeDTO);
		PaymentType.entityManager().flush();
		PaymentTypeDTO persistedDTO = PaymentTypeDTOFactory.toDTO(PaymentType.findPaymentType(paymentTypeDTO.getId()));
		assertTrue(EqualsBuilder.reflectionEquals(paymentTypeDTO, persistedDTO, "business"));
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.PAYMENT_TYPE, rec.getEntityType());
		assertEquals(paymentTypeDTO.getId(), rec.getEntityID());
		assertEquals(OperationType.UPDATE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(paymentTypeDTO.getName(), details.get(DBLoggerAspect.PAYMENT_TYPE_NAME));
	}
	
	@Test(expected = DataAccessException.class)
	public void updateUnathorizedTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
		PaymentTypeDTO paymentTypeDTO = addPaymentType();
		paymentTypeDTO.setName("Test name!!!");
		paymentTypeDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(getUnathorizedBusinessID())));
		paymentTypeService.update(paymentTypeDTO);
	}
	
	@Test(expected = DataAccessException.class)
	public void updateAuthorizedNullTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
		paymentTypeService.update(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void updateAuthorizedIDNullTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
		PaymentTypeDTO paymentTypeDTO = addPaymentType();
		paymentTypeDTO.setId(null);
		paymentTypeService.update(paymentTypeDTO);
	}
	
	@Test
	public void getAllAuthorizedTest() throws NotAuthenticatedException, DataAccessException{
		List<PaymentTypeDTO> paymentTypeDTOs = paymentTypeService.getAll(authenticatedPrincipal.getBusiness().getId());
		assertTrue(paymentTypeDTOs.size() == authenticatedPrincipal.getBusiness().getPaymentTypes().size());
	}
	
	
	@Test(expected = DataAccessException.class)
	public void getAllUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		paymentTypeService.getAll(getUnathorizedBusinessID());
	}

	@Test(expected = DataAccessException.class)
	public void getAllIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		paymentTypeService.getAll(null);
	}
	
	@Test
	public void getAuthorizedTest() throws NotAuthenticatedException, NoSuchObjectException, DataAccessException, ValidationException, FreeUserAccessForbiddenException{
		PaymentType paymentType = TestUtils.createPaymentType();
		PaymentTypeDTO paymentTypeDTO = PaymentTypeDTOFactory.toDTO(paymentType);
		paymentTypeDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		Long id = paymentTypeService.add(paymentTypeDTO);
		PaymentType.entityManager().flush();
		assertTrue(EqualsBuilder.reflectionEquals(PaymentTypeDTOFactory.toDTO(paymentType), paymentTypeService.get(id), "id"));
	}
	
	@Test(expected = DataAccessException.class)
	public void getIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		paymentTypeService.get(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void getUnauthorizedTest() throws NotAuthenticatedException, NoSuchObjectException, DataAccessException{
		PaymentType paymentType = TestUtils.createPaymentType();
		Business business = Business.findBusiness(getUnathorizedBusinessID());
		paymentType.setBusiness(business);
		business.getPaymentTypes().add(paymentType);
		PaymentType.entityManager().flush();
		paymentTypeService.get(paymentType.getId());
	}
	
}
