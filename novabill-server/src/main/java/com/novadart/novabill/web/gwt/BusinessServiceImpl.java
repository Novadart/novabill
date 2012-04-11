package com.novadart.novabill.web.gwt;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.BusinessDTOFactory;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.facade.BusinessService;

public class BusinessServiceImpl extends AbstractGwtController<BusinessService, BusinessServiceImpl> implements BusinessService{

	private static final long serialVersionUID = -8341228475620801759L;
	
	@Autowired
	private UtilsService utilsService;

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

	@Transactional(readOnly = true)
	private List<Invoice> getInvoicesForYear(int year) {
		Long id = utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId();
		Business business = Business.findBusiness(id);
		List<Invoice> invoices = new LinkedList<Invoice>();
		Iterator<Invoice> iter = business.getInvoices().iterator();
		Invoice inv;
		while(iter.hasNext()){
			inv = iter.next();
			if(inv.getInvoiceYear().intValue() == year)
				invoices.add(inv);
		}
		return invoices;
	}
	
	@Override
	public long countInvoicesForYear(int year) {
		return getInvoicesForYear(year).size();
	}

	@Override
	public BigDecimal getTotalAfterTaxesForYear(int year) {
		BigDecimal totalAfterTaxes = new BigDecimal("0.0");
		Iterator<Invoice> iter = getInvoicesForYear(year).iterator();
		while(iter.hasNext())
			totalAfterTaxes = totalAfterTaxes.add(iter.next().getTotal());
		return totalAfterTaxes.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}

	@Override
	@Transactional(readOnly = false)
	public void update(BusinessDTO businessDTO) throws DataAccessException, NoSuchObjectException {
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(businessDTO.getId()))
			throw new DataAccessException();
		Business business = Business.findBusiness(businessDTO.getId());
		if(business == null)
			throw new NoSuchObjectException();
		BusinessDTOFactory.copyFromDTO(business, businessDTO);
		business.flush();
	}

}
