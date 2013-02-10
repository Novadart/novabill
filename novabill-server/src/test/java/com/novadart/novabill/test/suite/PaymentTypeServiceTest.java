package com.novadart.novabill.test.suite;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.PaymentType;
import com.novadart.novabill.domain.dto.factory.BusinessDTOFactory;
import com.novadart.novabill.domain.dto.factory.PaymentTypeDTOFactory;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.PaymentTypeService;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:gwt-paymenttype-test-config.xml")
@Transactional
public class PaymentTypeServiceTest extends GWTServiceTest {
	
	@Autowired
	private PaymentTypeService paymentTypeService;
	
	@Test
	public void paymentServiceWiringTest(){
		assertNotNull(paymentTypeService);
	}
	
	@Test
	public void addAuthorizedTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		PaymentTypeDTO paymentTypeDTO = PaymentTypeDTOFactory.toDTO(TestUtils.createPaymentType());
		paymentTypeDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		Long id = paymentTypeService.add(paymentTypeDTO);
		PaymentType.entityManager().flush();
		PaymentTypeDTO persistedDTO = PaymentTypeDTOFactory.toDTO(PaymentType.findPaymentType(id));
		assertTrue(EqualsBuilder.reflectionEquals(paymentTypeDTO, persistedDTO, "id", "business"));
	}
	
	@Test(expected = DataAccessException.class)
	public void addUnauthorizedTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		PaymentTypeDTO paymentTypeDTO = PaymentTypeDTOFactory.toDTO(TestUtils.createPaymentType());
		paymentTypeDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(getUnathorizedBusinessID())));
		paymentTypeService.add(paymentTypeDTO);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedIDNullTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		paymentTypeService.add(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedIDNotNullTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		PaymentTypeDTO paymentTypeDTO = PaymentTypeDTOFactory.toDTO(TestUtils.createPaymentType());
		paymentTypeDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		paymentTypeDTO.setId(1l);
		paymentTypeService.add(null);
	}
	
	@Test
	public void addAuthorizedValidationFieldMappingTest() throws NotAuthenticatedException, AuthorizationException, DataAccessException{
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
			assertTrue(actual.contains(Field.paymentDateDelta));
			assertTrue(actual.contains(Field.paymentDateGenerator));
		}
	}
	
	private Long addPaymentType() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		PaymentTypeDTO paymentTypeDTO = PaymentTypeDTOFactory.toDTO(TestUtils.createPaymentType());
		paymentTypeDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		Long id = paymentTypeService.add(paymentTypeDTO);
		PaymentType.entityManager().flush();
		return id;
	}
	
	@Test
	public void removeAuthorizedTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException{
		Long id = addPaymentType();
		paymentTypeService.remove(authenticatedPrincipal.getBusiness().getId(), id);
		PaymentType.entityManager().flush();
		assertNull(Invoice.findInvoice(id));
	}
	
	@Test(expected = DataAccessException.class)
	public void removeUnAuthorizedTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException{
		Long id = addPaymentType();
		paymentTypeService.remove(getUnathorizedBusinessID(), id);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeBusinessIDNullTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException{
		Long id = addPaymentType();
		paymentTypeService.remove(null, id);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeAuthorizedIDNullTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException{
		Long id = addPaymentType();
		paymentTypeService.remove(getUnathorizedBusinessID(), null);
	}

}
