package com.novadart.novabill.service.web;

import java.util.List;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.PriceList;
import com.novadart.novabill.domain.dto.factory.PriceListDTOFactory;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.SimpleValidator;
import com.novadart.novabill.shared.client.dto.PriceListDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

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
		for(PriceListDTO priceListDTO: businessService.getPriceLists(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId()))
			if(priceListDTO.getId().equals(id))
				return priceListDTO;
		throw new NoSuchObjectException();
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
	
}
