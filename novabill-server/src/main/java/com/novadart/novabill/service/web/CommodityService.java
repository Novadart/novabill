package com.novadart.novabill.service.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.novadart.novabill.authorization.TrialOrPremiumChecker;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.annotation.Restrictions;
import com.novadart.novabill.authorization.PremiumChecker;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Commodity;
import com.novadart.novabill.domain.Price;
import com.novadart.novabill.domain.PriceList;
import com.novadart.novabill.domain.dto.transformer.CommodityDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.PriceDTOTransformer;
import com.novadart.novabill.service.validator.CommodityValidator;
import com.novadart.novabill.service.validator.SimpleValidator;
import com.novadart.novabill.shared.client.data.PriceListConstants;
import com.novadart.novabill.shared.client.data.PriceType;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.PriceDTO;
import com.novadart.novabill.shared.client.dto.PriceListDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.InvalidArgumentException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@Service
public class CommodityService {
	
	@Autowired
	private CommodityValidator validator;
	
	@Autowired
	private SimpleValidator simpleValidator;
	
	@Autowired
	private BusinessService businessService;
	
	@PreAuthorize("#businessID == principal.business.id")
	@Restrictions(checkers = TrialOrPremiumChecker.class)
	public List<CommodityDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return businessService.getCommodities(businessID);
	}

	@Restrictions(checkers = TrialOrPremiumChecker.class)
	@PreAuthorize("T(com.novadart.novabill.domain.Commodity).findCommodity(#id)?.business?.id == principal.business.id")
	public CommodityDTO get(Long businessID, Long id) throws NoSuchObjectException, NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		List<CommodityDTO> commodityDTOs = businessService.getCommodities(businessID); 
		for(CommodityDTO commodityDTO: commodityDTOs){
			if(commodityDTO.getId().equals(id))
				return commodityDTO;
		}
		throw new NoSuchObjectException();
	}

	private PriceList getDefaultPriceList(Business business) throws NoSuchObjectException{
		for(PriceList priceList: business.getPriceLists())
			if(priceList.getName().equals(PriceListConstants.DEFAULT))
				return priceList;
		throw new NoSuchObjectException();
	}
	
	private void setDefaultPrice(Commodity commodity, PriceDTO priceDTO) throws NoSuchObjectException, ValidationException, FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException {
		PriceList priceList = getDefaultPriceList(commodity.getBusiness());
		priceDTO.setPriceListID(priceList.getId());
		priceDTO.setCommodityID(commodity.getId());
		priceDTO.setPriceType(PriceType.FIXED);
		addOrUpdatePrice(commodity.getBusiness().getId(), priceDTO);
	}
	
	@PreAuthorize("#commodityDTO?.business?.id == principal.business.id and " +
				  "#commodityDTO?.id == null" )
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	public Long add(CommodityDTO commodityDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException {
		Commodity commodity = new Commodity();
		CommodityDTOTransformer.copyFromDTO(commodity, commodityDTO);
		if(StringUtils.isBlank(commodity.getSku()))
			commodity.setSku(Commodity.generateSku());
		Business business = Business.findBusiness(commodityDTO.getBusiness().getId());
		commodity.setBusiness(business);
		validator.validate(commodity, true);
		business.getCommodities().add(commodity);
		commodity.persist();
		commodity.flush();
		setDefaultPrice(commodity, commodityDTO.getPrices().get(PriceListConstants.DEFAULT));
		return commodity.getId();
	}

	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#commodityDTO?.business?.id == principal.business.id and " +
			  	  "#commodityDTO?.id != null" )
	public void update(CommodityDTO commodityDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException {
		Commodity persistentCommodity = Commodity.findCommodity(commodityDTO.getId());
		if(persistentCommodity == null)
			throw new NoSuchObjectException();
		Commodity copy = persistentCommodity.shallowCopy();
		CommodityDTOTransformer.copyFromDTO(copy, commodityDTO);
		validator.validate(copy, !copy.getSku().equals(persistentCommodity.getSku()));
		CommodityDTOTransformer.copyFromDTO(persistentCommodity, commodityDTO);
		PriceDTO defaultPriceDTO = commodityDTO.getPrices().get(PriceListConstants.DEFAULT);
		addOrUpdatePrice(commodityDTO.getBusiness().getId(), defaultPriceDTO);
	}

	@Transactional(readOnly = false)
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#businessID == principal.business.id and " +
			      "T(com.novadart.novabill.domain.Commodity).findCommodity(#id)?.business?.id == #businessID")
	public void remove(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		Commodity commodity = Commodity.findCommodity(id);
		commodity.remove(); //removing commodity
		if(Hibernate.isInitialized(commodity.getBusiness().getCommodities()))
			commodity.getBusiness().getCommodities().remove(commodity);
	}
	
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#businessID == principal.business.id and " +
			      "T(com.novadart.novabill.domain.Commodity).findCommodity(#priceDTO.commodityID)?.business?.id == #businessID and " +
			      "T(com.novadart.novabill.domain.PriceList).findPriceList(#priceDTO.priceListID)?.business?.id == #businessID")
	public Long addOrUpdatePrice(Long businessID, PriceDTO priceDTO) throws ValidationException, NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException{
		if(priceDTO.getId() == null){ //add
			Price price = new Price();
			PriceDTOTransformer.copyFromDTO(price, priceDTO);
			simpleValidator.validate(price);
			PriceList persistedPriceList = PriceList.findPriceList(priceDTO.getPriceListID());
			Commodity persistedCommodity = Commodity.findCommodity(priceDTO.getCommodityID());
			price.setCommodity(persistedCommodity);
			price.setPriceList(persistedPriceList);
			if(Hibernate.isInitialized(persistedCommodity.getPrices()))
				persistedCommodity.getPrices().add(price);
			if(Hibernate.isInitialized(persistedPriceList.getPrices()))
				persistedPriceList.getPrices().add(price);
			price.persist();
			Price.entityManager().flush();
			return price.getId();
		}else{ //update
			Price persisted = Price.findPrice(priceDTO.getId());
			PriceDTOTransformer.copyFromDTO(persisted, priceDTO);
			simpleValidator.validate(persisted);
			return persisted.merge().getId();
		}
	}
	
	@Transactional(readOnly = false)
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#businessID == principal.business.id and " + 
				"T(com.novadart.novabill.domain.PriceList).findPriceList(#priceListID)?.business?.id == #businessID and " +
				"T(com.novadart.novabill.domain.Commodity).findCommodity(#commodityID)?.business?.id == #businessID")
	public void removePrice(Long businessID, Long priceListID, Long commodityID) throws UnsupportedOperationException, NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		Price price = Price.findPrice(priceListID, commodityID);
		if(price.getPriceList().getName().equals(PriceListConstants.DEFAULT))
			throw new UnsupportedOperationException();
		price.remove();
		if(Hibernate.isInitialized(price.getPriceList().getPrices()))
			price.getPriceList().getPrices().remove(price);
		if(Hibernate.isInitialized(price.getCommodity().getPrices()))
			price.getCommodity().getPrices().remove(price);
	}

	public PageDTO<CommodityDTO> searchCommodities(Long businessID, String query, int start, int length)
			throws InvalidArgumentException, NotAuthenticatedException,
			DataAccessException {
		Business business = Business.findBusiness(businessID);
		PageDTO<Commodity> commodities = null;
		try{
			commodities = business.prefixCommoditiesSearch(query, start, length);
		}catch (Exception e) {
			throw new InvalidArgumentException();
		}
		List<CommodityDTO> commoditytDTOs = new ArrayList<CommodityDTO>();
		for(Commodity commodity: commodities.getItems())
			commoditytDTOs.add(CommodityDTOTransformer.toDTO(commodity));
		return new PageDTO<CommodityDTO>(commoditytDTOs, start, length, commodities.getTotal());
	}
	
	@Transactional(readOnly = true)
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#businessID == principal.business.id and " + 
				"T(com.novadart.novabill.domain.Commodity).findCommodity(#id)?.business?.id == #businessID")
	public Map<String, PriceDTO> getPrices(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException{
		List<PriceListDTO> priceLists = businessService.getPriceLists(businessID);
		Map<String, PriceDTO> result = new HashMap<>();
		Map<Long, String> plMap = new HashMap<>();
		for(PriceListDTO pl: priceLists){
			plMap.put(pl.getId(), pl.getName());
			result.put(pl.getName(), new PriceDTO(id, pl.getId()));
		}
		for(Price price: Price.findPricesForCommodity(id))
			result.put(plMap.get(price.getPriceList().getId()), PriceDTOTransformer.toDTO(price));
		return result;
	}

}
