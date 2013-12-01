package com.novadart.novabill.service.web;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Commodity;
import com.novadart.novabill.domain.Price;
import com.novadart.novabill.domain.PriceList;
import com.novadart.novabill.domain.dto.factory.CommodityDTOFactory;
import com.novadart.novabill.domain.dto.factory.PriceDTOFactory;
import com.novadart.novabill.service.validator.SimpleValidator;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.PriceDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.InvalidArgumentException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@Service
public class CommodityService {
	
	@Autowired
	private SimpleValidator validator;
	
	@Autowired
	private BusinessService businessService;
	
	@PreAuthorize("#businessID == principal.business.id")
	public List<CommodityDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return businessService.getCommodities(businessID);
	}
	
	@PreAuthorize("T(com.novadart.novabill.domain.Commodity).findCommodity(#id)?.business?.id == principal.business.id")
	public CommodityDTO get(Long businessID, Long id) throws NoSuchObjectException, NotAuthenticatedException, DataAccessException {
		for(CommodityDTO commodityDTO: businessService.getCommodities(businessID))
			if(commodityDTO.getId().equals(id))
				return commodityDTO;
		throw new NoSuchObjectException();
	}

	@PreAuthorize("#commodityDTO?.business?.id == principal.business.id and " +
				  "#commodityDTO?.id == null" )
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	public Long add(CommodityDTO commodityDTO) throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException {
		Commodity commodity = new Commodity();
		CommodityDTOFactory.copyFromDTO(commodity, commodityDTO);
		validator.validate(commodity);
		Business business = Business.findBusiness(commodityDTO.getBusiness().getId());
		business.getCommodities().add(commodity);
		commodity.setBusiness(business);
		commodity.persist();
		commodity.flush();
		return commodity.getId();
	}

	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("#commodityDTO?.business?.id == principal.business.id and " +
			  	  "#commodityDTO?.id != null" )
	public void update(CommodityDTO commodityDTO) throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException {
		Commodity persistentCommodity = Commodity.findCommodity(commodityDTO.getId());
		if(persistentCommodity == null)
			throw new NoSuchObjectException();
		CommodityDTOFactory.copyFromDTO(persistentCommodity, commodityDTO);
		validator.validate(persistentCommodity);
	}

	@Transactional(readOnly = false)
	@PreAuthorize("#businessID == principal.business.id and " +
			      "T(com.novadart.novabill.domain.Commodity).findCommodity(#id)?.business?.id == #businessID")
	public void remove(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException {
		Commodity commodity = Commodity.findCommodity(id);
		commodity.remove(); //removing commodity
		if(Hibernate.isInitialized(commodity.getBusiness().getCommodities()))
			commodity.getBusiness().getCommodities().remove(commodity);
	}
	
	@Transactional(readOnly = false)
	@PreAuthorize("#businessID == principal.business.id and " +
			      "T(com.novadart.novabill.domain.Commodity).findCommodity(#priceDTO.commodityID)?.business?.id == #businessID and " +
			      "T(com.novadart.novabill.domain.PriceList).findPriceList(#priceDTO.priceListID)?.business?.id == #businessID")
	public Long addOrUpdatePrice(Long businessID, PriceDTO priceDTO){
		if(priceDTO.getId() == null){ //add
			Price price = new Price();
			PriceDTOFactory.copyFromDTO(price, priceDTO);
			PriceList persistedPriceList = PriceList.findPriceList(priceDTO.getPriceListID());
			Commodity persistedCommodity = Commodity.findCommodity(priceDTO.getCommodityID());
			if(Hibernate.isInitialized(persistedCommodity.getPrices()))
				persistedCommodity.getPrices().add(price);
			if(Hibernate.isInitialized(persistedPriceList.getPrices()))
				persistedPriceList.getPrices().add(price);
			price.setCommodity(persistedCommodity);
			price.setPriceList(persistedPriceList);
			price.persist();
			Price.entityManager().flush();
			return price.getId();
		}else{ //update
			Price persisted = Price.findPrice(priceDTO.getId());
			PriceDTOFactory.copyFromDTO(persisted, priceDTO);
			return persisted.merge().getId();
		}
	}
	
	@Transactional(readOnly = false)
	@PreAuthorize("#businessID == principal.business.id and " + 
				"T(com.novadart.novabill.domain.PriceList).findPriceList(#priceListID)?.business?.id == #businessID and " +
				"T(com.novadart.novabill.domain.Commodity).findCommodity(#commodityID)?.business?.id == #businessID")
	public void removePrice(Long businessID, Long priceListID, Long commodityID){
		Price price = Price.findPrice(priceListID, commodityID);
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
			commoditytDTOs.add(CommodityDTOFactory.toDTO(commodity));
		return new PageDTO<CommodityDTO>(commoditytDTOs, start, length, commodities.getTotal());
	}

}
