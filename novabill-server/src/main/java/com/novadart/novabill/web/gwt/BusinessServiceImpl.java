package com.novadart.novabill.web.gwt;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.dto.factory.BusinessDTOFactory;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.XsrfTokenService;
import com.novadart.novabill.service.validator.TaxableEntityValidator;
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
	private TaxableEntityValidator validator;
	
	@Autowired
	private XsrfTokenService xsrfTokenService;

	public BusinessServiceImpl() {
		super(BusinessService.class);
	}
	
	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public BusinessStatsDTO getStats(Long businessID) {
		BusinessStatsDTO stats = new BusinessStatsDTO();
		stats.setClientsCount(countClients(businessID));
		int year = Calendar.getInstance().get(Calendar.YEAR);
		stats.setInvoicesCountForYear(countInvoicesForYear(businessID, year));
		stats.setTotalAfterTaxesForYear(getTotalAfterTaxesForYear(businessID, year));
		LOGGER.info("Stats: clients count {} invoices Count {} now it is {}",
				new Object[]{stats.getClientsCount(), stats.getInvoicesCountForYear(), new Date()});
		return stats;
	}
	
	@Override
	@Transactional(readOnly = true)
	@PreAuthorize("#businessID == principal.business.id")
	public long countClients(Long businessID) {
		return Business.findBusiness(businessID).getClients().size();
	}
	
	@Override
	@Transactional(readOnly = true)
	@PreAuthorize("#businessID == principal.business.id")
	public long countInvoices(Long businessID) {
		return Business.findBusiness(businessID).getInvoices().size();
	}

	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public long countInvoicesForYear(Long businessID, int year) {
		return Business.findBusiness(businessID).getInvoicesForYear(year).size();
	}

	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public BigDecimal getTotalAfterTaxesForYear(Long businessID, int year) {
		Business business = Business.findBusiness(businessID);
		BigDecimal totalAfterTaxes = new BigDecimal("0.0");
		Iterator<Invoice> iter = business.getInvoicesForYear(year).iterator();
		while(iter.hasNext())
			totalAfterTaxes = totalAfterTaxes.add(iter.next().getTotal());
		return totalAfterTaxes.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	public void update(BusinessDTO businessDTO) throws DataAccessException, NoSuchObjectException, ValidationException {
		if(!utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId().equals(businessDTO.getId()))
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
