package com.novadart.novabill.test.suite;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.novadart.novabill.aspect.logging.DBLoggerAspect;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Commodity;
import com.novadart.novabill.domain.LogRecord;
import com.novadart.novabill.domain.Price;
import com.novadart.novabill.domain.dto.transformer.BusinessDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.CommodityDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.PriceDTOTransformer;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.web.BusinessService;
import com.novadart.novabill.service.web.CommodityService;
import com.novadart.novabill.shared.client.data.EntityType;
import com.novadart.novabill.shared.client.data.OperationType;
import com.novadart.novabill.shared.client.data.PriceListConstants;
import com.novadart.novabill.shared.client.data.PriceType;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.PriceDTO;
import com.novadart.novabill.shared.client.dto.PriceListDTO;
import com.novadart.novabill.shared.client.exception.*;
import com.novadart.novabill.shared.client.facade.CommodityGwtService;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:gwt-commodity-test-config.xml")
@Transactional
@DirtiesContext
@ActiveProfiles("dev")
public class CommodityServiceTest extends ServiceTest {
	
	@Autowired
	private CommodityGwtService commodityGwtService;
	
	@Autowired
	private CommodityService commodityService;
	
	@Autowired
	private BusinessService businessService;
	
	@Resource(name = "testPL")
	private HashMap<String, String> testPL;
	
	@Override
	@Before
	public void authenticate() {
		authenticatedPrincipal = Principal.findByUsername("giordano.battilana@novadart.com");
		authenticatePrincipal(authenticatedPrincipal);
	}
	
