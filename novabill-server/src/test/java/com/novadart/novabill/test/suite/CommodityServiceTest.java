package com.novadart.novabill.test.suite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Commodity;
import com.novadart.novabill.domain.Price;
import com.novadart.novabill.domain.dto.factory.BusinessDTOFactory;
import com.novadart.novabill.domain.dto.factory.CommodityDTOFactory;
import com.novadart.novabill.domain.dto.factory.PriceDTOFactory;
import com.novadart.novabill.shared.client.data.PriceType;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.PriceDTO;
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
	
	@Resource(name = "testPL")
	private HashMap<String, String> testPL;
	
	@Test
	public void commodityServiceWiringTest(){
		assertNotNull(commodityService);
	}
	
	@Test
	public void addAuthorizedTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		CommodityDTO commodityDTO = CommodityDTOFactory.toDTO(TestUtils.createCommodity());
		commodityDTO.setSku("12345");
		Long id = commodityService.add(commodityDTO);
		Commodity.entityManager().flush();
		CommodityDTO persistedDTO = CommodityDTOFactory.toDTO(Commodity.findCommodity(id));
		assertTrue(EqualsBuilder.reflectionEquals(commodityDTO, persistedDTO, "id", "business"));
	}
	
	@Test(expected = DataAccessException.class)
	public void addUnauthorizedTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		CommodityDTO commodityDTO = CommodityDTOFactory.toDTO(TestUtils.createCommodity());
		commodityService.add(commodityDTO);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedNullTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		commodityService.add(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedIDNotNullTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		CommodityDTO commodityDTO = CommodityDTOFactory.toDTO(TestUtils.createCommodity());
		commodityDTO.setId(1l);
		commodityService.add(commodityDTO);
	}
	
	private CommodityDTO addCommodity() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		CommodityDTO commodityDTO = CommodityDTOFactory.toDTO(TestUtils.createCommodity());
		commodityDTO.setSku("12345");
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
     public void getAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
    	 Long businessID = authenticatedPrincipal.getBusiness().getId();
    	 CommodityDTO commodityDTO = commodityService.getAll(businessID).iterator().next();
    	 assertTrue(EqualsBuilder.reflectionEquals(commodityDTO, commodityService.get(businessID, commodityDTO.getId()), "business"));
     }
     
     @Test(expected = DataAccessException.class)
     public void getUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
    	 CommodityDTO commodityDTO = commodityService.getAll(authenticatedPrincipal.getBusiness().getId()).iterator().next();
    	 commodityService.get(getUnathorizedBusinessID(), commodityDTO.getId());
     }

     @Test(expected = DataAccessException.class)
     public void getAuthorizedNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
    	 commodityService.get(authenticatedPrincipal.getBusiness().getId(), null);
     }
     
     @Test(expected = DataAccessException.class)
     public void getUnAuthorizedNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
    	 commodityService.get(getUnathorizedBusinessID(), null);
     }
     
     @Test(expected = DataAccessException.class)
     public void getNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
    	 commodityService.get(null, null);
     }
     
     @Test
     public void addOrUpdate1Test() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException{
    	 Long commodityID = Long.parseLong(testPL.get(authenticatedPrincipal.getUsername() + ":commodityID"));
    	 Long priceListID = Long.parseLong(testPL.get(authenticatedPrincipal.getUsername()));
    	 Price price = Price.findPrice(priceListID, commodityID);
    	 PriceDTO priceDTO = PriceDTOFactory.toDTO(price);
    	 priceDTO.setPriceType(PriceType.DERIVED);
    	 priceDTO.setQuantity(new BigDecimal("5.00"));
    	 commodityService.addOrUpdatePrice(authenticatedPrincipal.getBusiness().getId(), priceDTO);
    	 price = Price.findPrice(priceListID, commodityID);
    	 assertEquals(PriceType.DERIVED, price.getPriceType());
    	 assertEquals(new BigDecimal("5.00"), price.getQuantity());
     }
     
     @Test
     public void addOrUpdate2Test() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException{
    	 Long priceListID = Long.parseLong(testPL.get(authenticatedPrincipal.getUsername()));
    	 Long commodityID = addCommodity().getId();
    	 PriceDTO priceDTO = new PriceDTO();
    	 priceDTO.setPriceType(PriceType.DERIVED);
    	 priceDTO.setQuantity(new BigDecimal("5.00"));
    	 priceDTO.setCommodityID(commodityID);
    	 priceDTO.setPriceListID(priceListID);
    	 Long id = commodityService.addOrUpdatePrice(authenticatedPrincipal.getBusiness().getId(), priceDTO);
    	 Price price = Price.findPrice(priceListID, commodityID);
    	 assertEquals(PriceType.DERIVED, price.getPriceType());
    	 assertEquals(new BigDecimal("5.00"), price.getQuantity());
    	 assertEquals(id, price.getId());
    	 assertEquals(commodityID, price.getCommodity().getId());
    	 assertEquals(priceListID, price.getPriceList().getId());
     }
    
     @Test
     public void removePriceTest() throws NotAuthenticatedException, DataAccessException{
    	 Long commodityID = Long.parseLong(testPL.get(authenticatedPrincipal.getUsername() + ":commodityID"));
    	 Long priceListID = Long.parseLong(testPL.get(authenticatedPrincipal.getUsername()));
    	 commodityService.removePrice(authenticatedPrincipal.getBusiness().getId(), priceListID, commodityID);
    	 Price price = Price.findPrice(priceListID, commodityID);
    	 assertNull(price);
     }
     
}
