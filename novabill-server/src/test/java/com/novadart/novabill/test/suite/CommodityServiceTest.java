package com.novadart.novabill.test.suite;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Commodity;
import com.novadart.novabill.domain.dto.factory.BusinessDTOFactory;
import com.novadart.novabill.domain.dto.factory.CommodityDTOFactory;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.CommodityGwtService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:gwt-commodity-test-config.xml")
@Transactional
public class CommodityServiceTest extends GWTServiceTest {
	
	@Autowired
	private CommodityGwtService commodityService;
	
	@Test
	public void commodityServiceWiringTest(){
		assertNotNull(commodityService);
	}
	
	@Test
	public void addAuthorizedTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		CommodityDTO commodityDTO = CommodityDTOFactory.toDTO(TestUtils.createCommodity());
		commodityDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		Long id = commodityService.add(commodityDTO);
		Commodity.entityManager().flush();
		CommodityDTO persistedDTO = CommodityDTOFactory.toDTO(Commodity.findCommodity(id));
		assertTrue(EqualsBuilder.reflectionEquals(commodityDTO, persistedDTO, "id", "business"));
	}
	
	@Test(expected = DataAccessException.class)
	public void addUnauthorizedTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		CommodityDTO commodityDTO = CommodityDTOFactory.toDTO(TestUtils.createCommodity());
		commodityDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(getUnathorizedBusinessID())));
		commodityService.add(commodityDTO);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedNullTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		commodityService.add(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedIDNotNullTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		CommodityDTO commodityDTO = CommodityDTOFactory.toDTO(TestUtils.createCommodity());
		commodityDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(getUnathorizedBusinessID())));
		commodityDTO.setId(1l);
		commodityService.add(commodityDTO);
	}
	
	private CommodityDTO addCommodity() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		CommodityDTO commodityDTO = CommodityDTOFactory.toDTO(TestUtils.createCommodity());
		commodityDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		commodityDTO.setId(commodityService.add(commodityDTO));
		Commodity.entityManager().flush();
		return commodityDTO;
	}
	
	@Test
	public void removeAuthorizedTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException {
		Long id = addCommodity().getId();
		commodityService.remove(authenticatedPrincipal.getBusiness().getId(), id);
		Commodity.entityManager().flush();
	    assertNull(Commodity.findCommodity(id));
	}
	
	@Test(expected = DataAccessException.class)
    public void removeUnAuthorizedTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException{
		Long id = addCommodity().getId();
		commodityService.remove(getUnathorizedBusinessID(), id);
    }

	 @Test(expected = DataAccessException.class)
     public void removeBusinessIDNullTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException{
        Long id = addCommodity().getId();
        commodityService.remove(null, id);
     }
	 
     @Test(expected = DataAccessException.class)
     public void removeAuthorizedIDNullTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException{
    	 commodityService.remove(getUnathorizedBusinessID(), null);
     }
     
     @Test
     public void updateAuthorizedTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException{
         CommodityDTO commodityDTO = addCommodity();
         commodityService.update(commodityDTO);
         Commodity.entityManager().flush();
         CommodityDTO persistedDTO = CommodityDTOFactory.toDTO(Commodity.findCommodity(commodityDTO.getId()));
         assertTrue(EqualsBuilder.reflectionEquals(commodityDTO, persistedDTO, "business"));
     }
     
     @Test(expected = DataAccessException.class)
     public void updateUnathorizedTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException{
    	 CommodityDTO commodityDTO = addCommodity();
    	 commodityDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(getUnathorizedBusinessID())));
         commodityService.update(commodityDTO);
     }
     
     @Test(expected = DataAccessException.class)
     public void updateAuthorizedNullTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException{
    	 commodityService.update(null);
     }
     
     @Test(expected = DataAccessException.class)
     public void updateAuthorizedIDNullTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException{
    	 CommodityDTO commodityDTO = addCommodity();
         commodityDTO.setId(null);
         commodityService.update(commodityDTO);
     }
     
     @Test
     public void getAllAuthorizedTest() throws NotAuthenticatedException, DataAccessException{
    	 List<CommodityDTO> commodityDTOs = commodityService.getAll(authenticatedPrincipal.getBusiness().getId());
    	 assertTrue(commodityDTOs.size() == authenticatedPrincipal.getBusiness().getCommodities().size());
     }
     
     
     @Test(expected = DataAccessException.class)
     public void getAllUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
    	 commodityService.getAll(getUnathorizedBusinessID());
     }
     
     @Test(expected = DataAccessException.class)
     public void getAllIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
    	 commodityService.getAll(null);
     }
     
     @Test
     public void customPricesSerializationDeserializationTest() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
    	 Commodity commodity1 = new Commodity();
    	 Map<String, BigDecimal> prices = new HashMap<String, BigDecimal>();
    	 prices.put("TestPriceList", new BigDecimal("100.00"));
    	 commodity1.setCustomPrices(prices);
    	 assertTrue(commodity1.getCustomPrices().equals(prices));
    	 Commodity commodity2 = new Commodity();
    	 Field f = commodity2.getClass().getDeclaredField("customPricesJson");
    	 f.setAccessible(true);
    	 f.set(commodity2, "{\"TestPriceList\":100.00}");
    	 assertTrue(commodity2.getCustomPrices().equals(prices));
     }
	
}