	@Test
	public void commodityServiceWiringTest(){
		assertNotNull(commodityGwtService);
	}
	
	
	private CommodityDTO addCommodity(Long businessID, BigDecimal defaultPrice) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
		CommodityDTO commodityDTO = CommodityDTOTransformer.toDTO(TestUtils.createCommodity());
		TestUtils.setDefaultPrice(commodityDTO, defaultPrice);
		commodityDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(businessID)));
		Long commodityID = commodityGwtService.add(commodityDTO);
		commodityDTO.setId(commodityID);
		return commodityDTO;
	}
	
	@Test
	public void addAuthorizedTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException, JsonParseException, JsonMappingException, IOException{
		BigDecimal defaultPrice = new BigDecimal("24.95");
		CommodityDTO commodityDTO = addCommodity(authenticatedPrincipal.getBusiness().getId(), defaultPrice);
		Commodity.entityManager().flush();
		CommodityDTO persistedDTO = CommodityDTOTransformer.toDTO(Commodity.findCommodity(commodityDTO.getId()));
		assertTrue(EqualsBuilder.reflectionEquals(commodityDTO, persistedDTO, "id", "business", "prices"));
		PriceDTO defaultPriceDTO = commodityService.getPrices(authenticatedPrincipal.getBusiness().getId(), persistedDTO.getId()).get(PriceListConstants.DEFAULT);
		assertEquals(defaultPrice, defaultPriceDTO.getPriceValue());
		assertEquals(PriceType.FIXED, defaultPriceDTO.getPriceType());
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.COMMODITY, rec.getEntityType());
		assertEquals(commodityDTO.getId(), rec.getEntityID());
		assertEquals(OperationType.CREATE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(commodityDTO.getDescription(), details.get(DBLoggerAspect.COMMODITY_NAME));
	}
	
	@Test(expected = DataAccessException.class)
	public void addUnauthorizedTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
		CommodityDTO commodityDTO = addCommodity(getUnathorizedBusinessID(), new BigDecimal("24.95"));
		commodityGwtService.add(commodityDTO);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedNullTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
		commodityGwtService.add(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedIDNotNullTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
		CommodityDTO commodityDTO = addCommodity(authenticatedPrincipal.getBusiness().getId(), new BigDecimal("24.95"));
		commodityDTO.setId(1l);
		commodityGwtService.add(commodityDTO);
	}
	
	@Test
	public void removeAuthorizedTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException, JsonParseException, JsonMappingException, IOException {
		Long commodityID = Long.parseLong(testPL.get(authenticatedPrincipal.getUsername() + ":commodityID"));
		String desc = Commodity.findCommodity(commodityID).getDescription();
		commodityGwtService.remove(authenticatedPrincipal.getBusiness().getId(), commodityID);
		Commodity.entityManager().flush();
	    assertNull(Commodity.findCommodity(commodityID));
	    LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.COMMODITY, rec.getEntityType());
		assertEquals(commodityID, rec.getEntityID());
		assertEquals(OperationType.DELETE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(desc, details.get(DBLoggerAspect.COMMODITY_NAME));
		assertEquals(true, rec.isReferringToDeletedEntity());
	}
	
	@Test(expected = DataAccessException.class)
    public void removeUnAuthorizedTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
		Long commodityID = Long.parseLong(testPL.get(authenticatedPrincipal.getUsername() + ":commodityID"));
		commodityGwtService.remove(getUnathorizedBusinessID(), commodityID);
    }

	 @Test(expected = DataAccessException.class)
     public void removeBusinessIDNullTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
		Long commodityID = Long.parseLong(testPL.get(authenticatedPrincipal.getUsername() + ":commodityID"));
        commodityGwtService.remove(null, commodityID);
     }
	 
     @Test(expected = DataAccessException.class)
     public void removeAuthorizedIDNullTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
    	 commodityGwtService.remove(getUnathorizedBusinessID(), null);
     }
     
     @Test
     public void updateAuthorizedTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException, JsonParseException, JsonMappingException, IOException{
    	 Long commodityID = Long.parseLong(testPL.get(authenticatedPrincipal.getUsername() + ":commodityID"));
         CommodityDTO commodityDTO = commodityGwtService.get(authenticatedPrincipal.getBusiness().getId(), commodityID);
         commodityDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
         commodityDTO.setDescription("Edited description");
         commodityGwtService.update(commodityDTO);
         Commodity.entityManager().flush();
         CommodityDTO persistedDTO = CommodityDTOTransformer.toDTO(Commodity.findCommodity(commodityDTO.getId()));
         assertEquals("Edited description", persistedDTO.getDescription());
         LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
         assertEquals(EntityType.COMMODITY, rec.getEntityType());
         assertEquals(commodityDTO.getId(), rec.getEntityID());
         assertEquals(OperationType.UPDATE, rec.getOperationType());
         Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
         assertEquals(commodityDTO.getDescription(), details.get(DBLoggerAspect.COMMODITY_NAME));
     }
     
     @Test(expected = DataAccessException.class)
     public void updateUnathorizedTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
    	 CommodityDTO commodityDTO = addCommodity(getUnathorizedBusinessID(), new BigDecimal("10.00"));
         commodityGwtService.update(commodityDTO);
     }
     
     @Test(expected = DataAccessException.class)
     public void updateAuthorizedNullTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
    	 commodityGwtService.update(null);
     }
     
     @Test(expected = DataAccessException.class)
     public void updateAuthorizedIDNullTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
    	 Long commodityID = Long.parseLong(testPL.get(authenticatedPrincipal.getUsername() + ":commodityID"));
         CommodityDTO commodityDTO = commodityGwtService.get(authenticatedPrincipal.getBusiness().getId(), commodityID);
         commodityDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
         commodityDTO.setId(null);
         commodityGwtService.update(commodityDTO);
     }
     
     @Test
     public void getAllAuthorizedTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
    	 List<CommodityDTO> commodityDTOs = commodityGwtService.getAll(authenticatedPrincipal.getBusiness().getId());
    	 assertTrue(commodityDTOs.size() == authenticatedPrincipal.getBusiness().getCommodities().size());
     }
     
     
     @Test(expected = DataAccessException.class)
     public void getAllUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
    	 commodityGwtService.getAll(getUnathorizedBusinessID());
     }
     
     @Test(expected = DataAccessException.class)
     public void getAllIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
    	 commodityGwtService.getAll(null);
     }
     
     @Test
     public void getAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, FreeUserAccessForbiddenException{
    	 Long commodityID = addCommodity(authenticatedPrincipal.getBusiness().getId(), new BigDecimal("10.00")).getId();
    	 CommodityDTO commodityDTO = commodityGwtService.get(authenticatedPrincipal.getBusiness().getId(), commodityID);
    	 assertTrue(EqualsBuilder.reflectionEquals(commodityDTO, CommodityDTOTransformer.toDTO(Commodity.findCommodity(commodityID)), "business", "prices"));
    	 assertNotNull(commodityDTO.getPrices());
    	 assertTrue(commodityDTO.getPrices().containsKey(PriceListConstants.DEFAULT));
     }
     
     @Test(expected = DataAccessException.class)
     public void getUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
    	 CommodityDTO commodityDTO = commodityGwtService.getAll(authenticatedPrincipal.getBusiness().getId()).iterator().next();
    	 commodityGwtService.get(getUnathorizedBusinessID(), commodityDTO.getId());
     }

     @Test(expected = DataAccessException.class)
     public void getAuthorizedNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
    	 commodityGwtService.get(authenticatedPrincipal.getBusiness().getId(), null);
     }
     
     @Test(expected = DataAccessException.class)
     public void getUnAuthorizedNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
    	 commodityGwtService.get(getUnathorizedBusinessID(), null);
     }
     
     @Test(expected = DataAccessException.class)
     public void getNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
    	 commodityGwtService.get(null, null);
     }
     
     @Test
     public void addOrUpdate1Test() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
    	 Long commodityID = Long.parseLong(testPL.get(authenticatedPrincipal.getUsername() + ":commodityID"));
    	 Long priceListID = Long.parseLong(testPL.get(authenticatedPrincipal.getUsername()));
    	 Price price = Price.findPrice(priceListID, commodityID);
    	 PriceDTO priceDTO = PriceDTOTransformer.toDTO(price);
    	 priceDTO.setPriceType(PriceType.DISCOUNT_PERCENT);
    	 priceDTO.setPriceValue(new BigDecimal("5.00"));
    	 commodityGwtService.addOrUpdatePrice(authenticatedPrincipal.getBusiness().getId(), priceDTO);
    	 price = Price.findPrice(priceListID, commodityID);
    	 assertEquals(PriceType.DISCOUNT_PERCENT, price.getPriceType());
    	 assertEquals(new BigDecimal("5.00"), price.getPriceValue());
     }
     
     @Test
     public void addOrUpdate2Test() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
    	 Long priceListID = Long.parseLong(testPL.get(authenticatedPrincipal.getUsername()));
    	 Long businessID = authenticatedPrincipal.getBusiness().getId();
    	 Long commodityID = addCommodity(authenticatedPrincipal.getBusiness().getId(), new BigDecimal("24.95")).getId();
    	 PriceDTO priceDTO = new PriceDTO();
    	 priceDTO.setPriceType(PriceType.DISCOUNT_PERCENT);
    	 priceDTO.setPriceValue(new BigDecimal("5.00"));
    	 priceDTO.setCommodityID(commodityID);
    	 priceDTO.setPriceListID(priceListID);
    	 Long id = commodityGwtService.addOrUpdatePrice(businessID, priceDTO);
    	 Price price = Price.findPrice(priceListID, commodityID);
    	 assertEquals(PriceType.DISCOUNT_PERCENT, price.getPriceType());
    	 assertEquals(new BigDecimal("5.00"), price.getPriceValue());
    	 assertEquals(id, price.getId());
    	 assertEquals(commodityID, price.getCommodity().getId());
    	 assertEquals(priceListID, price.getPriceList().getId());
     }
    
     @Test
     public void removePriceTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
    	 Long commodityID = Long.parseLong(testPL.get(authenticatedPrincipal.getUsername() + ":commodityID"));
    	 Long priceListID = Long.parseLong(testPL.get(authenticatedPrincipal.getUsername()));
    	 commodityGwtService.removePrice(authenticatedPrincipal.getBusiness().getId(), priceListID, commodityID);
    	 Price price = Price.findPrice(priceListID, commodityID);
    	 assertNull(price);
     }
     
     @Test(expected = UnsupportedOperationException.class)
     public void removeDefaultPriceTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
    	 Long commodityID = Long.parseLong(testPL.get(authenticatedPrincipal.getUsername() + ":commodityID"));
    	 Price defaultPrice = null;
    	 Iterator<Price> iter = Commodity.findCommodity(commodityID).getPrices().iterator();
    	 while(iter.hasNext()){
    		 defaultPrice = iter.next();
    		 if(defaultPrice.getPriceList().getName().equals(PriceListConstants.DEFAULT))
    			 break;
    	 }
    	 commodityGwtService.removePrice(authenticatedPrincipal.getBusiness().getId(), defaultPrice.getPriceList().getId(), defaultPrice.getCommodity().getId());
     }
     
     
     @Test
     public void getPricesTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
    	 Long businessID = authenticatedPrincipal.getBusiness().getId();
    	 Long commodityID = Long.parseLong(testPL.get(authenticatedPrincipal.getUsername() + ":commodityID"));
    	 Map<String, PriceDTO> prices = commodityGwtService.get(businessID, commodityID).getPrices();
    	 List<PriceListDTO> priceLists = businessService.getPriceLists(businessID);
    	 for(PriceListDTO pl: priceLists)
    		 assertTrue(prices.containsKey(pl.getName()));
     }
     
     @Test(expected = Exception.class)
     public void duplicateSkuTest() throws NotAuthenticatedException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException, ValidationException {
    	CommodityDTO commodityDTO = CommodityDTOTransformer.toDTO(TestUtils.createCommodity());
    	TestUtils.setDefaultPrice(commodityDTO, new BigDecimal("5.00"));
 		commodityDTO.setSku("12345");
 		commodityDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
 		commodityGwtService.add(commodityDTO);
		 try {
			 commodityGwtService.add(commodityDTO);
		 } catch (ValidationException e) {
			 assertTrue(true);
			 throw e;
		 }
		 fail();
	 }
     
}
