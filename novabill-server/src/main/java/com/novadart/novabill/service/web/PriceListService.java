package com.novadart.novabill.service.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Price;
import com.novadart.novabill.domain.PriceList;
import com.novadart.novabill.domain.dto.factory.PriceDTOFactory;
import com.novadart.novabill.domain.dto.factory.PriceListDTOFactory;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.SimpleValidator;
import com.novadart.novabill.shared.client.data.PriceListConstants;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.PriceDTO;
import com.novadart.novabill.shared.client.dto.PriceListDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.tuple.Pair;

@Service
public class PriceListService {
	
	@Autowired
	private SimpleValidator validator;

	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private UtilsService utilsService;
	
	@PreAuthorize("#businessID == principal.business.id")
	public List<PriceListDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return businessService.getPriceLists(businessID);
	}
	
	@PreAuthorize("T(com.novadart.novabill.domain.PriceList).findPriceList(#id)?.business?.id == principal.business.id")
	public PriceListDTO get(Long id) throws NotAuthenticatedException,NoSuchObjectException, DataAccessException {
		Long businessID = utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId();
		PriceList priceList = PriceList.getPriceListWithPrices(businessID, id);
		if(priceList == null)
			throw new NoSuchObjectException();
		if(priceList.getName().equals(PriceListConstants.DEFAULT))
			return PriceListDTOFactory.toDTO(priceList, true);
		PriceListDTO customPL = PriceListDTOFactory.toDTO(priceList, true);
		priceList = PriceList.getPriceListWithPrices(businessID, PriceListConstants.DEFAULT);
		if(priceList == null)
			throw new NoSuchObjectException();
		PriceListDTO publicPL = PriceListDTOFactory.toDTO(priceList, true);
		return PriceListDTOFactory.mergePriceListDTOs(publicPL, customPL);
		
	}
	
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("#priceListDTO?.business?.id == principal.business.id and " +
		  	  	  "#priceListDTO != null and #priceListDTO.id == null")
	public Long add(PriceListDTO priceListDTO) throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException {
		PriceList priceList = new PriceList();
		PriceListDTOFactory.copyFromDTO(priceList, priceListDTO);
		validator.validate(priceList);
		Business business = Business.findBusiness(priceListDTO.getBusiness().getId());
		business.getPriceLists().add(priceList);
		priceList.setBusiness(business);
		priceList.persist();
		priceList.flush();
		return priceList.getId();
	}
	
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("#priceListDTO?.business?.id == principal.business.id and " +
	  	  	  	  "#priceListDTOs?.id != null")
	public void update(PriceListDTO priceListDTO) throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException {
		PriceList persistedPriceList = PriceList.findPriceList(priceListDTO.getId());
		if(persistedPriceList == null)
			throw new NoSuchObjectException();
		PriceListDTOFactory.copyFromDTO(persistedPriceList, priceListDTO);
		validator.validate(persistedPriceList);
	}
	
	@Transactional(readOnly = false)
	@PreAuthorize("#businessID == principal.business.id and " +
		  	  	  "T(com.novadart.novabill.domain.PriceList).findPriceList(#id)?.business?.id == #businessID")
	public void remove(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException {
		PriceList priceList = PriceList.findPriceList(id);
		priceList.remove(); //removing payment type
		if(Hibernate.isInitialized(priceList.getBusiness().getPriceLists()))
			priceList.getBusiness().getPaymentTypes().remove(priceList);
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
			pair.setSecond(PriceDTOFactory.toDTO(price));
		}
		return result;
	}
	
}
