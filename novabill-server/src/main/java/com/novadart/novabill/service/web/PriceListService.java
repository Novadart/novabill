package com.novadart.novabill.service.web;

import com.novadart.novabill.annotation.Restrictions;
import com.novadart.novabill.authorization.PremiumChecker;
import com.novadart.novabill.domain.*;
import com.novadart.novabill.domain.dto.transformer.CommodityDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.PriceDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.PriceListDTOTransformer;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.PriceListValidator;
import com.novadart.novabill.shared.client.data.PriceListConstants;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.PriceDTO;
import com.novadart.novabill.shared.client.dto.PriceListDTO;
import com.novadart.novabill.shared.client.exception.*;
import com.novadart.novabill.shared.client.tuple.Pair;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PriceListService {
	
	@Autowired
	private PriceListValidator validator;

	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private UtilsService utilsService;
	
	@PreAuthorize("#businessID == principal.business.id")
	public List<PriceListDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return businessService.getPriceLists(businessID);
	}
	
	@PreAuthorize("T(com.novadart.novabill.domain.PriceList).findPriceList(#id)?.business?.id == principal.business.id")
	public PriceListDTO get(Long id) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException {
		Long businessID = utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId();
		PriceList defaultPL = PriceList.getDefaultPriceList(businessID);
		List<Price> defautPLPrices = Price.findPricesForPriceList(defaultPL.getId());
		List<CommodityDTO> commodities = new ArrayList<>(defautPLPrices.size());
		Map<String, Map<String, PriceDTO>> commoditiesPricesMap = new HashMap<>();
		for(Price price: defautPLPrices){
			CommodityDTO commodityDTO = CommodityDTOTransformer.toDTO(price.getCommodity());
			Map<String, PriceDTO> commodityPrices = new HashMap<>();
			commodityPrices.put(PriceListConstants.DEFAULT, PriceDTOTransformer.toDTO(price));
			commodityDTO.setPrices(commodityPrices);
			commoditiesPricesMap.put(price.getCommodity().getSku(), commodityPrices);
			commodities.add(commodityDTO);
		}
		if(defaultPL.getId() == id)
			return PriceListDTOTransformer.toDTO(defaultPL, commodities);
		PriceList priceList = PriceList.findPriceList(id);
		for(Map<String, PriceDTO> commPricesMap: commoditiesPricesMap.values())
			commPricesMap.put(priceList.getName(), new PriceDTO(commPricesMap.get(PriceListConstants.DEFAULT).getCommodityID(), priceList.getId()));
		for(Price price: Price.findPricesForPriceList(priceList.getId()))
			commoditiesPricesMap.get(price.getCommodity().getSku()).put(priceList.getName(), PriceDTOTransformer.toDTO(price));
		return PriceListDTOTransformer.toDTO(priceList, commodities);
	}
	
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("#priceListDTO?.business?.id == principal.business.id and " +
		  	  	  "#priceListDTO != null and #priceListDTO.id == null")
	@Restrictions(checkers = {PremiumChecker.class})
	public Long add(PriceListDTO priceListDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException {
		PriceList priceList = new PriceList();
		PriceListDTOTransformer.copyFromDTO(priceList, priceListDTO);
		Business business = Business.findBusiness(priceListDTO.getBusiness().getId());
		priceList.setBusiness(business);
		validator.validate(priceList, true);
		business.getPriceLists().add(priceList);
		priceList.persist();
		priceList.flush();
		return priceList.getId();
	}
	
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("#priceListDTO?.business?.id == principal.business.id and " +
	  	  	  	  "#priceListDTO?.id != null")
	public void update(PriceListDTO priceListDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException {
		PriceList persistedPriceList = PriceList.findPriceList(priceListDTO.getId());
		if(persistedPriceList == null)
			throw new NoSuchObjectException();
		PriceList copy = persistedPriceList.shallowCopy();
		PriceListDTOTransformer.copyFromDTO(copy, priceListDTO);
		validator.validate(copy, !persistedPriceList.getName().equals(priceListDTO.getName()));
		PriceListDTOTransformer.copyFromDTO(persistedPriceList, priceListDTO);
	}
	
	private void preRemove(PriceList priceList){
		PriceList defaultPriceList = PriceList.getDefaultPriceList(priceList.getBusiness().getId());
		boolean defaultPriceListClientsInitialized = Hibernate.isInitialized(defaultPriceList.getClients());
		for(Client client: priceList.getClients()){
			client.setDefaultPriceList(defaultPriceList);
			if(defaultPriceListClientsInitialized)
				defaultPriceList.getClients().add(client);
		}
	}
	
	@Transactional(readOnly = false)
	@PreAuthorize("#businessID == principal.business.id and " +
		  	  	  "T(com.novadart.novabill.domain.PriceList).findPriceList(#id)?.business?.id == #businessID")
	public void remove(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException, DataIntegrityException {
		PriceList priceList = PriceList.findPriceList(id);
		if(priceList.getName().equals(PriceListConstants.DEFAULT)) //removing default pricelist
			throw new DataIntegrityException();
		preRemove(priceList);
		priceList.remove(); //removing pricelist
		if(Hibernate.isInitialized(priceList.getBusiness().getPriceLists()))
			priceList.getBusiness().getPriceLists().remove(priceList);
	}
	
	@Transactional(readOnly = true)
	@PreAuthorize("#businessID == principal.business.id and " +
		  	  	  "T(com.novadart.novabill.domain.PriceList).findPriceList(#id)?.business?.id == #businessID")
	public Map<String, Pair<String, PriceDTO>> getPrices(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException {
		List<CommodityDTO> commodities = businessService.getCommodities(businessID);
		Map<String, Pair<String, PriceDTO>> result = new HashMap<>();
		Map<Long, String> commMap = new HashMap<>();
		for(CommodityDTO comm: commodities){
			commMap.put(comm.getId(), comm.getSku());
			result.put(comm.getSku(), new Pair<>(comm.getDescription(), new PriceDTO(comm.getId(), id)));
		}
		for(Price price: Price.findPricesForPriceList(id)) {
			Pair<String, PriceDTO> pair = result.get(commMap.get(price.getCommodity().getId()));
			pair.setSecond(PriceDTOTransformer.toDTO(price));
		}
		return result;
	}
	
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("#businessID == principal.business.id and " + 
				  "T(com.novadart.novabill.domain.PriceList).findPriceList(#id)?.business?.id == #businessID")
	public Long clonePriceList(Long businessID, Long id,  String priceListName) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException, ValidationException {
		PriceList priceList = PriceList.findPriceList(id);
		if(priceList == null)
			throw new NoSuchObjectException();
		PriceList clonedPriceList = new PriceList();
		clonedPriceList.setName(priceListName);
		Business business = Business.findBusiness(businessID);
		clonedPriceList.setBusiness(business);
		validator.validate(clonedPriceList, true);
		for(Price price: priceList.getPrices()){
			Price clonedPrice = new Price();
			clonedPrice.setPriceType(price.getPriceType());
			clonedPrice.setPriceValue(price.getPriceValue());
			//attach to cloned pricelist
			clonedPriceList.getPrices().add(clonedPrice);
			clonedPrice.setPriceList(clonedPriceList);
			//attach to commodity
			Commodity commodity = price.getCommodity();
			commodity.getPrices().add(clonedPrice);
			clonedPrice.setCommodity(commodity);
		}
		business.getPriceLists().add(clonedPriceList);
		clonedPriceList.persist();
		clonedPriceList.flush();
		return clonedPriceList.getId();
	}
	
}
