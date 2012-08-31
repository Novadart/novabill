package com.novadart.novabill.web.gwt;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.dto.factory.BusinessDTOFactory;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.XsrfTokenService;
import com.novadart.novabill.service.validator.SimpleValidator;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.BusinessService;
import com.novadart.novabill.web.mvc.ExportController;
import com.novadart.novabill.web.mvc.PDFController;

public class BusinessServiceImpl extends AbstractGwtController<BusinessService, BusinessServiceImpl> implements BusinessService{

	private static final long serialVersionUID = -8341228475620801759L;

	private static final Logger LOGGER = LoggerFactory.getLogger(BusinessServiceImpl.class);
	
	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private SimpleValidator validator;
	
	@Autowired
	private XsrfTokenService xsrfTokenService;

	public BusinessServiceImpl() {
		super(BusinessService.class);
	}
	
	@Override
	public BusinessStatsDTO getStats() {
		BusinessStatsDTO stats = new BusinessStatsDTO();
		stats.setClientsCount(countClients());
		int year = Calendar.getInstance().get(Calendar.YEAR);
		stats.setInvoicesCountForYear(countInvoicesForYear(year));
		stats.setTotalAfterTaxesForYear(getTotalAfterTaxesForYear(year));
		LOGGER.info("Stats: clients count {} invoices Count {} now it is {}",
				new Object[]{stats.getClientsCount(), stats.getInvoicesCountForYear(), new Date()});
		return stats;
	}
	
	@Override
	@Transactional(readOnly = true)
	public long countClients() {
		Long id = utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId();
		Business business = Business.findBusiness(id);
		return business.getClients().size();
	}
	
	@Override
	@Transactional(readOnly = true)
	public long countInvoices() {
		Long id = utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId();
		Business business = Business.findBusiness(id);
		return business.getInvoices().size();
	}

	@Override
	public long countInvoicesForYear(int year) {
		Long id = utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId();
		Business business = Business.findBusiness(id);
		return business.getInvoicesForYear(year).size();
	}

	@Override
	public BigDecimal getTotalAfterTaxesForYear(int year) {
		Long id = utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId();
		Business business = Business.findBusiness(id);
		BigDecimal totalAfterTaxes = new BigDecimal("0.0");
		Iterator<Invoice> iter = business.getInvoicesForYear(year).iterator();
		while(iter.hasNext())
			totalAfterTaxes = totalAfterTaxes.add(iter.next().getTotal());
		return totalAfterTaxes.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	public void update(BusinessDTO businessDTO) throws DataAccessException, NoSuchObjectException, ValidationException {
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(businessDTO.getId()))
			throw new DataAccessException();
		Business business = Business.findBusiness(businessDTO.getId());
		if(business == null)
			throw new NoSuchObjectException();
		BusinessDTOFactory.copyFromDTO(business, businessDTO);
		validator.validate(business);
	}
	
	private String generateToken(String tokenSessionField) throws NoSuchAlgorithmException{
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return xsrfTokenService.generateToken(attr.getRequest().getSession(), tokenSessionField);
	}
	
	@Override
	public String generatePDFToken() throws NotAuthenticatedException, ConcurrentAccessException, NoSuchAlgorithmException {
		return generateToken(PDFController.TOKENS_SESSION_FIELD);
	}

	@Override
	public String generateExportToken() throws NotAuthenticatedException, ConcurrentAccessException, NoSuchAlgorithmException {
		return generateToken(ExportController.TOKENS_SESSION_FIELD);
	}

}
