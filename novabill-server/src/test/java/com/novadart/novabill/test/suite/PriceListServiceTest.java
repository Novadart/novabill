package com.novadart.novabill.test.suite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.PaymentType;
import com.novadart.novabill.domain.PriceList;
import com.novadart.novabill.domain.dto.factory.BusinessDTOFactory;
import com.novadart.novabill.domain.dto.factory.PriceListDTOFactory;
import com.novadart.novabill.shared.client.data.PriceType;
import com.novadart.novabill.shared.client.dto.PriceDTO;
import com.novadart.novabill.shared.client.dto.PriceListDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.PriceListGwtService;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:gwt-pricelist-test-config.xml")
@Transactional
@DirtiesContext
public class PriceListServiceTest extends GWTServiceTest {

	@Autowired
	private PriceListGwtService priceListService;
	
	@Resource(name = "testPL")
	private HashMap<String, String> testPL;
	
	@Test
	public void priceListServiceTest(){
		assertNotNull(priceListService);
	}
	
	@Test
	public void addAuthorizedTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		PriceListDTO priceListDTO = PriceListDTOFactory.toDTO(TestUtils.createPriceList(), false);
		priceListDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		Long id = priceListService.add(priceListDTO);
		PriceList.entityManager().flush();
		PriceListDTO persistedDTO = PriceListDTOFactory.toDTO(PriceList.findPriceList(id), false);
		assertTrue(EqualsBuilder.reflectionEquals(priceListDTO, persistedDTO, "id", "business"));
	}
	
	@Test(expected = DataAccessException.class)
	public void addUnauthorizedTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		PriceListDTO priceListDTO = PriceListDTOFactory.toDTO(TestUtils.createPriceList(), false);
		priceListDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(getUnathorizedBusinessID())));
		priceListService.add(priceListDTO);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedNullTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		priceListService.add(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedIDNotNullTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		PriceListDTO priceListDTO = PriceListDTOFactory.toDTO(TestUtils.createPriceList(), false);
		priceListDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		priceListDTO.setId(1l);
		priceListService.add(priceListDTO);
	}
	
	@Test
	public void addAuthorizedValidationFieldMappingTest() throws NotAuthenticatedException, AuthorizationException, DataAccessException{
		PriceListDTO priceListDTO = new PriceListDTO();
		priceListDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		try{
			priceListService.add(priceListDTO);
		}catch (ValidationException e) {
			Set<Field> actual= new HashSet<Field>();
			for(ErrorObject error: e.getErrors())
				actual.add(error.getField());
			assertTrue(actual.contains(Field.name));
		}
	}
	
	@Test
	public void removeAuthorizedTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException{
		PriceListDTO priceListDTO = PriceListDTOFactory.toDTO(TestUtils.createPriceList(), false);
		priceListDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		Long id = priceListService.add(priceListDTO);
		priceListService.remove(authenticatedPrincipal.getBusiness().getId(), id);
		assertNull(PriceList.findPriceList(id));
	}
	
	@Test(expected = DataAccessException.class)
	public void removeUnAuthorizedTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException{
		PriceListDTO priceListDTO = PriceListDTOFactory.toDTO(TestUtils.createPriceList(), false);
		priceListDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		Long id = priceListService.add(priceListDTO);
		priceListService.remove(getUnathorizedBusinessID(), id);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeBusinessIDNullTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException{
		PriceListDTO priceListDTO = PriceListDTOFactory.toDTO(TestUtils.createPriceList(), false);
		priceListDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		Long id = priceListService.add(priceListDTO);
		priceListService.remove(null, id);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeAuthorizedIDNullTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException{
		priceListService.remove(getUnathorizedBusinessID(), null);
	}
	
	@Test
	public void getAllAuthorizedTest() throws NotAuthenticatedException, DataAccessException, ValidationException, AuthorizationException{
		PriceListDTO priceListDTO = PriceListDTOFactory.toDTO(TestUtils.createPriceList(), false);
		priceListDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		priceListService.add(priceListDTO);
		List<PriceListDTO> priceListDTOs = priceListService.getAll(authenticatedPrincipal.getBusiness().getId());
		assertTrue(priceListDTOs.size() == authenticatedPrincipal.getBusiness().getPriceLists().size());
	}
	
	
	@Test(expected = DataAccessException.class)
	public void getAllUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, AuthorizationException{
		PriceListDTO priceListDTO = PriceListDTOFactory.toDTO(TestUtils.createPriceList(), false);
		priceListDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		priceListService.add(priceListDTO);
		priceListService.getAll(getUnathorizedBusinessID());
	}

	@Test(expected = DataAccessException.class)
	public void getAllIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		priceListService.getAll(null);
	}
	
	@Test
	public void getAuthorizedTest() throws NotAuthenticatedException, NoSuchObjectException, DataAccessException, ValidationException, AuthorizationException{
		Long id = Long.parseLong(testPL.get(authenticatedPrincipal.getUsername()));
		PriceListDTO priceListDTO = priceListService.get(id);
		assertNotNull(priceListDTO);
		assertNotNull(priceListDTO.getPrices());
		assertTrue(priceListDTO.getPrices().size() > 0);
		boolean checked = false;
		for(PriceDTO priceDTO: priceListDTO.getPrices()){
			assertNotNull(priceDTO.getCommodityID());
			if(priceDTO.getCommodityID().equals(Long.parseLong(testPL.get(authenticatedPrincipal.getUsername() + ":commodityID")))){
				assertEquals(new BigDecimal(testProps.get("commodityPriceQuantity")), priceDTO.getQuantity());
				assertEquals(PriceType.valueOf(testProps.get("commodityPriceType")), priceDTO.getPriceType());
				assertEquals(id, priceDTO.getPriceListID());
				checked = true;
			}
		}
		assertTrue(checked);
	}
	
	@Test(expected = DataAccessException.class)
	public void getIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		priceListService.get(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void getUnauthorizedTest() throws NotAuthenticatedException, NoSuchObjectException, DataAccessException{
		PriceList priceList = TestUtils.createPriceList();
		Business business = Business.findBusiness(getUnathorizedBusinessID());
		priceList.setBusiness(business);
		business.getPriceLists().add(priceList);
		PaymentType.entityManager().flush();
		priceListService.get(priceList.getId());
	}
	
	
}
