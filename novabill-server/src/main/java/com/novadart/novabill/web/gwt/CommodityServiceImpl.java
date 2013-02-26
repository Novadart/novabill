package com.novadart.novabill.web.gwt;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Commodity;
import com.novadart.novabill.domain.dto.factory.CommodityDTOFactory;
import com.novadart.novabill.service.validator.SimpleValidator;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.BusinessService;
import com.novadart.novabill.shared.client.facade.CommodityService;

public class CommodityServiceImpl implements CommodityService {
	
	@Autowired
	private SimpleValidator validator;
	
	@Autowired
	private BusinessService businessService;
	
	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<CommodityDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return businessService.getCommodities(businessID);
	}

	@Override
	@PreAuthorize("#commodityDTO?.business?.id == principal.business.id and " +
				  "#commodityDTO != null and #commodityDTO.id == null" )
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	public Long add(CommodityDTO commodityDTO) throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException {
		Commodity commodity = new Commodity();
		CommodityDTOFactory.copyFromDTO(commodity, commodityDTO);
		validator.validate(commodity);
		Business business = Business.findBusiness(commodityDTO.getBusiness().getId());
		business.getCommodities().add(commodity);
		commodity.setBusiness(business);
		return commodity.merge().getId();
	}

	@Override
	@Transactional(readOnly = false)
	 @PreAuthorize("#businessID == principal.business.id and " +
			       "T(com.novadart.novabill.domain.Commodity).findCommodity(#id)?.business?.id == #businessID")
	public void remove(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException {
		Commodity commodity = Commodity.findCommodity(id);
		commodity.remove(); //removing commodity
		if(Hibernate.isInitialized(commodity.getBusiness().getCommodities()))
			commodity.getBusiness().getCommodities().remove(commodity);
	}

}
